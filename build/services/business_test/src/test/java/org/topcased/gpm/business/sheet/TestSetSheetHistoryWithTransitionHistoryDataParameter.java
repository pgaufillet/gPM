/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Mimoun Mennad (ATOS), Pierre-Hubert Tsaan (ATOS)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.serialization.data.TransitionHistoryData;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetHistoryData;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * This class tests the setSheetHistory(String, String, SheetHistoryData[])
 * method from the SheetService implementation.
 * 
 * @author mmennad
 */
public class TestSetSheetHistoryWithTransitionHistoryDataParameter extends
        AbstractBusinessServiceTestCase {

    /**
     * Test the method with an empty history
     */
    public void testEmptyHistoryCase() {

        sheetService = serviceLocator.getSheetService();

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
                        GpmTestValues.SHEET_TYPE_CAT, CacheProperties.MUTABLE);

        // Next the model of a sheet.
        CacheableSheet lCacheableSheetBefore =
                sheetService.getCacheableSheetModel(lAdminRoleToken,
                        lSheetType, getProductName(), Context.getContext());
        //Create a sheet to get its Id
        String lSheetId =
                sheetService.createSheet(lAdminRoleToken,
                        lCacheableSheetBefore, Context.getContext());
        assertNotNull(lCacheableSheetBefore.getCurrentStateName());
        assertNotNull(lSheetId);
        //Gets the sheet
        lCacheableSheetBefore =
                sheetService.getCacheableSheet(lAdminRoleToken, lSheetId,
                        CacheProperties.MUTABLE);
        assertNotNull(lCacheableSheetBefore.getCurrentStateName());

        CacheableSheet lCacheableSheetAfter =
                sheetService.getCacheableSheet(lAdminRoleToken, lSheetId,
                        CacheProperties.MUTABLE);

        //Creates an empty history

        List<TransitionHistoryData> lTransitionData =
                new ArrayList<TransitionHistoryData>();
        //Sets it in the sheet
        sheetService.setSheetHistory(lAdminRoleToken, lSheetId, lTransitionData);
        lCacheableSheetAfter =
                sheetService.getCacheableSheet(lAdminRoleToken, lSheetId,
                        CacheProperties.MUTABLE);

        SheetHistoryData[] lNewSheetHistoryData =
                sheetService.getSheetHistory(lAdminRoleToken,
                        lCacheableSheetAfter.getId());
        //Check the sheet history has not been erased
        assertEquals(0, lNewSheetHistoryData.length);

    }

    /**
     * Test the method with correct parameters
     */
    public void testNormalCase() {

        sheetService = serviceLocator.getSheetService();

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
                        GpmTestValues.SHEET_TYPE_CAT, CacheProperties.MUTABLE);

        // Next the model of a sheet.
        CacheableSheet lCacheableSheetBefore =
                sheetService.getCacheableSheetModel(lAdminRoleToken,
                        lSheetType, getProductName(), Context.getContext());
        //Create a sheet to get its Id
        String lSheetId =
                sheetService.createSheet(lAdminRoleToken,
                        lCacheableSheetBefore, Context.getContext());
        assertNotNull(lCacheableSheetBefore.getCurrentStateName());
        assertNotNull(lSheetId);
        //Gets the sheet
        lCacheableSheetBefore =
                sheetService.getCacheableSheet(lAdminRoleToken, lSheetId,
                        CacheProperties.MUTABLE);
        assertNotNull(lCacheableSheetBefore.getCurrentStateName());
        //Change its State to have an history
        sheetService.changeState(lAdminRoleToken, lSheetId, "Validate",
                Context.getContext());
        //Check
        CacheableSheet lCacheableSheetAfter =
                sheetService.getCacheableSheet(lAdminRoleToken, lSheetId,
                        CacheProperties.MUTABLE);
        //Once a sheet is validated, its state is open
        assertEquals(lCacheableSheetAfter.getCurrentStateName(), "Open");
        //Gathers the History information

        //Change the state again
        sheetService.changeState(lAdminRoleToken, lSheetId, "Close",
                Context.getContext());
        lCacheableSheetAfter =
                sheetService.getCacheableSheet(lAdminRoleToken, lSheetId,
                        CacheProperties.MUTABLE);
        //Once a validated sheet is closed, its state is closed
        assertEquals(lCacheableSheetAfter.getCurrentStateName(), "Closed");
        //Creates an empty history

        List<TransitionHistoryData> lTransitionData =
                new ArrayList<TransitionHistoryData>();
        //Sets it in the sheet
        sheetService.setSheetHistory(lAdminRoleToken, lSheetId, lTransitionData);
        lCacheableSheetAfter =
                sheetService.getCacheableSheet(lAdminRoleToken, lSheetId,
                        CacheProperties.MUTABLE);

        List<TransitionHistoryData> lNewSheetHistoryData =

        lCacheableSheetAfter.getTransitionsHistory();
        //Check the sheet history has not been erased
        assertNotSame(lTransitionData, lNewSheetHistoryData);

    }

}
