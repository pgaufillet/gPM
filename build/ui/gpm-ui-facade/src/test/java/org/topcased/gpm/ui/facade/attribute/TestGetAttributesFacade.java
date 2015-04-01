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

import java.util.List;

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.attribute.AttributeFacade;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.attribute.UiAttribute;

/**
 * TestGetAttributesFacade
 * 
 * @author jlouisy
 */
public class TestGetAttributesFacade extends AbstractFacadeTestCase {

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
            LOGGER.info("Get all attributes on ROOT PRODUCT.");
        }
        List<UiAttribute> lAttributes =
                lAttributeFacade.getAttributes(lSession, lProductId);

        assertEquals(2, lAttributes.size());

        // Get product_url attribute
        UiAttribute lAttribute = null;
        for (UiAttribute lTmpAttribute : lAttributes) {
            if ("product_url".equals(lTmpAttribute.getName())) {
                lAttribute = lTmpAttribute;
            }
        }
        assertNotNull(lAttribute);
        assertEquals(1, lAttribute.getValues().size());
        assertTrue(lAttribute.getValues().contains("http://www.topcased.org/"));

        // Get ROOT_PRODUCT_ATTRIBUTE attribute
        lAttribute = null;
        for (UiAttribute lTmpAttribute : lAttributes) {
            if ("ROOT_PRODUCT_ATTRIBUTE".equals(lTmpAttribute.getName())) {
                lAttribute = lTmpAttribute;
            }
        }
        assertNotNull(lAttribute);
        assertEquals(3, lAttribute.getValues().size());
        assertTrue(lAttribute.getValues().contains(
                "ROOT_PRODUCT_ATTRIBUTE VAL1"));
        assertTrue(lAttribute.getValues().contains(
                "ROOT_PRODUCT_ATTRIBUTE VAL2"));
        assertTrue(lAttribute.getValues().contains(
                "ROOT_PRODUCT_ATTRIBUTE VAL3"));
    }
}
