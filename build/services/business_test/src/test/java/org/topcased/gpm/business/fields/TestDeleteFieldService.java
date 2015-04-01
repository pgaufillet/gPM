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
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>deleteField</CODE> of the Fields Service.
 * 
 * @author sie
 */
public class TestDeleteFieldService extends AbstractBusinessServiceTestCase {

    /** The SimpleField name. */
    private static final String SIMPLE_FIELD_NAME = "MOUSE_firstname";

    /** The ChoiceField name. */
    private static final String CHOICE_FIELD_NAME = "CAT_furlength";

    /** The AttachedField name. */
    private static final String ATTACHED_FIELD_NAME = "CAT_picture";

    /** The MultipleField name. */
    private static final String MULTIPLE_FIELD_NAME = "MOUSE_owner";

    /** The SimpleField name. */
    private static final String SIMPLE_FIELD_NAME_FOR_NO_NOMINAL_TEST =
            "CAT_description";

    /** The ChoiceField name. */
    private static final String CHOICE_FIELD_NAME_FOR_NO_NOMINAL_TEST =
            "CAT_color";

    /** The AttachedField name. */
    private static final String ATTACHED_FIELD_NAME_FOR_NO_NOMINAL_TEST =
            "DOG_pedigre";

    /** The MultipleField name. */
    private static final String MULTIPLE_FIELD_NAME_FOR_NO_NOMINAL_TEST =
            "MOUSE_identification";

    /** The sheet type used. */
    private static final String SHEET_TYPE_MOUSE =
            GpmTestValues.SHEET_TYPE_MOUSE;

    /** The sheet type used. */
    private static final String SHEET_TYPE_CAT = GpmTestValues.SHEET_TYPE_CAT;

    /** The sheet type used. */
    private static final String SHEET_TYPE_DOG = GpmTestValues.SHEET_TYPE_DOG;

    /** The Sheet Service. */
    private SheetService sheetService;

    /** The Fields Service. */
    private FieldsService fieldsService;

    /**
     * Tests the deleteField() method for a simple field.
     */
    public void testSimpleFieldDeleteCase() {
        deleteField(SHEET_TYPE_MOUSE, SIMPLE_FIELD_NAME);
    }

    /**
     * Tests the deleteField() method for a choice field.
     */
    public void testChoiceFieldDeleteCase() {
        deleteField(SHEET_TYPE_CAT, CHOICE_FIELD_NAME);
    }

    /**
     * Tests the deleteField() method for a attached field.
     */
    public void testAttachedFieldDeleteCase() {
        deleteField(SHEET_TYPE_CAT, ATTACHED_FIELD_NAME);
    }

    /**
     * Tests the deleteField() method for a multiple field.
     */
    public void testMultipleFieldDeleteCase() {
        deleteField(SHEET_TYPE_MOUSE, MULTIPLE_FIELD_NAME);
    }

    /**
     * Tests the deleteFieldWithoutForcingDeletion() method for a simple field.
     */
    public void testSimpleFieldDeleteWithoutForcingDeletionCase() {
        deleteFieldWithoutForcingDeletion(SHEET_TYPE_CAT,
                SIMPLE_FIELD_NAME_FOR_NO_NOMINAL_TEST);
    }

    /**
     * Tests the deleteFieldWithoutForcingDeletion() method for a choice field.
     */
    public void testChoiceFieldDeleteWithoutForcingDeletionCase() {
        deleteFieldWithoutForcingDeletion(SHEET_TYPE_CAT,
                CHOICE_FIELD_NAME_FOR_NO_NOMINAL_TEST);
    }

    /**
     * Tests the deleteFieldWithoutForcingDeletion() method for a attached
     * field.
     */
    public void testAttachedFieldDeleteWithoutForcingDeletionCase() {
        deleteFieldWithoutForcingDeletion(SHEET_TYPE_DOG,
                ATTACHED_FIELD_NAME_FOR_NO_NOMINAL_TEST);
    }

    /**
     * Tests the deleteFieldWithoutForcingDeletion() method for a multiple
     * field.
     */
    public void testMultipleFieldDeleteWithoutForcingDeletionCase() {
        deleteFieldWithoutForcingDeletion(SHEET_TYPE_MOUSE,
                MULTIPLE_FIELD_NAME_FOR_NO_NOMINAL_TEST);
    }

    /**
     * Delete a field
     * 
     * @param pTypeName
     *            Sheet type name
     * @param pFieldName
     *            Name of the field to remove
     */
    private void deleteField(String pTypeName, String pFieldName) {
        // Gets the fields and the sheet service.
        fieldsService = serviceLocator.getFieldsService();
        sheetService = serviceLocator.getSheetService();

        // Gets the sheet type
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), pTypeName, CacheProperties.IMMUTABLE);
        assertNotNull("Sheet type " + pTypeName + " not found.", lType);

        // Gets the field
        FieldTypeData lFieldTypeData =
                fieldsService.getField(adminRoleToken, lType.getId(),
                        pFieldName);
        assertNotNull("Field " + pFieldName + " not found.", lFieldTypeData);

        // Deleting the field
        fieldsService.deleteField(adminRoleToken, lType.getId(), pFieldName,
                true);

        // Try to get the field #FIELD_NAME
        try {
            lFieldTypeData =
                    fieldsService.getField(adminRoleToken, lType.getId(),
                            pFieldName);
        }
        catch (InvalidNameException lINE) {
            lFieldTypeData = null;
        }

        assertNull("Field #" + pFieldName + " has not been deleted in DB.",
                lFieldTypeData);
    }

    /**
     * Delete a field without forcing the deletion of all fields values
     * 
     * @param pTypeName
     *            Sheet type name
     * @param pFieldName
     *            Name of the field to remove
     */
    public void deleteFieldWithoutForcingDeletion(String pTypeName,
            String pFieldName) {

        // Gets the fields and the sheet service.
        fieldsService = serviceLocator.getFieldsService();
        sheetService = serviceLocator.getSheetService();

        // Gets the sheet type
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), pTypeName, CacheProperties.IMMUTABLE);
        assertNotNull("Sheet type " + pTypeName + " not found.", lType);

        // Gets the field
        FieldTypeData lFieldTypeData =
                fieldsService.getField(adminRoleToken, lType.getId(),
                        pFieldName);
        assertNotNull("Field " + pFieldName + " not found.", lFieldTypeData);

        // Deleting the field
        try {
            fieldsService.deleteField(adminRoleToken, lType.getId(),
                    pFieldName, false);
        }
        catch (GDMException lGDMException) {
            // OK
        }
    }
}