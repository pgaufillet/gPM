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

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.LockException;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>getSheetSummaryByKey<CODE> of the Sheet Service.
 * 
 * @author nsamson
 */
public class TestGetSheetSummaryByKeyService extends
        AbstractBusinessServiceTestCase {

    /** The sheet type used. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_DOG;

    /** The Sheet Service. */
    private SheetService sheetService;

    /** The XML used to instantiate the confidential test case */
    private static final String XML_INSTANCE_CONFIDENTIAL_TEST =
            "sheet/TestGetSheetSummaryByKeyConfidentialAccess.xml";

    /** Role name for confidential test */
    private static final String ROLE_NAME = "notadmin";

    /** The read write lock sheet */
    private static final String SHEET_READ_WRITE_LOCK_REF = "Milou";

    /** The read lock sheet */
    private static final String SHEET_WRITE_LOCK_REF = "Snoopy";

    /**
     * Tests the getSheetSummaryByKey method.
     */
    public void testNormalCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Retrieving the TypeSheet
        CacheableSheetType lSheetType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        SHEET_TYPE, CacheProperties.IMMUTABLE);
        assertNotNull("getSheetTypeByName with the name " + SHEET_TYPE
                + " returns no SheetType.", lSheetType);

        // Retrieving the sheet.
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(),
                        lSheetType.getName());

        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());

        SheetSummaryData lFirstSheetSummary = lSheetSummary.get(0);
        String lId = lFirstSheetSummary.getId();
        String lRef = lFirstSheetSummary.getSheetReference();

        // Retrieving the sheet
        SheetSummaryData lSheetData =
                sheetService.getSheetSummaryByKey(adminRoleToken, lId);
        assertNotNull("Sheet #" + lId + " does not exist in DB", lSheetData);

        assertEquals("Sheet reference ", lRef, lSheetData.getSheetReference());
        assertEquals("The selectable attribute is incorrect.",
                lFirstSheetSummary.isSelectable(), lSheetData.isSelectable());
        assertEquals("The sheetName attribute is incorrect.",
                lFirstSheetSummary.getSheetName(), lSheetData.getSheetName());
        assertEquals("The sheetReference attribute is incorrect.",
                lFirstSheetSummary.getSheetReference(),
                lSheetData.getSheetReference());
        assertEquals("The sheetType attribute is incorrect.",
                lFirstSheetSummary.getSheetType(), lSheetData.getSheetType());
        assertEquals("The sheetTypeId attribute is incorrect.",
                lFirstSheetSummary.getSheetTypeId(),
                lSheetData.getSheetTypeId());
        if (lFirstSheetSummary.getFieldSummaryDatas() == null) {
            assertNull("Incorrect field summary data",
                    lFirstSheetSummary.getFieldSummaryDatas());
            assertNull("Incorrect field summary data",
                    lSheetData.getFieldSummaryDatas());
        }
        else {
            assertEquals("The field summary datas length are incorrect.",
                    lFirstSheetSummary.getFieldSummaryDatas().length,
                    lSheetData.getFieldSummaryDatas().length);
        }
    }

    /**
     * Tests the getSheetSummaryByKey method with a confidential access on the
     * corresponding sheet type.
     */
    public void testConfidentialAccessCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();
        authorizationService = serviceLocator.getAuthorizationService();

        // Retrieving the TypeSheet
        CacheableSheetType lSheetType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        SHEET_TYPE, CacheProperties.IMMUTABLE);
        assertNotNull("getSheetTypeByName with the name " + SHEET_TYPE
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

        // Retrieving the sheet
        SheetSummaryData lSheetData = null;
        try {
            lSheetData = sheetService.getSheetSummaryByKey(lRoleToken, lId);
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException ex) {
            // ok
        }
        finally {
            authorizationService.logout(lUserToken);
        }
        assertNull("Sheet #" + lId + " does not return null", lSheetData);
    }

    /**
     * Tests the getSheetSummaryByKey method by the user2 with a WRITE_LOCK set
     * by user1
     */
    public void testWriteLockOnSheetCase() {
        sheetService = serviceLocator.getSheetService();

        // User2 login
        String lUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        getProductName(), getProcessName());

        // Search sheet data
        CacheableSheet lSheetData =
                sheetService.getCacheableSheet(SHEET_WRITE_LOCK_REF,
                        CacheProperties.IMMUTABLE);

        try {
            sheetService.getSheetSummaryByKey(lRoleToken, lSheetData.getId());
        }
        catch (LockException ex) {
            fail("A 'write' lock should not prevent a read of the sheet");
        }
    }

    /**
     * Tests the getSheetSummaryByKey method by the user2 with a READ_WRITE_LOCK
     * set by user1
     */
    public void testReadWriteLockOnSheetCase() {
        sheetService = serviceLocator.getSheetService();

        // User2 login
        String lUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        getProductName(), getProcessName());

        // Search Milou sheet data
        CacheableSheet lSheetData =
                sheetService.getCacheableSheet(SHEET_READ_WRITE_LOCK_REF,
                        CacheProperties.IMMUTABLE);

        try {
            sheetService.getSheetSummaryByKey(lRoleToken, lSheetData.getId());
            fail("The exception has not been thrown.");
        }
        catch (LockException ex) {
            // ok
        }
        catch (Throwable e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}