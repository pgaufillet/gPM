/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;

/**
 * TestHasAdminAccessService Tests the two methods hasAdminAccess (roleToken)
 * and hasGlobalAdminAccess(roleToken)
 * 
 * @author ahaugommard
 */
public class TestHasAdminAccessService extends AbstractBusinessServiceTestCase {

    public static final String[] LOGIN_1 =
            { GpmTestValues.USER_ADMIN, GpmTestValues.USER_ADMIN };

    public static final String[] LOGIN_2 = { GpmTestValues.USER_USER1, "pwd1" };

    public static final String[] LOGIN_3 = { GpmTestValues.USER_USER2, "pwd2" };

    public static final String ROLE_1 = GpmTestValues.USER_ADMIN;

    public static final String ROLE_2_1 = "notadmin";

    public static final String PRODUCT_2_1 =
            GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME;

    public static final String ROLE_2_2 = GpmTestValues.USER_ADMIN;

    public static final String ROLE_3 = "notadmin";

    public void testNormalCase() {

        // Check global admin user with default product
        String lUserToken1 = authorizationService.login(LOGIN_1[0], LOGIN_1[1]);
        String lRoleToken1 =
                authorizationService.selectRole(lUserToken1, ROLE_1,
                        getProductName(), getProcessName());
        assertTrue(authorizationService.hasAdminAccess(lRoleToken1));
        assertTrue(authorizationService.hasGlobalAdminRole(lRoleToken1));

        // Check user global admin but not admin on a specific product
        String lUserToken2_1 =
                authorizationService.login(LOGIN_2[0], LOGIN_2[1]);
        String lRoleToken2_1 =
                authorizationService.selectRole(lUserToken2_1, ROLE_2_1,
                        PRODUCT_2_1, getProcessName());
        assertFalse(authorizationService.hasAdminAccess(lRoleToken2_1));
        assertTrue(authorizationService.hasGlobalAdminRole(lRoleToken2_1));

        // Check global admin on the default product
        String lUserToken2_2 =
                authorizationService.login(LOGIN_2[0], LOGIN_2[1]);
        String lRoleToken2_2 =
                authorizationService.selectRole(lUserToken2_2, ROLE_2_2,
                        getProductName(), getProcessName());
        assertTrue(authorizationService.hasAdminAccess(lRoleToken2_2));
        assertTrue(authorizationService.hasGlobalAdminRole(lRoleToken2_2));

        // Check user not admin with default product
        String lUserToken3 = authorizationService.login(LOGIN_3[0], LOGIN_3[1]);
        String lRoleToken3 =
                authorizationService.selectRole(lUserToken3, ROLE_3,
                        getProductName(), getProcessName());
        assertFalse(authorizationService.hasAdminAccess(lRoleToken3));
        assertFalse(authorizationService.hasGlobalAdminRole(lRoleToken3));
    }
}
