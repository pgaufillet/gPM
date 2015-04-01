/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.fields;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.product.service.ProductTypeData;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>getField</CODE> of the Fields Service.
 * 
 * @author sie
 */
public class TestGetFieldService extends AbstractBusinessServiceTestCase {

    /** The sheet type used. */
    private static final String SHEET_TYPE_CAT = GpmTestValues.SHEET_TYPE_CAT;

    /** The SimpleField name. */
    private static final String SIMPLE_FIELD_NAME = "CAT_ref";

    /** The ChoiceField name. */
    private static final String CHOICE_FIELD_NAME = "CAT_color";

    /** The AttachedField name. */
    private static final String ATTACHED_FIELD_NAME = "CAT_picture";

    /** The sheet type used. */
    private static final String SHEET_TYPE_MOUSE =
            GpmTestValues.SHEET_TYPE_MOUSE;

    /** The MultipleField name. */
    private static final String MULTIPLE_FIELD_NAME = "MOUSE_identification";

    /** The field name. */
    private static final String FIELD_NAME_FOR_NO_NOMINAL_TEST = "DOG_ref";

    /** The XML used to instantiate */
    private static final String XML_INSTANCE_TEST =
            "field/TestGetFieldService.xml";

    /** Field name for confidential test */
    private static final String CONFIDENTIAL_FIELD_NAME = "CAT_birthdate";

    /** Role name for confidential test */
    private static final String CONFIDENTIAL_ROLE_NAME = "cat_viewer";

    /** Product type name */
    private static final String PRODUCT_TYPE_NAME = "Store";

    /** Product type field name */
    private static final String PRODUCT_FIELD_NAME = "product_name";

    /**
     * Tests the getField method.
     */
    public void testNormalCase() {
        // Retrieving the sheet type
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE_CAT,
                        CacheProperties.IMMUTABLE);
        assertNotNull("Sheet type " + SHEET_TYPE_CAT + " not found.", lType);

        // Gets the SimpleField
        SimpleFieldData lSimpleFieldDataByFieldName =
                (SimpleFieldData) fieldsService.getField(adminRoleToken,
                        lType.getId(), SIMPLE_FIELD_NAME);
        assertNotNull("Field " + SIMPLE_FIELD_NAME + " not found.",
                lSimpleFieldDataByFieldName);

        SimpleFieldData lSimpleFieldDataByFieldId =
                (SimpleFieldData) fieldsService.getField(adminRoleToken,
                        lSimpleFieldDataByFieldName.getId());
        assertNotNull("Field " + SIMPLE_FIELD_NAME + " not found.",
                lSimpleFieldDataByFieldId);

        assertEquals("The field has not the expected Id.",
                lSimpleFieldDataByFieldName.getId(),
                lSimpleFieldDataByFieldId.getId());

        assertEquals("The created SimpleField has not the expected Label Key.",
                lSimpleFieldDataByFieldName.getLabelKey(),
                lSimpleFieldDataByFieldId.getLabelKey());

        // Gets the ChoiceField
        ChoiceFieldData lChoiceFieldDataByFieldName =
                (ChoiceFieldData) fieldsService.getField(adminRoleToken,
                        lType.getId(), CHOICE_FIELD_NAME);
        assertNotNull("Field " + CHOICE_FIELD_NAME + " not found.",
                lChoiceFieldDataByFieldName);

        ChoiceFieldData lChoiceFieldDataByFieldId =
                (ChoiceFieldData) fieldsService.getField(adminRoleToken,
                        lChoiceFieldDataByFieldName.getId());
        assertNotNull("Field " + CHOICE_FIELD_NAME + " not found.",
                lChoiceFieldDataByFieldId);

        assertEquals("The field has not the expected Id.",
                lChoiceFieldDataByFieldName.getId(),
                lChoiceFieldDataByFieldId.getId());

        assertEquals("The field has not the expected Label Key.",
                lChoiceFieldDataByFieldName.getLabelKey(),
                lChoiceFieldDataByFieldId.getLabelKey());

        // Gets the AttachedField
        AttachedFieldData lAttachedFieldDataByFieldName =
                (AttachedFieldData) fieldsService.getField(adminRoleToken,
                        lType.getId(), ATTACHED_FIELD_NAME);
        assertNotNull("Field " + ATTACHED_FIELD_NAME + " not found.",
                lAttachedFieldDataByFieldName);

        AttachedFieldData lAttachedFieldDataByFieldId =
                (AttachedFieldData) fieldsService.getField(adminRoleToken,
                        lAttachedFieldDataByFieldName.getId());
        assertNotNull("Field " + ATTACHED_FIELD_NAME + " not found.",
                lAttachedFieldDataByFieldId);

