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

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.impl.SheetServiceImpl;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method getCacheableSheetModel(String, String, String) of the Sheet
 * Service.
 * 
 * @author mmennad
 */
public class TestGetCacheableSheetModel extends AbstractBusinessServiceTestCase {

    private final static String FIELD_MAPPING_XML =
            "values/TestFieldMapping.xml";

    private SheetServiceImpl sheetService =
            (SheetServiceImpl) ContextLocator.getContext().getBean(
                    "sheetServiceImpl");

    /**
     * Tests the method in case of correct parameters were sent
     */
    public void testNormalCase() {
        instantiate(getProcessName(), FIELD_MAPPING_XML);
        // Admin login
        String lAdminUserToken =
                authorizationService.login(GpmTestValues.USER_ADMIN,
                        GpmTestValues.USER_ADMIN);
        String lAdminRoleToken =
                authorizationService.selectRole(lAdminUserToken,
                        GpmTestValues.USER_ADMIN, getProductName(),
                        getProcessName());
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(lAdminRoleToken,
                        GpmTestValues.SHEET_TYPE_SHEETTYPE1,
                        CacheProperties.IMMUTABLE);

        CacheableSheet lCacheableSheetModel =
                sheetService.getCacheableSheetModel(lAdminRoleToken, lType,
                        GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME,
                        Context.getContext());
        //Check the Cacheable sheet model exists
        assertNotNull(lCacheableSheetModel);
        //Check it's made from the same type from the Sheet Type given as a parameter
        assertEquals(lCacheableSheetModel.getTypeName(),
                GpmTestValues.SHEET_TYPE_SHEETTYPE1);

    }

    /**
     * Tests the method with a wrong product name
     */
    public void testInvalidProductNameCase() {
        instantiate(getProcessName(), FIELD_MAPPING_XML);
        // Admin login
        String lAdminUserToken =
                authorizationService.login(GpmTestValues.USER_ADMIN,
                        GpmTestValues.USER_ADMIN);
        String lAdminRoleToken =
                authorizationService.selectRole(lAdminUserToken,
                        GpmTestValues.USER_ADMIN, getProductName(),
                        getProcessName());
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(lAdminRoleToken,
                        GpmTestValues.SHEET_TYPE_SHEETTYPE1,
                        CacheProperties.IMMUTABLE);

        CacheableSheet lCacheableSheetModel = null;
        //Check
        try {
            lCacheableSheetModel =
                    sheetService.getCacheableSheetModel(lAdminRoleToken, lType,
                            INVALID_CONTAINER_ID, Context.getContext());
            //Check the Cacheable sheet model exists
            assertNotNull(lCacheableSheetModel);
            //Check it's made from the same type from the Sheet Type given as a parameter
            assertEquals(lCacheableSheetModel.getTypeName(),
                    GpmTestValues.SHEET_TYPE_SHEETTYPE1);
        }
        catch (InvalidNameException e) {
            assertTrue("InvalidNameException", true);
        }
        finally {
            assertNull(lCacheableSheetModel);
        }

    }

    /**
     * Tests the method with a wrong role
     */
    public void testInvalidRoleCase() {
        instantiate(getProcessName(), FIELD_MAPPING_XML);

        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        GpmTestValues.SHEET_TYPE_SHEETTYPE1,
                        CacheProperties.IMMUTABLE);

        CacheableSheet lCacheableSheetModel = null;
        //Check
        try {
            lCacheableSheetModel =
                    sheetService.getCacheableSheetModel(INVALID_CONTAINER_ID,
                            lType, GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME,
                            Context.getContext());
            //Check the Cacheable sheet model exists
            assertNotNull(lCacheableSheetModel);
            //Check it's made from the same type from the Sheet Type given as a parameter
            assertEquals(lCacheableSheetModel.getTypeName(),
                    GpmTestValues.SHEET_TYPE_SHEETTYPE1);
        }
        catch (InvalidTokenException e) {
            assertTrue("InvalidTokenException", true);
        }
        finally {
            assertNull(lCacheableSheetModel);
        }

    }

}