/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.authorization.service.AdminAccessControlData;

/**
 * Tests the methods <CODE>isAdminAccessDefineOnInstance<CODE>
 * of the Authorization Service.
 * 
 * @author mfranche
 */
public class TestIsAdminAccessDefineOnInstanceService extends
        AbstractBusinessServiceTestCase {

    /** The action key. */
    private static final String LABEL_KEY = "key1";

    private static final String ROLE_NAME = "viewer";

    /**
     * Tests the method isAdminAccessDefineOnInstance with an adminAccess define
     * on a role (without product) and the same role defined on instance
     */
    public void testNormalWithInstanceRoleCase() {
        // Login with viewer 4
        String lUserToken =
                authorizationService.login(GpmTestValues.USER_VIEWER4, "pwd4");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        getProductName(), getProcessName());

        // Create and set admin access control
        AdminAccessControlData lAdminAccessControl1 =
                new AdminAccessControlData(LABEL_KEY);
        lAdminAccessControl1.setBusinessProcessName(getProcessName());

        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setRoleName(ROLE_NAME);
        lAdminAccessControl1.setContext(lAccessControlContextData);

        authorizationService.setAdminAccessControl(lAdminAccessControl1);

        // Check if the admin access is set on instance
        boolean lIsAdminAccessOnInstance =
                authorizationService.isAdminAccessDefinedOnInstance(lRoleToken,
                        LABEL_KEY);

        assertTrue("The admin access is defined on all instance.",
                lIsAdminAccessOnInstance);
    }

    /**
     * Tests the method isAdminAccessDefineOnInstance with an admin access
     * define on a role (without product) and the same role defined on product
     */
    public void testNormalWithProductRoleCase() {
        // Login with viewer 1
        String lUserToken =
                authorizationService.login(GpmTestValues.USER_VIEWER1, "pwd1");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        GpmTestValues.PRODUCT1_NAME, getProcessName());

        // Create and set admin access control
        AdminAccessControlData lAdminAccessControl1 =
                new AdminAccessControlData(LABEL_KEY);
        lAdminAccessControl1.setBusinessProcessName(getProcessName());

        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setRoleName(ROLE_NAME);
        lAdminAccessControl1.setContext(lAccessControlContextData);

        authorizationService.setAdminAccessControl(lAdminAccessControl1);

        // Check if the admin access is set on instance
        boolean lIsAdminAccessOnInstance =
                authorizationService.isAdminAccessDefinedOnInstance(lRoleToken,
                        LABEL_KEY);

        assertFalse("The admin access is not defined on all instance.",
                lIsAdminAccessOnInstance);
    }

    /**
     * Test the method isAdminAccessDefineOnInstance with an admin access define
     * on a role and a product.
     */
    public void testNormalWithAdminAccessOnProductCase() {
        // Login with viewer 1
        String lUserToken =
                authorizationService.login(GpmTestValues.USER_VIEWER4, "pwd4");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        getProductName(), getProcessName());

        // Create and set admin access control
        AdminAccessControlData lAdminAccessControl1 =
                new AdminAccessControlData(LABEL_KEY);
        lAdminAccessControl1.setBusinessProcessName(getProcessName());

        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setRoleName(ROLE_NAME);
        lAccessControlContextData.setProductName(getProductName());
        lAdminAccessControl1.setContext(lAccessControlContextData);

        authorizationService.setAdminAccessControl(lAdminAccessControl1);

        // Check if the admin access is set on instance
        boolean lIsAdminAccessOnInstance =
                authorizationService.isAdminAccessDefinedOnInstance(lRoleToken,
                        LABEL_KEY);

        assertFalse("The admin access is not defined on all instance.",
                lIsAdminAccessOnInstance);
    }

    /**
     * Test the method isAdminAccessDefineOnProduct with an admin access define
     * only on a product (no role specified).
     */
    public void testNormalWithAdminAccessOnProductWithoutRoleCase() {
        // Login with viewer 1
        String lUserToken =
                authorizationService.login(GpmTestValues.USER_VIEWER4, "pwd4");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        getProductName(), getProcessName());

        // Create and set admin access control
        AdminAccessControlData lAdminAccessControl1 =
                new AdminAccessControlData(LABEL_KEY);
        lAdminAccessControl1.setBusinessProcessName(getProcessName());

        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setProductName(getProductName());
        lAdminAccessControl1.setContext(lAccessControlContextData);

        authorizationService.setAdminAccessControl(lAdminAccessControl1);

        // Check if the admin access is set on instance
        boolean lIsAdminAccessOnInstance =
                authorizationService.isAdminAccessDefinedOnInstance(lRoleToken,
                        LABEL_KEY);

        assertFalse("The admin access is not defined on all instance.",
                lIsAdminAccessOnInstance);
    }
}
