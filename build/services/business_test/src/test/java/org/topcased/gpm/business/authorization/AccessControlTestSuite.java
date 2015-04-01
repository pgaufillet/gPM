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
 * A test suite managing all tests around products.
 * 
 * @author nsamson
 */
public class AccessControlTestSuite extends TestSuite {

    /**
     * The test suite.
     * 
     * @return The tests to be run.
     */
    public static Test suite() {
        TestSuite lSuite = new TestSuite();
        lSuite.addTestSuite(TestSetGetTypeAccessControlService.class);
        lSuite.addTestSuite(TestSetGetAdminAccessControlService.class);
        lSuite.addTestSuite(TestIsAdminAccessDefineOnInstanceService.class);
        lSuite.addTestSuite(TestSetGetFieldAccessControlService.class);
        lSuite.addTestSuite(TestSetGetSheetTypeAccessControlService.class);
        lSuite.addTestSuite(TestSetGetTransitionAccessControlService.class);
        lSuite.addTestSuite(TestLoginLogoutService.class);
        lSuite.addTestSuite(TestAccessControlsHierarchy.class);
        lSuite.addTestSuite(TestApplyAccessControlsService.class);
        lSuite.addTestSuite(TestDeleteAcl.class);
        lSuite.addTestSuite(TestGetEditableFilterScopeService.class);
        lSuite.addTestSuite(TestGetExecutableFilterScopeService.class);
        lSuite.addTestSuite(TestGetSubFields.class);
        lSuite.addTestSuite(TestGetSubFieldsMultivalued.class);
        return lSuite;
    }

}
