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
 * Tests the getValue method.
 * 
 * @author tszadel
 */
public class TestGetValueService extends AbstractBusinessServiceTestCase {
    /** The default language of the application. */
    private static final String DEFAULT_LANGUAGE = "en";

    /** The tested label key. */
    private static final String LABEL_KEY = "CAT_birthdate";

    /** The expected translation for the label key. */
    private static final String EXPECTED_TRANSLATION = "Birth Date";

    /** The unknown label key. */
    private static final String UNKNOWN_LABEL_KEY = "@@UnitTest_Error@@";

    /** The i18n Service. */
    private I18nService i18nService;

    /**
     * Tests the method getValue.
     */
    public void testNormalCase() {
        i18nService = serviceLocator.getI18nService();

        String lValue = i18nService.getValue(LABEL_KEY, DEFAULT_LANGUAGE);
        assertNotNull("getValue returns a null value for label key "
                + LABEL_KEY, lValue);
        assertEquals("Translation for " + LABEL_KEY
                + " is incorrect. Expected : " + EXPECTED_TRANSLATION
                + " - found : " + lValue, EXPECTED_TRANSLATION, lValue);
    }

    /**
     * Tests an error case : the label is unknown.
     */
    public void testErrorCase() {
        i18nService = serviceLocator.getI18nService();
        String lValue =
                i18nService.getValue(UNKNOWN_LABEL_KEY, DEFAULT_LANGUAGE);
        assertNotNull("getValue returns a null value for label key "
                + UNKNOWN_LABEL_KEY, lValue);
        assertEquals("Translation for " + LABEL_KEY
                + " is incorrect. Expected : " + UNKNOWN_LABEL_KEY
                + " - found : " + lValue, UNKNOWN_LABEL_KEY, lValue);
    }

    /**
     * Tests an error case : the label is null.
     */
    public void testErrorCaseNullLabel() {
        i18nService = serviceLocator.getI18nService();
        try {
            i18nService.getValue(null, DEFAULT_LANGUAGE);
            fail("getValue should throw an IllegalArgumentException if labelKey is null");
        }
        catch (IllegalArgumentException e) {
            // Ok
        }
    }

    /**
     * Tests an error case : the language is null.
     */
    public void testErrorCaseNullLanguage() {
        i18nService = serviceLocator.getI18nService();
        try {
            i18nService.getValue(LABEL_KEY, null);
            fail("getValue should throw an IllegalArgumentException if labelKey is null");
        }
        catch (IllegalArgumentException e) {
            // Ok
        }
    }
}
