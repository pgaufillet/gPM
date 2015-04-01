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
 * Tests the getValueForUser method.
 * 
 * @author tszadel
 */
public class TestGetValueForUserService extends AbstractBusinessServiceTestCase {
    /** The tested label key. */
    private static final String LABEL_KEY = "CAT_birthdate";

    /** The expected translation for the label key. */
    private static final String EXPECTED_TRANSLATION = "Birth Date";

    /** The unknown label key. */
    private static final String UNKNOWN_LABEL_KEY = "@@UnitTest_Error@@";

    /** The i18n Service. */
    private I18nService i18nService;

    /**
     * Tests the method getValueForRole.
     */
    public void testNormalCase() {
        i18nService = serviceLocator.getI18nService();

        String lValue = i18nService.getValueForUser(adminUserToken, LABEL_KEY);
        assertNotNull("getValueForUser returns a null value for label key "
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
                i18nService.getValueForUser(adminUserToken, UNKNOWN_LABEL_KEY);
        assertNotNull("getValueForUser returns a null value for label key "
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
            i18nService.getValueForUser(adminUserToken, null);
            fail("getValueForUser should throw an InvalidNameException if labelKey is null");
        }
        catch (IllegalArgumentException e) {
            // Ok
        }
    }

    /**
     * Tests an error case : the role is null.
     */
    public void testErrorCaseNullRole() {
        i18nService = serviceLocator.getI18nService();

        try {
            i18nService.getValueForUser(null, LABEL_KEY);
            fail("getValueForUser should throw an InvalidTokenException if user token is null.");
        }
        catch (InvalidTokenException e) {
            // Ok
        }
    }
}
