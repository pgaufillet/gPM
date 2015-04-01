/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.user;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;

/**
 * UserFacade.getUserMailAddresses test
 * 
 * @author nveilet
 */
public class TestGetUserMailAddressesFacade extends AbstractFacadeTestCase {

    private static final int MAIL_NUMBER = 4;

    private static final Collection<String> MAIL_LIST =
            Arrays.asList("gpm-administrator@topcased.org",
                    "gpm-user@topcased.org");

    /**
     * Normal case
     */
    public void testNormalCase() {

        UiSession lSession = adminUserSession.getSession(DEFAULT_PRODUCT_NAME);

        // Get mail addresses
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get mail addresses.");
        }
        List<String> lUserMails =
                getFacadeLocator().getUserFacade().getUserMailAddresses(
                        lSession);

        assertEquals("Diferent number of user mail addresses.", MAIL_NUMBER,
                lUserMails.size());

        assertTrue("Invalid user mail addresses list.",
                lUserMails.containsAll(MAIL_LIST));
    }
}
