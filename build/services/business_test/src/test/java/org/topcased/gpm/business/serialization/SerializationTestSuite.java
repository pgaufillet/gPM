/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Sébastien René(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * SerializationTestSuite
 * 
 * @author srene
 */
public class SerializationTestSuite extends TestSuite {
    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();
        lSuite.addTestSuite(TestSerializeSheets.class);
        lSuite.addTestSuite(TestSerializeSheet.class);
        lSuite.addTestSuite(TestSerializeSheetsWithXMLDocument.class);
        return lSuite;
    }
}
