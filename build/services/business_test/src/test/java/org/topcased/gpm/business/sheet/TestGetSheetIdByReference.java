/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Mimoun Mennad (ATOS), Thibault Landre (ATOS), Pierre-Hubert Tsaan (ATOS)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * This class tests the getSheetIdByReference(String, String) method from the
 * SheetService implementation.
 * 
 * @author mmennad
 */
public class TestGetSheetIdByReference extends AbstractBusinessServiceTestCase {
    private static final String INSTANCE_FILE =
            "authorization/TestGetSubFields.xml";

    private static final String[] USER = { "user10", "pwd10" };

    private static final String ROLE_CONFIDENTIAL = "confidential";

    private static final String SHEET_REFERENCE = "accessControlMultiple_1:9";

    /**
     * Test the method with correct parameters
     */
    public void testNormalCase() {

        authorizationService = serviceLocator.getAuthorizationService();
        instantiate(getProcessName(), INSTANCE_FILE);
        String lUserToken = authorizationService.login(USER[0], USER[1]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_CONFIDENTIAL,
                        GpmTestValues.PRODUCT1_NAME, getProcessName());

        sheetService = serviceLocator.getSheetService();

        //Check
        String lId =
                sheetService.getSheetIdByReference(lRoleToken, SHEET_REFERENCE);
        assertNotNull(lId);

        //Let's see if the admin can check it
        assertTrue(sheetService.isSheetIdExist(adminRoleToken, lId));

        CacheableSheet lSheet =
                sheetService.getCacheableSheet(adminRoleToken, lId,
                        CacheProperties.MUTABLE);

        assertEquals(GpmTestValues.PRODUCT1_NAME, lSheet.getProductName());

    }

    /**
     * Test the method with a wrong reference case
     */
    public void testInvalidReferenceCase() {

        authorizationService = serviceLocator.getAuthorizationService();
        instantiate(getProcessName(), INSTANCE_FILE);
        String lUserToken = authorizationService.login(USER[0], USER[1]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_CONFIDENTIAL,
                        GpmTestValues.PRODUCT1_NAME, getProcessName());

        sheetService = serviceLocator.getSheetService();

        //Check

        String lId =
                sheetService.getSheetIdByReference(lRoleToken,
                        INVALID_CONTAINER_ID);
        assertNull(lId);

    }

}
