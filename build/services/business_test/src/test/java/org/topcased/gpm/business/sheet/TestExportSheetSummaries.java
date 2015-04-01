/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Mimoun Mennad (ATOS), Nicolas Samson (ATOS)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.sheet.export.impl.ExcelExporter;
import org.topcased.gpm.business.sheet.export.service.SheetExportFormat;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.util.lang.StringUtils;

/**
 * This class tests the getSheetTypes(String, String, CacheProperties) method
 * from the SheetService implementation.
 * 
 * @author mmennad
 */
public class TestExportSheetSummaries extends AbstractBusinessServiceTestCase {

    /**
     * Test the method with correct parameters
     */
    @SuppressWarnings("unchecked")
	public void testNormalCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheets

        ExecutableFilterData lExecutableFilterData =
                serviceLocator.getSearchService().getExecutableFilterByName(
                        normalRoleToken, getProcessName(), null, null,
                        GpmTestValues.FILTER_LONG_STRING_VALUE_SHEETS);

        // execute filter
        try {
            List<SheetSummaryData> lSheetsList =
                    IteratorUtils.toList(serviceLocator.getSearchService().executeFilter(
                            normalRoleToken,
                            lExecutableFilterData,
                            lExecutableFilterData.getFilterVisibilityConstraintData(),
                            new FilterQueryConfigurator()));

            ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();
            Map<String, String> lLabels = new HashMap<String, String>();
            lLabels.put("$SHEET_REFERENCE", "Sheet reference");
            lLabels.put("$SHEET_TYPE", "Sheet type");
            lLabels.put("EXCEL_STRING", "test string too long");

            // Exports sheets
            sheetService.exportSheetSummaries(normalRoleToken, lOutputStream,
                    lLabels, lSheetsList, SheetExportFormat.EXCEL,
                    Context.getEmptyContext());

            startTimer();
            assertTrue(
                    "The export method returns a empty byte array for Excel Export.",
                    lOutputStream.toByteArray().length != 0);

            ByteArrayInputStream lInputStream =
                    new ByteArrayInputStream(lOutputStream.toByteArray());
            HSSFWorkbook lWorkBook;
            lWorkBook = new HSSFWorkbook(lInputStream);
            testExcelContent(lWorkBook);
        }
        catch (IOException e) {
            fail("An IO Exception has been caught " + e.getMessage());
        }

        stopTimer();
    }

    /**
     * Iterate into an Excel document. Iterate into each sheets then into each
     * rows and finally into each cells
     * 
     * @param workBook
     */
    @SuppressWarnings("unchecked")
	private void testExcelContent(HSSFWorkbook pWorkBook) {
        //For each sheet of the document
        for (int i = 0; i < pWorkBook.getNumberOfSheets(); i++) {
            HSSFSheet lSheet = pWorkBook.getSheetAt(i);
            Iterator<HSSFRow> lRowIterator = lSheet.rowIterator();
            //For each row of the current sheet
            while (lRowIterator.hasNext()) {
                HSSFRow lCurrentRow = lRowIterator.next();
                Iterator<HSSFCell> lCellIterator = lCurrentRow.cellIterator();
                //For each cell of the row
                while (lCellIterator.hasNext()) {
                    HSSFCell lCell = lCellIterator.next();
                    String lCellString = lCell.getStringCellValue();
                    assertTrue(
                            "The cell size is under EXCEL_CELL_MAX_SIZE",
                            lCellString.length() <= ExcelExporter.EXCEL_CELL_MAX_SIZE);
                    //If the cell has the max size, 
                    //it must contains an indication that the content has been cutted
                    if (lCellString.length() == ExcelExporter.EXCEL_CELL_MAX_SIZE) {
                        assertTrue(
                                "The cell size is under EXCEL_CELL_MAX_SIZE AND a tag "
                                        + "informs that the value has been cut during export",
                                lCellString.startsWith(StringUtils.PARTIAL_INFO));
                    }
                }
            }
        }
    }

}
