/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.dictionary;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * DictionaryFacadeTestSuite
 * 
 * @author nveillet
 */
public class DictionaryFacadeTestSuite extends TestSuite {

    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite("DictionaryFacade test suite");
        lSuite.addTestSuite(TestGetEnvironmentNamesFacade.class);
        lSuite.addTestSuite(TestGetDictionaryCategoriesFacade.class);
        lSuite.addTestSuite(TestGetEnvironmentFacade.class);
        lSuite.addTestSuite(TestGetDictionaryCategoryValuesFacade.class);
        lSuite.addTestSuite(TestGetEnvironmentCategoryValuesFacade.class);
        lSuite.addTestSuite(TestCreateEnvironmentFacade.class);
        lSuite.addTestSuite(TestDeleteEnvironmentFacade.class);
        lSuite.addTestSuite(TestUpdatetDictionaryCategoryValuesFacade.class);
        lSuite.addTestSuite(TestUpdateEnvironmentCategoryValuesFacade.class);
        return lSuite;
    }
}
