/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.environment;

import static org.topcased.gpm.business.GpmTestValues.CATEGORY_COLOR;
import static org.topcased.gpm.business.GpmTestValues.CATEGORY_INVALID;
import static org.topcased.gpm.business.GpmTestValues.COLOR_VALUES_FOR_ENV;
import static org.topcased.gpm.business.GpmTestValues.ENVIRONMENT_ENV1;

import java.util.ArrayList;
import java.util.Arrays;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.dictionary.CategoryValueData;
import org.topcased.gpm.business.dictionary.EnvironmentData;
import org.topcased.gpm.business.environment.service.EnvironmentService;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidNameException;

/**
 * Tests the method <CODE>setEnvironmentCategory<CODE> of the Environment
 * Service.
 * 
 * @author nsamson
 */
public class TestSetEnvironmentCategoryValuesService extends
        AbstractBusinessServiceTestCase {

    /** The Environment Service. */
    private EnvironmentService environmentService;

    /**
     * The new value to set in the category. This value must be available in the
     * Color category, but not present in the env1 environment.
     */
    private static final String CATEGORY_VALUE =
            GpmTestValues.CATEGORY_COLOR_VALUE_GREEN;

    /**
     * Tests the setEnvironmentCategory method.
     */
    public void testNormalCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();

        // Retrieve the environment values
        EnvironmentData lEnvironment =
                environmentService.getEnvironmentCategory(adminRoleToken,
                        getProcessName(), ENVIRONMENT_ENV1, CATEGORY_COLOR);

        CategoryValueData[] lCVDs = lEnvironment.getCategoryValueDatas();
        assertEquals("Invalid values count", COLOR_VALUES_FOR_ENV.length,
                lCVDs.length);

        // The new category values
        int lLength = lCVDs.length;
        String[] lNewCVDs =
                Arrays.copyOf(COLOR_VALUES_FOR_ENV, COLOR_VALUES_FOR_ENV.length);
        lNewCVDs[lLength - 1] = CATEGORY_VALUE;

        // the old value
        String lOldValue = lCVDs[lLength - 1].getValue();

        // Setting the environment category
        environmentService.setEnvironmentCategory(adminRoleToken,
                getProcessName(), ENVIRONMENT_ENV1, CATEGORY_COLOR,
                new ArrayList<String>(Arrays.asList(lNewCVDs)));

        // Retrieve the environment values
        lEnvironment =
                environmentService.getEnvironmentCategory(adminRoleToken,
                        getProcessName(), ENVIRONMENT_ENV1, CATEGORY_COLOR);

        lCVDs = lEnvironment.getCategoryValueDatas();

        // The number of values remains the same.
        assertEquals("Invalid values count", COLOR_VALUES_FOR_ENV.length,
                lCVDs.length);

        // Check that the replaced value has...  actually been replaced
        boolean lNewCategoryCreated = false;
        boolean lOldCategoryRemoved = true;
        for (CategoryValueData lCVD : lCVDs) {
            String lValue = lCVD.getValue();
            if (lValue.equals(CATEGORY_VALUE)) {
                lNewCategoryCreated = true;
            }
            else if (lValue.equals(lOldValue)) {
                lOldCategoryRemoved = false;
            }
        }
        assertTrue("The new category value " + CATEGORY_VALUE
                + " has not been created for environment " + ENVIRONMENT_ENV1
                + " and category " + CATEGORY_COLOR + " in DB.",
                lNewCategoryCreated);
        assertTrue("The old category value " + lOldValue
                + " has not been removed for environment " + ENVIRONMENT_ENV1
                + " and category " + CATEGORY_COLOR + " in DB.",
                lOldCategoryRemoved);
    }

    /**
     * Tests the setEnvironmentCategory method with a bad env name.
     */
    public void testBadEnvNameCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();

        // Setting the environment category
        try {
            environmentService.setEnvironmentCategory(adminRoleToken,
                    getProcessName(), CATEGORY_INVALID, CATEGORY_COLOR,
                    new ArrayList<String>());
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
     * Tests the setEnvironmentCategory method with a bad cat name.
     */
    public void testBadCatNameCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();

        try {
            environmentService.setEnvironmentCategory(adminRoleToken,
                    getProcessName(), ENVIRONMENT_ENV1, CATEGORY_INVALID,
                    new ArrayList<String>());
            fail("The exception has not been thrown.");
        }
        catch (InvalidNameException e) {
            assertEquals(CATEGORY_INVALID, e.getInvalidName());
        }
        catch (Throwable e) {
            fail("The exception thrown is not a GDMException.");
        }
    }

    /**
     * Tests the method setEnvironmentCategory for an admin role and a private
     * environment.
     */
    public void testAdminAndPrivateEnvironmentCase() {
        // Get the environment service
        environmentService = serviceLocator.getEnvironmentService();

        try {
            environmentService.setEnvironmentCategory(adminRoleToken,
                    getProcessName(), GpmTestValues.PRIVATE_ENVIRONMENT_NAME,
                    GpmTestValues.CATEGORY_CAT_PEDIGRE, new ArrayList<String>());
        }
        catch (Exception ex) {
            fail("An exception has been thrown.");
        }
    }

    /**
     * Test the setEnvironmentCategory method with an admin access define on
     * instance (only public environments can be updated).
     */
    public void testSetEnvironmentCategoryWithAdminInstanceCase() {
        // Get the environment service
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

        // Public environment can be updated
        try {
            environmentService.setEnvironmentCategory(lAdminInstanceRoleToken,
                    getProcessName(), ENVIRONMENT_ENV1,
                    GpmTestValues.CATEGORY_USER_NAME, new ArrayList<String>());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            fail("An exception has been thrown.");
        }

        // Private environment cannot be updated
        try {
            environmentService.setEnvironmentCategory(lAdminInstanceRoleToken,
                    getProcessName(), GpmTestValues.PRIVATE_ENVIRONMENT_NAME,
                    GpmTestValues.CATEGORY_USER_NAME, new ArrayList<String>());
            fail("No exception has been thrown.");
        }
        catch (AuthorizationException lAuthException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an AuthorizationException.");
        }

    }

    /**
     * Test the setEnvironmentCategory method with and admin access define on
     * product (only public environements of this product can be udpated).
     */
    public void testSetEnvironmentCategoryWithProductInstanceCase() {
        // Get the environment service
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

        // Get the product environment
        // This environment can be updated
        try {
            environmentService.setEnvironmentCategory(
                    lProductInstanceRoleToken, getProcessName(),
                    GpmTestValues.ENVIRONMENT_ENV2,
                    GpmTestValues.CATEGORY_USER_NAME, new ArrayList<String>());
        }
        catch (Exception ex) {
            fail("An exception has been thrown.");
        }

        // Get another environment (not on the product)
        // This another environment cannot be updated
        try {
            environmentService.setEnvironmentCategory(
                    lProductInstanceRoleToken, getProcessName(),
                    ENVIRONMENT_ENV1, GpmTestValues.CATEGORY_USER_NAME,
                    new ArrayList<String>());
            fail("No exception has been thrown.");
        }
        catch (AuthorizationException lAuthException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an AuthorizationException.");
        }
    }

    /**
     * Test the setEnvironmentCategory method with a classical role (no global
     * admin, no admin access on instance, no admin access on product) (nor
     * public nor private environments can be updated).
     */
    public void testSetEnvironmentCategoryWithNoAdminAccessCase() {
        // Get the environment service
        environmentService = serviceLocator.getEnvironmentService();

        // Login with classical role
        String lUserToken =
                authorizationService.login(GpmTestValues.NO_ADMIN_LOGIN_PWD[0],
                        GpmTestValues.NO_ADMIN_LOGIN_PWD[1]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        GpmTestValues.VIEWER_ROLE, getProductName(),
                        getProcessName());

        // Public environment can be updated
        try {
            environmentService.setEnvironmentCategory(lRoleToken,
                    getProcessName(), ENVIRONMENT_ENV1,
                    GpmTestValues.CATEGORY_USER_NAME, new ArrayList<String>());
            fail("No exception has been thrown.");
        }
        catch (AuthorizationException lAuthException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an AuthorizationException.");
        }

        // Private environment cannot be updated
        try {
            environmentService.setEnvironmentCategory(lRoleToken,
                    getProcessName(), GpmTestValues.PRIVATE_ENVIRONMENT_NAME,
                    GpmTestValues.CATEGORY_USER_NAME, new ArrayList<String>());
            fail("No exception has been thrown.");
        }
        catch (AuthorizationException lAuthException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an AuthorizationException.");
        }

    }

}