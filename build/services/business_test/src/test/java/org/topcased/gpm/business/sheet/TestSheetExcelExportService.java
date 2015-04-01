/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.IteratorUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.sheet.export.impl.ExcelExporter;
import org.topcased.gpm.business.sheet.export.service.SheetExportFormat;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * Tests the method <CODE>sheetExport<CODE> of the Sheet Service for Excel
 * export.
 * 
 * @author nsamson
 */
public class TestSheetExcelExportService extends
        AbstractBusinessServiceTestCase {

    /** The Sheet Service. */
    private SheetService sheetService;

    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_CAT;

    /** The XML used to instantiate the confidential test case */
    private static final String XML_INSTANCE_CONFIDENTIAL_TEST =
            "sheet/TestSheetExportServiceConfidentialAccess.xml";

    /** Role name for confidential test */
    private static final String ROLE_NAME = "notadmin";

    /**
     * Tests the sheetExport method.
     */
    public void testNormalCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheets
        List<SheetSummaryData> lSheets =
                sheetService.getSheetsByType(DEFAULT_PROCESS_NAME, SHEET_TYPE);

        assertNotNull("The method getSheetsByType returned null.", lSheets);
        assertFalse("The method getSheetsByProduct returns an empty list.",
                lSheets.isEmpty());

        String[] lSheetIds = new String[lSheets.size()];
        int i = 0;
        for (SheetSummaryData lSheet : lSheets) {
            lSheetIds[i++] = lSheet.getId();
        }

        // Exports sheets into an outputstream

        List<String> lSheetIdsList = Arrays.asList(lSheetIds);
        ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();
        startTimer();
        sheetService.exportSheets(adminRoleToken, lOutputStream, lSheetIdsList,
                SheetExportFormat.EXCEL);
        stopTimer();

        assertTrue("The export method returns a empty XLS Export.",
                lOutputStream.toByteArray().length != 0);
    }

    /**
     * Tests the sheetExport method.
     */
    public void testConfidentialAccess() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheets
        List<SheetSummaryData> lSheets =
                sheetService.getSheetsByType(DEFAULT_PROCESS_NAME, SHEET_TYPE);
        assertNotNull("The method getSheetsByType returned null.", lSheets);
        assertFalse("The method getSheetsByProduct returns an empty list.",
                lSheets.isEmpty());

        String[] lSheetIds = new String[lSheets.size()];
        int i = 0;
        for (SheetSummaryData lSheet : lSheets) {
            lSheetIds[i++] = lSheet.getId();
        }

        // Exports sheets
        // set confidential access on all sheet types
        instantiate(getProcessName(), XML_INSTANCE_CONFIDENTIAL_TEST);

        String lUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        getProductName(), getProcessName());

        // Test export with OutputStream
        try {
            sheetService.exportSheets(lRoleToken, new ByteArrayOutputStream(),
                    Arrays.asList(lSheetIds), SheetExportFormat.EXCEL);
            fail("An authorization exception should have been thrown.");
        }
        catch (AuthorizationException e) {
            // ok
        }

        finally {
            authorizationService.logout(lUserToken);
        }

    }

    /**
     * Tests the exportSheetSummaries method, it tests the content of the
     * exported file. No cell must have a string with a size above 32768. If a
     * sheet's field has a size above 32768, the excel cell must start with
     * #PARTIAL INFO# and the size of the cell must be 32767 characters
     */
    @SuppressWarnings("unchecked")
	public void testTooLongCellSummariesCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheets

        ExecutableFilterData lExecutableFilterData =
                serviceLocator.getSearchService().getExecutableFilterByName(
                        normalRoleToken, getProcessName(), null, null,
                        GpmTestValues.FILTER_LONG_STRING_VALUE_SHEETS);

        // execute filter
        try {
            List<SheetSummaryData> sheetsList =
                    IteratorUtils.toList(serviceLocator.getSearchService().executeFilter(
                            normalRoleToken,
                            lExecutableFilterData,
                            lExecutableFilterData.getFilterVisibilityConstraintData(),
                            new FilterQueryConfigurator()));

            ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();
            Map<String, String> pLabels = new HashMap<String, String>();
            pLabels.put("$SHEET_REFERENCE", "Sheet reference");
            pLabels.put("$SHEET_TYPE", "Sheet type");
            pLabels.put("EXCEL_STRING", "test string too long");

            // Exports sheets
            sheetService.exportSheetSummaries(normalRoleToken, lOutputStream,
                    pLabels, sheetsList, SheetExportFormat.EXCEL,
                    Context.getEmptyContext());

            startTimer();
            assertTrue(
                    "The export method returns a empty byte array for Excel Export.",
                    lOutputStream.toByteArray().length != 0);

            ByteArrayInputStream inputStream =
                    new ByteArrayInputStream(lOutputStream.toByteArray());
            HSSFWorkbook workBook;
            workBook = new HSSFWorkbook(inputStream);
            testExcelContent(workBook);
        }
        catch (Exception e) {
            fail("The export has fail with an exception " + e.getMessage());
        }

        stopTimer();
    }

    /**
     * Iterate into an Excel document. Iterate into each sheets then into each
     * rows and finally into each cells
     * 
     * @param pWorkBook
     */
    @SuppressWarnings("unchecked")
	private void testExcelContent(HSSFWorkbook pWorkBook) {
        //For each sheet of the document
        for (int i = 0; i < pWorkBook.getNumberOfSheets(); i++) {
            HSSFSheet sheet = pWorkBook.getSheetAt(i);
            Iterator<HSSFRow> rowIterator = sheet.rowIterator();
            //For each row of the current sheet
            while (rowIterator.hasNext()) {
                HSSFRow currentRow = rowIterator.next();
                Iterator<HSSFCell> cellIterator = currentRow.cellIterator();
                //For each cell of the row
                while (cellIterator.hasNext()) {
                    HSSFCell cell = cellIterator.next();
                    String cellString = cell.getStringCellValue();
                    assertTrue(
                            "The cell size is under EXCEL_CELL_MAX_SIZE",
                            cellString.length() <= ExcelExporter.EXCEL_CELL_MAX_SIZE);
                    //If the cell has the max size, it must contains an indication that the content has been cutted
                    if (cellString.length() == ExcelExporter.EXCEL_CELL_MAX_SIZE) {
                        assertTrue(
                                "The cell size is under EXCEL_CELL_MAX_SIZE AND a tag informs that the value has been cut during export",
                                cellString.startsWith(org.topcased.gpm.util.lang.StringUtils.PARTIAL_INFO));
                    }
                }
            }
        }
    }
}
