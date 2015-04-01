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

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * OptionsTestSuite
 * 
 * @author mkargbo
 */
public class OptionsTestSuite {

    /**
     * Construct the suite with all options tests
     * 
     * @return The test suites
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();
        lSuite.addTestSuite(AdditionalOptionsCategoryTest.class);
        lSuite.addTestSuite(GpmOptionsTest.class);
        return lSuite;
    }

}
