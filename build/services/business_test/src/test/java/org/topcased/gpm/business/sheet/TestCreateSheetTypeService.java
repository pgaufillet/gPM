/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.ChoiceFieldData;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>createSheetType</CODE> of the Sheet Service.
 * 
 * @author sidjelli
 */
public class TestCreateSheetTypeService extends AbstractBusinessServiceTestCase {

    /** The Sheet Service. */
    private SheetService sheetService;

    /** The Fields Service. */
    private FieldsService fieldsService;

    /** The sheet type used for the update. */
    private static final String SHEET_TYPE_FOR_UPDATE =
            GpmTestValues.SHEET_TYPE_CAT;

    /** The sheet type description. */
    private static final String SHEET_TYPE_DESCR = "Squirrel eat nuts";

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
     * Tests the createSheetType method in update mode.
     */
    public void testUpdateSheetTypeCase() {
        // Gets the fields service and the sheet service.
        fieldsService = serviceLocator.getFieldsService();
        sheetService = serviceLocator.getSheetService();

        // Gets the sheet type
        final CacheableSheetType lSheetType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE_FOR_UPDATE,
                        CacheProperties.IMMUTABLE);

        assertNotNull("Data of sheet type null.", lSheetType);
        assertEquals("The sheet type has not the expected Name.",
                SHEET_TYPE_FOR_UPDATE, lSheetType.getName());
        assertTrue("The sheet type has not the expected selectable.",
                lSheetType.isSelectable());

        // The main goal of this test...

        String lInitialId = lSheetType.getId();
        String lInitialDescription = lSheetType.getDescription();
        boolean lInitialSelectable = lSheetType.isSelectable();

        lSheetType.setDescription(SHEET_TYPE_DESCR);
        lSheetType.setSelectable(false);

        final String lSheetTypeId =
                sheetService.createSheetType(adminRoleToken, getProcessName(),
                        lSheetType);

        // Gets the sheet type
        final CacheableSheetType lSheetTypeData =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE_FOR_UPDATE,
                        CacheProperties.IMMUTABLE);

        assertNotNull("Sheet type " + SHEET_TYPE_FOR_UPDATE + " not found.",
                lSheetTypeData);

        // We must verify that the sheet type has been truly updated ;)
        final CacheableSheetType lUpdatededSheetTypeData =
                sheetService.getCacheableSheetType(adminRoleToken,
                        lSheetTypeId, CacheProperties.IMMUTABLE);

        assertNotNull("Sheet type #" + lSheetTypeId + " hasn't been updated.",
                lUpdatededSheetTypeData);
        assertEquals("The updated sheet type has not the expected id.",
                lInitialId, lUpdatededSheetTypeData.getId());
        assertEquals("The updated sheet type has not the expected name.",
                lSheetType.getName(), lUpdatededSheetTypeData.getName());
        assertEquals(
                "The updated sheet type has not the expected description.",
                lSheetType.getDescription(),
                lUpdatededSheetTypeData.getDescription());
        assertEquals("The updated sheet type has not the expected selectable.",
                lSheetType.isSelectable(),
                lUpdatededSheetTypeData.isSelectable());
        assertNotSame("The updated sheet type has the same description.",
                lInitialDescription, lUpdatededSheetTypeData.getDescription());

        assertNotSame("The updated sheet type has the same Selectable.",
                lInitialSelectable, lUpdatededSheetTypeData.isSelectable());

        // Create a ChoiceField data.
        final ChoiceFieldData lChoiceFieldData = new ChoiceFieldData();

        lChoiceFieldData.setLabelKey(FIELD_NAME);
        lChoiceFieldData.setCategoryName(FIELD_CATEGORY_NAME);
        lChoiceFieldData.setDefaultValue(FIELD_DEFAULT_VALUE);
        lChoiceFieldData.setValuesSeparator(FIELD_VALUES_SEPARATOR);

        assertNotNull("Data of ChoiceField null.", lChoiceFieldData);

        // Creating the ChoiceField
        fieldsService.createChoiceField(adminRoleToken,
                lUpdatededSheetTypeData.getId(), lChoiceFieldData);
        // Gets the ChoiceField
        final ChoiceFieldData lChoiceField =
                (ChoiceFieldData) fieldsService.getField(adminRoleToken,
                        lUpdatededSheetTypeData.getId(), FIELD_NAME);

        assertNotNull("ChoiceField " + FIELD_NAME + " not found.", lChoiceField);
    }
}