/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.fields;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.display.service.DisplayService;
import org.topcased.gpm.business.facilities.DisplayGroupData;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.domain.fields.FieldType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestAddToDisplayGroupDataService
 * 
 * @author mfranche
 */
public class TestAddToDisplayGroupDataService extends
        AbstractBusinessServiceTestCase {

    /** The name of a sheet type */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_CAT;

    private static final String DISPLAY_GROUP = "DISPLAY_GROUP";

    private static final String[] FIELD1 =
            { "CAT_description", "Pet description", FieldType.STRING.getValue() };

    private static final String FIELD_ADDED_LABEL_KEY = "CAT_color";

    private static final String FIELD_ADDED_TYPE = "CHOICE_FIELD";

    private static final String FIELD_ADDED_I18N_NAME =
            GpmTestValues.CATEGORY_COLOR;

    /** The Display Service */
    private DisplayService displayService;

    /**
     * Tests the method in a normal case.
     */
    public void testNormalCase() {
        displayService = serviceLocator.getDisplayService();

        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);

        //Creating display group data
        DisplayGroupData lDisplayGroupData = new DisplayGroupData();

        lDisplayGroupData.setContainerId(lType.getId());
        lDisplayGroupData.setDisplayOrder(0);
        lDisplayGroupData.setI18nName(DISPLAY_GROUP);
        lDisplayGroupData.setLabelKey(DISPLAY_GROUP);

        FieldSummaryData lFieldSummaryData =
                new FieldSummaryData(FIELD1[0], FIELD1[1], null, FIELD1[2]);

        lDisplayGroupData.setFieldSummaryDatas(new FieldSummaryData[] { lFieldSummaryData });

        // Create the display group with display service
        displayService.createDisplayGroup(lDisplayGroupData);

        // Add a new field to the display group data
        fieldsService.addToDisplayGroupData(adminRoleToken, lDisplayGroupData,
                FIELD_ADDED_LABEL_KEY);

        // Check that the new field has correctly been added
        assertNotNull("The field summary datas is incorrect.",
                lDisplayGroupData.getFieldSummaryDatas());
        assertTrue("The field summary datas size is not correct.",
                lDisplayGroupData.getFieldSummaryDatas().length == 2);
        // Get the last field summary data
        FieldSummaryData lLastFieldSummaryData =
                lDisplayGroupData.getFieldSummaryDatas()[1];

        assertEquals("The I18n Name of the FieldSummaryData is incorrect.",
                FIELD_ADDED_I18N_NAME, lLastFieldSummaryData.getI18nName());
        assertEquals("The type of the FieldSummaryData is incorrect.",
                FIELD_ADDED_TYPE, lLastFieldSummaryData.getType());
        assertEquals("The labelKey of the FieldSummaryData is incorrect.",
                FIELD_ADDED_LABEL_KEY, lLastFieldSummaryData.getLabelKey());
    }
}
