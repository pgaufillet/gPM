/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.values;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A test suite managing all tests for values service.
 * 
 * @author tpanuel
 */
public class ValuesTestSuite extends TestSuite {

    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        final TestSuite lSuite = new TestSuite();

        lSuite.addTestSuite(TestTypeMapping.class);

        return lSuite;
    }
}