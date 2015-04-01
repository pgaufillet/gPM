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
 * Tests the setValue method.
 * 
 * @author tszadel
 */
public class TestSetValueService extends AbstractBusinessServiceTestCase {
    /** The default language of the application. */
    private static final String DEFAULT_LANGUAGE = "en";

    /** The tested label key. */
    private static final String LABEL_KEY = "@@UnitTest_New_Label@@";

    /** The translation for the label key. */
    private static final String LABEL_TRANSLATION = "Test Label";

    /** An existing label key. */
    private static final String EXISTING_LABEL_KEY = "CAT_birthdate";

    /** The i18n Service. */
    private I18nService i18nService;

    /**
     * Tests the method setValue.
     */
    public void testNormalCase() {
        i18nService = serviceLocator.getI18nService();

        // The test
        i18nService.setValue(LABEL_KEY, DEFAULT_LANGUAGE, LABEL_TRANSLATION);

        // Has the value been set ?
        String lValue = i18nService.getValue(LABEL_KEY, DEFAULT_LANGUAGE);
        assertNotNull("getValue returns a null value for label key "
                + LABEL_KEY, lValue);
        assertEquals("Translation for " + LABEL_KEY
                + " is incorrect. Expected : " + LABEL_TRANSLATION
                + " - found : " + lValue, LABEL_TRANSLATION, lValue);
    }

    /**
     * Tests the method setValue. Try to change the value of a label.
     */
    public void testNormalCaseExistingLabel() {
        i18nService = serviceLocator.getI18nService();

        // First, we test that the value exists
        String lValue =
                i18nService.getValue(EXISTING_LABEL_KEY, DEFAULT_LANGUAGE);
        assertNotNull("getValue returns a null value for label key "
                + EXISTING_LABEL_KEY, lValue);
        assertNotSame("The label " + EXISTING_LABEL_KEY + " doesn't exists.",
                EXISTING_LABEL_KEY, lValue);

        // The test
        i18nService.setValue(EXISTING_LABEL_KEY, DEFAULT_LANGUAGE,
                LABEL_TRANSLATION);

        // Has the value been set ?
        lValue = i18nService.getValue(EXISTING_LABEL_KEY, DEFAULT_LANGUAGE);
        assertNotNull("getValue returns a null value for label key "
                + EXISTING_LABEL_KEY, lValue);
        assertEquals("Translation for " + EXISTING_LABEL_KEY
                + " is incorrect. Expected : " + LABEL_TRANSLATION
                + " - found : " + lValue, LABEL_TRANSLATION, lValue);
    }

    /**
     * Tests an error case : the label is null.
     */
    public void testErrorCaseNullLabel() {
        i18nService = serviceLocator.getI18nService();

        try {
            i18nService.setValue(null, DEFAULT_LANGUAGE, LABEL_TRANSLATION);
            fail("setValue should throw an InvalidNameException if label key is null.");
        }
        catch (IllegalArgumentException e) {
            // Ok
        }
    }

    /**
     * Tests an error case : the translation is null.
     */
    public void testErrorCaseNullTranslation() {
        i18nService = serviceLocator.getI18nService();

        try {
            i18nService.setValue(LABEL_KEY, DEFAULT_LANGUAGE, null);
            fail("setValue should throw an IllegalArgumentException if the translation is null.");
        }
        catch (IllegalArgumentException e) {
            // Ok
        }
    }
}
