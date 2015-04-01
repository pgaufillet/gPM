/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.user;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * UserFacadeTestSuite
 * 
 * @author nveillet
 */
public class UserFacadeTestSuite extends TestSuite {

    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite("UserFacade test suite");
        lSuite.addTestSuite(TestGetUserMailAddressesFacade.class);
        lSuite.addTestSuite(TestGetUserListSortedByLoginFacade.class);
        lSuite.addTestSuite(TestGetUserListSortedByNameFacade.class);
        lSuite.addTestSuite(TestGetUserFacade.class);
        lSuite.addTestSuite(TestGetUserAffectationFacade.class);
        lSuite.addTestSuite(TestCreateUserFacade.class);
        lSuite.addTestSuite(TestUpdateUserFacade.class);
        lSuite.addTestSuite(TestDeleteUserFacade.class);
        lSuite.addTestSuite(TestUpdateUserAffectationFacade.class);
        return lSuite;
    }
}
