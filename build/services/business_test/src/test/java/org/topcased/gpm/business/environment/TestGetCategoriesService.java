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
import org.topcased.gpm.business.dictionary.CategoryData;
import org.topcased.gpm.business.environment.service.EnvironmentService;

/**
 * Tests the method <CODE>getCategories<CODE> of the Environment Service.
 * 
 * @author nsamson
 */
public class TestGetCategoriesService extends AbstractBusinessServiceTestCase {

    /** The Environment Service. */
    private EnvironmentService environmentService;

    /** The number of category. */
    private static final int CATEGORY_NUMBER =
            GpmTestValues.CATEGORIES_NAME.length;

    /**
     * Tests the getCategories method.
     */
    public void testNormalCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();

        // Retrieving the categories
        Collection<CategoryData> lCategories =
                environmentService.getCategories(adminRoleToken,
                        getProcessName());

        assertNotNull("Method getCategories returns null.", lCategories);
        int lSize = lCategories.size();
        assertEquals("Method getCategoryNames returns " + lSize
                + " category names instead of " + CATEGORY_NUMBER,
                CATEGORY_NUMBER, lSize);
    }
}