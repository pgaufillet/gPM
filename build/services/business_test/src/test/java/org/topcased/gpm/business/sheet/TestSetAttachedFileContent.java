/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Mimoun Mennad (ATOS), Pierre Hubert Tsaan (ATOS)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.io.IOException;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * This class tests the setAttachedFileContent() method from the SheetService
 * implementation.
 * 
 * @author mmennad
 */
public class TestSetAttachedFileContent extends AbstractBusinessServiceTestCase {

    private static final String CAT_SHEET_REF = "Tom";

    /**
     * Test the method with correct parameters
     * 
     * @throws IOException
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
        byte[] lFile =
                sheetService.getAttachedFileContent(
                        lAdminRoleToken,
                        lCacheableSheet.getAllAttachedFileValues().iterator().next().getId());

        assertNotNull(lFile);
        assertTrue(lFile.length > 0);

        byte[] lEmptyFile = new byte[0];

        //Attaches it to the sheet
        sheetService.setAttachedFileContent(
                lAdminRoleToken,
                lCacheableSheet.getAllAttachedFileValues().iterator().next().getId(),
                lEmptyFile);

        //Check
        byte[] lOutputFile =
                sheetService.getAttachedFileContent(
                        lAdminRoleToken,
                        lCacheableSheet.getAllAttachedFileValues().iterator().next().getId());
        assertNotNull(lOutputFile);
        assertTrue(lOutputFile.length == 0);

        //Attaches it to the sheet
        sheetService.setAttachedFileContent(
                lAdminRoleToken,
                lCacheableSheet.getAllAttachedFileValues().iterator().next().getId(),
                lFile);

        lFile =
                sheetService.getAttachedFileContent(
                        lAdminRoleToken,
                        lCacheableSheet.getAllAttachedFileValues().iterator().next().getId());
        assertNotNull(lOutputFile);
        assertTrue(lOutputFile.length == 0);

        assertNotNull(lFile);
        assertTrue(lFile.length > 0);

    }
}