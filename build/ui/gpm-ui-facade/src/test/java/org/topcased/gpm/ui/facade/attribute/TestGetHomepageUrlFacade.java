/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.attribute;

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.attribute.AttributeFacade;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;

/**
 * TestGetHomepageUrlFacade
 * 
 * @author jlouisy
 */
public class TestGetHomepageUrlFacade extends AbstractFacadeTestCase {

    /**
     * Normal case
     */
    public void testNormalCase() {
        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        AttributeFacade lAttributeFacade =
                getFacadeLocator().getAttributeFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get ROOT_PRODUCT_ATTRIBUTE on ROOT PRODUCT.");
        }
        String lAttribute = lAttributeFacade.getHomepageUrl(lUiSession);

        assertEquals("http://www.topcased.org/", lAttribute);
    }
}
