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

import java.util.Arrays;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.lifecycle.service.ProcessInformation;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>getSheetProcessInformationService<CODE> of the Sheet
 * Service.
 * 
 * @author nsamson
 */
public class TestGetSheetProcessInformationService extends
        AbstractBusinessServiceTestCase {

    /** The Sheet Service. */
    private SheetService sheetService;

    /** The name of the sheet type used. */
    private static final String SHEET_TYPE_NAME = GpmTestValues.SHEET_TYPE_DOG;

    /** The process state. */
    private static final String PROCESS_STATE = "Temporary";

    /** The process transitions. */
    private static final String[] PROCESS_TRANSITIONS =
            { "Delete", "Validate" };

    /** The XML used to instantiate the confidential test case */
    private static final String XML_INSTANCE_CONFIDENTIAL_TEST =
            "sheet/TestGetSheetProcessInformationServiceConfidentialAccess.xml";

    /** Role name for confidential test */
    private static final String ROLE_NAME = "notadmin";

    /**
     * Tests the getSheetProcessInformationService method.
     */
    public void testNormalCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Retrieving the TypeSheet
        CacheableSheetType lSheetType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        SHEET_TYPE_NAME, CacheProperties.IMMUTABLE);
        assertNotNull("getSheetTypeByName with the name " + SHEET_TYPE_NAME
                + " returns no SheetType.", lSheetType);

        // Retrieving the sheet.
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(),
                        lSheetType.getName());
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lId = lSheetSummary.get(0).getId();

        // Retrieves the process information
        startTimer();
        ProcessInformation lProcessInfo =
                sheetService.getSheetProcessInformation(adminRoleToken, lId);
        stopTimer();

        assertNotNull("The method getSheetProcessInformation retuns null.",
                lProcessInfo);

        String lState = lProcessInfo.getCurrentState();
        String[] lTransitions = lProcessInfo.getTransitions();
        int lSize = lTransitions.length;
        int lExpectedSize = PROCESS_TRANSITIONS.length;

        assertEquals("Process has not the expected state.", PROCESS_STATE,
                lState);
        assertEquals("Process has " + lSize + " transitions instead of "
                + lExpectedSize + ".", lExpectedSize, lSize);
        assertTrue("Process transitions are not thoses expected.",
                Arrays.asList(lTransitions).containsAll(
                        Arrays.asList(PROCESS_TRANSITIONS)));
    }

    /**
     * Tests the getSheetProcessInformation method with a invalid token.
     */
    public void testInvalidTokenCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Uses a empty token.
        try {
            sheetService.getSheetProcessInformation("", "");
            fail("The exception has not been thrown.");
        }
        catch (InvalidTokenException e) {
            // ok
        }
        catch (Throwable e) {

            fail("The exception thrown is not a InvalidTokenException.");
        }
    }

    /**
     * Tests the getSheetProcessInformationService method with a confidential
     * access on the corresponding sheet type.
     */
    public void testConfidentialAccessCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();
        authorizationService = serviceLocator.getAuthorizationService();

        // Retrieving the TypeSheet
        CacheableSheetType lSheetType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        SHEET_TYPE_NAME, CacheProperties.IMMUTABLE);
        assertNotNull("getSheetTypeByName with the name " + SHEET_TYPE_NAME
                + " returns no SheetType.", lSheetType);

        // Retrieving the sheet.
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(),
                        lSheetType.getName());
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lId = lSheetSummary.get(0).getId();

        // set confidential access on sheet type SHEET_TYPE_NAME
        instantiate(getProcessName(), XML_INSTANCE_CONFIDENTIAL_TEST);

        String lUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        getProductName(), getProcessName());

        // Retrieves the process information
        ProcessInformation lProcessInfo = null;
        try {
            lProcessInfo =
                    sheetService.getSheetProcessInformation(lRoleToken, lId);
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException ex) {
            // ok
        }
        finally {
            authorizationService.logout(lUserToken);
        }
        assertNull("The method getSheetProcessInformation doesn't retun null.",
                lProcessInfo);
    }
}