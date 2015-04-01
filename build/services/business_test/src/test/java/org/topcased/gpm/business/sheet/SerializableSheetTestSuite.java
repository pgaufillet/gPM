/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * SerializableSheetTestSuite
 * 
 * @author mfranche
 */
public class SerializableSheetTestSuite extends TestSuite {

    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();

        lSuite.addTestSuite(TestGetSerializableSheetTypesService.class);
        lSuite.addTestSuite(TestGetCreatableSerializableSheetTypesService.class);
        lSuite.addTestSuite(TestGetSerializableSheetTypeByNameService.class);
        lSuite.addTestSuite(TestGetSerializableSheetTypeService.class);

        lSuite.addTestSuite(TestGetSerializableSheetByRefService.class);

        return lSuite;
    }
}
