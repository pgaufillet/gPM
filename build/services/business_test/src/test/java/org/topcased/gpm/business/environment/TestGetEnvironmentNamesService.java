/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.environment;

import java.util.Arrays;
import java.util.Collection;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.environment.service.EnvironmentService;

/**
 * Tests the method <CODE>getEnvironmentNames<CODE> of the Environment
 * Service.
 * 
 * @author nsamson
 */
public class TestGetEnvironmentNamesService extends
        AbstractBusinessServiceTestCase {

    /** The Environment Service. */
    private EnvironmentService environmentService;

    /** The environments names. (both private and public) */
    private static final String[] ENVIRONMENT_NAMES =
            { GpmTestValues.ENVIRONMENT_PROFESSIONAL,
             GpmTestValues.ENVIRONMENT_CLASSICAL,
             GpmTestValues.ENVIRONMENT_ENV1, GpmTestValues.ENVIRONMENT_ENV2,
             "Classical_1" };

    private static final String[] PUBLIC_ENVIRONMENT_NAMES =
            { GpmTestValues.ENVIRONMENT_PROFESSIONAL,
             GpmTestValues.ENVIRONMENT_CLASSICAL,
             GpmTestValues.ENVIRONMENT_ENV1, GpmTestValues.ENVIRONMENT_ENV2 };

    /**
     * Tests the getEnvironmentNames method.
     */
    public void testNormalCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();

        // Retrieving the environments names
        Collection<String> lNames =
                environmentService.getEnvironmentNames(adminRoleToken,
                        getProcessName());

        assertNotNull("Method getEnvironmentNames returns null.", lNames);
        int lSize = lNames.size();
        int lExpectedSize = ENVIRONMENT_NAMES.length;
        assertEquals("Method getEnvironmentNames returns " + lSize
                + " environments names instead of " + lExpectedSize,
                lExpectedSize, lSize);
        assertTrue("Environments names are not thoses expected.",
                lNames.containsAll(Arrays.asList(ENVIRONMENT_NAMES)));
    }

    /**
     * Tests the method getEnvironmentNames for an admin access set on instance
     * (only public environments are returned)
     */
    public void testGetEnvironmentNamesWithAdminInstanceCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();

        // Login with Admin Instance
        String lAdminInstanceUserToken =
                authorizationService.login(
                        GpmTestValues.ADMIN_INSTANCE_LOGIN_PWD[0],
                        GpmTestValues.ADMIN_INSTANCE_LOGIN_PWD[1]);
        String lAdminInstanceRoleToken =
                authorizationService.selectRole(lAdminInstanceUserToken,
                        GpmTestValues.ADMINISTRATOR_ROLE, getProductName(),
                        getProcessName());

        Collection<String> lNames =
                environmentService.getEnvironmentNames(lAdminInstanceRoleToken,
                        getProcessName());

        assertNotNull("Method getEnvironmentNames returns null.", lNames);
        int lSize = lNames.size();
        int lExpectedSize = PUBLIC_ENVIRONMENT_NAMES.length;
        assertEquals("Method getEnvironmentNames returns " + lSize
                + " environments names instead of " + lExpectedSize,
                lExpectedSize, lSize);
        assertTrue("Environments names are not thoses expected.",
                lNames.containsAll(Arrays.asList(PUBLIC_ENVIRONMENT_NAMES)));
    }

    /**
     * Tests the method getEnvironmentNames with an admin access set on product
     * (only public environments of this product are returned).
     */
    public void testGetEnvironmentNamesWithProductInstanceCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();

        // Login with Product Instance
        String lProductInstanceUserToken =
                authorizationService.login(
                        GpmTestValues.ADMIN_PRODUCT_LOGIN_PWD[0],
                        GpmTestValues.ADMIN_PRODUCT_LOGIN_PWD[1]);
        String lProductInstanceRoleToken =
                authorizationService.selectRole(lProductInstanceUserToken,
                        GpmTestValues.ADMINISTRATOR_ROLE,
                        GpmTestValues.PRODUCT1_NAME, getProcessName());

        Collection<String> lNames =
                environmentService.getEnvironmentNames(
                        lProductInstanceRoleToken, getProcessName());

        assertNotNull("Method getEnvironmentNames returns null.", lNames);

        // Product 1 contains one environment. 
        int lSize = lNames.size();
        int lExpectedSize = 1;
        assertEquals("Method getEnvironmentNames returns " + lSize
                + " environments names instead of " + lExpectedSize,
                lExpectedSize, lSize);

        assertTrue("Environments names are not thoses expected.",
                lNames.contains(GpmTestValues.ENVIRONMENT_ENV2));

    }

    /**
     * Check that a classical role (no global admin, no admin access on
     * instance, no admin access on product) cannot create a user
     */
    public void testGetEnvironmentNamesWithNoAdminAccessCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();

        // Login with classical role
        String lUserToken =
                authorizationService.login(GpmTestValues.NO_ADMIN_LOGIN_PWD[0],
                        GpmTestValues.NO_ADMIN_LOGIN_PWD[1]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        GpmTestValues.VIEWER_ROLE, getProductName(),
                        getProcessName());

        Collection<String> lNames =
                environmentService.getEnvironmentNames(lRoleToken,
                        getProcessName());

        assertNotNull("Method getEnvironmentNames returns null.", lNames);

        int lSize = lNames.size();
        int lExpectedSize = 0;
        assertEquals("Method getEnvironmentNames returns " + lSize
                + " environments names instead of " + lExpectedSize,
                lExpectedSize, lSize);
    }
}