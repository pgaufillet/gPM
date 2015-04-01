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
 * Tests the method <CODE>createChoiceField</CODE> of the Fields Service.
 * 
 * @author sie
 */
public class TestCreateChoiceFieldService extends
        AbstractBusinessServiceTestCase {

    /** The Fields Service. */
    private FieldsService fieldsService;

    /** The Sheet Service. */
    private SheetService sheetService;

    /** The field name. */
    private static final String FIELD_NAME = "TEST_ChoiceField";

    /** The field name. */
    private static final String FIELD_NAME_ALREADY_EXIST = "CAT_furlength";

    /** The field category name. */
    private static final String FIELD_CATEGORY_NAME =
            GpmTestValues.CATEGORY_COLOR;

    /** The field defaultValue. */
    private static final String FIELD_DEFAULT_VALUE =
            GpmTestValues.CATEGORY_COLOR_VALUE_WHITE;

    /** The field valuesSeparator. */
    private static final String FIELD_VALUES_SEPARATOR = "ValuesSeparator";

    /** The sheet type used. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_CAT;

    /** The bad field category name. */
    private static final String FIELD_BAD_CATEGORY_NAME = "CategoryName";

    /** The bad field defaultValue. */
    private static final String FIELD_BAD_DEFAULT_VALUE = "DefaultValue";

    /** The initial field description. */
    private static final String FIELD_INITIAL_DESC = "Fur Length";

    /** The field description. */
    private static final String FIELD_DESC = "TEST_Description";

    /**
     * Tests the createChoiceField method.
     */
    public void testNormalCase() {
        // Gets the fields service and the sheet service.
        fieldsService = serviceLocator.getFieldsService();

        sheetService = serviceLocator.getSheetService();

        // Create a ChoiceField data.
        ChoiceFieldData lChoiceFieldData = new ChoiceFieldData();
        lChoiceFieldData.setLabelKey(FIELD_NAME);
        lChoiceFieldData.setCategoryName(FIELD_CATEGORY_NAME);
        lChoiceFieldData.setDefaultValue(FIELD_DEFAULT_VALUE);
        lChoiceFieldData.setValuesSeparator(FIELD_VALUES_SEPARATOR);
        lChoiceFieldData.setDescription(FIELD_DESC);

        assertNotNull("Data of ChoiceField null.", lChoiceFieldData);

        // Retrieving the sheet type
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);
        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lType);

        // Creating the ChoiceField
        String lFieldId =
                fieldsService.createChoiceField(adminRoleToken, lType.getId(),
                        lChoiceFieldData);

        // Gets the ChoiceField
        ChoiceFieldData lChoiceField =
                (ChoiceFieldData) fieldsService.getField(adminRoleToken,
                        lType.getId(), FIELD_NAME);
        assertNotNull("ChoiceField " + FIELD_NAME + " not found.", lChoiceField);

        // We must verify that the ChoiceField has been truly created ;)
        ChoiceFieldData lCreatedChoiceFieldData =
                (ChoiceFieldData) fieldsService.getField(adminRoleToken,
                        lFieldId);
        assertNotNull("ChoiceField #" + lFieldId + " hasn't been created.",
                lCreatedChoiceFieldData);

        assertEquals("The created ChoiceField has not the expected Id.",
                lFieldId, lCreatedChoiceFieldData.getId());

        assertEquals("The created ChoiceField has not the expected Id.",
                lChoiceField.getId(), lCreatedChoiceFieldData.getId());

        assertEquals("The created ChoiceField has not the expected Label Key.",
                lChoiceFieldData.getLabelKey(),
                lCreatedChoiceFieldData.getLabelKey());

        assertEquals(
                "The created ChoiceField has not the expected Category Name.",
                lChoiceFieldData.getCategoryName(),
                lCreatedChoiceFieldData.getCategoryName());

        assertEquals(
                "The created ChoiceField has not the expected Default Value.",
                lChoiceFieldData.getDefaultValue(),
                lCreatedChoiceFieldData.getDefaultValue());

        assertEquals(
                "The created ChoiceField has not the expected Values Separator.",
                lChoiceFieldData.getValuesSeparator(),
                lCreatedChoiceFieldData.getValuesSeparator());

        assertEquals(
                "The created ChoiceField has not the expected Description.",
                FIELD_DESC, lCreatedChoiceFieldData.getDescription());

    }

    /**
     * Tests the createChoiceField method with already exist name.
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

        // Gets the ChoiceField
        ChoiceFieldData lChoiceFieldData =
                (ChoiceFieldData) fieldsService.getField(adminRoleToken,
                        lType.getId(), FIELD_NAME_ALREADY_EXIST);
        assertNotNull(
                "ChoiceField " + FIELD_NAME_ALREADY_EXIST + " not found.",
                lChoiceFieldData);

        assertEquals("The ChoiceField has not the expected Description.",
                FIELD_INITIAL_DESC, lChoiceFieldData.getDescription());

        // Updating the ChoiceField

        String lInitialCategoryName = lChoiceFieldData.getCategoryName();
        String lInitialDefaultValue = lChoiceFieldData.getDefaultValue();
        String lInitialValuesSeparator = lChoiceFieldData.getValuesSeparator();
        String lInitialDescription = lChoiceFieldData.getDescription();

        lChoiceFieldData.setCategoryName(FIELD_CATEGORY_NAME);
        lChoiceFieldData.setDefaultValue(FIELD_DEFAULT_VALUE);
        lChoiceFieldData.setValuesSeparator(FIELD_VALUES_SEPARATOR);
        lChoiceFieldData.setDescription(FIELD_DESC);

        String lFieldId =
                fieldsService.createChoiceField(adminRoleToken, lType.getId(),
                        lChoiceFieldData);

        // Gets the ChoiceField
        ChoiceFieldData lChoiceField =
                (ChoiceFieldData) fieldsService.getField(adminRoleToken,
                        lType.getId(), FIELD_NAME_ALREADY_EXIST);
        assertNotNull(
                "ChoiceField " + FIELD_NAME_ALREADY_EXIST + " not found.",
                lChoiceField);

        // We must verify that the ChoiceField has been truly updated ;)
        ChoiceFieldData lUpdatedChoiceFieldData =
                (ChoiceFieldData) fieldsService.getField(adminRoleToken,
                        lFieldId);
        assertNotNull("ChoiceField #" + lFieldId + " hasn't been updated.",
                lUpdatedChoiceFieldData);

        assertEquals("The updated ChoiceField has not the expected Id.",
                lChoiceFieldData.getId(), lUpdatedChoiceFieldData.getId());

        assertEquals("The updated ChoiceField has not the expected LabelKey.",
                lChoiceFieldData.getLabelKey(),
                lUpdatedChoiceFieldData.getLabelKey());

        assertNotSame("The updated ChoiceField has the same Description.",
                lInitialDescription, lUpdatedChoiceFieldData.getDescription());

        assertNotSame("The updated ChoiceField has the same Category Name.",
                lInitialCategoryName, lUpdatedChoiceFieldData.getCategoryName());

        assertNotSame("The updated ChoiceField has the same Default Value.",
                lInitialDefaultValue, lUpdatedChoiceFieldData.getDefaultValue());

        assertNotSame("The updated ChoiceField has the same Values Separator.",
                lInitialValuesSeparator,
                lUpdatedChoiceFieldData.getValuesSeparator());
    }

    /**
     * Tests the createChoiceField method with a CategoryName not present in DB.
     */
    public void testNotPresentInDbCategoryNameCase() {
        // Gets the environment service.
        fieldsService = serviceLocator.getFieldsService();

        sheetService = serviceLocator.getSheetService();

        // Retrieving the sheet type
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);
        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lType);

        // Creating the ChoiceField
        ChoiceFieldData lChoiceFieldData =
                new ChoiceFieldData(FIELD_BAD_CATEGORY_NAME,
                        FIELD_DEFAULT_VALUE, FIELD_VALUES_SEPARATOR);
        lChoiceFieldData.setLabelKey(FIELD_NAME);

        try {
            fieldsService.createChoiceField(adminRoleToken, lType.getId(),
                    lChoiceFieldData);
        }
        catch (GDMException lGDMException) {
            // OK
        }
    }

    /**
     * Tests the createChoiceField method with a DefaultValue impossible for the
     * CategoryName.
     */
    public void testBadDefaultValueForCategoryNameCase() {
        // Gets the environment service.
        fieldsService = serviceLocator.getFieldsService();

        sheetService = serviceLocator.getSheetService();

        // Retrieving the sheet type
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);
        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lType);

        // Creating the ChoiceField
        ChoiceFieldData lChoiceFieldData = new ChoiceFieldData();
        lChoiceFieldData.setLabelKey(FIELD_NAME);
        lChoiceFieldData.setCategoryName(FIELD_CATEGORY_NAME);
        lChoiceFieldData.setDefaultValue(FIELD_BAD_DEFAULT_VALUE);
        lChoiceFieldData.setValuesSeparator(FIELD_VALUES_SEPARATOR);

        try {
            fieldsService.createChoiceField(adminRoleToken, lType.getId(),
                    lChoiceFieldData);
        }
        catch (GDMException lGDMException) {
            // OK
        }
    }
}