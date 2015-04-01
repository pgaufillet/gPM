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
 * TestUpdateUserFacade
 * 
 * @author jlouisy
 */
public class TestUpdateUserFacade extends AbstractFacadeTestCase {

    private static final String USER_LOGIN = "user";

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

        UiUser lUserToUpdate =
                new UiUser(USER_LOGIN, NEW_USER_FORENAME, NEW_USER_NAME,
                        NEW_USER_LANGUAGE);
        lUserToUpdate.setEmailAdress(NEW_USER_EMAIL);
        lUserToUpdate.setPassWord(NEW_USER_PASSWORD);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Update User.");
        }
        getFacadeLocator().getUserFacade().updateUser(lSession, lUserToUpdate);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get updated User.");
        }
        UiUser lUpdatedUser =
                getFacadeLocator().getUserFacade().getUser(USER_LOGIN);

        assertEquals(lUpdatedUser.getLogin(), lUserToUpdate.getLogin());
        assertEquals(lUpdatedUser.getName(), lUserToUpdate.getName());
        assertEquals(lUpdatedUser.getForename(), lUserToUpdate.getForename());
        assertEquals(lUpdatedUser.getEmailAdress(),
                lUserToUpdate.getEmailAdress());
        assertEquals(lUpdatedUser.getLanguage(), lUserToUpdate.getLanguage());
        // Password attribute cannot be checked => trying to login
        UiUserSession lUserSession = null;
        try {
            lUserSession =
                    getFacadeLocator().getAuthorizationFacade().login(
                            USER_LOGIN, NEW_USER_PASSWORD);
        }
        catch (Exception e) {
            fail(e);
        }
        getFacadeLocator().getAuthorizationFacade().logout(lUserSession);
    }
}
