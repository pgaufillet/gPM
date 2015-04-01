/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.serialization.data.User;

/**
 * TestGetUserFromRole
 * 
 * @author mfranche
 */
public class TestGetUserFromRole extends AbstractBusinessServiceTestCase {

    /**
     * Tests the method in a normal case.
     */
    public void testNormalCase() {
        User lUser = authorizationService.getUserFromRole(adminRoleToken);

        assertNotNull("The user should not be null", lUser);
        assertEquals("The attribute login of the user is incorrect.",
                lUser.getLogin(), getAdminLogin()[0]);
    }
}
