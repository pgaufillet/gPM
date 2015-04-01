/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Mimoun Mennad (ATOS)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.serialization.data.Lock;
import org.topcased.gpm.business.serialization.data.Lock.LockTypeEnumeration;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.util.bean.LockProperties;

/**
 * Tests the method getLock() of the Sheet Service.
 * 
 * @author mmennad
 */
public class TestGetLock extends AbstractBusinessServiceTestCase {
    /**
     * Test the method with correct parameters
     */
    public void testNormalCase() {
        String lAdminUserToken =
                authorizationService.login(GpmTestValues.USER_ADMIN,
                        GpmTestValues.USER_ADMIN);
        String lAdminRoleToken =
                authorizationService.selectRole(lAdminUserToken,
                        GpmTestValues.USER_ADMIN, getProductName(),
                        getProcessName());
        // User login
        String lUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, "notadmin",
                        getProductName(), getProcessName());
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheet type
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(),
                        GpmTestValues.SHEET_TYPE_DOG);
        assertNotNull("Sheet type " + GpmTestValues.SHEET_TYPE_DOG
                + " not found.", lSheetSummary);

        // Gets the sheetSummaryData

        SheetSummaryData lSheetData = lSheetSummary.iterator().next();
        //Remove the lock 
        sheetService.removeLock(lAdminRoleToken, lSheetData.getId());
        //Check the sheet is unlocked
        assertFalse(sheetService.isSheetLocked(lRoleToken, lSheetData.getId(),
                DisplayMode.EDITION));
        sheetService.lockSheet(lAdminRoleToken, lSheetData.getId(),
                new LockProperties(LockTypeEnumeration.WRITE));
        //Check the sheet is now locked
        assertTrue(sheetService.isSheetLocked(lRoleToken, lSheetData.getId(),
                DisplayMode.EDITION));

        Lock lLock = sheetService.getLock(lAdminRoleToken, lSheetData.getId());

        //Check the lock exists
        assertNotNull("The sheet has no container lock.", lLock);
        assertEquals("The container lock user name is incorrect.",
                lLock.getOwner(), GpmTestValues.USER_ADMIN);
        assertNotNull("The container lock type is null.", lLock.getType());
        assertEquals("The container lock type is incorrect.", lLock.getType(),
                LockTypeEnumeration.WRITE);

        sheetService.removeLock(lAdminRoleToken, lSheetData.getId());

        assertFalse(sheetService.isSheetLocked(lRoleToken, lSheetData.getId(),
                DisplayMode.EDITION));

        //

    }

}
