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

/**
 * Tests the method <CODE>setProductEnvironment<CODE> of the Product Service.
 * 
 * @author nsamson
 */
public class TestSetProductEnvironmentService extends
        AbstractBusinessServiceTestCase {

    /**
     * Tests the setProductEnvironment method.
     */
    public void testNormalCase() {
        // Gets the product service.
        //        productService = serviceLocator.getProductService();
        //
        //        // Retrieving the product
        //
        //        ProductData lProductData = productService.getProduct(adminRoleToken,
        //                getProcessName(), PRODUCT_NAME);
        //
        //        assertNotNull("Product with name " + PRODUCT_NAME + " is not in DB.",
        //                lProductData);
        //
        //        // Setting the product environment
        //        productService.setProductEnvironment(adminRoleToken, lProductData.getId(), new String[]{ENV_NAME});
        //
        //        // Verify
        //
        //        lProductData = productService.getProduct(adminRoleToken, getProcessName(),
        //                PRODUCT_NAME);
        //
        //        assertNotNull("Product with name " + PRODUCT_NAME + " is not in DB.",
        //                lProductData);
        //        assertNotNull(lProductData.getEnvironmentNames());
        //        assertTrue(lProductData.getEnvironmentNames().length == 1);
        //        assertEquals("The environement " + ENV_NAME
        //                + " has not been set to the product " + PRODUCT_NAME + ".",
        //                ENV_NAME, lProductData.getEnvironmentNames()[0]);
        //
    }
}