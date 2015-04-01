/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien EBALLARD (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.user;

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUser;

/**
 * TestGetUserProfileFacade
 * 
 * @author jeballar
 */
public class TestGetUserProfileFacade extends AbstractFacadeTestCase {

    /**
     * Normal case
     */
    public void testNormalCase() {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get User.");
        }

        UiUserSession lUserSession = loginAsUser();
        UiUser lUser =
                getFacadeLocator().getUserFacade().getUserProfile(
                        lUserSession.getToken());

        assertEquals("user", lUser.getLogin());
        assertEquals("gPM User", lUser.getName());
        assertEquals("forname 1", lUser.getForename());
        assertEquals("gpm-user@topcased.org", lUser.getEmailAdress());
        assertEquals("en", lUser.getLanguage());
        assertNull(lUser.getPassWord());

        logoutAsUser(lUserSession);
    }
}
