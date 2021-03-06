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

import java.util.List;

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;

/**
 * TestGetDictionaryCategoryValuesFacade
 * 
 * @author jlouisy
 */
public class TestGetDictionaryCategoryValuesFacade extends
        AbstractFacadeTestCase {

    private static final String[] CATEGORY_VALUES =
            { "CHOICE 1", "CHOICE 2", "CHOICE 3", "CHOICE 4", "CHOICE 5",
             "CHOICE 6", "CHOICE 7", "CHOICE 8", "CHOICE 9", "CHOICE 10" };

    private static final String CATEGORY_NAME = "CHOICE_CAT_1";

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

        // Get environments names
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get dictionary category values.");
        }
        List<String> lCategoryValues =
                getFacadeLocator().getDictionaryFacade().getDictionaryCategoryValues(
                        lSession, CATEGORY_NAME);

        areEquals(CATEGORY_VALUES, lCategoryValues.toArray(new String[0]));
    }
}
