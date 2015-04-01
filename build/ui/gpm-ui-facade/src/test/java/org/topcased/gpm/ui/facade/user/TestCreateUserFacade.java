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
import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUser;

/**
 * TestGetUserListSortedByLoginFacade
 * 
 * @author jlouisy
 */
public class TestCreateUserFacade extends AbstractFacadeTestCase {

    private static final String NEW_USER_LOGIN = "new user";

    private static final String NEW_USER_FORENAME = "new user forename";

    private static final String NEW_USER_NAME = "new user name";

    private static final String NEW_USER_LANGUAGE = "pl";

    private static final String NEW_USER_EMAIL = "new.user@topcased.org";

    private static final String NEW_USER_PASSWORD = "newuserpwd";

    /**
     * Normal case
     */
    public void testNormalCase() {

        UiSession lSession = adminUserSession.getSession(DEFAULT_PRODUCT_NAME);

        UiUser lUserToCreate =
                new UiUser(NEW_USER_LOGIN, NEW_USER_FORENAME, NEW_USER_NAME,
                        NEW_USER_LANGUAGE);
        lUserToCreate.setEmailAdress(NEW_USER_EMAIL);
        lUserToCreate.setPassWord(NEW_USER_PASSWORD);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Create User.");
        }
        getFacadeLocator().getUserFacade().createUser(lSession, lUserToCreate);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get created User.");
        }
        UiUser lCreatedUser =
                getFacadeLocator().getUserFacade().getUser(NEW_USER_LOGIN);

        assertEquals(lCreatedUser.getLogin(), lUserToCreate.getLogin());
        assertEquals(lCreatedUser.getName(), lUserToCreate.getName());
        assertEquals(lCreatedUser.getForename(), lUserToCreate.getForename());
        assertEquals(lCreatedUser.getEmailAdress(),
                lUserToCreate.getEmailAdress());
        assertEquals(lCreatedUser.getLanguage(), lUserToCreate.getLanguage());
        // Password attribute cannot be checked => trying to login
        UiUserSession lUserSession = null;
        try {
            lUserSession =
                    getFacadeLocator().getAuthorizationFacade().login(
                            NEW_USER_LOGIN, NEW_USER_PASSWORD);
        }
        catch (Exception e) {
            fail(e);
        }
        getFacadeLocator().getAuthorizationFacade().logout(lUserSession);
    }
}
