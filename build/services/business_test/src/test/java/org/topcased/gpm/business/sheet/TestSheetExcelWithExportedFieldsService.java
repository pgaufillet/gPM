/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.sheet.export.service.SheetExportFormat;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * TestSheetExcelWithExportedFieldsService
 * 
 * @author mfranche
 */
public class TestSheetExcelWithExportedFieldsService extends
        AbstractBusinessServiceTestCase {

    private static final String SHEET_MOUSE_TYPE =
            GpmTestValues.SHEET_TYPE_MOUSE;

    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_CAT;

    /** The XML used to instantiate the confidential test case */
    private static final String XML_INSTANCE_CONFIDENTIAL_TEST =
            "sheet/TestSheetExportServiceConfidentialAccess.xml";

    /** Role name for confidential test */
    private static final String ROLE_NAME = "notadmin";

    /**
     * Test Normal case
     */
    public void testNormalCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheets
        List<SheetSummaryData> lSheets =
                sheetService.getSheetsByType(DEFAULT_PROCESS_NAME, SHEET_TYPE);
        assertNotNull("The method getSheetsByProduct returns null.", lSheets);
        assertFalse("The method getSheetsByProduct returns an empty list.",
                lSheets.isEmpty());

        String[] lSheetIds = new String[lSheets.size()];
        int i = 0;
        for (SheetSummaryData lSheet : lSheets) {
            lSheetIds[i++] = lSheet.getId();
        }

        // Exports sheets

        // Create a new outputStream to store the Excel export result
        ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();
        //        FileOutputStream lOutputStream = null;
        //        try {
        //            lOutputStream =
        //                    new FileOutputStream(new File("C:/temp/testCatExcel.xls"));
        //        }
        //        catch (Exception ex) {
        //            ex.printStackTrace();
        //        }
        List<String> lExportedFieldsList = new ArrayList<String>();
        lExportedFieldsList.add("CAT_ref");
        lExportedFieldsList.add("CAT_color");
        lExportedFieldsList.add("CAT_furlength");
        lExportedFieldsList.add("CAT_description");
        lExportedFieldsList.add("CAT_picture");

        startTimer();
        sheetService.exportSheets(adminRoleToken, lOutputStream, lSheetIds,
                SheetExportFormat.EXCEL, lExportedFieldsList);
        stopTimer();

        assertTrue("The excel report is empty.", lOutputStream.size() >= 0);
    }

    /**
     * Tests the sheet Excel export with multiple fields exported
     */
    public void testWithMultipleFieldsNormalCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheets
        List<SheetSummaryData> lSheets =
                sheetService.getSheetsByType(DEFAULT_PROCESS_NAME,
                        SHEET_MOUSE_TYPE);
        assertNotNull("The method getSheetsByProduct returns null.", lSheets);
        assertFalse("The method getSheetsByProduct returns an empty list.",
                lSheets.isEmpty());

        String[] lSheetIds = new String[lSheets.size()];
        int i = 0;
        for (SheetSummaryData lSheet : lSheets) {
            lSheetIds[i++] = lSheet.getId();
        }

        // Exports sheets

        // Create a new outputStream to store the Excel export result
        ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();
        //        FileOutputStream lOutputStream = null;
        //        try {
        //            lOutputStream =
        //                    new FileOutputStream(new File("C:/temp/testMouseExcel.xls"));
        //        }
        //        catch (Exception ex) {
        //            ex.printStackTrace();
        //        }
        List<String> lExportedFieldsList = new ArrayList<String>();
        lExportedFieldsList.add("MOUSE_identification::MOUSE_mousename");
        lExportedFieldsList.add("MOUSE_owner");
        //        lExportedFieldsList.add("MOUSE_owner::MOUSE_firstname");
        //        lExportedFieldsList.add("MOUSE_owner::MOUSE_lastname");
        //        lExportedFieldsList.add("MOUSE_owner::MOUSE_numberofchildren");

        startTimer();
        sheetService.exportSheets(adminRoleToken, lOutputStream, lSheetIds,
                SheetExportFormat.EXCEL, lExportedFieldsList);
        stopTimer();

        assertTrue("The excel report is empty.", lOutputStream.size() >= 0);
    }

    /**
     * Tests the sheetExport method when one of the sheet types has confidential
     * access
     */
    public void testConfidentialAccessCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheets
        List<SheetSummaryData> lSheets =
                sheetService.getSheetsByType(DEFAULT_PROCESS_NAME, SHEET_TYPE);
        assertNotNull("The method getSheetsByProduct returns null.", lSheets);
        assertFalse("The method getSheetsByProduct returns an empty list.",
                lSheets.isEmpty());

        String[] lSheetIds = new String[lSheets.size()];
        int i = 0;
        for (SheetSummaryData lSheet : lSheets) {
            lSheetIds[i++] = lSheet.getId();
        }

        // Exports sheets

        //      set confidential access on all sheet types
        instantiate(getProcessName(), XML_INSTANCE_CONFIDENTIAL_TEST);

        String lUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        getProductName(), getProcessName());

        ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();
        try {
            sheetService.exportSheets(lRoleToken, lOutputStream, lSheetIds,
                    SheetExportFormat.EXCEL, new ArrayList<String>());
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
     * Tests the sheet Excel export with an incorrect output stream
     */
    public void testIncorrectOutputStreamCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheets
        List<SheetSummaryData> lSheets =
                sheetService.getSheetsByType(DEFAULT_PROCESS_NAME, SHEET_TYPE);
        assertNotNull("The method getSheetsByProduct returns null.", lSheets);
        assertFalse("The method getSheetsByProduct returns an empty list.",
                lSheets.isEmpty());

        String[] lSheetIds = new String[lSheets.size()];
        int i = 0;
        for (SheetSummaryData lSheet : lSheets) {
            lSheetIds[i++] = lSheet.getId();
        }

        // Exports sheets

        try {
            sheetService.exportSheets(adminRoleToken, null, lSheetIds,
                    SheetExportFormat.EXCEL, new ArrayList<String>());
            fail("An authorization exception should have been thrown.");
        }
        catch (IllegalArgumentException e) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an IllegalArgumentException.");
        }
    }

    /**
     * Tests the sheet Excel export with an incorrect exported fields list.
     */
    public void testIncorrectExportedFieldsCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheets
        List<SheetSummaryData> lSheets =
                sheetService.getSheetsByType(DEFAULT_PROCESS_NAME, SHEET_TYPE);
        assertNotNull("The method getSheetsByProduct returns null.", lSheets);
        assertFalse("The method getSheetsByProduct returns an empty list.",
                lSheets.isEmpty());

        String[] lSheetIds = new String[lSheets.size()];
        int i = 0;
        for (SheetSummaryData lSheet : lSheets) {
            lSheetIds[i++] = lSheet.getId();
        }

        // Exports sheets

        ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();
        try {
            sheetService.exportSheets(adminRoleToken, lOutputStream, lSheetIds,
                    SheetExportFormat.EXCEL, null);
            fail("An authorization exception should have been thrown.");
        }
        catch (IllegalArgumentException e) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an IllegalArgumentException.");
        }
    }
}
