/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.authorization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.AuthorizationFacade;
import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;
import org.topcased.gpm.ui.facade.shared.container.product.UiProduct;

/**
 * TestGetAvailableProductsFacade
 * 
 * @author jlouisy
 */
public class TestGetAvailableProductsFacade extends AbstractFacadeTestCase {

    private static final String[] USER_SELECTABLE_PRODUCTS =
            new String[] { "EMPTY_CHILD_PRODUCT_2", "ROOT_PRODUCT" };

    private static final String[] ADMIN_SELECTABLE_PRODUCTS =
            new String[] { "CHILD_PRODUCT_1", "CHILD_PRODUCT_2",
                          "EMPTY_CHILD_PRODUCT_1", "EMPTY_CHILD_PRODUCT_2",
                          "EMPTY_ROOT_PRODUCT", "ROOT_PRODUCT" };

    private static final String[] ADMIN_SELECTABLE_PRODUCTS_DESCRIPTIONS =
            new String[] { "", "", "", "",
                          "Empty root product test description",
                          "Root product test description" };

    /**
     * normal case Hierarchy is : |- EMPTY_ROOT_PRODUCT (selectable) |-
     * EMPTY_CHILD_PRODUCT_1 (selectable) |- EMPTY_CHILD_PRODUCT_2 (selectable)
     * |- ROOT_PRODUCT (selectable) |- CHILD_PRODUCT_1 (selectable) |-
     * CHILD_PRODUCT_2 (selectable) |- EMPTY_CHILD_PRODUCT_1 (selectable) |-
     * EMPTY_CHILD_PRODUCT_2 (selectable)
     */
    public void testNormalCase() {
        UiUserSession lUiUserSession = getAdminUserSession();
        AuthorizationFacade lAuthorizationFacade =
                getFacadeLocator().getAuthorizationFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get products names.");
        }
        List<String> lProducts =
                lAuthorizationFacade.getAvailableProducts(lUiUserSession);

        List<String> lExpected = Arrays.asList(ADMIN_SELECTABLE_PRODUCTS);
        assertEquals(lExpected, lProducts);
    }

    /**
     * restricted Rights case Hierarchy is : |- EMPTY_ROOT_PRODUCT (not
     * selectable) |- EMPTY_CHILD_PRODUCT_1 (not selectable) |-
     * EMPTY_CHILD_PRODUCT_2 (selectable) |- ROOT_PRODUCT (selectable) |-
     * CHILD_PRODUCT_1 (not selectable) |- CHILD_PRODUCT_2 (not selectable) |-
     * EMPTY_CHILD_PRODUCT_1 (selectable) |- EMPTY_CHILD_PRODUCT_2 (not
     * selectable)
     */
    public void testRestrictedRightsCase() {
        //login as USER
        UiUserSession lUiUserSession = loginAsUser();
        AuthorizationFacade lAuthorizationFacade =
                getFacadeLocator().getAuthorizationFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get products names.");
        }
        List<String> lProducts =
                lAuthorizationFacade.getAvailableProducts(lUiUserSession);

        List<String> lExpected = Arrays.asList(USER_SELECTABLE_PRODUCTS);
        assertEquals(lExpected, lProducts);

        //logout USER
        logoutAsUser(lUiUserSession);
    }

    /**
     * Same test as testNormalCase but also get and checks products description
     * 
     * @see TestGetAvailableProductsFacade#testNormalCase()
     */
    public void testGetAvailableUiProducts() {
        UiUserSession lUiUserSession = getAdminUserSession();
        AuthorizationFacade lAuthorizationFacade =
                getFacadeLocator().getAuthorizationFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get products names and descriptions.");
        }
        List<UiProduct> lProducts =
                lAuthorizationFacade.getAvailableUiProducts(lUiUserSession);

        // Create expected ShortUiProducts list
        List<ShortUiProduct> lExpected = new ArrayList<ShortUiProduct>();
        for (int i = 0; i < ADMIN_SELECTABLE_PRODUCTS.length; i++) {
            lExpected.add(new ShortUiProduct(ADMIN_SELECTABLE_PRODUCTS[i],
                    ADMIN_SELECTABLE_PRODUCTS_DESCRIPTIONS[i]));
        }
        assertEquals(lExpected, lProducts);
    }

    /**
     * Utility class to handle UiProduct values test
     */
    private class ShortUiProduct extends UiProduct {

        private static final long serialVersionUID = 1L;

        public ShortUiProduct(String pName, String pDescription) {
            setName(pName);
            setDescription(pDescription);
        }

        @Override
        public boolean equals(Object pObj) {
            UiProduct lP = (UiProduct) pObj;
            boolean lEmptyExpected =
                    getDescription() == null || getDescription().length() == 0;
            boolean lEmptyValue =
                    lP.getDescription() == null
                            || lP.getDescription().length() == 0;
            return ((lEmptyExpected && lEmptyValue) || getDescription().equals(
                    lP.getDescription()))
                    && getName().equals(lP.getName());
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }

}
