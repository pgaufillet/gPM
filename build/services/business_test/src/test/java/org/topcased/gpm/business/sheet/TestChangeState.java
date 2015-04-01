/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Mimoun Mennad (ATOS), Pierre-Hubert TSAAN (ATOS)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method ChangeState of the Sheet Service.
 * 
 * @author mmennad
 */
public class TestChangeState extends AbstractBusinessServiceTestCase {

    /**
     * Tests the method in case of correct parameters were sent in order
     * according to the lifecycle
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
        CacheableSheetType lSheetType =
                sheetService.getCacheableSheetTypeByName(lAdminRoleToken,
                        GpmTestValues.SHEET_TYPE_CAT, CacheProperties.IMMUTABLE);

        // Next the model of a sheet.
        CacheableSheet lCacheableSheetBefore =
                sheetService.getCacheableSheetModel(lAdminRoleToken,
                        lSheetType, getProductName(), Context.getContext());
        String lSheetId =
                sheetService.createSheet(lAdminRoleToken,
                        lCacheableSheetBefore, Context.getContext());
        assertNotNull(lCacheableSheetBefore.getCurrentStateName());
        assertNotNull(lSheetId);

        lCacheableSheetBefore =
                sheetService.getCacheableSheet(lAdminRoleToken, lSheetId,
                        CacheProperties.MUTABLE);
        assertNotNull(lCacheableSheetBefore.getCurrentStateName());
        sheetService.changeState(lAdminRoleToken, lSheetId, "Validate",
                Context.getContext());
        //Check
        CacheableSheet lCacheableSheetAfter =
                sheetService.getCacheableSheet(lAdminRoleToken, lSheetId,
                        CacheProperties.MUTABLE);
        //Once a sheet is validated, its state is open
        assertEquals(lCacheableSheetAfter.getCurrentStateName(), "Open");

        sheetService.changeState(lAdminRoleToken, lSheetId, "Close",
                Context.getContext());
        lCacheableSheetAfter =
                sheetService.getCacheableSheet(lAdminRoleToken, lSheetId,
                        CacheProperties.MUTABLE);
        //Once a validated sheet is closed, its state is closed
        assertEquals(lCacheableSheetAfter.getCurrentStateName(), "Closed");
    }

    /**
     * Tests the method with an invalid state name
     */
    public void testInvalidStateCase() {
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
        CacheableSheetType lSheetType =
                sheetService.getCacheableSheetTypeByName(lAdminRoleToken,
                        GpmTestValues.SHEET_TYPE_CAT, CacheProperties.IMMUTABLE);

        // Next the model of a sheet.
        CacheableSheet lCacheableSheetBefore =
                sheetService.getCacheableSheetModel(lAdminRoleToken,
                        lSheetType, getProductName(), Context.getContext());
        assertNotNull(lCacheableSheetBefore.getCurrentStateName());
        String lSheetId =
                sheetService.createSheet(lAdminRoleToken,
                        lCacheableSheetBefore, Context.getContext());
        assertNotNull(lSheetId);

        lCacheableSheetBefore =
                sheetService.getCacheableSheet(lAdminRoleToken, lSheetId,
                        CacheProperties.MUTABLE);
        assertNotNull(lCacheableSheetBefore.getCurrentStateName());
        CacheableSheet lCacheableSheetAfter = null;
        try {
            sheetService.changeState(lAdminRoleToken, lSheetId,
                    INVALID_CONTAINER_ID, Context.getContext());
        }
        catch (IllegalStateException e) {

        }
        finally {
            //Check
            lCacheableSheetAfter =
                    sheetService.getCacheableSheet(lAdminRoleToken, lSheetId,
                            CacheProperties.IMMUTABLE);
            assertEquals(lCacheableSheetBefore.getCurrentStateName(),
                    lCacheableSheetAfter.getCurrentStateName());
        }
    }

    /**
     * Tests the method with an good state name in a wrong order
     */
    public void testWrongOrderStateCase() {
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
        CacheableSheetType lSheetType =
                sheetService.getCacheableSheetTypeByName(lAdminRoleToken,
                        GpmTestValues.SHEET_TYPE_CAT, CacheProperties.IMMUTABLE);

        // Next the model of a sheet.
        CacheableSheet lCacheableSheetBefore =
                sheetService.getCacheableSheetModel(lAdminRoleToken,
                        lSheetType, getProductName(), Context.getContext());
        assertNotNull(lCacheableSheetBefore.getCurrentStateName());
        String lSheetId =
                sheetService.createSheet(lAdminRoleToken,
                        lCacheableSheetBefore, Context.getContext());
        assertNotNull(lSheetId);

        lCacheableSheetBefore =
                sheetService.getCacheableSheet(lAdminRoleToken, lSheetId,
                        CacheProperties.MUTABLE);
        assertNotNull(lCacheableSheetBefore.getCurrentStateName());
        CacheableSheet lCacheableSheetAfter = null;
        try {
            //You can't close a non validated sheet
            sheetService.changeState(lAdminRoleToken, lSheetId, "Close",
                    Context.getContext());
        }
        catch (IllegalStateException e) {

        }
        finally {
            //Check
            lCacheableSheetAfter =
                    sheetService.getCacheableSheet(lAdminRoleToken, lSheetId,
                            CacheProperties.MUTABLE);
            assertEquals(lCacheableSheetBefore.getCurrentStateName(),
                    lCacheableSheetAfter.getCurrentStateName());
        }
    }

}