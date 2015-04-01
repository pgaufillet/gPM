/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin), Anne Haugommard
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.display;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.display.service.DisplayService;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exception.InvalidValueException;
import org.topcased.gpm.business.facilities.DisplayGroupData;
import org.topcased.gpm.business.fields.FieldSummaryData;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.domain.fields.FieldType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestCreateDisplayGroupService
 * 
 * @author ahaugomm
 */
public class TestCreateDisplayGroupService extends
        AbstractBusinessServiceTestCase {

    /** The name of a sheet type */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_CAT;

    private static final String DISPLAY_GROUP = "DISPLAY_GROUP";

    private static final String[] FIELD1 =
            { "CAT_description", "Pet description", FieldType.STRING.getValue() };

    private static final String[] FIELD2 =
            { "CAT_color", GpmTestValues.CATEGORY_COLOR,
             FieldType.STRING.getValue() };

    /** The Display Service */
    private DisplayService displayService;

    /**
     * Test the creation of a display group.
     */
    public void testNormalCase() {
        displayService = serviceLocator.getDisplayService();

        CacheableSheetType lSheetTypeData =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);

        assertNotNull("The sheet type data " + SHEET_TYPE + " is null. ",
                lSheetTypeData);

        //Creating display group data
        DisplayGroupData lDisplayGroupData = new DisplayGroupData();

        lDisplayGroupData.setContainerId(lSheetTypeData.getId());
        lDisplayGroupData.setDisplayOrder(0);
        lDisplayGroupData.setI18nName(DISPLAY_GROUP);
        lDisplayGroupData.setLabelKey(DISPLAY_GROUP);
        FieldSummaryData[] lFieldSummaryDatas = new FieldSummaryData[2];

        lFieldSummaryDatas[0] =
                new FieldSummaryData(FIELD1[0], FIELD1[1], null, FIELD1[2]);
        lFieldSummaryDatas[1] =
                new FieldSummaryData(FIELD2[0], FIELD2[1], null, FIELD2[2]);

        lDisplayGroupData.setFieldSummaryDatas(lFieldSummaryDatas);

        displayService.createDisplayGroup(lDisplayGroupData);

        lSheetTypeData =
                sheetService.getCacheableSheetType(adminRoleToken,
                        lSheetTypeData.getId(), CacheProperties.IMMUTABLE);
        //Check that display group has been created.
        List<DisplayGroupData> lDisplayGroupDatas =
                displayService.getDisplayGroupList(adminUserToken,
                        lSheetTypeData.getId());

        List<String> lDisplayGroupNames =
                new ArrayList<String>(lDisplayGroupDatas.size());
        for (DisplayGroupData lDGroup : lDisplayGroupDatas) {
            lDisplayGroupNames.add(lDGroup.getLabelKey());
        }
        assertTrue("The display group data has not been created.",
                lDisplayGroupNames.contains(DISPLAY_GROUP));

    }

    /**
     * When Null Field SummaryData.
     */
    public void testCaseWithNullFieldSummaryData() {
        sheetService = serviceLocator.getSheetService();

        displayService = serviceLocator.getDisplayService();

        CacheableSheetType lSheetTypeData =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);

        assertNotNull("The sheet type data " + SHEET_TYPE + " is null. ",
                lSheetTypeData);

        //Creating display group data
        DisplayGroupData lDisplayGroupData =
                new DisplayGroupData(DISPLAY_GROUP, DISPLAY_GROUP,
                        lSheetTypeData.getId(), 0, true, null);

        displayService.createDisplayGroup(lDisplayGroupData);

        //Check that display group has been created.
        List<DisplayGroupData> lDisplayGroupDatas =
                displayService.getDisplayGroupList(adminUserToken,
                        lSheetTypeData.getId());

        List<String> lDisplayGroupNames =
                new ArrayList<String>(lDisplayGroupDatas.size());
        for (DisplayGroupData lDGroup : lDisplayGroupDatas) {
            lDisplayGroupNames.add(lDGroup.getLabelKey());
        }
        assertTrue("The display group data has not been created.",
                lDisplayGroupNames.contains(DISPLAY_GROUP));
    }

    /**
     * When Null LabelKey
     */
    public void testCaseWithNullLabelKey() {
        sheetService = serviceLocator.getSheetService();
        displayService = serviceLocator.getDisplayService();

        CacheableSheetType lSheetTypeData =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);

        assertNotNull("The sheet type data " + SHEET_TYPE + " is null. ",
                lSheetTypeData);

        //Creating display group data
        DisplayGroupData lDisplayGroupData = new DisplayGroupData();

        lDisplayGroupData.setContainerId(lSheetTypeData.getId());
        lDisplayGroupData.setDisplayOrder(0);
        lDisplayGroupData.setI18nName(null);
        lDisplayGroupData.setLabelKey(null);
        lDisplayGroupData.setFieldSummaryDatas(null);

        try {
            displayService.createDisplayGroup(lDisplayGroupData);
            fail("The display group should not have been created with null labelkey.");
        }
        catch (InvalidNameException e) {

        }
    }

    /**
     * When Null LabelKey
     */
    public void testCaseWithNullContainerID() {
        sheetService = serviceLocator.getSheetService();
        displayService = serviceLocator.getDisplayService();

        CacheableSheetType lSheetTypeData =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);

        assertNotNull("The sheet type data " + SHEET_TYPE + " is null. ",
                lSheetTypeData);

        //Creating display group data
        DisplayGroupData lDisplayGroupData = new DisplayGroupData();

        lDisplayGroupData.setContainerId(null);
        lDisplayGroupData.setDisplayOrder(0);
        lDisplayGroupData.setI18nName(DISPLAY_GROUP);
        lDisplayGroupData.setLabelKey(DISPLAY_GROUP);
        lDisplayGroupData.setFieldSummaryDatas(null);

        try {
            displayService.createDisplayGroup(lDisplayGroupData);
            fail("The display group should not have been created with null ContainerID.");
        }
        catch (InvalidValueException e) {
        }
    }
}
