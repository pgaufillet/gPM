/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.fields;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>createSimpleField</CODE> of the Fields Service.
 * 
 * @author sie
 */
public class TestCreateSimpleFieldService extends
        AbstractBusinessServiceTestCase {

    /** The Fields Service. */
    private FieldsService fieldsService;

    /** The Sheet Service. */
    private SheetService sheetService;

    /** The field name. */
    private static final String FIELD_NAME = "TEST_SimpleField";

    /** The field name. */
    private static final String FIELD_NAME_ALREADY_EXIST = "CAT_ref";

    /** The field value. */
    private static final String FIELD_VALUE = "10";

    /** The sheet type used. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_CAT;

    /** The field description. */
    private static final String FIELD_DESC = "TEST_Description";

    /**
     * Tests the createSimpleField method.
     */
    public void testNormalCase() {
        // Gets the fields service and the sheet service.
        fieldsService = serviceLocator.getFieldsService();

        sheetService = serviceLocator.getSheetService();

        // Create a SimpleField data.
        SimpleFieldData lSimpleFieldData = new SimpleFieldData();
        lSimpleFieldData.setLabelKey(FIELD_NAME);
        lSimpleFieldData.setValueType(ValueType.INTEGER);
        lSimpleFieldData.setDefaultValue(FIELD_VALUE);
        lSimpleFieldData.setDescription(FIELD_DESC);

        assertNotNull("Data of simple field null.", lSimpleFieldData);

        // Retrieving the sheet type
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);
        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lType);

        // Creating the SimpleField
        String lFieldId =
                fieldsService.createSimpleField(adminRoleToken, lType.getId(),
                        lSimpleFieldData);

        // Gets the SimpleField
        SimpleFieldData lSimpleField =
                (SimpleFieldData) fieldsService.getField(adminRoleToken,
                        lType.getId(), FIELD_NAME);
        assertNotNull("SimpleField " + FIELD_NAME + " not found.", lSimpleField);

        // We must verify that the SimpleField has been truly created ;)
        SimpleFieldData lCreatedSimpleFieldData =
                (SimpleFieldData) fieldsService.getField(adminRoleToken,
                        lFieldId);
        assertNotNull("SimpleField #" + lFieldId + " hasn't been created.",
                lCreatedSimpleFieldData);

        assertEquals("The created SimpleField has not the expected Id.",
                lFieldId, lCreatedSimpleFieldData.getId());

        assertEquals("The created SimpleField has not the expected Id.",
                lSimpleField.getId(), lCreatedSimpleFieldData.getId());

        assertEquals("The created SimpleField has not the expected Label Key.",
                lSimpleFieldData.getLabelKey(),
                lCreatedSimpleFieldData.getLabelKey());

        assertEquals(
                "The created SimpleField has not the expected Value Type.",
                lSimpleFieldData.getValueType(),
                lCreatedSimpleFieldData.getValueType());

        assertEquals(
                "The created SimpleField has not the expected Default Value.",
                lSimpleFieldData.getDefaultValue(),
                lCreatedSimpleFieldData.getDefaultValue());

        assertEquals(
                "The created SimpleField has not the expected Description.",
                FIELD_DESC, lCreatedSimpleFieldData.getDescription());
    }

    /**
     * Tests the createSimpleField method with already exist name.
     */
    public void testAlreadyExistFieldNameCase() {
        // Gets the environment service.
        fieldsService = serviceLocator.getFieldsService();

        sheetService = serviceLocator.getSheetService();

        // Retrieving the sheet type
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);
        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lType);

        // Gets the SimpleField
        SimpleFieldData lSimpleFieldData =
                (SimpleFieldData) fieldsService.getField(adminRoleToken,
                        lType.getId(), FIELD_NAME_ALREADY_EXIST);
        assertNotNull(
                "SimpleField " + FIELD_NAME_ALREADY_EXIST + " not found.",
                lSimpleFieldData);

        // Updating the SimpleField

        String lIinitialDescription = lSimpleFieldData.getDescription();
        String lInitialDefaultValue = lSimpleFieldData.getDefaultValue();

        lSimpleFieldData.setDescription(FIELD_DESC);
        lSimpleFieldData.setDefaultValue(FIELD_VALUE);
        lSimpleFieldData.setValueType(ValueType.STRING);

        String lFieldId =
                fieldsService.createSimpleField(adminRoleToken, lType.getId(),
                        lSimpleFieldData);

        // Gets the SimpleField
        SimpleFieldData lSimpleField =
                (SimpleFieldData) fieldsService.getField(adminRoleToken,
                        lType.getId(), FIELD_NAME_ALREADY_EXIST);
        assertNotNull(
                "SimpleField " + FIELD_NAME_ALREADY_EXIST + " not found.",
                lSimpleField);

        // We must verify that the SimpleField has been truly updated ;)
        SimpleFieldData lUpdatedSimpleFieldData =
                (SimpleFieldData) fieldsService.getField(adminRoleToken,
                        lFieldId);
        assertNotNull("SimpleField #" + lFieldId + " hasn't been updated.",
                lUpdatedSimpleFieldData);

        assertEquals("The updated SimpleField has not the expected Id.",
                lSimpleFieldData.getId(), lUpdatedSimpleFieldData.getId());

        assertEquals("The updated SimpleField has not the expected Label Key.",
                lSimpleFieldData.getLabelKey(),
                lUpdatedSimpleFieldData.getLabelKey());

        assertEquals(
                "The updated SimpleField has not the expected Value Type.",
                lSimpleFieldData.getValueType(),
                lUpdatedSimpleFieldData.getValueType());

        assertNotSame("The updated SimpleField has the same Description.",
                lIinitialDescription, lUpdatedSimpleFieldData.getDescription());

        assertNotSame("The updated SimpleField has the same Default Value.",
                lInitialDefaultValue, lUpdatedSimpleFieldData.getDefaultValue());
    }

    /**
     * Tests the createSimpleField method with a new ValueType name.
     */
    public void testBadValueTypeCase() {
        // Gets the environment service.
        fieldsService = serviceLocator.getFieldsService();

        sheetService = serviceLocator.getSheetService();

        // Retrieving the sheet type
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);
        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lType);

        // Gets the SimpleField
        SimpleFieldData lSimpleFieldData =
                (SimpleFieldData) fieldsService.getField(adminRoleToken,
                        lType.getId(), FIELD_NAME_ALREADY_EXIST);
        assertNotNull(
                "SimpleField " + FIELD_NAME_ALREADY_EXIST + " not found.",
                lSimpleFieldData);

        // Updating the SimpleField with a no compatible ValueType

        lSimpleFieldData.setValueType(ValueType.DATE);

        try {
            fieldsService.createSimpleField(adminRoleToken, lType.getId(),
                    lSimpleFieldData);
        }
        catch (GDMException lGDMException) {
            // OK
        }
    }
}