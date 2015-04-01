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

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;

/**
 * TestGetUserListSortedByLoginFacade
 * 
 * @author jlouisy
 */
public class TestDeleteUserFacade extends AbstractFacadeTestCase {

    private static final String USER_LOGIN = "user";

    /**
     * Normal case
     */
    public void testNormalCase() {

        UiSession lSession = adminUserSession.getSession(DEFAULT_PRODUCT_NAME);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Delete User.");
        }
        getFacadeLocator().getUserFacade().deleteUser(lSession, USER_LOGIN);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Trying to get deleted User.");
        }
        assertNull(getFacadeLocator().getUserFacade().getUser(USER_LOGIN));
    }
}
