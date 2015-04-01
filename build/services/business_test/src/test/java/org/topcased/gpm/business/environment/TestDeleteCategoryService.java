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
import org.topcased.gpm.business.dictionary.CategoryAccessData;
import org.topcased.gpm.business.dictionary.CategoryData;
import org.topcased.gpm.business.dictionary.CategoryValueData;
import org.topcased.gpm.business.environment.service.EnvironmentService;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.UndeletableElementException;

/**
 * Tests the method <CODE>deleteCategory<CODE> of the Environment Service.
 * 
 * @author nsamson
 */
public class TestDeleteCategoryService extends AbstractBusinessServiceTestCase {

    /** The Environment Service. */
    private EnvironmentService environmentService;

    /** The Label of category. */
    private static final String CATEGORY_LABEL = "CategoryTest";

    /** The bad label key. */
    private static final String BAD_LABEL_KEY = "bad key";

    /** The already exist key. */
    private static final String LABEL_KEY = GpmTestValues.CATEGORY_COLOR;

    /** The values. */
    private static final String[] VALUES = { "value1", "value2", "value3" };

    /**
     * Tests the deleteCategory method.
     */
    public void testNormalCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();

        // Defining the category in the dictionary the categories
        int lValuesSize = VALUES.length;
        CategoryValueData[] lCVDs = new CategoryValueData[lValuesSize];
        for (int i = 0; i < lValuesSize; i++) {
            lCVDs[i] = new CategoryValueData(VALUES[i]);
        }
        CategoryData lCategory =
                new CategoryData(null, CATEGORY_LABEL, "", getProcessName(),
                        CategoryAccessData.PROCESS, lCVDs);
        environmentService.setDictionaryCategory(adminRoleToken, lCategory);

        // Deletes the category
        environmentService.deleteCategory(adminRoleToken, getProcessName(),
                lCategory.getLabelKey());

        // Verifies the category has been deleted
        Collection<CategoryData> lCategorieDatas =
                environmentService.getCategories(adminRoleToken,
                        getProcessName());

        for (CategoryData lCategoryData : lCategorieDatas) {
            assertFalse("Category " + lCategory.getLabelKey()
                    + " has not been deleted in DB.",
                    lCategory.getLabelKey().equals(lCategoryData.getLabelKey()));
        }
    }

    /**
     * Tests the deleteCategory method with a bad key.
     */
    public void testBadKeyCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();

        // Deletes the category
        try {
            environmentService.deleteCategory(adminRoleToken, getProcessName(),
                    BAD_LABEL_KEY);
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
     * Tests the deleteCategory method with an undeletable Category.
     */
    public void testUndeletableCategoryCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();
        // Deletes the category
        try {
            environmentService.deleteCategory(adminRoleToken, getProcessName(),
                    LABEL_KEY);
            fail("The exception has not been thrown.");
        }
        catch (UndeletableElementException lGDMException) {
            // ok
        }
        catch (Throwable e) {

            fail("The exception thrown is not a UndeletableElementException.");
        }
    }
}