/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.help;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * HelpTestSuite
 * 
 * @author mfranche
 */
public class HelpTestSuite extends TestSuite {

    /**
     * The extended attributes test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();
        lSuite.addTestSuite(TestGetHelpContentUrlService.class);
        return lSuite;
    }
}