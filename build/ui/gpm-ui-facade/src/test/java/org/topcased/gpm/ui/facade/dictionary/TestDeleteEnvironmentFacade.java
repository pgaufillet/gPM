/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.dictionary;

import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;

/**
 * TestDeleteEnvironmentFacade
 * 
 * @author jlouisy
 */
public class TestDeleteEnvironmentFacade extends AbstractFacadeTestCase {

    private static final String ENVIRONMENT_NAME = "new";

    /**
     * Normal case
     */
    public void testNormalCase() {

        UiSession lSession = adminUserSession.getDefaultGlobalSession();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Create Environment.");
        }
        getFacadeLocator().getDictionaryFacade().createEnvironment(lSession,
                ENVIRONMENT_NAME, true);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Delete Environment.");
        }
        getFacadeLocator().getDictionaryFacade().deleteEnvironment(lSession,
                ENVIRONMENT_NAME);

        assertFalse(getFacadeLocator().getDictionaryFacade().getEnvironmentNames(
                lSession).contains(ENVIRONMENT_NAME));
    }

    /**
     * Not existing environment case
     */
    public void testNotExistingEnvironmentCase() {

        UiSession lSession = adminUserSession.getDefaultGlobalSession();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Delete Environment.");
        }
        try {
            getFacadeLocator().getDictionaryFacade().deleteEnvironment(
                    lSession, "not_exists_env");
        }
        catch (InvalidNameException e) {
            assertEquals("Invalid environment not_exists_env", e.getMessage());
            return;
        }
        fail("Exception should be thrown.");
    }
}
