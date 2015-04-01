/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exportation;

import java.util.HashSet;
import java.util.Set;

import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exportation.ExportProperties.ExportFlag;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.serialization.data.Product;

/**
 * Tests the product export.
 * 
 * @author tpanuel
 */
public class TestProductExport extends
        AbstractValuesContainerTestExport<Product> {
    private final static Set<String> allIds = new HashSet<String>();

    private final static Set<String> byProductIds = new HashSet<String>();

    private final static Set<String> byTypeIds = new HashSet<String>();

    private final static Set<String> limitedProductNames =
            new HashSet<String>();

    private final static Set<String> limitedTypeNames = new HashSet<String>();

    private final static Set<String> idsWithRoleOn = new HashSet<String>();

    private static boolean init = false;

    private ProductService productService;

    /**
     * Create TestProductExport.
     */
    public TestProductExport() {
        super("products", Product.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    @Override
    protected void setUp() {
        super.setUp();
        if (!init) {
            productService = serviceLocator.getProductService();
            // Fill limited product names
            limitedProductNames.add(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME);
            //limitedProductNames.add(GpmTestValues.PRODUCT_STORE1_NAME);
            // Fill limited type names
            limitedTypeNames.add("Store");
            // Fill all elements ids
            init(GpmTestValues.PRODUCT_BERNARD_STORE_NAME, "Store");
            init(GpmTestValues.PRODUCT_ENVIRONMENT_TEST_STORE, "Store");
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "Store");
            init(GpmTestValues.PRODUCT_PRODUCT_WITH_NO_USERS, "Store");
            init(GpmTestValues.PRODUCT1_NAME, "Store");
            init(GpmTestValues.PRODUCT_PRODUCT1_1, "Store");
            init(GpmTestValues.PRODUCT_PRODUCT1_2, "Store");
            init(GpmTestValues.PRODUCT_PRODUCT2, "Store");
            init(GpmTestValues.PRODUCT_PRODUCT3, "Store");
            init(GpmTestValues.PRODUCT_STORE1_NAME, "Store");
            init(GpmTestValues.PRODUCT_STORE1_1_NAME, "Store");
            init(GpmTestValues.PRODUCT_STORE2_NAME, "Store");
            init(GpmTestValues.PRODUCT_SUBSTORE, "Store");
            init(GpmTestValues.PRODUCT_PRODUCT1, "Store");
            init(GpmTestValues.PRODUCT_CHEZ_MIMOUN, "Store");
            init(GpmTestValues.PRODUCT_4, "Store");
            init = true;
        }
    }

    private void init(final String pProductName, final String pTypeName) {
        final String lProductId =
                productService.getProductId(adminRoleToken, pProductName);

        allIds.add(lProductId);
        if (limitedProductNames.contains(pProductName)) {
            byProductIds.add(lProductId);
        }
        if (limitedTypeNames.contains(pTypeName)) {
            byTypeIds.add(lProductId);
        }
        if (productNamesWithRoleOn.contains(pProductName)) {
            idsWithRoleOn.add(lProductId);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#setSpecificFlag(org.topcased.gpm.business.exportation.ExportProperties,
     *      org.topcased.gpm.business.exportation.ExportProperties.ExportFlag)
     */
    protected void setSpecificFlag(final ExportProperties pProperties,
            final ExportFlag pFlag) {
        pProperties.setProductsFlag(pFlag);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractValuesContainerTestExport#getId(org.topcased.gpm.business.serialization.data.ValuesContainerData)
     */
    protected String getId(final Product pObject) {
        return productService.getProductId(adminRoleToken, pObject.getName());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getExpectedIdsForAll()
     */
    protected Set<String> getExpectedIdsForAll() {
        return allIds;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getExpectedIdsForLimitedByProduct()
     */
    protected Set<String> getExpectedIdsForLimitedByProduct() {
        return byProductIds;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getLimitedProductNames()
     */
    protected Set<String> getLimitedProductNames() {
        return limitedProductNames;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getExpectedIdsForLimitedByType()
     */
    protected Set<String> getExpectedIdsForLimitedByType() {
        return byTypeIds;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getLimitedTypeNames()
     */
    protected Set<String> getLimitedTypeNames() {
        return limitedTypeNames;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getIdsWithRoleOn()
     */
    protected Set<String> getIdsWithRoleOn() {
        return idsWithRoleOn;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getElementInfo(java.lang.String)
     */
    protected String getElementInfo(final String pElementId) {
        return pElementId;
    }
}