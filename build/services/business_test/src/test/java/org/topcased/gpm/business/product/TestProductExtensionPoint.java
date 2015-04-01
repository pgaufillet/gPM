/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.product;

import java.util.Collections;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.extensions.product.ProductExtensionPointTest;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the extension of products.
 * 
 * @author tpanuel
 */
public class TestProductExtensionPoint extends AbstractBusinessServiceTestCase {
    private static final String PRODUCT_TYPE_NAME = "Store";

    private static final String PRODUCT_NAME = "ProductTest";

    private static final List<String> ENVIRONMENT_NAMES =
            Collections.singletonList(GpmTestValues.ENVIRONMENT_PROFESSIONAL);

    private ProductService productService;

    private AttributesService attributesService;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    @Override
    protected void setUp() {
        super.setUp();
        productService = serviceLocator.getProductService();
        attributesService = serviceLocator.getAttributesService();
    }

    /**
     * Tests the extension point postGetModel.
     */
    public void testGetModel() {
        final CacheableProductType lType =
                productService.getCacheableProductTypeByName(adminRoleToken,
                        getProcessName(), PRODUCT_TYPE_NAME,
                        CacheProperties.IMMUTABLE);
        final CacheableProduct lProductModel =
                productService.getProductModel(adminRoleToken, lType,
                        ENVIRONMENT_NAMES, null);
        final Boolean[] lExpectedResult =
                new Boolean[] { true, false, false, false, false, false, false };

        checkProductBooleanFieldValue(lProductModel, lExpectedResult);
    }

    /**
     * Tests the extension point preCreateProduct and postCreateProduct.
     */
    public void testCreateProduct() {
        final CacheableProductType lType =
                productService.getCacheableProductTypeByName(adminRoleToken,
                        getProcessName(), PRODUCT_TYPE_NAME,
                        CacheProperties.IMMUTABLE);
        final CacheableProduct lProductModel =
                productService.getProductModel(adminRoleToken, lType,
                        ENVIRONMENT_NAMES, null);

        resetProductBooleanValues(lProductModel);
        lProductModel.setProductName(PRODUCT_NAME);

        final String lProductId =
                serviceLocator.getProductService().createProduct(
                        adminRoleToken, lProductModel, null);
        final CacheableProduct lNewProduct =
                serviceLocator.getProductService().getCacheableProduct(
                        adminRoleToken, lProductId, CacheProperties.IMMUTABLE);
        final Boolean[] lExpectedResult =
                new Boolean[] { false, true, true, false, false, false, false };

        checkProductBooleanFieldValue(lNewProduct, lExpectedResult);
    }

    /**
     * Tests the extension point preUpdateProduct and postUpdateProduct.
     */
    public void testUpdateProduct() {
        final CacheableProductType lType =
                productService.getCacheableProductTypeByName(adminRoleToken,
                        getProcessName(), PRODUCT_TYPE_NAME,
                        CacheProperties.IMMUTABLE);
        final CacheableProduct lProductModel =
                productService.getProductModel(adminRoleToken, lType,
                        ENVIRONMENT_NAMES, null);

        lProductModel.setProductName(PRODUCT_NAME);

        final String lProductId =
                productService.createProduct(adminRoleToken, lProductModel,
                        null);
        final CacheableProduct lNewProduct =
                productService.getCacheableProduct(adminRoleToken, lProductId,
                        CacheProperties.MUTABLE);

        resetProductBooleanValues(lNewProduct);

        productService.updateProduct(adminRoleToken, lNewProduct, null);

        final CacheableProduct lUpdatedProduct =
                productService.getCacheableProduct(adminRoleToken, lProductId,
                        CacheProperties.IMMUTABLE);
        final Boolean[] lExpectedResult =
                new Boolean[] { false, false, false, true, true, false, false };

        checkProductBooleanFieldValue(lUpdatedProduct, lExpectedResult);
    }

