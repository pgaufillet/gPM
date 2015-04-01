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
import org.topcased.gpm.business.serialization.data.SheetData;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method getCacheableSheet() of the Sheet Service.
 * 
 * @author mmennad
 */
public class TestGetCacheableSheetWithSheetDataParameter extends
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
                        GpmTestValues.SHEET_TYPE_DOG);
        assertNotNull("Sheet type " + GpmTestValues.SHEET_TYPE_DOG
                + " not found.", lSheetSummary);

        // Gets a Id
        String lSheetId = lSheetSummary.iterator().next().getId();

        //Gets a sheet by another method
        CacheableSheet lCacheableSheetSource =
                sheetService.getCacheableSheet(lAdminRoleToken, lSheetId,
                        CacheProperties.IMMUTABLE);

        //Gets its reference
        String lCacheableSheetRef =
                lCacheableSheetSource.getFunctionalReference();

        //Use the reference to get the SheetData
        SheetData lSheetData =
                sheetService.getSerializableSheetByRef(lAdminRoleToken,
                        getProcessName(), getProductName(), lCacheableSheetRef);
        CacheableSheet lCacheableSheet =
                sheetService.getCacheableSheet(lAdminRoleToken,
                        getProcessName(), lSheetData);
        //Check that the object has been correctly caught
        assertNotNull(lCacheableSheet);
        //Check that both retrieved sheets are the same
        assertEquals(lCacheableSheetSource.getId(), lCacheableSheet.getId());

    }

}