        assertEquals("The field has not the expected Id.",
                lAttachedFieldDataByFieldName.getId(),
                lAttachedFieldDataByFieldId.getId());

        assertEquals("The field has not the expected Label Key.",
                lAttachedFieldDataByFieldName.getLabelKey(),
                lAttachedFieldDataByFieldId.getLabelKey());

        // Retrieving the sheet type
        lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE_MOUSE,
                        CacheProperties.IMMUTABLE);
        assertNotNull("Sheet type " + SHEET_TYPE_MOUSE + " not found.", lType);

        // Gets the MultipleField
        MultipleFieldData lMultipleFieldDataByFieldName =
                (MultipleFieldData) fieldsService.getField(adminRoleToken,
                        lType.getId(), MULTIPLE_FIELD_NAME);
        assertNotNull("Field " + MULTIPLE_FIELD_NAME + " not found.",
                lMultipleFieldDataByFieldName);

        MultipleFieldData lMultipleFieldDataByFieldId =
                (MultipleFieldData) fieldsService.getField(adminRoleToken,
                        lMultipleFieldDataByFieldName.getId());
        assertNotNull("Field " + MULTIPLE_FIELD_NAME + " not found.",
                lMultipleFieldDataByFieldId);

        assertEquals("The field has not the expected Id.",
                lMultipleFieldDataByFieldName.getId(),
                lMultipleFieldDataByFieldId.getId());

        assertEquals("The created SimpleField has not the expected Label Key.",
                lMultipleFieldDataByFieldName.getLabelKey(),
                lMultipleFieldDataByFieldId.getLabelKey());
    }

    /**
     * Tests the getField method.
     */
    public void testNormalCaseWithProduct() {
        // Retrieving the sheet type
        ProductService lProductService = serviceLocator.getProductService();
        ProductTypeData lProductTypeData =
                lProductService.getProductTypeByName(adminRoleToken,
                        getProcessName(), PRODUCT_TYPE_NAME);
        assertNotNull("Product type " + PRODUCT_TYPE_NAME + " not found.",
                lProductTypeData);

        // Gets the SimpleField
        SimpleFieldData lSimpleFieldDataByFieldName =
                (SimpleFieldData) fieldsService.getField(adminRoleToken,
                        lProductTypeData.getId(), PRODUCT_FIELD_NAME);
        assertNotNull("Field " + PRODUCT_FIELD_NAME + " not found.",
                lSimpleFieldDataByFieldName);

        assertEquals(PRODUCT_FIELD_NAME,
                lSimpleFieldDataByFieldName.getLabelKey());
    }

    /**
     * Tests the getField method.
     */
    public void testInvalidNameFieldCase() {
        // Retrieving the sheet type
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE_CAT,
                        CacheProperties.IMMUTABLE);
        assertNotNull("Sheet type " + SHEET_TYPE_CAT + " not found.", lType);

        // Gets the field
        try {
            fieldsService.getField(adminRoleToken, lType.getId(),
                    FIELD_NAME_FOR_NO_NOMINAL_TEST);
        }
        catch (GDMException lGDMException) {
            // OK
        }
    }

    /**
     * testConfidentialAccess
     */
    public void testConfidentialAccess() {
        // set confidential access on field CAT_birthdate
        instantiate(getProcessName(), XML_INSTANCE_TEST);

        authorizationService = serviceLocator.getAuthorizationService();
        adminUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");
        adminRoleToken =
                authorizationService.selectRole(adminUserToken,
                        CONFIDENTIAL_ROLE_NAME, getProductName(),
                        getProcessName());

        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE_CAT,
                        CacheProperties.IMMUTABLE);
        assertNotNull("Sheet type " + SHEET_TYPE_CAT + " not found.", lType);
        SimpleFieldData lSimpleFieldData =
                (SimpleFieldData) fieldsService.getField(adminRoleToken,
                        lType.getId(), CONFIDENTIAL_FIELD_NAME);
        assertFalse("The default access to " + CONFIDENTIAL_FIELD_NAME
                + " must not be confidential",
                lSimpleFieldData.getDefaultAccess().getConfidential());
    }

    /**
     * Test with an invalid container id
     */
    public void testInvalidFieldContainerCase() {
        // Retrieving the sheet type
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE_CAT,
                        CacheProperties.IMMUTABLE);
        assertNotNull("Sheet type " + SHEET_TYPE_CAT + " not found.", lType);

        // Gets the field
        try {
            fieldsService.getField(adminRoleToken, "",
                    FIELD_NAME_FOR_NO_NOMINAL_TEST);
            fail("The exception has not been thrown.");
        }
        catch (InvalidIdentifierException ex) {
            // ok.
        }
    }
}
