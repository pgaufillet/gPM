/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.filter.FilterFacade;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterScope;

/**
 * TestGetCategoryValuesFacade
 * 
 * @author jlouisy
 */
public class TestGetCategoryValuesFacade extends AbstractFacadeTestCase {

    private static final String CATEGORY = "CHOICE_CAT_1";

    private static final String[] CATEGORY_VALUES =
            { "$NOT_SPECIFIED", "CHOICE 1", "CHOICE 2", "CHOICE 3", "CHOICE 4",
             "CHOICE 5", "CHOICE 6" };

    /**
     * Normal Case
     */
    public void testNormalCase() {

        FilterFacade lFilterFacade = getFacadeLocator().getFilterFacade();
        UiSession lUiSession =
                getAdminUserSession().getSession(getProductName());

        List<UiFilterScope> lScopes = new ArrayList<UiFilterScope>();
        lScopes.add(new UiFilterScope(new Translation("ROOT_PRODUCT",
                "ROOT_PRODUCT"), false));
        lScopes.add(new UiFilterScope(new Translation("EMPTY_CHILD_PRODUCT_1",
                "EMPTY_CHILD_PRODUCT_1"), false));

        List<String> lResult =
                lFilterFacade.getCategoryValues(lUiSession, CATEGORY, lScopes);

        assertEquals(CATEGORY_VALUES.length, lResult.size());
        assertTrue(lResult.containsAll(Arrays.asList(CATEGORY_VALUES)));
    }
}
