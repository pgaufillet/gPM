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
import org.topcased.gpm.ui.facade.shared.attribute.UiAttribute;

/**
 * TestGetAttributeFacade
 * 
 * @author jlouisy
 */
public class TestGetAttributeFacade extends AbstractFacadeTestCase {

    /**
     * Normal case
     */
    public void testNormalCase() {
        UiSession lSession = adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        AttributeFacade lAttributeFacade =
                getFacadeLocator().getAttributeFacade();

        // Get product Id
        String lProductId =
                getProductService().getProductId(lSession.getRoleToken(),
                        DEFAULT_PRODUCT_NAME);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get ROOT_PRODUCT_ATTRIBUTE on ROOT PRODUCT.");
        }
        UiAttribute lAttribute =
                lAttributeFacade.getAttribute(lSession, lProductId,
                        "ROOT_PRODUCT_ATTRIBUTE");

        assertEquals("ROOT_PRODUCT_ATTRIBUTE", lAttribute.getName());
        assertEquals(3, lAttribute.getValues().size());
        assertTrue(lAttribute.getValues().contains(
                "ROOT_PRODUCT_ATTRIBUTE VAL1"));
        assertTrue(lAttribute.getValues().contains(
                "ROOT_PRODUCT_ATTRIBUTE VAL2"));
        assertTrue(lAttribute.getValues().contains(
                "ROOT_PRODUCT_ATTRIBUTE VAL3"));

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get ROOT_PRODUCT_ATTRIBUTE_NO_VALUE on ROOT PRODUCT.");
        }
        lAttribute =
                lAttributeFacade.getAttribute(lSession, lProductId,
                        "ROOT_PRODUCT_ATTRIBUTE_NO_VALUE");

        assertNull(lAttribute);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get unknown attribute on ROOT PRODUCT.");
        }
        lAttribute =
                lAttributeFacade.getAttribute(lSession, lProductId, "UNKNOWN");

        assertNull(lAttribute);
    }
}
