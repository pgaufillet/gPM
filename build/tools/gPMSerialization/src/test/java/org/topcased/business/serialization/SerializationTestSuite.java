/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.business.serialization;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A test suite managing all tests around serialization.
 * 
 * @author nveillet
 */
public class SerializationTestSuite extends TestSuite {

    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();

        lSuite.addTestSuite(TestExportation.class);
        lSuite.addTestSuite(TestExportationLimited.class);
        lSuite.addTestSuite(TestExportationKind.class);

        lSuite.addTestSuite(TestMigration.class);

        return lSuite;
    }
}
