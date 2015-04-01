/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Szadel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.i18n;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A test suite managing all tests around I18N.
 * 
 * @author tszadel
 */
public class I18nTestSuite extends TestSuite {

    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();
        lSuite.addTestSuite(TestGetPreferredLanguageService.class);
        lSuite.addTestSuite(TestSetPreferredLanguageService.class);
        lSuite.addTestSuite(TestGetValueService.class);
        lSuite.addTestSuite(TestGetValueForUserService.class);
        lSuite.addTestSuite(TestSetValueService.class);
        return lSuite;
    }
}
