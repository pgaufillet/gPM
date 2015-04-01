/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * ExtensionsGroupTestSuite
 * 
 * @author llatil
 */
public class ExtensionsGroupTestSuite extends TestSuite {
    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();
        lSuite.addTestSuite(TestContext.class);

        lSuite.addTestSuite(TestDefineAndCallExtension.class);

        lSuite.addTestSuite(TestGetExtensionCommandNamesService.class);
        lSuite.addTestSuite(TestRemoveExtensionService.class);
        lSuite.addTestSuite(TestCreateAndGetCommandsService.class);

        return lSuite;
    }
}
