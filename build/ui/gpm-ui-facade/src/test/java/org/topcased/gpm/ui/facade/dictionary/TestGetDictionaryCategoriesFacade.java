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
import org.topcased.gpm.ui.facade.shared.dictionary.UiCategory;

/**
 * TestGetDictionaryCategoriesFacade
 * 
 * @author jlouisy
 */
public class TestGetDictionaryCategoriesFacade extends AbstractFacadeTestCase {

    private static final String[] CATEGORIES =
            { "CAT_2", "CAT_3", "CAT_4", "CHOICE_CAT_1", "CHOICE_TREE" };

    /**
     * Normal case
     */
    public void testNormalCase() {

        UiSession lSession = adminUserSession.getDefaultGlobalSession();

        // Get environments names
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get dictionary categories.");
        }
        List<UiCategory> lCategories =
                getFacadeLocator().getDictionaryFacade().getDictionaryCategories(
                        lSession);

        assertEquals(CATEGORIES.length, lCategories.size());
        for (int i = 0; i < CATEGORIES.length; i++) {
            assertEquals(CATEGORIES[i], lCategories.get(i).getName());
        }
    }
}
