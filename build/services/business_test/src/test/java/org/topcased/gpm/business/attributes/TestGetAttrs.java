/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.attributes;

import org.apache.commons.lang.ArrayUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.authorization.service.EndUserData;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.fields.FieldTypeData;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.product.service.ProductTypeData;

/**
 * Test the methods used to get attributes values & names.
 * <p>
 * The attributes are defined in a specific instance definition file.
 * 
 * @author llatil
 */
public class TestGetAttrs extends AbstractBusinessServiceTestCase {

    /** The expected process names. */
    private static final String INSTANCE_NAME = "AttributesInstance";

    /** The XML used to instantiate */
    private static final String XML_INSTANCE_TEST =
            "attributes/instanceWithAttributes.xml";

    private static final String USER_LOGIN = GpmTestValues.USER_USER1;

    private static final String PRODUCT_TYPE_NAME = "productTypeWithAttributes";

    private static final String PRODUCT_NAME =
            GpmTestValues.PRODUCT_STORE1_NAME;

    private static final String FIELD_NAME = "productField";

    private AttributesService attributesService;

    private FieldsService fieldsService;

    private ProductService productService;

    // List of names of all attributes defined on the product type
    private static final String[] PRODUCT_TYPE_ATTR_NAMES =
            { "productTypeAttr1", "productTypeAttr2" };

    private static final String[] PRODUCT_ATTR_NAMES =
            { "productAttr1", "productAttr2" };

    private static final String[] FIELD_ATTR_NAMES =
            { "fieldAttr1", "fieldAttr2" };

    private static final String[] USER_ATTR_NAMES =
            { "userAttr1", "userAttr2" };

    private static final String[] GLOBAL_ATTR_NAMES =
            { "globalAttr1", "globalAttr2" };

    private static final String[] ATTR1_VALUES = { "val1" };

    private static final String[] ATTR2_VALUES = { "multi1", "multi2" };

    private static final String INCORRECT_CONTAINER_ID = "foo";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    @Override
    protected void setUp() {
        super.setUp();

        instantiate(INSTANCE_NAME, XML_INSTANCE_TEST);

        // Get the services.
        attributesService = serviceLocator.getAttributesService();
        fieldsService = serviceLocator.getFieldsService();
        productService = serviceLocator.getProductService();
    }

    /**
     * Tests the attributes retrieval for an user.
     */
    public void testUserAttributes() {
        EndUserData lUserData = authorizationService.getUserData(USER_LOGIN);

        // Check the attribute names list of the user
        String[] lUserAttrNames =
                attributesService.getAttrNames(lUserData.getId());
        assertTrue("List of user attribute names invalid", ArrayUtils.isEquals(
                lUserAttrNames, USER_ATTR_NAMES));

        AttributeData[] lUserAttrs;
        lUserAttrs = attributesService.getAll(lUserData.getId());

        assertEquals(lUserAttrs[0].getName(), USER_ATTR_NAMES[0]);
        assertEqualsOrdered(ATTR1_VALUES, lUserAttrs[0].getValues());

        assertEquals(lUserAttrs[1].getName(), USER_ATTR_NAMES[1]);
        assertEqualsOrdered(ATTR2_VALUES, lUserAttrs[1].getValues());
    }

    /**
     * Tests the attributes retrieval for a product.
     */
    public void testProductAttributes() {
        ProductTypeData lProductTypeData =
                productService.getProductTypeByName(adminRoleToken,
                        INSTANCE_NAME, PRODUCT_TYPE_NAME);

        FieldTypeData lFieldTypeData =
                fieldsService.getField(adminRoleToken,
                        lProductTypeData.getId(), FIELD_NAME);

        // Check the attribute names list of the product type
        String[] lProductTypeAttrNames =
                attributesService.getAttrNames(lProductTypeData.getId());
        assertTrue("List of product type attribute names invalid",
                ArrayUtils.isEquals(lProductTypeAttrNames,
                        PRODUCT_TYPE_ATTR_NAMES));

        AttributeData[] lProductTypeAttrs;
        lProductTypeAttrs = attributesService.getAll(lProductTypeData.getId());

        assertEquals(lProductTypeAttrs[0].getName(), PRODUCT_TYPE_ATTR_NAMES[0]);
        assertEqualsOrdered(ATTR1_VALUES, lProductTypeAttrs[0].getValues());

        assertEquals(lProductTypeAttrs[1].getName(), PRODUCT_TYPE_ATTR_NAMES[1]);
        assertEqualsOrdered(ATTR2_VALUES, lProductTypeAttrs[1].getValues());

        // Check the attribute names list of the field
        String[] lFieldAttrNames =
                attributesService.getAttrNames(lFieldTypeData.getId());
        assertEqualsOrdered("List of field attribute names invalid",
                FIELD_ATTR_NAMES, lFieldAttrNames);

        // Get the product id
        String lProductId =
                productService.getProductId(adminRoleToken, PRODUCT_NAME);
        // Check the attribute names list of the product
        String[] lProductAttrNames = attributesService.getAttrNames(lProductId);
        assertEqualsOrdered("List of product attribute names invalid",
                PRODUCT_ATTR_NAMES, lProductAttrNames);
    }

    /**
     * Tests the global attributes retrieval.
     */
    public void testGlobalAttributes() {

        AttributeData[] lGlobalAttrs =
                attributesService.getGlobalAttributes(GLOBAL_ATTR_NAMES);

        assertEquals(lGlobalAttrs[0].getName(), GLOBAL_ATTR_NAMES[0]);
        assertEqualsOrdered(ATTR1_VALUES, lGlobalAttrs[0].getValues());

        assertEquals(lGlobalAttrs[1].getName(), GLOBAL_ATTR_NAMES[1]);
        assertEqualsOrdered(ATTR2_VALUES, lGlobalAttrs[1].getValues());
    }

    /**
     * Tests the method getAll method with an incorrect container id
     */
    public void testIncorrectContainerIdCase() {
        try {
            attributesService.getAll(INCORRECT_CONTAINER_ID);
            fail("The exception has not been thrown.");
        }
        catch (InvalidIdentifierException lInvalidIdentifierException) {
            // ok
        }
    }
}
