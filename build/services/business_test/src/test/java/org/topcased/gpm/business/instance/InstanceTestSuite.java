/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.instance;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * InstanceTestSuite
 * 
 * @author ahaugomm
 */
public class InstanceTestSuite extends TestSuite {

    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();
        lSuite.addTestSuite(TestCreateBusinessProcessService.class);
        lSuite.addTestSuite(TestCreateGpmService.class);
        return lSuite;
    }
}
