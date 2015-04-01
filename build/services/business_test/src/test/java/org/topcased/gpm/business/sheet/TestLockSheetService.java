/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.ConstraintException;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.util.bean.CacheProperties;
import org.topcased.gpm.util.bean.LockProperties;

/**
 * Tests the method lockSheet of the SheetService.
 * 
 * @author mfranche
 */
public class TestLockSheetService extends AbstractBusinessServiceTestCase {

    /** The sheet type used. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_DOG;

    /** Role name */
    private static final String ROLE_NAME = "notadmin";

    private static final String ADMIN_ROLE_NAME = GpmTestValues.USER_ADMIN;

    /**
     * Test : user1 set READ_LOCK and user2 try to set READ_WRITE_LOCK or
     * NO_LOCK
     */
    public void testChangeLockByTwoDistinctUsers() {
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
        String lSheetId = lSheetSummary.get(0).getId();

        // User1 login
        String lUserToken =
                authorizationService.login(GpmTestValues.USER_USER1, "pwd1");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ADMIN_ROLE_NAME,
                        getProductName(), getProcessName());

        // Change the sheet type to READ_LOCK
        sheetService.lockSheet(lRoleToken, lSheetId,
                LockProperties.PERMANENT_WRITE_LOCK);

        // User2 login
        lUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        getProductName(), getProcessName());

        try {
            // Try to change the sheet type to READ_WRITE_LOCK
            sheetService.lockSheet(lRoleToken, lSheetId,
                    LockProperties.PERMANENT_READ_WRITE_LOCK);
            fail("The exception has not been thrown.");
        }
        catch (ConstraintException lConstraintException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not a ConstraintException.");
        }
    }

    /**
     * Test : user1 set READ_LOCK and admin user try to set NO_LOCK
     */
    public void testChangeLockByAdmin() {
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
        String lSheetId = lSheetSummary.get(0).getId();

        String lUserToken =
                authorizationService.login(GpmTestValues.USER_USER1, "pwd1");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ADMIN_ROLE_NAME,
                        getProductName(), getProcessName());

        // Change the sheet type to READ_LOCK
        sheetService.lockSheet(lRoleToken, lSheetId,
                LockProperties.PERMANENT_WRITE_LOCK);
        //sheetService.lockSheet(roleToken, lSheetId, ContainerLockType.READ_LOCK);

        lUserToken =
                authorizationService.login(GpmTestValues.USER_ADMIN,
                        GpmTestValues.USER_ADMIN);
        lRoleToken =
                authorizationService.selectRole(lUserToken,
                        GpmTestValues.USER_ADMIN, getProductName(),
                        getProcessName());

        sheetService.lockSheet(lRoleToken, lSheetId,
                LockProperties.PERMANENT_READ_WRITE_LOCK);
    }

    /**
     * Tests the method lockSheet with an incorrect role token
     */
    public void testInvalidTokenCase() {
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
        String lSheetId = lSheetSummary.get(0).getId();

        try {
            // Change the sheet type to READ_LOCK
            sheetService.lockSheet("", lSheetId,
                    LockProperties.PERMANENT_WRITE_LOCK);
            fail("The exception has not been thrown.");
        }

        catch (InvalidTokenException ex) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an InvalidTokenException.");
        }
    }

    /**
     * Tests the method lockSheet with an incorrect sheet id
     */
    public void testIncorrectSheetIdCase() {
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        try {
            sheetService.lockSheet(adminRoleToken, "",
                    LockProperties.PERMANENT_WRITE_LOCK);
            fail("The exception has not been thrown.");
        }
        catch (IllegalArgumentException ex) {
            // ok
        }
        catch (Throwable e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Tests the method lockSheet with an incorrect container lock type.
     */
    public void testIncorrectContainerLockTypeCase() {
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
        String lSheetId = lSheetSummary.get(0).getId();

        try {
            // Change the sheet type to READ_LOCK
            sheetService.lockSheet(adminRoleToken, lSheetId,
                    new LockProperties(null));
            fail("The exception has not been thrown.");
        }
        catch (IllegalArgumentException ex) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an IllegalArgumentException.");
        }
    }
}
