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

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.dictionary.UiEnvironment;

/**
 * TestGetEnvironmentFacade
 * 
 * @author jlouisy
 */
public class TestGetEnvironmentFacade extends AbstractFacadeTestCase {

    /**
     * Normal case
     */
    public void testNormalCase() {

        UiSession lSession = adminUserSession.getDefaultGlobalSession();

        // Get environments names
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Environment.");
        }
        UiEnvironment lEnvironment =
                getFacadeLocator().getDictionaryFacade().getEnvironment(
                        lSession, "default");

        assertEquals("default", lEnvironment.getName());
        assertTrue(lEnvironment.isPublic());
    }
}
