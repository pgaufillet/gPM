/***************************************************************
 * Copyright (c) 2007-2012 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin), Sophian Idjellidaine (Atos Origin), Thibault Landre (Atos)
 ******************************************************************/
package org.topcased.gpm.business.product;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method
 * <CODE>org.topcased.gpm.business.product.impl.ProductServiceImpl.getProductId(String, String)<CODE> of the Product Service.
 * 
 * @see org.topcased.gpm.business.product.impl.ProductServiceImpl.getProductId(
 *      String, String)
 */
public class TestGetProductIdService extends AbstractBusinessServiceTestCase {

    /** The Product Service. */
    private ProductService productService;

    /** A non existing product name */
    private static final String NON_EXISTING_PRODUCT_NAME =
            "Product that does not exist";

    /**
     * Tests the getProductId method.
     */
    public void testNormalCase() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Get the product id
        String lProductId =
                productService.getProductId(adminRoleToken,
                        GpmTestValues.PRODUCT_BERNARD_STORE_NAME);

        assertNotNull("Product id is null", lProductId);
        // Get a cacheable product with this id.
        CacheableProduct lCacheableProduct =
                productService.getCacheableProduct(adminRoleToken, lProductId,
                        CacheProperties.IMMUTABLE);

        // Check that the cacheable product is not null.
        assertNotNull(lCacheableProduct);
        // Check that the product retrieves is the correct one.
        assertEquals(GpmTestValues.PRODUCT_BERNARD_STORE_NAME,
                lCacheableProduct.getProductName());
    }

    /**
     * Test the case where the name of the product does not exist in the
     * database.
     */
    public void testNullCase() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Get the product id
        String lProductId =
                productService.getProductId(adminRoleToken,
                        NON_EXISTING_PRODUCT_NAME);

        // The result should be null.
        assertNull("Product id is not null whereas it should be", lProductId);
    }
}