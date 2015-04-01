/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.instantiation.options;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.environment.service.EnvironmentService;
import org.topcased.gpm.business.serialization.data.CategoryValue;
import org.topcased.gpm.instantiation.AbstractInstantiationTestCase;

/**
 * AdditionalOptionsCategoryTest Test the additional options 'cat_Values'.
 * 
 * @author mkargbo
 */
public class AdditionalOptionsCategoryTest extends
        AbstractInstantiationTestCase {
    private static final String INSTANCE_FILE_01 =
            "options/Test_create_instance_CF_007_01.xml";

    private static final String INSTANCE_FILE_02 =
            "options/Test_create_instance_CF_007_02.xml";

    private static final String CATEGORY_NAME = "Cat_01";

    private EnvironmentService environmentService;

    /**
     * {@inheritDoc}
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        environmentService = serviceLocator.getEnvironmentService();
    }

    private static final String[] DEFAULT_EXPECTED_VALUE_01 =
            { "value_01", "value_02", "value_03" };

    private static final String[] DEFAULT_EXPECTED_VALUE_02 =
            { "value_01", "value_02", "value_03", "value_04", "value_05",
             "value_06" };

    /**
     * Default test case 'Test_create_instance_CF_xx1' update a category
     */
    public void testDefaultBehaviour() {
        instantiate(INSTANCE_FILE_01);

        Map<String, List<CategoryValue>> lCategories =
                environmentService.getCategoryValues(processName,
                        Collections.singletonList(CATEGORY_NAME));

        List<String> lCategoryValues = new LinkedList<String>();
        for (CategoryValue lCategoryValue : lCategories.get(CATEGORY_NAME)) {
            lCategoryValues.add(lCategoryValue.getValue());
        }

        assertEquals(DEFAULT_EXPECTED_VALUE_01.length, lCategoryValues.size());
        assertTrue(lCategoryValues.containsAll(Arrays.asList(DEFAULT_EXPECTED_VALUE_01)));

        instantiate(INSTANCE_FILE_02);

        lCategories =
                environmentService.getCategoryValues(processName,
                        Collections.singletonList(CATEGORY_NAME));

        lCategoryValues = new LinkedList<String>();
        for (CategoryValue lCategoryValue : lCategories.get(CATEGORY_NAME)) {
            lCategoryValues.add(lCategoryValue.getValue());
        }

        assertEquals(DEFAULT_EXPECTED_VALUE_02.length, lCategoryValues.size());
        assertTrue(lCategoryValues.containsAll(Arrays.asList(DEFAULT_EXPECTED_VALUE_02)));
    }

    private static final String[] ERASE_EXPECTED_VALUE_01 =
            { "value_01", "value_02", "value_03" };

    private static final String[] ERASE_EXPECTED_VALUE_02 =
            { "value_04", "value_05", "value_06" };

    /**
     * Erase not redefine values.
     */
    public void testEraseBehaviour() {
        instantiate(INSTANCE_FILE_01);

        Map<String, List<CategoryValue>> lCategories =
                environmentService.getCategoryValues(processName,
                        Collections.singletonList(CATEGORY_NAME));

        List<String> lCategoryValues = new LinkedList<String>();
        for (CategoryValue lCategoryValue : lCategories.get(CATEGORY_NAME)) {
            lCategoryValues.add(lCategoryValue.getValue());
        }

        assertEquals(ERASE_EXPECTED_VALUE_01.length, lCategoryValues.size());
        assertTrue(lCategoryValues.containsAll(Arrays.asList(ERASE_EXPECTED_VALUE_01)));

        setAdditionalOption("delete_catValues");
        instantiate(INSTANCE_FILE_02);

        lCategories =
                environmentService.getCategoryValues(processName,
                        Collections.singletonList(CATEGORY_NAME));

        lCategoryValues = new LinkedList<String>();
        for (CategoryValue lCategoryValue : lCategories.get(CATEGORY_NAME)) {
            lCategoryValues.add(lCategoryValue.getValue());
        }

        assertEquals(ERASE_EXPECTED_VALUE_02.length, lCategoryValues.size());
        assertTrue(lCategoryValues.containsAll(Arrays.asList(ERASE_EXPECTED_VALUE_02)));
    }
}
