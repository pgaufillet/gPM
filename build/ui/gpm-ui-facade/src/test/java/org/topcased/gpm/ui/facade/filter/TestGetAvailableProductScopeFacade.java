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

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.filter.FilterFacade;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterScope;

/**
 * TestGetAvailableProductScopeFacade
 * 
 * @author jlouisy
 */
public class TestGetAvailableProductScopeFacade extends AbstractFacadeTestCase {

    private static final String[] EXPECTED_PRODUCTS_ADMIN =
            new String[] { "CHILD_PRODUCT_1", "CHILD_PRODUCT_2",
                          "EMPTY_CHILD_PRODUCT_1", "EMPTY_CHILD_PRODUCT_2",
                          "EMPTY_ROOT_PRODUCT", "ROOT_PRODUCT",
                          "$CURRENT_PRODUCT", "$NOT_SPECIFIED" };

    private static final String[] EXPECTED_PRODUCTS_USER =
            new String[] { "EMPTY_CHILD_PRODUCT_2", "ROOT_PRODUCT",
                          "$CURRENT_PRODUCT", "$NOT_SPECIFIED" };

    /**
     * Normal case
     */
    public void testNormalCase() {

        FilterFacade lFilterFacade = getFacadeLocator().getFilterFacade();
        UiSession lUiSession =
                getAdminUserSession().getSession(getProductName());

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Available products.");
        }
        List<UiFilterScope> lResult =
                lFilterFacade.getAvailableProductScope(lUiSession);

        assertEquals(EXPECTED_PRODUCTS_ADMIN.length, lResult.size());
        ArrayList<String> lActualProductNames = new ArrayList<String>();
        for (UiFilterScope lUiFilterScope : lResult) {
            lActualProductNames.add(lUiFilterScope.getProductName().getValue());
        }
        assertTrue(lActualProductNames.containsAll(Arrays.asList(EXPECTED_PRODUCTS_ADMIN)));
    }

    /**
     * User rights case
     */
    public void testUserCase() {

        FilterFacade lFilterFacade = getFacadeLocator().getFilterFacade();
        UiSession lUiSession = loginAsUser().getSession(getProductName());

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Available roducts.");
        }
        List<UiFilterScope> lResult =
                lFilterFacade.getAvailableProductScope(lUiSession);

        assertEquals(EXPECTED_PRODUCTS_USER.length, lResult.size());
        ArrayList<String> lActualProductNames = new ArrayList<String>();
        for (UiFilterScope lUiFilterScope : lResult) {
            lActualProductNames.add(lUiFilterScope.getProductName().getValue());
        }
        assertTrue(lActualProductNames.containsAll(Arrays.asList(EXPECTED_PRODUCTS_USER)));
    }
}
