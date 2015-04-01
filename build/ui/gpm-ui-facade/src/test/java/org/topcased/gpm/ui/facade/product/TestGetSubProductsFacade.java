/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.product;

import java.util.Arrays;
import java.util.List;

import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.product.ProductFacade;
import org.topcased.gpm.ui.facade.shared.container.product.UiProduct;

/**
 * ProductFacade.getProduct test
 * 
 * @author jlouisy
 */
public class TestGetSubProductsFacade extends AbstractFacadeTestCase {

    private static final String PRODUCT_NAME = "ROOT_PRODUCT";

    private static final String CHILD_1_PRODUCT_NAME = "CHILD_PRODUCT_1";

    private static final String CHILD_2_PRODUCT_NAME = "CHILD_PRODUCT_2";

    private static final String CHILD_3_PRODUCT_NAME = "EMPTY_CHILD_PRODUCT_1";

    private static final String[] EXPECTED_SUBPRODUCTS =
            new String[] { CHILD_1_PRODUCT_NAME, CHILD_2_PRODUCT_NAME,
                          CHILD_3_PRODUCT_NAME };

    /**
     * Confidential product case
     */
    public void testConfidentialProduct() {
        //login as USER
        UiSession lUiSession = loginAsUser().getSession(DEFAULT_PRODUCT_NAME);
        ProductFacade lProductFacade = getFacadeLocator().getProductFacade();

        // Get product Id
        ProductService lProductService = getProductService();
        String lProductId =
                lProductService.getProductId(lUiSession.getRoleToken(),
                        PRODUCT_NAME);
        String lChildProductId =
                lProductService.getProductId(lUiSession.getRoleToken(),
                        CHILD_1_PRODUCT_NAME);

        // Count sheets
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get subproducts.");
        }
        try {
            lProductFacade.getSubProducts(lUiSession, lProductId,
                    DisplayMode.EDITION);
            fail("Exception should be thrown.");
        }
        catch (AuthorizationException lException) {
            String lMessage =
                    "Illegal access to the product " + lChildProductId
                            + " : the access is confidential ";
            assertEquals("Bad exception.", lMessage, lException.getMessage());

            //logout USER
            logoutAsUser(lUiSession.getParent());
        }
    }

    /**
     * Normal case
     */
    public void testNormalCase() {

        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        ProductFacade lProductFacade = getFacadeLocator().getProductFacade();

        // Get product Id
        ProductService lProductService = getProductService();
        String lProductId =
                lProductService.getProductId(lUiSession.getRoleToken(),
                        PRODUCT_NAME);

        // Count sheets
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get subproducts.");
        }
        List<UiProduct> lSubProductsList =
                lProductFacade.getSubProducts(lUiSession, lProductId,
                        DisplayMode.EDITION);

        assertEquals(3, lSubProductsList.size());

        String[] lActual = new String[3];
        for (int i = 0; i < 3; i++) {
            lActual[i] = lSubProductsList.get(i).getName();
        }
        assertEquals(Arrays.asList(EXPECTED_SUBPRODUCTS),
                Arrays.asList(lActual));
    }
}
