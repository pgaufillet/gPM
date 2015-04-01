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
 * TestGetUserListSortedByNameFacade
 * 
 * @author jlouisy
 */
public class TestGetUserListSortedByNameFacade extends AbstractFacadeTestCase {

    private static final String[] EXPECTED_RESULT_NAME =
            { "gPM Administrator", "gPM User", "gPM User", "gPM User" };

    private static final String[] EXPECTED_RESULT_FORNAME =
            { null, "forname 1", "forname 2", "forname 3" };

    /**
     * Normal case
     */
    public void testNormalCase() {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get User list sorted by login.");
        }
        List<UiUser> lUserList =
                getFacadeLocator().getUserFacade().getUserLists().getUserListSortedByName();

        assertEquals(EXPECTED_RESULT_NAME.length, lUserList.size());
        for (int i = 0; i < lUserList.size(); i++) {
            assertEquals(EXPECTED_RESULT_NAME[i], lUserList.get(i).getName());
            assertEquals(EXPECTED_RESULT_FORNAME[i],
                    lUserList.get(i).getForename());
        }
    }
}
