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
import java.util.List;

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.AuthorizationFacade;
import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;
import org.topcased.gpm.ui.facade.shared.container.product.UiProductHierarchy;

/**
 * TestGetAvailableProductsHierarchyFacade
 * 
 * @author jlouisy
 */
public class TestGetAvailableProductsHierarchyFacade extends
        AbstractFacadeTestCase {

    private void assertEqualsProductHierarchy(UiProductHierarchy pExpected,
            UiProductHierarchy pActual) {
        assertEquals(pExpected.getProductName(), pActual.getProductName());
        assertEquals(pExpected.isSelectable(), pActual.isSelectable());
        assertEquals(pExpected.getChildren().size(),
                pActual.getChildren().size());
        for (int i = 0; i < pExpected.getChildren().size(); i++) {
            assertEqualsProductHierarchy(pExpected.getChildren().get(i),
                    pActual.getChildren().get(i));
        }
    }

    /**
     * normal case
     */
    //  Hierarchy is :
    //    |- EMPTY_ROOT_PRODUCT (selectable)
    //        |- EMPTY_CHILD_PRODUCT_1 (selectable)
    //            |- EMPTY_CHILD_PRODUCT_2 (selectable)
    //    |- ROOT_PRODUCT (selectable)
    //        |- CHILD_PRODUCT_1 (selectable)
    //        |- CHILD_PRODUCT_2 (selectable)
    //        |- EMPTY_CHILD_PRODUCT_1 (selectable)
    //            |- EMPTY_CHILD_PRODUCT_2 (selectable)
    public void testNormalCase() {
        UiUserSession lUiUserSession = getAdminUserSession();
        AuthorizationFacade lAuthorizationFacade =
                getFacadeLocator().getAuthorizationFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get products hierarchy.");
        }
        startTimer("testRestrictedRightsCase");
        List<UiProductHierarchy> lProducts =
                lAuthorizationFacade.getAvailableProductsHierarchy(lUiUserSession);
        stopTimer();

        //Make hierarchy
        UiProductHierarchy lEmptyRootProduct =
                new UiProductHierarchy("EMPTY_ROOT_PRODUCT", null,
                        new ArrayList<UiProductHierarchy>(), true);
        UiProductHierarchy lEmptyChildProduct1 =
                new UiProductHierarchy("EMPTY_CHILD_PRODUCT_1", null,
                        new ArrayList<UiProductHierarchy>(), true);
        UiProductHierarchy lEmptyChildProduct2 =
                new UiProductHierarchy("EMPTY_CHILD_PRODUCT_2", null,
                        new ArrayList<UiProductHierarchy>(), true);
        UiProductHierarchy lRootProduct =
                new UiProductHierarchy("ROOT_PRODUCT", null,
                        new ArrayList<UiProductHierarchy>(), true);
        UiProductHierarchy lChildProduct1 =
                new UiProductHierarchy("CHILD_PRODUCT_1", null,
                        new ArrayList<UiProductHierarchy>(), true);
        UiProductHierarchy lChildProduct2 =
                new UiProductHierarchy("CHILD_PRODUCT_2", null,
                        new ArrayList<UiProductHierarchy>(), true);

        lEmptyRootProduct.getChildren().add(lEmptyChildProduct1);
        lEmptyChildProduct1.getChildren().add(lEmptyChildProduct2);

        lRootProduct.getChildren().add(lChildProduct1);
        lRootProduct.getChildren().add(lChildProduct2);
        lRootProduct.getChildren().add(lEmptyChildProduct1);

        assertEquals("Wrong product name.", "EMPTY_ROOT_PRODUCT",
                lProducts.get(0).getProductName());
        assertEqualsProductHierarchy(lEmptyRootProduct, lProducts.get(0));
        assertEquals("Wrong product name.", "ROOT_PRODUCT",
                lProducts.get(1).getProductName());
        assertEqualsProductHierarchy(lRootProduct, lProducts.get(1));
    }

    /**
     * restricted Rights case
     */
    //  Hierarchy is :
    //    |- EMPTY_ROOT_PRODUCT (not selectable)        - DISPLAYED
    //        |- EMPTY_CHILD_PRODUCT_1 (not selectable) - DISPLAYED
    //            |- EMPTY_CHILD_PRODUCT_2 (selectable) - DISPLAYED
    //    |- ROOT_PRODUCT (selectable)                  - DISPLAYED
    //        |- CHILD_PRODUCT_1 (not selectable)       - NOT DISPLAYED
    //        |- CHILD_PRODUCT_2 (not selectable)       - NOT DISPLAYED
    //        |- EMPTY_CHILD_PRODUCT_1 (not selectable) - DISPLAYED
    //            |- EMPTY_CHILD_PRODUCT_2 (selectable) - DISPLAYED
    public void testRestrictedRightsCase() {
        //login as USER
        UiUserSession lUiUserSession = loginAsUser();
        AuthorizationFacade lAuthorizationFacade =
                getFacadeLocator().getAuthorizationFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get products hierarchy.");
        }
        startTimer();
        List<UiProductHierarchy> lProducts =
                lAuthorizationFacade.getAvailableProductsHierarchy(lUiUserSession);
        stopTimer();

        //Make hierarchy
        UiProductHierarchy lEmptyRootProduct =
                new UiProductHierarchy("EMPTY_ROOT_PRODUCT", null,
                        new ArrayList<UiProductHierarchy>(), false);
        UiProductHierarchy lEmptyChildProduct1 =
                new UiProductHierarchy("EMPTY_CHILD_PRODUCT_1", null,
                        new ArrayList<UiProductHierarchy>(), false);
        UiProductHierarchy lEmptyChildProduct2 =
                new UiProductHierarchy("EMPTY_CHILD_PRODUCT_2", null,
                        new ArrayList<UiProductHierarchy>(), true);
        UiProductHierarchy lRootProduct =
                new UiProductHierarchy("ROOT_PRODUCT", null,
                        new ArrayList<UiProductHierarchy>(), true);

        lEmptyRootProduct.getChildren().add(lEmptyChildProduct1);
        lEmptyChildProduct1.getChildren().add(lEmptyChildProduct2);
        lRootProduct.getChildren().add(lEmptyChildProduct1);

        assertEquals("EMPTY_ROOT_PRODUCT", lProducts.get(0).getProductName());
        assertEqualsProductHierarchy(lEmptyRootProduct, lProducts.get(0));
        assertEquals("ROOT_PRODUCT", lProducts.get(1).getProductName());
        assertEqualsProductHierarchy(lRootProduct, lProducts.get(1));

        assertEquals("EMPTY_CHILD_PRODUCT_1",
                lProducts.get(0).getChildren().get(0).getProductName());
        assertEqualsProductHierarchy(lEmptyChildProduct1,
                lProducts.get(0).getChildren().get(0));

        assertEquals(
                "EMPTY_CHILD_PRODUCT_2",
                lProducts.get(0).getChildren().get(0).getChildren().get(0).getProductName());
        assertEqualsProductHierarchy(lEmptyChildProduct2,
                lProducts.get(0).getChildren().get(0).getChildren().get(0));

        //logout USER
        logoutAsUser(lUiUserSession);
    }

}
