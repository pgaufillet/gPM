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
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.i18n.service.I18nService;

/**
 * Tests the setPreferredLanguage method.
 * 
 * @author tszadel
 */
public class TestSetPreferredLanguageService extends
        AbstractBusinessServiceTestCase {

    /** The default language of the application. */
    private static final String DEFAULT_LANGUAGE = "en";

    /** The new preferred language of the user. */
    private static final String NEW_PREFERRED_LANGUAGE = "fr";

    /** The i18n Service. */
    private I18nService i18nService;

    /**
     * Tests the method setPreferredLanguage.
     */
    public void testNormalCase() {
        i18nService = serviceLocator.getI18nService();

        // We begin to test if the preferred language is not the one we are
        // about to set.
        String lPrefLang = i18nService.getPreferredLanguage(adminUserToken);
        assertNotSame("The preferred language is set to " + lPrefLang
                + ". Please use another one.", NEW_PREFERRED_LANGUAGE,
                lPrefLang);

        // The test...
        i18nService.setPreferredLanguage(adminUserToken, NEW_PREFERRED_LANGUAGE);

        // Verification...
        lPrefLang = i18nService.getPreferredLanguage(adminUserToken);
        assertNotNull("getPreferredLanguage returns a null value.", lPrefLang);
        assertEquals("The preferred language of the user differs. Expected : "
                + NEW_PREFERRED_LANGUAGE + " - actual : " + lPrefLang,
                NEW_PREFERRED_LANGUAGE, lPrefLang);
    }

    /**
     * Tests the setPreferredLanguage with null lang.
     */
    public void testErrorCaseNullLang() {
        i18nService = serviceLocator.getI18nService();

        i18nService.setPreferredLanguage(adminUserToken, null);

        // In this case, the preferred language should be the default one
        String lPrefLang = i18nService.getPreferredLanguage(adminUserToken);
        assertNotNull("getPreferredLanguage returns a null value.", lPrefLang);
        assertEquals("The preferred language of the user differs. Expected : "
                + DEFAULT_LANGUAGE + " - actual : " + lPrefLang,
                DEFAULT_LANGUAGE, lPrefLang);
    }

    /**
     * Tests the setPreferredLanguage with null user token.
     */
    public void testErrorCaseNullUserToken() {
        i18nService = serviceLocator.getI18nService();

        try {
            i18nService.setPreferredLanguage(null, NEW_PREFERRED_LANGUAGE);
        }
        catch (InvalidTokenException e) {
            // This is normal
        }
    }

    /**
     * Tests the setPreferredLanguage with a bad language.
     */
    public void testBadLanguageCase() {
        i18nService = serviceLocator.getI18nService();

        i18nService.setPreferredLanguage(adminUserToken, "");

        // Verification...
        String lPrefLang = i18nService.getPreferredLanguage(adminUserToken);
        assertNotNull("getPreferredLanguage returns a null value.", lPrefLang);
    }
}
