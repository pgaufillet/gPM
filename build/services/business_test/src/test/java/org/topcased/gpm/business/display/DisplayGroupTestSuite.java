/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin), Anne Haugommard
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.display;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A test suite managing the test for display group.
 * 
 * @author nsamson
 */
public class DisplayGroupTestSuite extends TestSuite {

    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();
        lSuite.addTestSuite(TestGetDisplayGroupListService.class);
        lSuite.addTestSuite(TestCreateDisplayGroupService.class);
        lSuite.addTestSuite(TestDisplayHintOnLinks.class);
        lSuite.addTestSuite(TestSetChoiceTreeDisplayHint.class);
        return lSuite;
    }
}