    /**
     * Tests the extension point preDeleteProduct and postDeleteProduct.
     */
    public void testDeleteProduct() {
        final CacheableProductType lType =
                productService.getCacheableProductTypeByName(adminRoleToken,
                        getProcessName(), PRODUCT_TYPE_NAME,
                        CacheProperties.IMMUTABLE);
        final CacheableProduct lProductModel =
                productService.getProductModel(adminRoleToken, lType,
                        ENVIRONMENT_NAMES, null);

        lProductModel.setProductName(PRODUCT_NAME);

        final String lProductId =
                productService.createProduct(adminRoleToken, lProductModel,
                        null);
        final CacheableProduct lNewProduct =
                productService.getCacheableProduct(adminRoleToken, lProductId,
                        CacheProperties.MUTABLE);

        resetProductBooleanValues(lNewProduct);

        productService.deleteProduct(adminRoleToken, lProductId, false, null);

        final Boolean[] lExpectedResult =
                new Boolean[] { false, false, false, false, false, true, true };

        checkProductBooleanFieldValue(lNewProduct, lExpectedResult);
    }

    private void resetProductBooleanValues(final CacheableProduct pProduct) {
        resetProductBooleanFieldValue(pProduct,
                ProductExtensionPointTest.POST_GET_MODEL_CHECK);
        resetProductBooleanFieldValue(pProduct,
                ProductExtensionPointTest.PRE_CREATE_CHECK);
        resetProductBooleanFieldValue(pProduct,
                ProductExtensionPointTest.POST_CREATE_CHECK);
        resetProductBooleanFieldValue(pProduct,
                ProductExtensionPointTest.PRE_UPDATE_CHECK);
        resetProductBooleanFieldValue(pProduct,
                ProductExtensionPointTest.POST_UPDATE_CHECK);
        resetProductBooleanAttributeValue(pProduct,
                ProductExtensionPointTest.PRE_DELETE_CHECK);
        resetProductBooleanAttributeValue(pProduct,
                ProductExtensionPointTest.POST_DELETE_CHECK);
    }

    private void resetProductBooleanFieldValue(final CacheableProduct pProduct,
            final String pFieldName) {
        ((FieldValueData) pProduct.getValue(pFieldName)).setValue(Boolean.FALSE.toString());
    }

    private void resetProductBooleanAttributeValue(
            final CacheableProduct pProduct, final String pAttributeName) {
        attributesService.set(pProduct.getTypeId(),
                new AttributeData[] { new AttributeData(pAttributeName,
                        new String[] { Boolean.FALSE.toString() }) });
    }

    private void checkProductBooleanFieldValue(final CacheableProduct pProduct,
            final Boolean[] pValues) {
        checkProductBooleanFieldValue(pProduct,
                ProductExtensionPointTest.POST_GET_MODEL_CHECK, pValues[0]);
        checkProductBooleanFieldValue(pProduct,
                ProductExtensionPointTest.PRE_CREATE_CHECK, pValues[1]);
        checkProductBooleanFieldValue(pProduct,
                ProductExtensionPointTest.POST_CREATE_CHECK, pValues[2]);
        checkProductBooleanFieldValue(pProduct,
                ProductExtensionPointTest.PRE_UPDATE_CHECK, pValues[3]);
        checkProductBooleanFieldValue(pProduct,
                ProductExtensionPointTest.POST_UPDATE_CHECK, pValues[4]);
        checkProductBooleanAttributeValue(pProduct,
                ProductExtensionPointTest.PRE_DELETE_CHECK, pValues[5]);
        checkProductBooleanAttributeValue(pProduct,
                ProductExtensionPointTest.POST_DELETE_CHECK, pValues[6]);
    }

    private void checkProductBooleanFieldValue(final CacheableProduct pProduct,
            final String pFieldName, final Boolean pValue) {
        assertEquals("Product extension point has not be executed",
                pValue.toString(),
                ((FieldValueData) pProduct.getValue(pFieldName)).getValue());
    }

    private void checkProductBooleanAttributeValue(
            final CacheableProduct pProduct, final String pAttributeName,
            final Boolean pValue) {
        final AttributeData[] lAttributeDatas =
                serviceLocator.getAttributesService().get(pProduct.getTypeId(),
                        new String[] { pAttributeName });

        if (lAttributeDatas == null || lAttributeDatas.length != 1
                || lAttributeDatas[0] == null) {
            assertEquals("Product extension point has not be executed",
                    Boolean.FALSE, pValue);
        }
        else {
            final String[] lStringDatas = lAttributeDatas[0].getValues();

            if (lStringDatas == null || lStringDatas.length != 1) {
                assertEquals("Product extension point has not be executed",
                        Boolean.FALSE, pValue);
            }
            else {
                assertEquals("Product extension point has not be executed",
                        pValue.toString(), lStringDatas[0]);
            }
        }
    }
}