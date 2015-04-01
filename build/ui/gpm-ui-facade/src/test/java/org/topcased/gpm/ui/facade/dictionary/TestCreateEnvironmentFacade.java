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
import org.topcased.gpm.ui.facade.shared.dictionary.UiEnvironment;

/**
 * TestCreateEnvironmentFacade
 * 
 * @author jlouisy
 */
public class TestCreateEnvironmentFacade extends AbstractFacadeTestCase {

    private static final String NEW_ENVIRONMENT_NAME = "NEW_CAT";

    /**
     * Already existing environment case
     */
    public void testAlreadyExistingEnvironmentCreationCase() {

        UiSession lSession = adminUserSession.getDefaultGlobalSession();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Create Environment.");
        }
        try {
            getFacadeLocator().getDictionaryFacade().createEnvironment(
                    lSession, "default", true);
        }
        catch (InvalidNameException e) {
            assertEquals("Invalid name 'default'", e.getMessage());
            return;
        }
        fail("Exception should be thrown.");
    }

    /**
     * Normal case
     */
    public void testNormalCase() {

        UiSession lSession = adminUserSession.getDefaultGlobalSession();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Create Environment.");
        }
        getFacadeLocator().getDictionaryFacade().createEnvironment(lSession,
                NEW_ENVIRONMENT_NAME, true);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get created Environment.");
        }
        UiEnvironment lEnvironment =
                getFacadeLocator().getDictionaryFacade().getEnvironment(
                        lSession, NEW_ENVIRONMENT_NAME);

        assertEquals(NEW_ENVIRONMENT_NAME, lEnvironment.getName());
        assertTrue(lEnvironment.isPublic());
    }
}
