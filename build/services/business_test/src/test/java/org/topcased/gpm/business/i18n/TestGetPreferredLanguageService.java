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
public class TestGetPreferredLanguageService extends
        AbstractBusinessServiceTestCase {

    /** The preferred language of the user. */
    private static final String PREFERRED_LANGUAGE = "en";

    /** The i18n Service. */
    private I18nService i18nService;

    /**
     * Tests the method getPreferredLanguage.
     */
    public void testNormalCase() {
        i18nService = serviceLocator.getI18nService();

        String lPrefLang = i18nService.getPreferredLanguage(adminUserToken);
        assertNotNull("getPreferredLanguage returns a null value.", lPrefLang);
        assertEquals("The preferred language of the user is incorrect",
                PREFERRED_LANGUAGE, lPrefLang);
    }
}
