/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.fields;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A test suite managing all tests around fields.
 * 
 * @author sie
 */
public class FieldsTestSuite extends TestSuite {

    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();
        lSuite.addTestSuite(TestGetFieldService.class);
        lSuite.addTestSuite(TestGetFieldNamesService.class);

        lSuite.addTestSuite(TestCreateSimpleFieldService.class);
        lSuite.addTestSuite(TestCreateChoiceFieldService.class);
        lSuite.addTestSuite(TestCreateAttachedFieldService.class);
        lSuite.addTestSuite(TestCreateMultipleFieldService.class);

        lSuite.addTestSuite(TestDeleteFieldService.class);

        lSuite.addTestSuite(TestAddToDisplayGroupDataService.class);

        lSuite.addTestSuite(TestGetFieldsContainer.class);
        return lSuite;
    }

}
