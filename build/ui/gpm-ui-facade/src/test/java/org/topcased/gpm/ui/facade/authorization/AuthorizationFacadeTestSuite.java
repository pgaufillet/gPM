/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.authorization;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * AuthorizationFacadeTestSuite
 * 
 * @author nveillet
 */
public class AuthorizationFacadeTestSuite extends TestSuite {
    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite("AuthorizationFacade test suite");
        lSuite.addTestSuite(TestLoginFacade.class);
        lSuite.addTestSuite(TestLogoutFacade.class);
        lSuite.addTestSuite(TestGetAvailableProductsFacade.class);
        lSuite.addTestSuite(TestGetAvailableProductsHierarchyFacade.class);
        lSuite.addTestSuite(TestGetAvailableRolesFacade.class);
        lSuite.addTestSuite(TestGetAvailableProcessesFacade.class);
        lSuite.addTestSuite(TestGetProductWithHighRoleFacade.class);
        lSuite.addTestSuite(TestGetDefaultRoleFacade.class);
        lSuite.addTestSuite(TestGetAllRolesFacade.class);
        return lSuite;
    }
}
