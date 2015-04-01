/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Mimoun Mennad (ATOS), Pierre-Hubert TSAAN (ATOS)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.util.Arrays;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.MultipleFieldData;
import org.topcased.gpm.business.fields.SimpleFieldData;
import org.topcased.gpm.business.fields.ValueType;
import org.topcased.gpm.business.serialization.data.MultipleField;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method setSheetTypeReferenceField of the Sheet Service.
 * 
 * @author mmennad
 */
public class TestSetSheetTypeReferenceField extends
        AbstractBusinessServiceTestCase {

    /** The field name. */
    private static final String FIELD_NAME = "TEST_MultipleField";

    /** The fieldSeparator. */
    private static final String FIELD_SEPARATOR = "FieldSeparator";

    /** The field description. */
    private static final String FIELD_DESC = "TEST_Description";

    private static final String NEW_SUBFIELD_NAME = "TEST_Subfield";

    /** The subfields. */
    private static final String[] SUBFIELDS_REFERENCES = { NEW_SUBFIELD_NAME };

    /** The sheet type used. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_MOUSE;

    /** The defined maximum size of the file used. */
    private static final int MAX_FILE_SIZE = 400;

    /**
     * Tests the method in case of correct parameters were sent
     */
    public void testNormalCase() {

        // Gets the fields service and the sheet service.
        fieldsService = serviceLocator.getFieldsService();

        // sheetService = serviceLocator.getSheetService();

        // Get the sheet type
        CacheableSheetType lSheetTypeData =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        GpmTestValues.SHEET_TYPE_MOUSE, CacheProperties.MUTABLE);
        assertNotNull("Sheet type " + SHEET_TYPE + " not found.",
                lSheetTypeData);

        // Get the original reference values
        MultipleField lOldFieldRef = lSheetTypeData.getReferenceField();

        SimpleFieldData lSimpleFieldDef =
                new SimpleFieldData(ValueType.STRING, "Default", MAX_FILE_SIZE);
        lSimpleFieldDef.setLabelKey(NEW_SUBFIELD_NAME);

        fieldsService.createSimpleField(adminRoleToken, lSheetTypeData.getId(),
                lSimpleFieldDef);

        // Create a MultipleField data.
        MultipleFieldData lMultipleFieldData = new MultipleFieldData();
        lMultipleFieldData.setLabelKey(FIELD_NAME);
        lMultipleFieldData.setFieldSeparator(FIELD_SEPARATOR);
        lMultipleFieldData.setDescription(FIELD_DESC);
        lMultipleFieldData.setSubfields(SUBFIELDS_REFERENCES);

        assertNotNull("Data of MultipleField null.", lMultipleFieldData);

        // Creating the MultipleField
        String lMultipleFieldId =
                fieldsService.createMultipleField(adminRoleToken,
                        lSheetTypeData.getId(), lMultipleFieldData);

        // Gets the MultipleField
        MultipleFieldData lMultipleField =
                (MultipleFieldData) fieldsService.getField(adminRoleToken,
                        lSheetTypeData.getId(), FIELD_NAME);
        assertNotNull("MultipleField " + FIELD_NAME + " not found.",
                lMultipleField);

        // We must verify that the MultipleField has been truly created ;)
        MultipleFieldData lCreatedMultipleFieldData =
                (MultipleFieldData) fieldsService.getField(adminRoleToken,
                        lMultipleFieldId);
        assertNotNull("MultipleField #" + lMultipleFieldId
                + " hasn't been created.", lCreatedMultipleFieldData);

        assertEquals("The created MultipleField has not the expected Id.",
                lMultipleFieldId, lCreatedMultipleFieldData.getId());

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

        sheetService.setSheetTypeReferenceField(adminRoleToken,
                lSheetTypeData.getId(), lMultipleFieldId);

        CacheableSheetType lNewCacheableSheetTypeData =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        GpmTestValues.SHEET_TYPE_MOUSE, CacheProperties.MUTABLE);

        MultipleField lNewFieldRef =
                lNewCacheableSheetTypeData.getReferenceField();

        //Check new values are not the same as they were before

        assertNotSame(lOldFieldRef.getId(), lNewFieldRef.getId());
        assertNotSame(lOldFieldRef.getDescription(),
                lNewFieldRef.getDescription());
        assertNotSame(lOldFieldRef.getFieldSeparator(),
                lNewFieldRef.getFieldSeparator());
        assertNotSame(lOldFieldRef.getLabelKey(), lNewFieldRef.getLabelKey());

        //Check new values are the same as they are specified above

        assertEquals(lCreatedMultipleFieldData.getDescription(),
                lNewFieldRef.getDescription());
        assertEquals(lCreatedMultipleFieldData.getId(), lNewFieldRef.getId());
        assertEquals(lCreatedMultipleFieldData.getFieldSeparator(),
                lNewFieldRef.getFieldSeparator());
        assertEquals(lCreatedMultipleFieldData.getLabelKey(),
                lNewFieldRef.getLabelKey());

    }

}
