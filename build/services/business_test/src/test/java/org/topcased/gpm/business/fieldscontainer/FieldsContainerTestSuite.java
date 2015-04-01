/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.fieldscontainer;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * FieldsContainerTestSuite
 * 
 * @author mkargbo
 */
public class FieldsContainerTestSuite {

    /**
     * Test over FieldsContainerService
     * 
     * @return Tests
     */
    public static Test suite() {
        TestSuite lSuite =
                new TestSuite(
                        "Test for org.topcased.gpm.business.fieldscontainer");
        //$JUnit-BEGIN$
        lSuite.addTestSuite(TestGetValuesContainer.class);
        lSuite.addTestSuite(TestGetFieldsContainerInfo.class);
        //$JUnit-END$
        return lSuite;
    }

}
