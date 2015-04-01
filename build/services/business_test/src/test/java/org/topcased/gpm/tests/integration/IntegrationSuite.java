/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.tests.integration;

import junit.framework.Test;
import junit.framework.TestSuite;

public class IntegrationSuite extends TestSuite {

    public static Test suite() {
        TestSuite lSuite = new TestSuite();
        lSuite.addTestSuite(TestSheetExport.class);
        lSuite.addTestSuite(TestFilters.class);
        lSuite.addTestSuite(TestSerialization.class);
        return lSuite;
    }

}
