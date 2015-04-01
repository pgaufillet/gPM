/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.product;

import static org.topcased.gpm.business.GpmTestValues.PRODUCT1_NAME;
import static org.topcased.gpm.business.GpmTestValues.PRODUCT1_SUBPRODUCTS;

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestGetProductsService
 * 
 * @author mfranche, nveillet
 */
public class TestGetProductsService extends AbstractBusinessServiceTestCase {

    /**
     * Tests the method in a normal case.
     */
    public void testNormalCase() {
        ProductService lProductService = serviceLocator.getProductService();

        CacheableProduct lProduct =
                lProductService.getCacheableProduct(adminRoleToken,
                        lProductService.getProductId(adminRoleToken,
                                PRODUCT1_NAME), CacheProperties.IMMUTABLE);

        // Get all sub-products
        List<CacheableProduct> lSubproducts =
                lProductService.getCacheableProducts(getProcessName(),
                        lProduct, CacheProperties.IMMUTABLE);

        String[] lProductNames = new String[lSubproducts.size()];
        int i = 0;
        for (CacheableProduct lSubProduct : lSubproducts) {
            lProductNames[i++] = lSubProduct.getProductName();
        }

        assertEqualsOrdered(PRODUCT1_SUBPRODUCTS, lProductNames);
    }

    /** Tests the method in a normal case. */
    public void testNormalCaseOld() {
        //        ProductService lProductService = serviceLocator.getProductService();
        //        ProductData lProductData =
        //                lProductService.getProduct(adminRoleToken, getProcessName(),
        //                        PRODUCT1_NAME);
        //
        //        // Get all sub-products
        //        List<ProductData> lSubproductsDataList =
        //                lProductService.getProducts(getProcessName(), lProductData);
        //
        //        String[] lProductNameList = new String[lSubproductsDataList.size()];
        //        int i = 0;
        //        for (ProductData lPData : lSubproductsDataList) {
        //            lProductNameList[i++] = lPData.getName();
        //        }
        //
        //        assertEqualsOrdered(PRODUCT1_SUBPRODUCTS, lProductNameList);
    }
}
