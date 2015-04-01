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
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.product.service.ProductService;

/**
 * Tests the method <CODE>getProductHierarchy</CODE> of the Product Service.
 * 
 * @author ogehin
 */
public class TestGetProductHierarchy extends AbstractBusinessServiceTestCase {

    /** The Product Service. */
    private ProductService productService;

    /** The product names. */
    private static final String[] PRODUCT_NAMES =
            { GpmTestValues.PRODUCT_BERNARD_STORE_NAME };

    /** The expected result. */
    private static final String[] PRODUCT_HIERARCHY =
            { GpmTestValues.PRODUCT_BERNARD_STORE_NAME,
             GpmTestValues.PRODUCT_STORE1_NAME,
             GpmTestValues.PRODUCT_STORE2_NAME, GpmTestValues.PRODUCT_SUBSTORE,
             GpmTestValues.PRODUCT_STORE1_1_NAME };

    /**
     * Tests the getProductTypes method.
     */
    public void testNormalCase() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Retrieving the Product types
        List<String> lProducts = new ArrayList<String>(PRODUCT_NAMES.length);
        for (String lProductName : PRODUCT_NAMES) {
            lProducts.add(lProductName);
        }

        List<String> lProductHierarchy =
                productService.getProductHierarchy(getProcessName(), lProducts);

        assertNotNull("getProductHierarchy returns null.", lProductHierarchy);

        int lExpectedSize = PRODUCT_HIERARCHY.length;
        int lSize = lProductHierarchy.size();

        assertEquals("The method getProductHierarchy returns " + lSize
                + " types instead of " + lExpectedSize + ".", lExpectedSize,
                lSize);

        assertTrue("The Products returned are not thoses expected.",
                lProductHierarchy.contains(PRODUCT_HIERARCHY[0]));
        assertTrue("The Products returned are not thoses expected.",
                lProductHierarchy.contains(PRODUCT_HIERARCHY[1]));
        assertTrue("The Products returned are not thoses expected.",
                lProductHierarchy.contains(PRODUCT_HIERARCHY[2]));
        assertTrue("The Products returned are not thoses expected.",
                lProductHierarchy.contains(PRODUCT_HIERARCHY[3]));
        assertTrue("The Products returned are not thoses expected.",
                lProductHierarchy.contains(PRODUCT_HIERARCHY[4]));
    }

    /**
     * Tests the getProductTypes method with an invalid product name.
     */
    public void testInvalidProducts() {

        // Gets the product service.
        productService = serviceLocator.getProductService();

        List<String> lProducts = new ArrayList<String>(1);
        lProducts.add("InvalidProduct");

        List<String> lProductHierarchy =
                productService.getProductHierarchy(getProcessName(), lProducts);

        assertNotNull("getProductHierarchy returns null.", lProductHierarchy);

        int lExpectedSize = 0;
        int lSize = lProductHierarchy.size();
        assertEquals("The method getProductHierarchy returns " + lSize
                + " types instead of " + lExpectedSize + ".", lExpectedSize,
                lSize);
    }
}