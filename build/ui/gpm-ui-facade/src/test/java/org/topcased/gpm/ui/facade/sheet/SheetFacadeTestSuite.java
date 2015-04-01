/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.sheet;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * SheetFacadeTestSuite
 * 
 * @author nveillet
 */
public class SheetFacadeTestSuite extends TestSuite {

    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite("SheetFacade test suite");
        lSuite.addTestSuite(TestGetSheetFacade.class);
        lSuite.addTestSuite(TestGetSheetByTypeFacade.class);
        lSuite.addTestSuite(TestCreateSheetFacade.class);
        lSuite.addTestSuite(TestDuplicateSheetFacade.class);
        lSuite.addTestSuite(TestInitializeSheetFacade.class);
        lSuite.addTestSuite(TestDeleteSheetFacade.class);
        lSuite.addTestSuite(TestUpdateSheetFacade.class);
        lSuite.addTestSuite(TestChangeStateFacade.class);
        lSuite.addTestSuite(TestGetAvailableTransitionsFacade.class);
        lSuite.addTestSuite(TestGetCreatableSheetTypesFacade.class);
        lSuite.addTestSuite(TestGetInitializableSheetTypesFacade.class);
        lSuite.addTestSuite(TestGetSheetTypeNameFacade.class);
        return lSuite;
    }
}
