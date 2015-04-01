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
 * Tests the method getCacheableSheetInitializationModel(String, String, String)
 * of the Sheet Service.
 * 
 * @author mmennad
 */
public class TestGetCacheableSheetInitializationModel extends
        AbstractBusinessServiceTestCase {
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
        CacheableSheet lOriginalCacheableSheet =
                sheetService.getCacheableSheet(lAdminRoleToken, lSourceSheetId,
                        CacheProperties.MUTABLE);

        CacheableSheet lCopiedSheet =
                sheetService.getCacheableSheetInitializationModel(
                        lAdminRoleToken, lOriginalCacheableSheet.getTypeId(),
                        lOriginalCacheableSheet.getId());

        //Check both are equals - except the Id
        assertNotSame(lOriginalCacheableSheet.getId(), lCopiedSheet.getId());
        assertEquals(lOriginalCacheableSheet.getProductName(),
                lCopiedSheet.getProductName());
        assertEquals(lOriginalCacheableSheet.getTypeId(),
                lCopiedSheet.getTypeId());
        assertEquals(lOriginalCacheableSheet.getTypeName(),
                lCopiedSheet.getTypeName());
        assertEquals(lOriginalCacheableSheet.getVersion(),
                lCopiedSheet.getVersion());
        assertEquals(lOriginalCacheableSheet.getEnvironmentNames(),
                lCopiedSheet.getEnvironmentNames());
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

        CacheableSheet lCopiedSheet = null;

        //Check
        try {
            lCopiedSheet =
                    sheetService.getCacheableSheetInitializationModel(
                            lAdminRoleToken,
                            lOriginalCacheableSheet.getTypeId(),
                            INVALID_CONTAINER_ID);
        }
        catch (InvalidIdentifierException e) {
            assertTrue("InvalidIdentifierException", true);
        }
        finally {
            assertNull(lCopiedSheet);
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

        CacheableSheet lCopiedSheet = null;

        //Check
        try {
            lCopiedSheet =
                    sheetService.getCacheableSheetInitializationModel(
                            INVALID_CONTAINER_ID,
                            lOriginalCacheableSheet.getTypeId(),
                            lOriginalCacheableSheet.getId());
        }
        catch (InvalidTokenException e) {
            assertTrue("InvalidTokenException", true);
        }
        finally {
            assertNull(lCopiedSheet);
        }

    }

    /**
     * Tests the method with a wrong type sheet role
     */
    public void testInvalidTypeCase() {
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

        CacheableSheet lCopiedSheet = null;

        //Check
        try {
            lCopiedSheet =
                    sheetService.getCacheableSheetInitializationModel(
                            lAdminRoleToken, INVALID_CONTAINER_ID,
                            lOriginalCacheableSheet.getId());
        }
        catch (InvalidIdentifierException e) {
            assertTrue("InvalidIdentifierException", true);
        }
        finally {
            assertNull(lCopiedSheet);
        }

    }

}