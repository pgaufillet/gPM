/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.i18n;

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.i18n.I18nFacade;

/**
 * TestGetUserLanguageFacade
 * 
 * @author jlouisy
 */
public class TestGetUserLanguageFacade extends AbstractFacadeTestCase {

    /**
     * Normal case
     */
    public void testNormalCase() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        I18nFacade lI18nFacade = getFacadeLocator().getI18nFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get ADMIN language.");
        }
        assertEquals("fr", lI18nFacade.getUserLanguage(lUiSession.getParent()));

        lUiSession = loginAsUser().getSession(DEFAULT_PRODUCT_NAME);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get USER language.");
        }
        assertEquals("en", lI18nFacade.getUserLanguage(lUiSession.getParent()));
        logoutAsUser(lUiSession.getParent());
    }
}
