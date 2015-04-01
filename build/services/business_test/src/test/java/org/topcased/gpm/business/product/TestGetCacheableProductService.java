/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thibault Landre (Atos)
 ******************************************************************/
package org.topcased.gpm.business.product;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Test the method org.topcased.gpm.business.product.service.ProductService.
 * getCacheableProduct(String, String, CacheProperties)
 */
public class TestGetCacheableProductService extends
        AbstractBusinessServiceTestCase {

    /** The Product Service. */
    private ProductService productService;

    /**
     * 
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

        // Check that the cacheable product is the correct product. 
        assertEquals(GpmTestValues.PRODUCT_BERNARD_STORE_NAME,
                lCacheableProduct.getProductName());

        // Check that the cacheable product is immutable. 
        lCacheableProduct.setProductName("toto");
    }

    public void testWrongIdCase() {
        // Get a cacheable product with this id.
        productService.getCacheableProduct(adminRoleToken, "wrongId",
                CacheProperties.IMMUTABLE);
    }

}
