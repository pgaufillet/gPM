/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.dictionary;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;

/**
 * ProductFacade.getProduct test
 * 
 * @author nveillet
 */
public class TestGetEnvironmentNamesFacade extends AbstractFacadeTestCase {

    private static final List<String> ENVIRONMENTS;

    static {
        ENVIRONMENTS = new ArrayList<String>();
        ENVIRONMENTS.add("default");
    }

    /**
     * Normal case
     */
    public void testNormalCase() {

        UiSession lSession = adminUserSession.getDefaultGlobalSession();

        // Get environments names
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get environments names.");
        }
        List<String> lEnvironmentNames =
                getFacadeLocator().getDictionaryFacade().getEnvironmentNames(
                        lSession);

        assertEquals(ENVIRONMENTS.size(), lEnvironmentNames.size());
        assertTrue(ENVIRONMENTS.containsAll(lEnvironmentNames));
        assertTrue(lEnvironmentNames.containsAll(ENVIRONMENTS));
    }
}
