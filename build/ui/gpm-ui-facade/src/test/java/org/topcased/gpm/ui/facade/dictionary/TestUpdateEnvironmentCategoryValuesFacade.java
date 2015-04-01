/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.dictionary;

import java.util.Arrays;
import java.util.List;

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;

/**
 * TestGetEnvironmentCategoryValuesFacade
 * 
 * @author jlouisy
 */
public class TestUpdateEnvironmentCategoryValuesFacade extends
        AbstractFacadeTestCase {

    private static final String[] CATEGORY_VALUES =
            { "CHOICE 1", "NEW CHOICE 2", "CHOICE 3" };

    private static final String[] EXPECTED_CATEGORY_VALUES =
            { "CHOICE 1", "CHOICE 3" };

    private static final String CATEGORY_NAME = "CHOICE_CAT_1";

    private static final String ENVIRONMENT_NAME = "default";

    private void areEquals(String[] pExpected, String[] pActual) {
        assertEquals(pExpected.length, pActual.length);
        for (int i = 0; i < pExpected.length; i++) {
            assertEquals(pExpected[i], pActual[i]);
        }
    }

    /**
     * Normal case
     */
    public void testNormalCase() {

        UiSession lSession = adminUserSession.getDefaultGlobalSession();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Update environment category values.");
        }
        getFacadeLocator().getDictionaryFacade().updateEnvironmentCategoryValues(
                lSession, ENVIRONMENT_NAME, CATEGORY_NAME,
                Arrays.asList(CATEGORY_VALUES));

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get environment category values.");
        }
        List<String> lCategoryValues =
                getFacadeLocator().getDictionaryFacade().getEnvironmentCategoryValues(
                        lSession, ENVIRONMENT_NAME, CATEGORY_NAME);

        areEquals(EXPECTED_CATEGORY_VALUES,
                lCategoryValues.toArray(new String[0]));
    }
}
