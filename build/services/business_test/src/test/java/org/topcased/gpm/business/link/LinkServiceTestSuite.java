/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.link;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * LinkServiceTestSuite
 * 
 * @author mkargbo
 */
public class LinkServiceTestSuite extends TestSuite {

    /**
     * The link service test suite.
     * 
     * @return Tests to run.
     */
    public static Test suite() {
        TestSuite lSuite =
                new TestSuite("Test for org.topcased.gpm.business.link");
        lSuite.addTestSuite(TestLinkDirection.class);
        return lSuite;
    }

}
