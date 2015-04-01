/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * The extended Services test suite.
 * 
 * @author ogehin
 */
public class WebservicesTestSuite extends TestSuite {
    /**
     * The extended Services test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();

        lSuite.addTestSuite(TestWSAuthorization.class);
        lSuite.addTestSuite(TestWSGetSheets.class);
        lSuite.addTestSuite(TestWSUpdateSheets.class);
        lSuite.addTestSuite(TestWSLockSheets.class);
        lSuite.addTestSuite(TestWSAttributesSheets.class);
        lSuite.addTestSuite(TestWSLinkSheets.class);
        lSuite.addTestSuite(TestWSRevision.class);
        lSuite.addTestSuite(TestWSProducts.class);
        lSuite.addTestSuite(TestWSContext.class);
        lSuite.addTestSuite(TestWSFilters.class);
        lSuite.addTestSuite(TestWSExtendedAction.class);
        lSuite.addTestSuite(TestWSDictionnary.class);
        lSuite.addTestSuite(TestWSReport.class);
        lSuite.addTestSuite(TestWSVirtualFields.class);
        lSuite.addTestSuite(TestWSImportSheet.class);

        return lSuite;
    }
}