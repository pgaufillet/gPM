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

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.sheet.export.service.SheetExportFormat;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * Tests the method <CODE>sheetExport<CODE> of the Sheet Service for XML export.
 * 
 * @author nsamson
 */
public class TestSheetXMLExportService extends AbstractBusinessServiceTestCase {

    /** The Sheet Service. */
    private SheetService sheetService;

    /** The XML used to instantiate the confidential test case */
    private static final String XML_INSTANCE_CONFIDENTIAL_TEST =
            "sheet/TestSheetExportServiceConfidentialAccess.xml";

    /** Role name for confidential test */
    private static final String ROLE_NAME = "notadmin";

    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_CAT;

    /**
     * Tests the sheetExport method.
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

        // Exports sheets into an outputstream
        List<String> lSheetIdsList = Arrays.asList(lSheetIds);
        ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();
        startTimer();
        sheetService.exportSheets(adminRoleToken, lOutputStream, lSheetIdsList,
                SheetExportFormat.XML);
        stopTimer();

        assertTrue("The export method returns a empty XML Export.",
                lOutputStream.toByteArray().length != 0);
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

        // Test export with OutputStream
        try {
            sheetService.exportSheets(lRoleToken, new ByteArrayOutputStream(),
                    Arrays.asList(lSheetIds), SheetExportFormat.XML);
            fail("An authorization exception should have been thrown.");
        }
        catch (AuthorizationException e) {
            // ok
        }

        finally {
            authorizationService.logout(lUserToken);
        }
    }
}