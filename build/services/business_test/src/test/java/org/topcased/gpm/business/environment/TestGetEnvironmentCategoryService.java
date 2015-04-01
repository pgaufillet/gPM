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
import static org.topcased.gpm.business.GpmTestValues.ENVIRONMENT_INVALID_ENV;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.dictionary.CategoryValueData;
import org.topcased.gpm.business.dictionary.EnvironmentData;
import org.topcased.gpm.business.environment.service.EnvironmentService;
import org.topcased.gpm.business.exception.GDMException;

/**
 * Tests the method <CODE>getEnvironmentCategory<CODE> of the Environment
 * Service.
 * 
 * @author nsamson
 */
public class TestGetEnvironmentCategoryService extends
        AbstractBusinessServiceTestCase {

    /** The Environment Service. */
    private EnvironmentService environmentService;

    /**
     * Tests the getEnvironmentCategory method.
     */
    public void testNormalCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();

        // Retrieving the environment category
        EnvironmentData lEnvironment =
                environmentService.getEnvironmentCategory(adminRoleToken,
                        getProcessName(), ENVIRONMENT_ENV1, CATEGORY_COLOR);
        assertNotNull("Environment " + ENVIRONMENT_ENV1 + " is not in DB.",
                lEnvironment);

        // Verify key
        String lCategory = lEnvironment.getCategoryKey();
        assertEquals("Bad category key returned", CATEGORY_COLOR, lCategory);
        CategoryValueData[] lCategoryValueDatas =
                lEnvironment.getCategoryValueDatas();

        String[] lCatValues = new String[lCategoryValueDatas.length];
        int i = 0;
        for (CategoryValueData lCVD : lCategoryValueDatas) {
            lCatValues[i++] = lCVD.getValue();
        }

        assertEqualsOrdered(COLOR_VALUES_FOR_ENV, lCatValues);
    }

    /**
     * Tests the getEnvironmentCategory method with a bad env name.
     */
    public void testBadEnvNameCase() {
        // Get the environment service.
        environmentService = serviceLocator.getEnvironmentService();

        // Retrieving the environment category
        try {
            environmentService.getEnvironmentCategory(adminRoleToken,
                    getProcessName(), ENVIRONMENT_INVALID_ENV, CATEGORY_COLOR);
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
     * Tests the getEnvironmentCategory method with a bad environment name.
     */
    public void testBadCatNameCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();

        // Retrieving the environment category
        try {
            environmentService.getEnvironmentCategory(adminRoleToken,
                    getProcessName(), ENVIRONMENT_ENV1, CATEGORY_INVALID);
            fail("The exception has not been thrown.");
        }
        catch (GDMException lGDMException) {
            // ok
        }
        catch (Throwable e) {

            fail("The exception thrown is not a GDMException.");
        }
    }
}