/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.product;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.product.service.ProductService;

/**
 * TestDeleteProductService
 * 
 * @author nveillet
 */
public class TestDeleteProductService extends AbstractBusinessServiceTestCase {

    /** The Product Service. */
    private ProductService productService;

    private static final String PRODUCT_NAME = GpmTestValues.PRODUCT_PRODUCT3;

    private static final String PRODUCT_NAME_WITH_SHEETS =
            GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME;

    private static final String PRODUCT_NAME_WITH_CHILDREN =
            GpmTestValues.PRODUCT_ENVIRONMENT_TEST_STORE;

    private static final String PRODUCT_NAME_HIERARCHY =
            GpmTestValues.PRODUCT_ENVIRONMENT_TEST_STORE;

    /**
     * Delete a product without sheets and sub-products
     */
    public void testNormalCase() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Get Product ID
        String lProductId =
                productService.getProductId(normalRoleToken, PRODUCT_NAME);

        // Delete product.
        String lErrorMessage = null;
        try {
            productService.deleteProduct(normalRoleToken, lProductId, false,
                    null);
        }
        catch (AuthorizationException e) {
            lErrorMessage = e.getLocalizedMessage();
        }

        assertNull(lErrorMessage, lErrorMessage);

    }

    /**
     * Delete a product with sheets
     */
    public void testWithSheets() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Get Product ID
        String lProductId =
                productService.getProductId(normalRoleToken,
                        PRODUCT_NAME_WITH_SHEETS);

        // Delete product.
        String lErrorMessage = null;
        try {
            productService.deleteProduct(normalRoleToken, lProductId, false,
                    null);
        }
        catch (AuthorizationException e) {
            lErrorMessage = e.getLocalizedMessage();
        }

        assertNotNull(lErrorMessage);
    }

    /**
     * Delete a product with sub-product
     */
    public void testWithChildren() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Get Product ID
        String lProductId =
                productService.getProductId(normalRoleToken,
                        PRODUCT_NAME_WITH_CHILDREN);

        // Delete product.
        String lErrorMessage = null;
        try {
            productService.deleteProduct(normalRoleToken, lProductId, false,
                    null);
        }
        catch (AuthorizationException e) {
            lErrorMessage = e.getLocalizedMessage();
        }

        assertNotNull(lErrorMessage);
    }

    /**
     * Delete a product hierarchy without sheets
     */
    public void testHierarchyNormalCase() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Get Product ID
        String lProductId =
                productService.getProductId(normalRoleToken,
                        PRODUCT_NAME_HIERARCHY);

        // Delete product.
        String lErrorMessage = null;
        try {
            productService.deleteProduct(normalRoleToken, lProductId, true,
                    null);
        }
        catch (AuthorizationException e) {
            lErrorMessage = e.getLocalizedMessage();
        }

        assertNull(lErrorMessage, lErrorMessage);
    }

    /**
     * Delete a product hierarchy when a sub-product contains sheets
     */
    public void testHierarchyWithSheets() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Get Product ID
        String lProductId =
                productService.getProductId(normalRoleToken,
                        GpmTestValues.PRODUCT_BERNARD_STORE_NAME);

        // Delete product.
        String lErrorMessage = null;
        try {
            productService.deleteProduct(normalRoleToken, lProductId, true,
                    null);
        }
        catch (AuthorizationException e) {
            lErrorMessage = e.getLocalizedMessage();
        }
        assertNotNull(lErrorMessage);
    }
}
