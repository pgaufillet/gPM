/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.environment;

import static org.topcased.gpm.business.GpmTestValues.CATEGORIES_NAME;
import static org.topcased.gpm.business.GpmTestValues.COLOR_VALUES;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.dictionary.CategoryData;
import org.topcased.gpm.business.dictionary.CategoryValueData;
import org.topcased.gpm.business.environment.service.EnvironmentService;
import org.topcased.gpm.business.i18n.service.I18nService;

/**
 * TestGetModifiableCategoriesService
 * 
 * @author mfranche
 */
public class TestGetModifiableCategoriesService extends
        AbstractBusinessServiceTestCase {

    /** The Environment Service. */
    private EnvironmentService environmentService;

    private I18nService i18nService;

    /**
     * Tests the method getModifiableCategories in a normal case.
     */
    public void testNormalCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();
        i18nService = serviceLocator.getI18nService();

        Collection<CategoryData> lResu =
                environmentService.getModifiableCategories(adminRoleToken,
                        getProcessName());

        assertNotNull("The collection of category data is incorrect.", lResu);

        Collection<String> lCategoryDataNames = new ArrayList<String>();
        for (CategoryData lCategoryData : lResu) {
            lCategoryDataNames.add(lCategoryData.getLabelKey());
        }
        assertTrue("The category data names are incorrect.", Arrays.asList(
                CATEGORIES_NAME).containsAll(lCategoryDataNames));

        for (CategoryData lCategoryData : lResu) {
            if (lCategoryData.getLabelKey().compareTo(CATEGORIES_NAME[0]) == 0) {
                assertEquals("The business process is incorrect.",
                        getProcessName(), lCategoryData.getBusinessProcess());
                assertEquals("The i18n name is incorrect.",
                        i18nService.getValueForUser(adminRoleToken,
                                CATEGORIES_NAME[0]),
                        lCategoryData.getI18nName());
                assertNotNull("The category value data is incorrect.",
                        lCategoryData.getCategoryValueDatas());
                assertTrue(
                        "The category value data is incorrect.",
                        lCategoryData.getCategoryValueDatas().length >= COLOR_VALUES.length);
                Collection<String> lCategoryValueDataName =
                        new ArrayList<String>();
                for (CategoryValueData lCategoryValueData : lCategoryData.getCategoryValueDatas()) {
                    lCategoryValueDataName.add(lCategoryValueData.getValue());
                }
                assertTrue("The category value data is incorrect.",
                        Arrays.asList(COLOR_VALUES).containsAll(
                                lCategoryValueDataName));
            }
        }
    }
}
