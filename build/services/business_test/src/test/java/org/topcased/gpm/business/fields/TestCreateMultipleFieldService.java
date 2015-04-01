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

import java.util.Arrays;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>createMultipleField</CODE> of the Fields Service.
 * 
 * @author sie
 */
public class TestCreateMultipleFieldService extends
        AbstractBusinessServiceTestCase {

    /** The Fields Service. */
    private FieldsService fieldsService;

    /** The field name. */
    private static final String FIELD_NAME = "TEST_MultipleField";

    private static final String NEW_SUBFIELD_NAME = "TEST_Subfield";

    /** The field name. */
    private static final String FIELD_NAME_ALREADY_EXIST = "MOUSE_owner";

    /** The fieldSeparator. */
    private static final String FIELD_SEPARATOR = "FieldSeparator";

    /** The subfields. */
    private static final String[] SUBFIELDS_REFERENCES = { NEW_SUBFIELD_NAME };

    /** The subfields. */
    private static final String[] SUBFIELDS_FOR_UPDATE =
            { "MOUSE_firstname", "MOUSE_lastname", "MOUSE_numberofchildren",
             "MOUSE_mousename" };

    /** The bad subfields. */
    private static final String[] BAD_SUBFIELDS_REFERENCES =
            { "1", "2", "3", "4" };

    /** The sheet type used. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_MOUSE;

    /** The field description. */
    private static final String FIELD_DESC = "TEST_Description";
    
    /** The field max size. */
    private static final int FIELD_MAX_SIZE = 400;

    /**
     * Tests the createMultipleField method.
     */
    public void testNormalCase() {
        // Gets the fields service and the sheet service.
        fieldsService = serviceLocator.getFieldsService();

        // sheetService = serviceLocator.getSheetService();

        // Get the sheet type
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);
        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lType);

        SimpleFieldData lSimpleFieldDef =
                new SimpleFieldData(ValueType.STRING, "Default", FIELD_MAX_SIZE);
        lSimpleFieldDef.setLabelKey(NEW_SUBFIELD_NAME);

        fieldsService.createSimpleField(adminRoleToken, lType.getId(),
                lSimpleFieldDef);

        // Create a MultipleField data.
        MultipleFieldData lMultipleFieldData = new MultipleFieldData();
        lMultipleFieldData.setLabelKey(FIELD_NAME);
        lMultipleFieldData.setFieldSeparator(FIELD_SEPARATOR);

        lMultipleFieldData.setDescription(FIELD_DESC);
        lMultipleFieldData.setSubfields(SUBFIELDS_REFERENCES);

        assertNotNull("Data of MultipleField null.", lMultipleFieldData);

        // Creating the MultipleField
        String lFieldId =
                fieldsService.createMultipleField(adminRoleToken,
                        lType.getId(), lMultipleFieldData);

        // Gets the MultipleField
        MultipleFieldData lMultipleField =
                (MultipleFieldData) fieldsService.getField(adminRoleToken,
                        lType.getId(), FIELD_NAME);
        assertNotNull("MultipleField " + FIELD_NAME + " not found.",
                lMultipleField);

        // We must verify that the MultipleField has been truly created ;)
        MultipleFieldData lCreatedMultipleFieldData =
                (MultipleFieldData) fieldsService.getField(adminRoleToken,
                        lFieldId);
        assertNotNull("MultipleField #" + lFieldId + " hasn't been created.",
                lCreatedMultipleFieldData);

        assertEquals("The created MultipleField has not the expected Id.",
                lFieldId, lCreatedMultipleFieldData.getId());

        assertEquals("The created MultipleField has not the expected Id.",
                lMultipleField.getId(), lCreatedMultipleFieldData.getId());

        assertEquals(
                "The created MultipleField has not the expected Label Key.",
                lMultipleFieldData.getLabelKey(),
                lCreatedMultipleFieldData.getLabelKey());

        assertEquals(
                "The created MultipleField has not the expected FieldSeparator.",
                lMultipleFieldData.getFieldSeparator(),
                lCreatedMultipleFieldData.getFieldSeparator());

        assertEquals(
                "The created MultipleField has not the expected Description.",
                FIELD_DESC, lCreatedMultipleFieldData.getDescription());

        int lSize = lCreatedMultipleFieldData.getSubfields().length;
        int lExpectedSize = SUBFIELDS_REFERENCES.length;
        assertEquals("The MultipleField created returns a list of " + lSize
                + " subfields instead of a list of " + lExpectedSize
                + " subfields", lExpectedSize, lSize);

        for (String lNameSub : lCreatedMultipleFieldData.getSubfields()) {
            assertTrue("The subfield " + lNameSub
                    + " is not an expected subfield.", Arrays.asList(
                    SUBFIELDS_REFERENCES).contains(lNameSub));
        }
    }

    /**
     * Tests the createMultipleField method with already exist name.
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

        // Gets the MultipleField
        MultipleFieldData lMultipleFieldData =
                (MultipleFieldData) fieldsService.getField(adminRoleToken,
                        lType.getId(), FIELD_NAME_ALREADY_EXIST);
        assertNotNull("MultipleField " + FIELD_NAME_ALREADY_EXIST
                + " not found.", lMultipleFieldData);

        // Updating the MultipleField

        String lInitialFieldSeparatore = lMultipleFieldData.getFieldSeparator();
        String[] lInitialSubfields = lMultipleFieldData.getSubfields();
        String lInitialDescription = lMultipleFieldData.getDescription();

        lMultipleFieldData.setFieldSeparator(FIELD_SEPARATOR);
        lMultipleFieldData.setSubfields(SUBFIELDS_FOR_UPDATE);
        lMultipleFieldData.setDescription(FIELD_DESC);

        String lFieldId =
                fieldsService.createMultipleField(adminRoleToken,
                        lType.getId(), lMultipleFieldData);

        // Gets the MultipleField
        MultipleFieldData lMultipleField =
                (MultipleFieldData) fieldsService.getField(adminRoleToken,
                        lType.getId(), FIELD_NAME_ALREADY_EXIST);
        assertNotNull("MultipleField " + FIELD_NAME_ALREADY_EXIST
                + " not found.", lMultipleField);

        // We must verify that the MultipleField has been truly updated ;)
        MultipleFieldData lUpdatedMultipleFieldData =
                (MultipleFieldData) fieldsService.getField(adminRoleToken,
                        lFieldId);
        assertNotNull("MultipleField #" + lFieldId + " hasn't been updated.",
                lUpdatedMultipleFieldData);

        assertEquals("The updated MultipleField has not the expected Id.",
                lMultipleFieldData.getId(), lUpdatedMultipleFieldData.getId());

        assertEquals(
                "The updated MultipleField has not the expected LabelKey.",
                lMultipleFieldData.getLabelKey(),
                lUpdatedMultipleFieldData.getLabelKey());

        assertNotSame("The updated MultipleField has the same Description.",
                lInitialDescription, lUpdatedMultipleFieldData.getDescription());

        assertNotSame("The updated MultipleField has the same FielsSeparator.",
                lInitialFieldSeparatore,
                lUpdatedMultipleFieldData.getLabelKey());

        assertNotSame("The updated MultipleField has the same Description.",
                lInitialSubfields, lUpdatedMultipleFieldData.getLabelKey());

        int lSize = lUpdatedMultipleFieldData.getSubfields().length;
        int lInitialSize = lInitialSubfields.length;
        assertNotSame("The MultipleField updated returns a list of "
                + lInitialSize + " subfields instead of a list of " + lSize
                + " subfields", lInitialSize, lSize);

        for (String lNameSub : lUpdatedMultipleFieldData.getSubfields()) {
            assertTrue("The subfield " + lNameSub
                    + " is not an expected subfield.", Arrays.asList(
                    SUBFIELDS_FOR_UPDATE).contains(lNameSub));
        }
    }

    /**
     * Tests the createMultipleField method with Subfields not present in DB.
     */
    public void testBadSubfieldsCase() {
        // Gets the environment service.
        fieldsService = serviceLocator.getFieldsService();

        sheetService = serviceLocator.getSheetService();

        // Retrieving the sheet type
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);
        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lType);

        // Create a MultipleField data.
        MultipleFieldData lMultipleFieldData =
                new MultipleFieldData(FIELD_SEPARATOR, BAD_SUBFIELDS_REFERENCES);
        lMultipleFieldData.setLabelKey(FIELD_NAME);

        assertNotNull("Data of MultipleField null.", lMultipleFieldData);

        // Creating the MultipleField
        try {
            fieldsService.createMultipleField(adminRoleToken, lType.getId(),
                    lMultipleFieldData);
        }
        catch (GDMException lGDMException) {
            // OK
        }
    }
}