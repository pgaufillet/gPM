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
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.product.service.ProductTypeData;

/**
 * Tests the method <CODE>getProductTypeByName</CODE> of the Product Service.
 * 
 * @author nsamson
 */
public class TestGetProductTypeByNameService extends
        AbstractBusinessServiceTestCase {

    /** The Product Service. */
    private ProductService productService;

    /** The Product Type. */
    private static final String PRODUCT_TYPE = "Store";

    /** The Product Type description. */
    private static final String PRODUCT_TYPE_DESC = "Product type";

    /**
     * Tests the getProductType method.
     */
    public void testNormalCase() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Retrieving the product type for the process
        ProductTypeData lProductTypeData =
                productService.getProductTypeByName(adminRoleToken,
                        getProcessName(), PRODUCT_TYPE);

        assertNotNull("Product type for the process " + getProcessName()
                + " is not in DB.", lProductTypeData);

        assertEquals("Product type Name does not match with " + PRODUCT_TYPE,
                PRODUCT_TYPE, lProductTypeData.getName());

        assertEquals("Product type Description does not match with "
                + PRODUCT_TYPE_DESC, PRODUCT_TYPE_DESC,
                lProductTypeData.getDescription());
    }

    /**
     * Tests the getProductTypeByName method with a invalid token.
     */
    public void testInvalidTokenCase() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Uses a empty token
        try {
            productService.getProductTypeByName("", getProcessName(),
                    getProductTypeName());
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