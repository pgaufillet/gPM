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
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.fields.ChoiceFieldData;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>createProductType</CODE> of the Product Service.
 * 
 * @author sidjelli, nveillet
 */
public class TestCreateProductTypeService extends
        AbstractBusinessServiceTestCase {

    /** The Product Service. */
    private ProductService productService;

    /** The Fields Service. */
    private FieldsService fieldsService;

    /** The product type used for the creation. */
    private static final String PRODUCT_TYPE = "Engine";

    /** The descriptionof the product type. */
    private static final String PRODUCT_TYPE_DESC = "Chemical Engine";

    /** The product type used for the update. */
    private static final String PRODUCT_TYPE_FOR_UPDATE = "Store";

    /** The field name. */
    private static final String FIELD_NAME = "TEST_ChoiceField";

    /** The field category name. */
    private static final String FIELD_CATEGORY_NAME =
            GpmTestValues.CATEGORY_COLOR;

    /** The field defaultValue. */
    private static final String FIELD_DEFAULT_VALUE =
            GpmTestValues.CATEGORY_COLOR_VALUE_WHITE;

    /** The field valuesSeparator. */
    private static final String FIELD_VALUES_SEPARATOR = "ValuesSeparator";

    /**
     * Tests the createProductType method.
     */
    public void testNormalCase() {
        // Gets the fields service and the sheet service.
        fieldsService = serviceLocator.getFieldsService();
        productService = serviceLocator.getProductService();

        // Create a product type data.
        CacheableProductType lProductType = new CacheableProductType();
        lProductType.setId(null);
        lProductType.setName(PRODUCT_TYPE);
        lProductType.setDescription(PRODUCT_TYPE_DESC);

        assertNotNull("Data of product type null.", lProductType);

        // The main goal of this test...
        String lProductTypeId =
                productService.createProductType(adminRoleToken,
                        getProcessName(), lProductType);

        // Gets the product type
        CacheableProductType lCacheableProductType =
                productService.getCacheableProductTypeByName(adminRoleToken,
                        getProcessName(), PRODUCT_TYPE,
                        CacheProperties.IMMUTABLE);
        assertNotNull("Product type " + PRODUCT_TYPE + " not found.",
                lCacheableProductType);

        // We must verify that the product type has been truly created ;)
        CacheableProductType lCreatedProductTypeData =
                productService.getCacheableProductType(adminRoleToken,
                        lProductTypeId, CacheProperties.IMMUTABLE);
        assertNotNull("Product type #" + lProductTypeId
                + " hasn't been created.", lCreatedProductTypeData);

        assertEquals("The created product type has not the expected id.",
                lCacheableProductType.getId(), lCreatedProductTypeData.getId());

        assertEquals("The created product type has not the expected name.",
                lProductType.getName(), lCreatedProductTypeData.getName());

        assertEquals(
                "The created product type has not the expected description.",
                lProductType.getDescription(),
                lCreatedProductTypeData.getDescription());

        // Add ChoiceField to the product type
        // Create a ChoiceField data.
        ChoiceFieldData lChoiceFieldData = new ChoiceFieldData();
        lChoiceFieldData.setLabelKey(FIELD_NAME);
        lChoiceFieldData.setCategoryName(FIELD_CATEGORY_NAME);
        lChoiceFieldData.setDefaultValue(FIELD_DEFAULT_VALUE);
        lChoiceFieldData.setValuesSeparator(FIELD_VALUES_SEPARATOR);

        assertNotNull("Data of ChoiceField null.", lChoiceFieldData);

        // Creating the ChoiceField
        fieldsService.createChoiceField(adminRoleToken,
                lCreatedProductTypeData.getId(), lChoiceFieldData);

        // Gets the ChoiceField
        ChoiceFieldData lChoiceField =
                (ChoiceFieldData) fieldsService.getField(adminRoleToken,
                        lCreatedProductTypeData.getId(), FIELD_NAME);
        assertNotNull("ChoiceField " + FIELD_NAME + " not found.", lChoiceField);
    }

    /**
     * Tests the createProductType method in update mode.
     */
    public void testUpdateProductTypeCase() {
        // Gets the fields service and the sheet service.
        fieldsService = serviceLocator.getFieldsService();
        productService = serviceLocator.getProductService();

        CacheableProductType lProductType =
                productService.getCacheableProductTypeByName(adminRoleToken,
                        getProcessName(), PRODUCT_TYPE_FOR_UPDATE,
                        CacheProperties.MUTABLE);
        assertNotNull(
                "Product type " + PRODUCT_TYPE_FOR_UPDATE + " not found.",
                lProductType);

        // The main goal of this test...
        String lInitialId = lProductType.getId();
        String lInitialDescription = lProductType.getDescription();

        lProductType.setId("12345");
        lProductType.setDescription(PRODUCT_TYPE_DESC);

        String lProductTypeId =
                productService.createProductType(adminRoleToken,
                        getProcessName(), lProductType);

        // Gets the product type
        CacheableProductType lCacheableProductType =
                productService.getCacheableProductTypeByName(adminRoleToken,
                        getProcessName(), PRODUCT_TYPE_FOR_UPDATE,
                        CacheProperties.MUTABLE);
        assertNotNull(
                "Product type " + PRODUCT_TYPE_FOR_UPDATE + " not found.",
                lCacheableProductType);

        // We must verify that the product type has been truly updated ;)
        CacheableProductType lUpdatedProductType =
                productService.getCacheableProductType(adminRoleToken,
                        lProductTypeId, CacheProperties.IMMUTABLE);
        assertNotNull("Product type #" + lProductTypeId
                + " hasn't been updated.", lUpdatedProductType);

        assertEquals("The updated product type has not the expected id.",
                lInitialId, lUpdatedProductType.getId());

        assertEquals("The updated product type has not the expected name.",
                lCacheableProductType.getName(), lUpdatedProductType.getName());

        assertEquals(
                "The updated product type has not the expected description.",
                lProductType.getDescription(),
                lUpdatedProductType.getDescription());

        assertNotSame("The updated product type has the same description.",
                lInitialDescription, lUpdatedProductType.getDescription());

        // Add ChoiceField to the product type
        // Create a ChoiceField data.
        ChoiceFieldData lChoiceFieldData = new ChoiceFieldData();
        lChoiceFieldData.setLabelKey(FIELD_NAME);
        lChoiceFieldData.setCategoryName(FIELD_CATEGORY_NAME);
        lChoiceFieldData.setDefaultValue(FIELD_DEFAULT_VALUE);
        lChoiceFieldData.setValuesSeparator(FIELD_VALUES_SEPARATOR);

        assertNotNull("Data of ChoiceField null.", lChoiceFieldData);

        // Creating the ChoiceField
        fieldsService.createChoiceField(adminRoleToken,
                lUpdatedProductType.getId(), lChoiceFieldData);

        // Gets the ChoiceField
        ChoiceFieldData lChoiceField =
                (ChoiceFieldData) fieldsService.getField(adminRoleToken,
                        lUpdatedProductType.getId(), FIELD_NAME);
        assertNotNull("ChoiceField " + FIELD_NAME + " not found.", lChoiceField);
    }

    /**
     * Tests the creation of a product type with Name null.
     */
    public void testNullNameCase() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Create a product type data.
        CacheableProductType lProductType = new CacheableProductType();
        lProductType.setId(null);
        lProductType.setName(PRODUCT_TYPE);

        assertNotNull("Data of product type null.", lProductType);

        // The main goal of this test...

        // Set the Name to null value
        lProductType.setName(null);

        try {
            productService.createProductType(adminRoleToken, getProcessName(),
                    lProductType);
            fail("The exception has not been thrown.");
        }
        catch (GDMException lGDMException) {
            // ok
        }
        catch (Throwable e) {

            fail("The exception thrown is not a GDMException.");
        }
    }

    /**
     * Tests the createProductType method with a invalid token.
     */
    public void testInvalidTokenCase() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Uses a empty token.
        try {
            productService.createProductType("", getProcessName(),
                    new CacheableProductType());
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