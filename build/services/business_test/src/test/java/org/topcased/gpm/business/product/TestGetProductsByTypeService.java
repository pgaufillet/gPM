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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>getProductsByType<CODE> of the Product Service.
 * 
 * @author sidjelli, nveillet
 */
public class TestGetProductsByTypeService extends
        AbstractBusinessServiceTestCase {

    /** The Id of the first product used. */
    private static final String PRODUCT_TYPE = "Store";

    /** The product references. */
    private static final String[] PRODUCT_REFERENCES =
            { GpmTestValues.PRODUCT_BERNARD_STORE_NAME,
             GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME };

    /** The Product Service. */
    private ProductService productService;

    /**
     * Tests the getProductsByType method with the product type id.
     */
    public void testNormalCase() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Retrieving the product type
        CacheableProductType lProductType =
                productService.getCacheableProductTypeByName(adminRoleToken,
                        getProcessName(), PRODUCT_TYPE,
                        CacheProperties.IMMUTABLE);

        assertNotNull("Product type for the process " + getProcessName()
                + " is not in DB.", lProductType);

        // Retrieving the products
        List<CacheableProduct> lProducts =
                productService.getCacheableProductsByType(adminRoleToken,
                        lProductType.getId(), CacheProperties.IMMUTABLE);

        assertNotNull(
                "getProductsByType returns null instead of a list of id of products",
                lProducts);

        int lSize = lProducts.size();
        int lExpectedSize = PRODUCT_REFERENCES.length;
        assertTrue("The method getProductNames returns less than "
                + lExpectedSize + " results.", lExpectedSize <= lSize);

        Collection<String> lProductNames = new ArrayList<String>();

        for (CacheableProduct lProduct : lProducts) {
            lProductNames.add(lProduct.getProductName());
        }
        assertTrue("Some products are missing",
                lProductNames.containsAll(Arrays.asList(PRODUCT_REFERENCES)));
    }

    /**
     * Tests the getProductsByType (with the product type id) method with a
     * invalid token.
     */
    public void testInvalidTokenCase() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Uses a empty token.
        try {
            productService.getCacheableProductsByType("", "idProductType",
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