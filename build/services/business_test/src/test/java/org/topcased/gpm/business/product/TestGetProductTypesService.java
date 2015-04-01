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

import java.util.Arrays;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>getProductTypes</CODE> of the Product Service.
 * 
 * @author sidjelli, nveillet
 */
public class TestGetProductTypesService extends AbstractBusinessServiceTestCase {

    /** The Product Service. */
    private ProductService productService;

    /** The products type names. */
    private static final String[] PRODUCT_TYPE_NAMES = { "Store" };

    /**
     * Tests the getProductTypes method.
     */
    public void testNormalCase() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Retrieving the Product types
        List<CacheableProductType> lProductTypes =
                productService.getProductTypes(adminRoleToken,
                        getProcessName(), CacheProperties.IMMUTABLE);

        assertNotNull("getProductTypes with returns null.", lProductTypes);

        int lExpectedSize = PRODUCT_TYPE_NAMES.length;
        int lSize = lProductTypes.size();

        assertEquals("The method getProductTypes returns " + lSize
                + " types instead of " + lExpectedSize + ".", lExpectedSize,
                lSize);

        String[] lReturnedTypes = new String[lSize];
        int i = 0;
        for (CacheableProductType lProductType : lProductTypes) {
            lReturnedTypes[i++] = lProductType.getName();
        }

        assertTrue("The Types returned are not thoses expected.",
                Arrays.asList(lReturnedTypes).containsAll(
                        Arrays.asList(PRODUCT_TYPE_NAMES)));
    }

    /**
     * Tests the getProductTypes method with a invalid token.
     */
    public void testInvalidTokenCase() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Uses a empty token.
        try {
            productService.getProductTypes("", getProcessName(),
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