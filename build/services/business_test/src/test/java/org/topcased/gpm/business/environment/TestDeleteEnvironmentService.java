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
import org.topcased.gpm.business.dictionary.EnvironmentData;
import org.topcased.gpm.business.environment.service.EnvironmentService;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.UndeletableElementException;

/**
 * Tests the method <CODE>deleteEnvironment<CODE> of the Environment Service.
 * 
 * @author nsamson
 */
public class TestDeleteEnvironmentService extends
        AbstractBusinessServiceTestCase {

    /** The Environment Service. */
    private EnvironmentService environmentService;

    /** The environment name. */
    private static final String ENVIRONMENT_NAME = "Personnal";

    /** The category name. */
    private static final String CATEGORY_NAME = GpmTestValues.CATEGORY_COLOR;

    /** The bad label key. */
    private static final String BAD_LABEL_KEY = "bad key";

    /** The already exist key. */
    private static final String LABEL_KEY =
            GpmTestValues.ENVIRONMENT_PROFESSIONAL;

    /** Role name for non admin test */
    private static final String ROLE_NAME = "notadmin";

    /**
     * Tests the deleteEnvironment method.
     */
    public void testNormalCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();

        // Creating the environment
        environmentService.createEnvironment(adminRoleToken, getProcessName(),
                ENVIRONMENT_NAME, true);

        // Retrieving the environment category
        EnvironmentData lEnvironment =
                environmentService.getEnvironmentCategory(adminRoleToken,
                        getProcessName(), ENVIRONMENT_NAME, CATEGORY_NAME);
        assertNotNull("Environment " + ENVIRONMENT_NAME + " is not in DB.",
                lEnvironment);

        // deleting the environment
        environmentService.deleteEnvironment(adminRoleToken, getProcessName(),
                lEnvironment.getLabelKey());

        // Verify
        Collection<String> lNames =
                environmentService.getEnvironmentNames(adminRoleToken,
                        getProcessName());

        assertNotNull("Method getEnvironmentNames returns null.", lNames);
        assertFalse("Environment " + ENVIRONMENT_NAME
                + " has not been deleted in DB.",
                lNames.contains(ENVIRONMENT_NAME));
    }

    /**
     * Tests the deleteEnvironment method with a bad key.
     */
    public void testBadKeyCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();

        // deleting the environment
        try {
            environmentService.deleteEnvironment(adminRoleToken,
                    getProcessName(), BAD_LABEL_KEY);
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
     * Tests the deleteEnvironment method with an undeletable env.
     */
    public void testUndeletableCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();

        // deleting the environment

        try {
            environmentService.deleteEnvironment(adminRoleToken,
                    getProcessName(), LABEL_KEY);
            fail("The exception has not been thrown.");
        }
        catch (UndeletableElementException lGDMException) {
            // ok
        }
        catch (Throwable e) {

            fail("The exception thrown is not a UndeletableElementException.");
        }
    }

    /**
     * Test the deleteEnvironment method with a non admin role
     */
    public void testNonAdminCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();

        // User2 login
        String lUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        String lRoleToken =
                authorizationService.selectRole(lUserToken, ROLE_NAME,
                        getProductName(), getProcessName());

        // deleting the environment
        try {
            environmentService.deleteEnvironment(lRoleToken, getProcessName(),
                    LABEL_KEY);
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lAuthException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an AuthorizationException.");
        }
    }
}