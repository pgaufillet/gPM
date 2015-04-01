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

import java.util.Collection;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.environment.service.EnvironmentService;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;

/**
 * Tests the method <CODE>createEnvironment<CODE> of the Environment Service.
 * 
 * @author nsamson
 */
public class TestCreateEnvironmentService extends
        AbstractBusinessServiceTestCase {

    /** The Environment Service. */
    private EnvironmentService environmentService;

    /** The environment name. */
    private static final String ENVIRONMENT_NAME = "Personnal";

    /** The bad environment name. */
    private static final String BAD_ENVIRONMENT_NAME =
            GpmTestValues.ENVIRONMENT_PROFESSIONAL;

    /**
     * Tests the createEnvironment method.
     */
    public void testNormalCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();

        // Creating the environment
        environmentService.createEnvironment(adminRoleToken, getProcessName(),
                ENVIRONMENT_NAME, true);

        Collection<String> lNames =
                environmentService.getEnvironmentNames(adminRoleToken,
                        getProcessName());

        assertNotNull("Method getEnvironmentNames returns null.", lNames);
        assertTrue("Environment " + ENVIRONMENT_NAME
                + " has not been created in DB.",
                lNames.contains(ENVIRONMENT_NAME));
    }

    /**
     * Tests the createEnvironment method with already exist name.
     */
    public void testAlreadyExistEnvironmentNameCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();

        // Creating the environment
        try {
            environmentService.createEnvironment(adminRoleToken,
                    getProcessName(), BAD_ENVIRONMENT_NAME, true);
            fail("The exception has not been thrown.");
        }
        catch (GDMException lGDMException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not a GDMException.");
        }
    }

    /**
     * Check that an admin access set on instance can create an environment
     */
    public void testCreateEnvironmentWithAdminInstanceCase() {
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

        environmentService.createEnvironment(lAdminInstanceRoleToken,
                getProcessName(), ENVIRONMENT_NAME, true);

        Collection<String> lNames =
                environmentService.getEnvironmentNames(lAdminInstanceRoleToken,
                        getProcessName());

        assertNotNull("Method getEnvironmentNames returns null.", lNames);
        assertTrue("Environment " + ENVIRONMENT_NAME
                + " has not been created in DB.",
                lNames.contains(ENVIRONMENT_NAME));
    }

    /**
     * Check that an admin access set on product cannot create an environment
     */
    public void testCreateEnvironmentWithProductInstanceCase() {
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
        try {
            environmentService.createEnvironment(lProductInstanceRoleToken,
                    getProcessName(), ENVIRONMENT_NAME, true);
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lAuthorizationException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an AuthorizationException.");
        }
    }

    /**
     * Check that a classical role (no global admin, no admin access on
     * instance, no admin access on product) cannot create an environment
     */
    public void testCreateEnvironmentWithNoAdminAccessCase() {
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
        try {
            environmentService.createEnvironment(lRoleToken, getProcessName(),
                    ENVIRONMENT_NAME, true);
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lAuthorizationException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an AuthorizationException.");
        }
    }
}