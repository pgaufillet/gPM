/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.attributes;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite for the service AttributesService.
 * 
 * @author llatil
 */
public class AttributesTestSuite extends TestSuite {

    /**
     * The extended attributes test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();
        lSuite.addTestSuite(TestGetGlobalAttributes.class);
        lSuite.addTestSuite(TestGetGlobalAttrNames.class);
        lSuite.addTestSuite(TestGet.class);
        lSuite.addTestSuite(TestGetAll.class);
        lSuite.addTestSuite(TestSet.class);
        lSuite.addTestSuite(TestSetGlobalAttributes.class);
        lSuite.addTestSuite(TestRemoveAll.class);
        lSuite.addTestSuite(TestAtomicIncrement.class);
        lSuite.addTestSuite(TestAtomicTestAndSet.class);
        return lSuite;
    }
}
