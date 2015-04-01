/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * ExtendedActionTestSuite
 * 
 * @author ahaugomm
 */
public class ExtendedActionTestSuite extends TestSuite {
    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();
        lSuite.addTestSuite(TestCreateExtendedActionService.class);
        lSuite.addTestSuite(TestUpdateExtendedActionService.class);
        lSuite.addTestSuite(TestDeleteExtendedActionService.class);

        lSuite.addTestSuite(TestGetAvailableExtendedActionsService.class);
        lSuite.addTestSuite(TestGetExtendedActionService.class);
        lSuite.addTestSuite(TestCreateExtension.class);

        return lSuite;
    }
}
