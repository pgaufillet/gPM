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

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.attribute.AttributeFacade;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.attribute.UiAttribute;

/**
 * TestSetAttributesFacade
 * 
 * @author jlouisy
 */
public class TestSetAttributesFacade extends AbstractFacadeTestCase {

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

        List<UiAttribute> lAttributes = new ArrayList<UiAttribute>();

        //ATTRIBUTE 1
        List<String> lAttributeValues = new ArrayList<String>();
        lAttributeValues.add("ATTRIBUTE1/VALUE1");
        lAttributeValues.add("ATTRIBUTE1/VALUE2");
        lAttributeValues.add("ATTRIBUTE1/VALUE3");
        lAttributes.add(new UiAttribute("ATTRIBUTE1", lAttributeValues));

        //ATTRIBUTE 2
        lAttributes.add(new UiAttribute("ATTRIBUTE2", null));

        //ATTRIBUTE 3
        lAttributes.add(new UiAttribute("ATTRIBUTE3", new ArrayList<String>()));

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Set Attributes on ROOT_PRODUCT.");
        }
        lAttributeFacade.setAttributes(lSession, lProductId, lAttributes);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get all attributes on ROOT PRODUCT.");
        }
        List<UiAttribute> lResultAttributes =
                lAttributeFacade.getAttributes(lSession, lProductId);

        assertEquals(1, lResultAttributes.size());

        UiAttribute lAttribute = lAttributes.get(0);
        assertEquals("ATTRIBUTE1", lAttribute.getName());
        assertTrue(lAttribute.getValues().contains("ATTRIBUTE1/VALUE1"));
        assertTrue(lAttribute.getValues().contains("ATTRIBUTE1/VALUE2"));
        assertTrue(lAttribute.getValues().contains("ATTRIBUTE1/VALUE3"));
    }
}
