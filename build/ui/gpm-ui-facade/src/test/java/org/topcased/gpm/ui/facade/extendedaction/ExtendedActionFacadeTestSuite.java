/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.extendedaction;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * ExtendedActionFacadeTestSuite
 * 
 * @author nveillet
 */
public class ExtendedActionFacadeTestSuite extends TestSuite {
    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite("ExtendedActionFacade test suite");
        lSuite.addTestSuite(TestGetAvailableExtendedActionsFacade.class);
        lSuite.addTestSuite(TestHasInputDataFacade.class);
        lSuite.addTestSuite(TestGetInputDataFacade.class);
        lSuite.addTestSuite(TestExecuteExtendedActionFacade.class);
        return lSuite;
    }
}
