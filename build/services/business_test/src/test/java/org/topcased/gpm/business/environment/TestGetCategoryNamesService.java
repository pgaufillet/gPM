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

import static org.topcased.gpm.business.GpmTestValues.CATEGORIES_NAME;

import java.util.Arrays;
import java.util.Collection;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.environment.service.EnvironmentService;

/**
 * Tests the method <CODE>getCategoryNames<CODE> of the Environment Service.
 * 
 * @author nsamson
 */
public class TestGetCategoryNamesService extends
        AbstractBusinessServiceTestCase {

    /** The Environment Service. */
    private EnvironmentService environmentService;

    private final int categoriesCount = CATEGORIES_NAME.length;

    /**
     * Tests the getCategoryNames method.
     */
    public void testNormalCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();

        // Retrieving the category names
        Collection<String> lCategoryNames =
                environmentService.getCategoryNames(adminRoleToken,
                        getProcessName());

        assertNotNull("Method getCategoryNames returns null.", lCategoryNames);

        int lReturnedCategoriesCount = lCategoryNames.size();
        assertEquals("Method getCategoryNames returns "
                + lReturnedCategoriesCount + " category names instead of "
                + categoriesCount, categoriesCount, lReturnedCategoriesCount);
        assertTrue("Category Names in BD are not thoses expected.",
                lCategoryNames.containsAll(Arrays.asList(CATEGORIES_NAME)));

    }
}
