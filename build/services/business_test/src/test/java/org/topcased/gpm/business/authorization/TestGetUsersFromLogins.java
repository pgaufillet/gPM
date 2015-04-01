/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thibault Landre (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.authorization.service.EndUserData;

/**
 * TestGetUserFromRole
 * 
 * @author tlandre
 */
public class TestGetUsersFromLogins extends AbstractBusinessServiceTestCase {

    /**
     * Tests the method in a normal case.
     */
    public void testNormalCase() {

        List<String> logins = new ArrayList<String>();
        logins.add(GpmTestValues.USER_ADMIN);
        logins.add(GpmTestValues.USER_USER2);

        // Test to get a light end user data without attributes
        List<EndUserData> lEndUserDatas =
                authorizationService.getUsersFromLogins(logins, true, true);
        assertNotNull(lEndUserDatas);
        assertTrue(lEndUserDatas.size() == 2);
        assertNull(lEndUserDatas.get(0).getUserAttributes());
        assertNull(lEndUserDatas.get(1).getUserAttributes());

        // Test to get a complete end user data with attributes
        List<EndUserData> lEndUserDatasWithAttributes =
                authorizationService.getUsersFromLogins(logins, true, false);
        assertNotNull(lEndUserDatasWithAttributes);
        assertTrue(lEndUserDatasWithAttributes.size() == 2);
        assertNotNull(lEndUserDatasWithAttributes.get(0).getUserAttributes());
        assertNotNull(lEndUserDatasWithAttributes.get(1).getUserAttributes());
    }
}
