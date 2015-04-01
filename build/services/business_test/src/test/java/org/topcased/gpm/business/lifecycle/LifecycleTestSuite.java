/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.lifecycle;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * LifecycleTestSuite
 * 
 * @author ahaugomm
 */
public class LifecycleTestSuite extends TestSuite {

    /**
     * The test suite
     * 
     * @return the suite
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();
        lSuite.addTestSuite(TestGetProcessStateNameService.class);
        lSuite.addTestSuite(TestCreateProcessDefinitionService.class);
        lSuite.addTestSuite(TestGetProcessInstanceInformationService.class);
        lSuite.addTestSuite(TestPerformTransitionService.class);
        lSuite.addTestSuite(TestGetProcessDefinitionService.class);
        lSuite.addTestSuite(TestSetCurrentState.class);
        return lSuite;
    }
}
