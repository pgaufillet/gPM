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

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A test suite managing all tests around categories.
 * 
 * @author nsamson
 */
public class CategoryTestSuite extends TestSuite {

    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();
        lSuite.addTestSuite(TestGetCategoryNamesService.class);
        lSuite.addTestSuite(TestGetCategoriesService.class);
        lSuite.addTestSuite(TestSetDictionaryCategoryService.class);
        lSuite.addTestSuite(TestDeleteCategoryService.class);
        lSuite.addTestSuite(TestUpdateUsableFieldDatasService.class);
        lSuite.addTestSuite(TestGetModifiableCategoriesService.class);
        lSuite.addTestSuite(TestGetCategoryValuesService.class);
        return lSuite;
    }

}
