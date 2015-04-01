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
 * Tests the methods <CODE>getAdminAccessControl<CODE>
 * and <CODE>setAdminAccessControl<CODE>
 * of the Authorization Service.
 * 
 * @author mfranche
 */
public class TestSetGetAdminAccessControlService extends
        AbstractBusinessServiceTestCase {

    /** The action key. */
    private static final String[] LABEL_KEY = { "key1", "key2" };

    /** The role name. */
    private static final String ROLE_NAME = GpmTestValues.USER_ADMIN;

    /**
     * Tests the methods getAdminAccessControl and setAdminAccessControl in
     * normal cases.
     */
    public void testNormalCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Create first admin access control
        AdminAccessControlData lAdminAccessControl1 =
                new AdminAccessControlData(LABEL_KEY[0]);
        lAdminAccessControl1.setBusinessProcessName(getProcessName());

        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setProductName(getProductName());
        lAccessControlContextData.setRoleName(ROLE_NAME);
        lAdminAccessControl1.setContext(lAccessControlContextData);

        authorizationService.setAdminAccessControl(lAdminAccessControl1);

        // Create snd admin access control
        AdminAccessControlData lAdminAccessControl2 =
                new AdminAccessControlData(LABEL_KEY[1]);
        lAdminAccessControl2.setBusinessProcessName(getProcessName());

        AccessControlContextData lAccessControlContextData2 =
                new AccessControlContextData();
        lAccessControlContextData2.setProductName(getProductName());
        lAccessControlContextData2.setRoleName(ROLE_NAME);
        lAdminAccessControl2.setContext(lAccessControlContextData2);

        authorizationService.setAdminAccessControl(lAdminAccessControl2);

        // Retrieving the Admin Access Control
        AdminAccessControlData lAdminAccessControlData =
                authorizationService.getAdminAccessControl(adminRoleToken,
                        lAccessControlContextData, LABEL_KEY[0]);

        assertNotNull(
                "The method getApplicationActionAccessControl returns null.",
                lAdminAccessControlData);

        // Verifies the fields
        String lProcessName = lAdminAccessControlData.getBusinessProcessName();

        assertEquals("Process name expected = " + getProcessName()
                + ". Process name actual = " + lProcessName + ".",
                getProcessName(), lProcessName);

        String lLabelKey = lAdminAccessControlData.getLabelKey();
        assertEquals("Label Key expected = " + LABEL_KEY[0]
                + ". Label Key actual = " + lLabelKey + ".", LABEL_KEY[0],
                lLabelKey);

        String lProductName =
                lAdminAccessControlData.getContext().getProductName();
        assertEquals("Product name expected = " + getProductName()
                + ". Product name actual = " + lProductName + ".",
                getProductName(), lProductName);

        String lRoleName = lAdminAccessControlData.getContext().getRoleName();
        assertEquals("Role name expected = " + ROLE_NAME
                + ". Role name actual = " + lRoleName + ".", ROLE_NAME,
                lRoleName);

        AdminAccessControlData lAdminAccessControlData2 =
                authorizationService.getAdminAccessControl(adminRoleToken,
                        lAccessControlContextData2, LABEL_KEY[1]);

        assertNotNull(
                "The method getApplicationActionAccessControl returns null.",
                lAdminAccessControlData2);

        // Verifies the fields
        lProcessName = lAdminAccessControlData2.getBusinessProcessName();

        assertEquals("Process name expected = " + getProcessName()
                + ". Process name actual = " + lProcessName + ".",
                getProcessName(), lProcessName);

        lLabelKey = lAdminAccessControlData2.getLabelKey();
        assertEquals("Label Key expected = " + LABEL_KEY[1]
                + ". Label Key actual = " + lLabelKey + ".", LABEL_KEY[1],
                lLabelKey);

        lProductName = lAdminAccessControlData2.getContext().getProductName();
        assertEquals("Product name expected = " + getProductName()
                + ". Product name actual = " + lProductName + ".",
                getProductName(), lProductName);

        lRoleName = lAdminAccessControlData2.getContext().getRoleName();
        assertEquals("Role name expected = " + ROLE_NAME
                + ". Role name actual = " + lRoleName + ".", ROLE_NAME,
                lRoleName);
    }

    /**
     * Tests the method getAdminAccessControl when no admin access has been set.
     */
    public void testNotDefinedControlCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        AdminAccessControlData lAdminAccessControlData =
                authorizationService.getAdminAccessControl(adminRoleToken,
                        lAccessControlContextData, LABEL_KEY[0]);

        assertNull(
                "The method getAdminAccessControl should have returned null.",
                lAdminAccessControlData);
    }

    /**
     * Test the method getAdminAccessControl with a null action key
     */
    public void testNullActionKey() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        try {
            AccessControlContextData lAccessControlContextData =
                    new AccessControlContextData();
            authorizationService.getAdminAccessControl(adminRoleToken,
                    lAccessControlContextData, (String) null);
        }
        catch (IllegalArgumentException lIllegalArgumentException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an IllegalArgumentException.");
        }
    }

    /**
     * Test the method getAdminAccessControl with no specified product
     */
    public void testNoProductCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Create first admin access control
        AdminAccessControlData lAdminAccessControl1 =
                new AdminAccessControlData(LABEL_KEY[0]);
        lAdminAccessControl1.setBusinessProcessName(getProcessName());

        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setRoleName(ROLE_NAME);
        lAdminAccessControl1.setContext(lAccessControlContextData);

        authorizationService.setAdminAccessControl(lAdminAccessControl1);

        // Retrieving the Admin Access Control
        AdminAccessControlData lAdminAccessControlData =
                authorizationService.getAdminAccessControl(adminRoleToken,
                        lAccessControlContextData, LABEL_KEY[0]);

        assertNotNull(
                "The method getApplicationActionAccessControl returns null.",
                lAdminAccessControlData);

        // Verifies the fields
        String lProcessName = lAdminAccessControlData.getBusinessProcessName();

        assertEquals("Process name expected = " + getProcessName()
                + ". Process name actual = " + lProcessName + ".",
                getProcessName(), lProcessName);

        String lLabelKey = lAdminAccessControlData.getLabelKey();
        assertEquals("Label Key expected = " + LABEL_KEY[0]
                + ". Label Key actual = " + lLabelKey + ".", LABEL_KEY[0],
                lLabelKey);

        String lRoleName = lAdminAccessControlData.getContext().getRoleName();
        assertEquals("Role name expected = " + ROLE_NAME
                + ". Role name actual = " + lRoleName + ".", ROLE_NAME,
                lRoleName);
    }

    /**
     * In this test, there are 2 admin access defined on the same key : one with
     * only product specified, on with product and role specified
     */
    public void testMultipleAdminAccessOnSameKeyCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Create first admin access control : only product is specified
        AdminAccessControlData lAdminAccessControl2 =
                new AdminAccessControlData(LABEL_KEY[0]);
        lAdminAccessControl2.setBusinessProcessName(getProcessName());

        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setProductName(getProductName());
        lAdminAccessControl2.setContext(lAccessControlContextData);

        authorizationService.setAdminAccessControl(lAdminAccessControl2);

        // Create snd admin access control : product and role are specified
        AdminAccessControlData lAdminAccessControl3 =
                new AdminAccessControlData(LABEL_KEY[0]);
        lAdminAccessControl3.setBusinessProcessName(getProcessName());

        lAccessControlContextData = new AccessControlContextData();
        lAccessControlContextData.setProductName(getProductName());
        lAccessControlContextData.setRoleName(ROLE_NAME);
        lAdminAccessControl3.setContext(lAccessControlContextData);

        authorizationService.setAdminAccessControl(lAdminAccessControl3);

        lAccessControlContextData = new AccessControlContextData();
        lAccessControlContextData.setProductName(getProductName());
        lAccessControlContextData.setRoleName(ROLE_NAME);

        AdminAccessControlData lAdminAccessControlData =
                authorizationService.getAdminAccessControl(adminRoleToken,
                        lAccessControlContextData, LABEL_KEY[0]);

        assertNotNull(lAdminAccessControlData);
        assertNotNull(lAdminAccessControlData.getContext().getRoleName());
        assertEquals(ROLE_NAME,
                lAdminAccessControlData.getContext().getRoleName());
        assertNotNull(getProductName(),
                lAdminAccessControlData.getContext().getProductName());
    }
}
