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
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>createAttachedField</CODE> of the Fields Service.
 * 
 * @author sie
 */
public class TestCreateAttachedFieldService extends
        AbstractBusinessServiceTestCase {

    /** The Fields Service. */
    private FieldsService fieldsService;

    /** The Sheet Service. */
    private SheetService sheetService;

    /** The field name. */
    private static final String FIELD_NAME = "TEST_AttachedField";

    /** The field name. */
    private static final String FIELD_NAME_ALREADY_EXIST = "CAT_picture";

    /** The sheet type used. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_CAT;

    /** The initial field description. */
    private static final String FIELD_INITIAL_DESC =
            "File with a picture of the pet";

    /** The field description. */
    private static final String FIELD_DESC = "TEST_Description";

    /**
     * Tests the createAttachedField method.
     */
    public void testNormalCase() {
        // Gets the fields service and the sheet service.
        fieldsService = serviceLocator.getFieldsService();

        sheetService = serviceLocator.getSheetService();

        // Create a AttachedField data.
        AttachedFieldData lAttachedFieldData = new AttachedFieldData();
        lAttachedFieldData.setLabelKey(FIELD_NAME);
        lAttachedFieldData.setDescription(FIELD_DESC);

        assertNotNull("Data of AttachedField null.", lAttachedFieldData);

        // Retrieving the sheet type
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);
        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lType);

        // Creating the AttachedField
        String lFieldId =
                fieldsService.createAttachedField(adminRoleToken,
                        lType.getId(), lAttachedFieldData);

        // Gets the AttachedField
        AttachedFieldData lAttachedField =
                (AttachedFieldData) fieldsService.getField(adminRoleToken,
                        lType.getId(), FIELD_NAME);
        assertNotNull("AttachedField " + FIELD_NAME + " not found.",
                lAttachedField);

        // We must verify that the AttachedField has been truly created ;)
        AttachedFieldData lCreatedAttachedFieldData =
                (AttachedFieldData) fieldsService.getField(adminRoleToken,
                        lFieldId);
        assertNotNull("AttachedField #" + lFieldId + " hasn't been created.",
                lCreatedAttachedFieldData);

        assertEquals("The created AttachedField has not the expected Id.",
                lFieldId, lCreatedAttachedFieldData.getId());

        assertEquals("The created AttachedField has not the expected Id.",
                lAttachedField.getId(), lCreatedAttachedFieldData.getId());

        assertEquals(
                "The created AttachedField has not the expected Label Key.",
                lAttachedFieldData.getLabelKey(),
                lCreatedAttachedFieldData.getLabelKey());

        assertEquals(
                "The created AttachedField has not the expected Description.",
                FIELD_DESC, lCreatedAttachedFieldData.getDescription());
    }

    /**
     * Tests the createAttachedField method with already exist name.
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

        // Gets the AttachedField
        AttachedFieldData lAttachedFieldData =
                (AttachedFieldData) fieldsService.getField(adminRoleToken,
                        lType.getId(), FIELD_NAME_ALREADY_EXIST);
        assertNotNull("AttachedField " + FIELD_NAME_ALREADY_EXIST
                + " not found.", lAttachedFieldData);

        assertEquals("The AttachedField has not the expected Description.",
                FIELD_INITIAL_DESC, lAttachedFieldData.getDescription());

        // Updating the AttachedField

        String lInitialDescription = lAttachedFieldData.getDescription();

        lAttachedFieldData.setDescription(FIELD_DESC);

        String lFieldId =
                fieldsService.createAttachedField(adminRoleToken,
                        lType.getId(), lAttachedFieldData);

        // Gets the AttachedField
        AttachedFieldData lAttachedField =
                (AttachedFieldData) fieldsService.getField(adminRoleToken,
                        lType.getId(), FIELD_NAME_ALREADY_EXIST);
        assertNotNull("AttachedField " + FIELD_NAME_ALREADY_EXIST
                + " not found.", lAttachedField);

        // We must verify that the AttachedField has been truly updated ;)
        AttachedFieldData lUpdatedAttachedFieldData =
                (AttachedFieldData) fieldsService.getField(adminRoleToken,
                        lFieldId);
        assertNotNull("AttachedField #" + lFieldId + " hasn't been updated.",
                lUpdatedAttachedFieldData);

        assertEquals("The updated AttachedField has not the expected Id.",
                lAttachedFieldData.getId(), lUpdatedAttachedFieldData.getId());

        assertEquals(
                "The updated AttachedField has not the expected Label Key.",
                lAttachedFieldData.getLabelKey(),
                lUpdatedAttachedFieldData.getLabelKey());

        assertNotSame("The updated AttachedField has the same Description.",
                lInitialDescription, lUpdatedAttachedFieldData.getDescription());

        assertEquals(
                "The updated AttachedField has not the expected Description.",
                FIELD_DESC, lUpdatedAttachedFieldData.getDescription());

    }
}