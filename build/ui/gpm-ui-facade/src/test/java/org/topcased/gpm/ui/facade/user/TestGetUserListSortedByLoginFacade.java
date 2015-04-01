/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.user;

import java.util.List;

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUser;

/**
 * TestGetUserListSortedByLoginFacade
 * 
 * @author jlouisy
 */
public class TestGetUserListSortedByLoginFacade extends AbstractFacadeTestCase {

    private static final String[] EXPECTED_RESULT =
            { "admin", "user", "user bis", "user ter" };

    /**
     * Normal case
     */
    public void testNormalCase() {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get User list sorted by login.");
        }
        List<UiUser> lUserList =
                getFacadeLocator().getUserFacade().getUserLists().getUserListSortedByLogin();

        assertEquals(EXPECTED_RESULT.length, lUserList.size());
        for (int i = 0; i < lUserList.size(); i++) {
            assertEquals(EXPECTED_RESULT[i], lUserList.get(i).getLogin());
        }
    }
}
