/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.utils;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * UtilsTestSuite
 * 
 * @author tpanuel
 */
public class UtilsTestSuite extends TestSuite {

    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();

        lSuite.addTestSuite(MutableImmutableTest.class);

        return lSuite;
    }
}
