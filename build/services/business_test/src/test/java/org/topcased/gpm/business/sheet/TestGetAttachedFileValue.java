/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Mimoun Mennad (ATOS), Pierre Hubert Tsaan
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.util.NoSuchElementException;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.domain.fields.AttachedFieldValue;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * This class tests the getAttachedFile() method from the SheetService
 * implementation.
 * 
 * @author mmennad
 */
public class TestGetAttachedFileValue extends AbstractBusinessServiceTestCase {

    private static final String CAT_SHEET_REF = "Tom";

    private static final String CAT_FILE_NAME_VALUE = "cat1.jpg";

    private static final String CAT_SHEET_MIME_TYPE_VALUE = "image/jpeg";

    /**
     * Test the method with correct parameters
     */

    public void testNormalCase() {
        // Admin login
        String lAdminUserToken =
                authorizationService.login(GpmTestValues.USER_ADMIN,
                        GpmTestValues.USER_ADMIN);
        String lAdminRoleToken =
                authorizationService.selectRole(lAdminUserToken,
                        GpmTestValues.USER_ADMIN,
                        GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME,
                        getProcessName());
        sheetService = serviceLocator.getSheetService();
        //Gets an Id from the reference
        String lId =
                sheetService.getSheetIdByReference(lAdminRoleToken,
                        CAT_SHEET_REF);
        //Gets a cacheablesheet with the Id
        CacheableSheet lCacheableSheet =
                sheetService.getCacheableSheet(lAdminRoleToken, lId,
                        CacheProperties.IMMUTABLE);
        //Gets the file
        AttachedFieldValue lAttachedFieldValue =
                sheetService.getAttachedFileValue(
                        lAdminRoleToken,
                        lCacheableSheet.getAllAttachedFileValues().iterator().next().getId());

        assertNotNull(lAttachedFieldValue);
        assertEquals(lAttachedFieldValue.getName(), CAT_FILE_NAME_VALUE);
        assertEquals(lAttachedFieldValue.getMimeType(),
                CAT_SHEET_MIME_TYPE_VALUE);

    }

    /**
     * Test the method with a sheet which doesn't have any file attached
     */

    public void testSheetWithoutFileCase() {
        // Admin login
        String lAdminUserToken =
                authorizationService.login(GpmTestValues.USER_ADMIN,
                        GpmTestValues.USER_ADMIN);
        String lAdminRoleToken =
                authorizationService.selectRole(lAdminUserToken,
                        GpmTestValues.USER_ADMIN,
                        GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME,
                        getProcessName());
        sheetService = serviceLocator.getSheetService();
        //Gets an Id from the reference
        String lId =
                sheetService.getSheetIdByReference(lAdminRoleToken,
                        GpmTestValues.SHEET_REF_GARFIELD);
        //Gets a cacheablesheet with the Id
        CacheableSheet lCacheableSheet =
                sheetService.getCacheableSheet(lAdminRoleToken, lId,
                        CacheProperties.IMMUTABLE);
        //Tries to Get the file
        AttachedFieldValue lAttachedFieldValue = null;

        try {
            lAttachedFieldValue =
                    sheetService.getAttachedFileValue(
                            lAdminRoleToken,
                            lCacheableSheet.getAllAttachedFileValues().iterator().next().getId());

            assertNotNull(lAttachedFieldValue);

        }
        catch (NoSuchElementException e) {

        }
        finally {
            assertNull(lAttachedFieldValue);

        }

    }

}
