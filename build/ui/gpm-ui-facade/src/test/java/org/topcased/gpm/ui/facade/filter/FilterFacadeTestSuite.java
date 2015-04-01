/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * FilterFacadeTestSuite
 * 
 * @author nveillet
 */
public class FilterFacadeTestSuite extends TestSuite {
    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite("FilterFacade test suite");
        lSuite.addTestSuite(TestGetFiltersFacade.class);
        lSuite.addTestSuite(TestGetFilterFacade.class);
        lSuite.addTestSuite(TestExecuteFilterTableFacade.class);
        lSuite.addTestSuite(TestExecuteFilterTreeFacade.class);
        lSuite.addTestSuite(TestExecuteFilterLinkCreationFacade.class);
        lSuite.addTestSuite(TestExecuteFilterLinkDeletionFacade.class);
        lSuite.addTestSuite(TestExecuteFilterSheetInitializationFacade.class);
        lSuite.addTestSuite(TestDeleteFilterFacade.class);
        lSuite.addTestSuite(TestGetHierarchyContainersFacade.class);
        lSuite.addTestSuite(TestGetUsableFieldsFacade.class);
        lSuite.addTestSuite(TestGetCategoryValuesFacade.class);
        lSuite.addTestSuite(TestGetSearcheableContainersFacade.class);
        lSuite.addTestSuite(TestGetAvailableVisibilitiesFacade.class);
        lSuite.addTestSuite(TestGetAvailableProductScopeFacade.class);
        lSuite.addTestSuite(TestSaveFilterFacade.class);
        return lSuite;
    }
}
