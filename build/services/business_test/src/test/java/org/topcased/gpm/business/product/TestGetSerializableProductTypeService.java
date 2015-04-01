/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.product;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.product.service.ProductTypeData;
import org.topcased.gpm.business.serialization.data.ProductType;

/**
 * TestGetSerializableProductTypeService
 * 
 * @author mfranche
 */
public class TestGetSerializableProductTypeService extends
        AbstractBusinessServiceTestCase {

    /** The Product Service. */
    private ProductService productService;

    /** The name of the product type used. */
    private static final String PRODUCT_TYPE = "Store";

    /**
     * Tests the method in normal conditions.
     */
    public void testNormalCase() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Retrieving the product type
        ProductTypeData lProductTypeData =
                productService.getProductTypeByName(adminRoleToken,
                        getProcessName(), PRODUCT_TYPE);

        assertNotNull("Product type for the process " + getProcessName()
                + " is not in DB.", lProductTypeData);

        // Retrieving the serializable product type
        ProductType lProductType =
                productService.getSerializableProductType(adminRoleToken,
                        lProductTypeData.getId());

        assertNotNull("Product type does not exist in DB", lProductType);

        assertEquals("getProductTypeByProductKey returns product type #"
                + lProductType.getName() + " instead of a product type #"
                + PRODUCT_TYPE, PRODUCT_TYPE, lProductType.getName());
    }
}
