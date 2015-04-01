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

import static org.topcased.gpm.business.GpmTestValues.CATEGORY_COLOR;
import static org.topcased.gpm.business.GpmTestValues.COLOR_VALUES_FOR_ENV;
import static org.topcased.gpm.business.GpmTestValues.ENVIRONMENT_ENV1;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.environment.service.EnvironmentService;
import org.topcased.gpm.business.serialization.data.CategoryValue;

/**
 * TestGetCategoryValuesService
 * 
 * @author mfranche
 */
public class TestGetCategoryValuesService extends
        AbstractBusinessServiceTestCase {

    /**
     * Tests the method in normal conditions.
     */
    public void testNormalCase() {
        // Gets the environment service.
        EnvironmentService lEnvironmentService =
                serviceLocator.getEnvironmentService();
        Map<String, List<CategoryValue>> lMap =
                lEnvironmentService.getCategoryValues(adminRoleToken,
                        getProcessName(), ENVIRONMENT_ENV1,
                        Collections.singletonList(CATEGORY_COLOR));

        assertEquals("Expect only one value in the category map", 1,
                lMap.size());
        assertTrue("The map should contain " + CATEGORY_COLOR,
                lMap.containsKey(CATEGORY_COLOR));

        List<CategoryValue> lCategoryValueList = lMap.get(CATEGORY_COLOR);

        String[] lCategoryValueNames = new String[lCategoryValueList.size()];
        int i = 0;
        for (CategoryValue lCategoryValue : lCategoryValueList) {
            lCategoryValueNames[i++] = lCategoryValue.getValue();
        }
        assertEqualsOrdered(COLOR_VALUES_FOR_ENV, lCategoryValueNames);
    }
}
