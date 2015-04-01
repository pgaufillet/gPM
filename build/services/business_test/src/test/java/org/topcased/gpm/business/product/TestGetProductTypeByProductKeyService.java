/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.product;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>getProductTypeByProductKey</CODE> of the Product
 * Service.
 * 
 * @author sidjelli, nveillet
 */
public class TestGetProductTypeByProductKeyService extends
        AbstractBusinessServiceTestCase {

    /** The name of the product type used. */
    private static final String PRODUCT_TYPE = "Store";

    /** The product name. */
    private static final String PRODUCT_NAME =
            GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME;

    /** The Product Service. */
    private ProductService productService;

    /**
     * Tests the getProductTypeByProductKey method.
     */
    public void testNormalCase() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Retrieving the product type
        CacheableProductType lProductTypeExpected =
                productService.getCacheableProductTypeByName(adminRoleToken,
                        getProcessName(), PRODUCT_TYPE,
                        CacheProperties.IMMUTABLE);
        assertNotNull("Product type for the process " + getProcessName()
                + " is not in DB.", lProductTypeExpected);

        // Retrieving the product id
        String lProductId =
                productService.getProductId(adminRoleToken, PRODUCT_NAME);

        // Retrieving the product type
        CacheableProductType lProductType =
                productService.getProductTypeByProductKey(adminRoleToken,
                        lProductId, CacheProperties.IMMUTABLE);
        assertNotNull("Product type associated whith the #" + lProductId
                + " does not exist in DB", lProductType);

        assertEquals("getProductTypeByProductKey returns product type #"
                + lProductType.getName() + " instead of a product type #"
                + PRODUCT_TYPE, PRODUCT_TYPE, lProductType.getName());
    }

    /**
     * Tests the getProductTypeByProductKey method with a invalid token.
     */
    public void testInvalidTokenCase() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Uses a empty token.
        try {
            productService.getProductTypeByProductKey("", "idProductType",
                    CacheProperties.IMMUTABLE);
            fail("The exception has not been thrown.");
        }
        catch (InvalidTokenException e) {
            // ok
        }
        catch (Throwable e) {

            fail("The exception thrown is not a InvalidTokenException.");
        }
    }
}