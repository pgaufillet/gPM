/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin), Sébastien René
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A test suite managing all tests around roles.
 * 
 * @author nsamson
 */
public class RoleTestSuite extends TestSuite {

    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();
        lSuite.addTestSuite(TestGetRolesService.class);
        lSuite.addTestSuite(TestAddRoleService.class);
        lSuite.addTestSuite(TestGetRolesNamesService.class);
        lSuite.addTestSuite(TestCreateRoleService.class);
        lSuite.addTestSuite(TestDeleteRoleService.class);
        lSuite.addTestSuite(TestSetRolesService.class);
        lSuite.addTestSuite(TestGetAdminRolesNamesService.class);
        lSuite.addTestSuite(TestSelectRole.class);
        lSuite.addTestSuite(TestAddRoleForUsersService.class);
        lSuite.addTestSuite(TestOverriddenRole.class);
        return lSuite;
    }

}
