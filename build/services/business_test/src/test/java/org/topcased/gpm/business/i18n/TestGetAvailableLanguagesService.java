/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Szadel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.i18n;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.i18n.service.I18nService;

/**
 * Tests the getPreferredLanguage method.
 * 
 * @author tszadel
 */
public class TestGetAvailableLanguagesService extends
        AbstractBusinessServiceTestCase {

    /** The preferred language of the user. */
    private static final String PREFERRED_LANGUAGE = "en_EN";

    /** The i18n Service. */
    private I18nService i18nService;

    /**
     * Tests the method getPreferredLanguage.
     */
    public void testNormalCase() {
        i18nService = serviceLocator.getI18nService();

        String[] lLangs = i18nService.getAvailableLanguages();
        assertNotNull("getPreferredLanguage returns a null value.", lLangs);
        assertEquals("The preferred language of the user differs. Expected : "
                + PREFERRED_LANGUAGE + " - actual : " + lLangs.length,
                lLangs.length, 2);
    }
}
