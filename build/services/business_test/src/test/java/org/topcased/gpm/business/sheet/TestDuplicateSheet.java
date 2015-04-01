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
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method DuplicateSheet(String, String, String) of the Sheet Service.
 * 
 * @author mmennad
 */
public class TestDuplicateSheet extends AbstractBusinessServiceTestCase {
    /**
     * Tests the method in case of correct parameters were sent
     */
    public void testNormalCase() {
        // Admin login
        String lAdminUserToken =
                authorizationService.login(GpmTestValues.USER_ADMIN,
                        GpmTestValues.USER_ADMIN);
        String lAdminRoleToken =
                authorizationService.selectRole(lAdminUserToken,
                        GpmTestValues.USER_ADMIN, getProductName(),
                        getProcessName());
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheet type
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(),
                        GpmTestValues.SHEET_TYPE_CAT);
        assertNotNull("Sheet type " + GpmTestValues.SHEET_TYPE_CAT
                + " not found.", lSheetSummary);

        //Get a sheet Id to use as a source

        String lSourceSheetId = lSheetSummary.get(0).getId();

        String lDuplicateSheetId =
                sheetService.duplicateSheet(lAdminRoleToken, lSourceSheetId);

        //Check
        assertNotNull(lDuplicateSheetId);
        assertNotSame(lSourceSheetId, lDuplicateSheetId);

    }

    /**
     * Tests the method with a wrong Sheet Id
     */
    public void testInvalidSheetIdCase() {
        // Admin login
        String lAdminUserToken =
                authorizationService.login(GpmTestValues.USER_ADMIN,
                        GpmTestValues.USER_ADMIN);
        String lAdminRoleToken =
                authorizationService.selectRole(lAdminUserToken,
                        GpmTestValues.USER_ADMIN, getProductName(),
                        getProcessName());
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        //Check
        String lDuplicateSheetId = null;

        try {
            lDuplicateSheetId =
                    sheetService.duplicateSheet(lAdminRoleToken,
                            INVALID_CONTAINER_ID);
        }
        catch (InvalidIdentifierException e) {
            assertTrue("InvalidIdentifierException", true);
        }
        finally {
            assertNull(lDuplicateSheetId);
        }

    }

    /**
     * Tests the method with a wrong user role
     */
    public void testInvalidRoleCase() {
        // Admin login
        String lAdminUserToken =
                authorizationService.login(GpmTestValues.USER_ADMIN,
                        GpmTestValues.USER_ADMIN);
        String lAdminRoleToken =
                authorizationService.selectRole(lAdminUserToken,
                        GpmTestValues.USER_ADMIN, getProductName(),
                        getProcessName());
        // Gets the sheet service.
        sheetService = serviceLocator.getSheetService();

        // Gets the sheet type
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(),
                        GpmTestValues.SHEET_TYPE_CAT);
        assertNotNull("Sheet type " + GpmTestValues.SHEET_TYPE_CAT
                + " not found.", lSheetSummary);

        //Get a sheet Id to use as a source

        String lSourceSheetId = lSheetSummary.get(0).getId();
        CacheableSheet lOriginalCacheableSheet =
                sheetService.getCacheableSheet(lAdminRoleToken, lSourceSheetId,
                        CacheProperties.MUTABLE);

        String lDuplicateSheetId = null;

        //Check
        try {
            lDuplicateSheetId =
                    sheetService.duplicateSheet(INVALID_CONTAINER_ID,
                            lOriginalCacheableSheet.getId());
        }
        catch (InvalidTokenException e) {
            assertTrue("InvalidTokenException", true);
        }
        finally {
            assertNull(lDuplicateSheetId);
        }

    }

}