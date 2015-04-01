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

import org.topcased.gpm.business.exception.UndeletableValuesException;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;

/**
 * TestUpdatetDictionaryCategoryValuesFacade
 * 
 * @author jlouisy
 */
public class TestUpdatetDictionaryCategoryValuesFacade extends
        AbstractFacadeTestCase {

    private static final String[] CATEGORY_VALUES =
            { "NEW CHOICE 1", "NEW CHOICE 2", "NEW CHOICE 3" };

    private static final String CATEGORY_NAME = "CAT_2";

    private static final String CATEGORY_USED_VALUES_NAME = "CHOICE_CAT_1";

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
            LOGGER.info("Update dictionary category values.");
        }
        getFacadeLocator().getDictionaryFacade().updateDictionaryCategoryValues(
                lSession, CATEGORY_NAME, Arrays.asList(CATEGORY_VALUES));

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get dictionary category values.");
        }
        List<String> lCategoryValues =
                getFacadeLocator().getDictionaryFacade().getDictionaryCategoryValues(
                        lSession, CATEGORY_NAME);

        areEquals(CATEGORY_VALUES, lCategoryValues.toArray(new String[0]));
    }

    /**
     * Used values case
     */
    public void testUsedValueCase() {

        UiSession lSession = adminUserSession.getDefaultGlobalSession();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Update dictionary category values.");
        }
        try {
            getFacadeLocator().getDictionaryFacade().updateDictionaryCategoryValues(
                    lSession, CATEGORY_USED_VALUES_NAME,
                    Arrays.asList(CATEGORY_VALUES));
        }
        catch (UndeletableValuesException e) {
            assertEquals(
                    "Category values cannot be deleted because used in container. [CHOICE 5]",
                    e.getMessage());
            return;
        }
        fail("Exception should be thrown.");
    }
}
