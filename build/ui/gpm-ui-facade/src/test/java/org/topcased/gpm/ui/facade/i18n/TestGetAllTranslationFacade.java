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

import java.util.Map;

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.i18n.I18nFacade;
import org.topcased.gpm.ui.facade.server.i18n.I18nTranslationManager;

/**
 * TestGetAllTranslationFacade
 * 
 * @author jlouisy
 */
public class TestGetAllTranslationFacade extends AbstractFacadeTestCase {

    /**
     * Normal case
     */
    public void testNormalCase() {
        I18nFacade lI18nFacade = getFacadeLocator().getI18nFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get FR translations.");
        }
        I18nTranslationManager lTranslationManager =
                lI18nFacade.getTranslationManager("fr_FR");

        //Messages
        Map<String, String> lMessages =
                lTranslationManager.getAllTextTranslations();
        assertEquals(3, lMessages.size());
        assertTrue(lMessages.containsKey("SHEET_NAME"));
        assertEquals("SHEET_NAME/fr_FR", lMessages.get("SHEET_NAME"));
        assertTrue(lMessages.containsKey("EXTENSION_POINT_FIELD"));
        assertEquals("EXTENSION_POINT_FIELD/fr_FR",
                lMessages.get("EXTENSION_POINT_FIELD"));
        assertTrue(lMessages.containsKey("EXTENDED_ACTION_FIELD"));
        assertEquals("EXTENDED_ACTION_FIELD/fr_FR",
                lMessages.get("EXTENDED_ACTION_FIELD"));

        //Images
        Map<String, String> lImages =
                lTranslationManager.getAllImageTranslations();
        assertEquals(0, lImages.size());

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get EN translations.");
        }
        lTranslationManager = lI18nFacade.getTranslationManager("en_EN");

        //Messages
        lMessages = lTranslationManager.getAllTextTranslations();
        assertEquals(6, lMessages.size());
        assertTrue(lMessages.containsKey("SHEET_NAME"));
        assertEquals("SHEET_NAME/en_EN", lMessages.get("SHEET_NAME"));
        assertTrue(lMessages.containsKey("EXTENSION_POINT_FIELD"));
        assertEquals("EXTENSION_POINT_FIELD/en_EN",
                lMessages.get("EXTENSION_POINT_FIELD"));
        assertTrue(lMessages.containsKey("EXTENDED_ACTION_FIELD"));
        assertEquals("EXTENDED_ACTION_FIELD/en_EN",
                lMessages.get("EXTENDED_ACTION_FIELD"));

        //Images
        lImages = lTranslationManager.getAllImageTranslations();
        assertEquals(6, lImages.size());
        assertTrue(lImages.containsKey("CHOICE 1"));
        assertEquals("images/arrow_down.gif", lImages.get("CHOICE 1"));
        assertTrue(lImages.containsKey("CHOICE 2"));
        assertEquals("images/arrow_down.gif", lImages.get("CHOICE 2"));
        assertTrue(lImages.containsKey("CHOICE 3"));
        assertEquals("images/arrow_down.gif", lImages.get("CHOICE 3"));
        assertTrue(lImages.containsKey("CHOICE 4"));
        assertEquals("images/arrow_down.gif", lImages.get("CHOICE 4"));
        assertTrue(lImages.containsKey("CHOICE 5"));
        assertEquals("images/arrow_down.gif", lImages.get("CHOICE 5"));
        assertTrue(lImages.containsKey("CHOICE 6"));
        assertEquals("images/arrow_down.gif", lImages.get("CHOICE 6"));
    }
}
