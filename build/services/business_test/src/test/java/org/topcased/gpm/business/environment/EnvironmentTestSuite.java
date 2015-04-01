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
 * A test suite managing all tests around environments.
 * 
 * @author nsamson
 */
public class EnvironmentTestSuite extends TestSuite {

    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();
        lSuite.addTestSuite(TestGetEnvironmentNamesService.class);
        lSuite.addTestSuite(TestCreateEnvironmentService.class);
        lSuite.addTestSuite(TestGetEnvironmentCategoryService.class);
        lSuite.addTestSuite(TestSetEnvironmentCategoryService.class);
        lSuite.addTestSuite(TestDeleteEnvironmentService.class);
        lSuite.addTestSuite(TestSetEnvironmentCategoryValuesService.class);
        return lSuite;
    }

}
