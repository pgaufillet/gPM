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

import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.product.ProductFacade;

/**
 * ProductFacade.getProduct test
 * 
 * @author jlouisy
 */
public class TestDeleteProductFacade extends AbstractFacadeTestCase {

    private static final String EMPTY_ROOT_PRODUCT = "EMPTY_ROOT_PRODUCT";

    private static final String EMPTY_CHILD_PRODUCT_1 = "EMPTY_CHILD_PRODUCT_1";

    private static final String EMPTY_CHILD_PRODUCT_2 = "EMPTY_CHILD_PRODUCT_2";

    /**
     * Confidential product case
     */
    public void testConfidentialProduct() {
        // login as USER
        UiSession lUiSession = loginAsUser().getSession(DEFAULT_PRODUCT_NAME);
        ProductFacade lProductFacade = getFacadeLocator().getProductFacade();

        // Get product Id
        ProductService lProductService = getProductService();
        String lProductId =
                lProductService.getProductId(lUiSession.getRoleToken(),
                        EMPTY_CHILD_PRODUCT_2);

        // Deletion
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Delete Confidential product.");
        }
        try {
            lProductFacade.deleteProduct(lUiSession, lProductId, false);
            fail("Exception should be thrown.");
        }
        catch (AuthorizationException lException) {
            String lMessage =
                    "The type of the product 'EMPTY_CHILD_PRODUCT_2' can not be deleted.";
            assertEquals("Bad exception.", lMessage, lException.getMessage());

            //logout USER
            logoutAsUser(lUiSession.getParent());
        }
    }

    /**
     * Delete root product
     */
    public void testDeleteProduct() {

        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        ProductFacade lProductFacade = getFacadeLocator().getProductFacade();

        // Get product Id
        ProductService lProductService = getProductService();
        String lProductId =
                lProductService.getProductId(lUiSession.getRoleToken(),
                        EMPTY_CHILD_PRODUCT_2);

        // Deletion
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Delete Product.");
        }
        lProductFacade.deleteProduct(lUiSession, lProductId, false);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.debug("Try to get deleted product.");
        }
        assertFalse(lProductService.isProductExists(lUiSession.getRoleToken(),
                EMPTY_CHILD_PRODUCT_2));
    }

    /**
     * Delete root product and sub products
     */
    public void testDeleteProductAndSubProducts() {

        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        ProductFacade lProductFacade = getFacadeLocator().getProductFacade();

        // Get product Id
        ProductService lProductService = getProductService();
        String lProductId =
                lProductService.getProductId(lUiSession.getRoleToken(),
                        EMPTY_ROOT_PRODUCT);

        // Deletion
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Delete Product.");
        }
        lProductFacade.deleteProduct(lUiSession, lProductId, true);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.debug("Try to get deleted product.");
        }
        assertFalse(lProductService.isProductExists(lUiSession.getRoleToken(),
                EMPTY_ROOT_PRODUCT));
        assertFalse(lProductService.isProductExists(lUiSession.getRoleToken(),
                EMPTY_CHILD_PRODUCT_1));
        assertFalse(lProductService.isProductExists(lUiSession.getRoleToken(),
                EMPTY_CHILD_PRODUCT_2));
    }

}
