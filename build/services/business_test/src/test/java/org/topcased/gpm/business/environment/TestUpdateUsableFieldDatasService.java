/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.environment.service.EnvironmentService;
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.search.impl.fields.UsableFieldsManager;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>updateUsableFieldDatas<CODE> of the Environment
 * Service.
 * 
 * @author ogehin
 */
public class TestUpdateUsableFieldDatasService extends
        AbstractBusinessServiceTestCase {

    /** The Environment Service. */
    private EnvironmentService environmentService;

    private static final String CONTAINER_NAME = GpmTestValues.SHEET_TYPE_CAT;

    /** The product names. */
    private static final String[] PRODUCT_NAMES =
            { GpmTestValues.PRODUCT_ENVIRONMENT_TEST_STORE,
             GpmTestValues.PRODUCT_SUBSTORE };

    /** The Color category values. */
    private static final String[] COLOR_VALUES =
            { GpmTestValues.CATEGORY_COLOR_VALUE_WHITE,
             GpmTestValues.CATEGORY_COLOR_VALUE_GREY,
             GpmTestValues.CATEGORY_COLOR_VALUE_BLACK,
             GpmTestValues.CATEGORY_COLOR_VALUE_RED };

    /** The Usage category values. */
    private static final String[] USAGE_VALUES =
            { GpmTestValues.CATEGORY_USAGE_VALUE_SELF_DEFENSE,
             GpmTestValues.CATEGORY_USAGE_VALUE_POLICE, "Children" };

    /**
     * Tests the getEnvironmentNames method.
     */
    public void testNormalCase() {
        // Gets the environment service.
        environmentService = serviceLocator.getEnvironmentService();

        // Retrieving the environments names
        // Building of parameters
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), CONTAINER_NAME,
                        CacheProperties.IMMUTABLE);
        String[] lFieldsContainerIds = { lType.getId() };

        Map<String, UsableFieldData> lUsableFieldDatas =
                new TreeMap<String, UsableFieldData>();

        List<String> lPossibleValues =
                new ArrayList<String>(COLOR_VALUES.length + 1);
        lPossibleValues.add(UsableFieldsManager.NOT_SPECIFIED);
        for (int i = 1; i < lPossibleValues.size(); i++) {
            lPossibleValues.add(COLOR_VALUES[i - 1]);
        }

        UsableFieldData lUsableFieldData =
                new UsableFieldData(FieldType.CHOICE_FIELD,
                        GpmTestValues.CATEGORY_COLOR, StringUtils.EMPTY,
                        lPossibleValues, StringUtils.EMPTY, false, -1, false,
                        StringUtils.EMPTY, 0,
                        new ArrayList<FilterFieldsContainerInfo>());
        lUsableFieldData.setFieldName("CAT_color");
        lUsableFieldDatas.put(lUsableFieldData.getFieldName(), lUsableFieldData);

        lPossibleValues = new ArrayList<String>(USAGE_VALUES.length + 1);
        lPossibleValues.add(UsableFieldsManager.NOT_SPECIFIED);
        for (int i = 1; i < lPossibleValues.size(); i++) {
            lPossibleValues.add(USAGE_VALUES[i - 1]);
        }
        lUsableFieldData =
                new UsableFieldData(FieldType.CHOICE_FIELD,
                        GpmTestValues.CATEGORY_USAGE, StringUtils.EMPTY,
                        lPossibleValues, StringUtils.EMPTY, false, -1, false,
                        StringUtils.EMPTY, 0,
                        new ArrayList<FilterFieldsContainerInfo>());
        lUsableFieldData.setFieldName("CAT_usage");
        lUsableFieldDatas.put(lUsableFieldData.getFieldName(), lUsableFieldData);

        environmentService.updateUsableFieldDatas(adminRoleToken,
                getProcessName(), PRODUCT_NAMES, lUsableFieldDatas,
                lFieldsContainerIds);

        assertNotNull("Method updateUsableFieldDatas returns null.",
                lUsableFieldDatas);

        List<String> lPossibleValuesResult =
                lUsableFieldDatas.get("CAT_color").getPossibleValues();

        int lSize = lPossibleValuesResult.size();
        int lExpectedSize = 4;
        assertEquals("Invalid StringValueData count", lExpectedSize, lSize);

        List<String> lValueDatasResult = lPossibleValuesResult;
        assertTrue(
                "Scalar value datas are not thoses expected.",
                lValueDatasResult.contains(GpmTestValues.CATEGORY_COLOR_VALUE_BLACK));
        assertTrue(
                "Scalar value datas are not thoses expected.",
                lValueDatasResult.contains(GpmTestValues.CATEGORY_COLOR_VALUE_WHITE));
        assertTrue(
                "Scalar value datas are not thoses expected.",
                lValueDatasResult.contains(GpmTestValues.CATEGORY_COLOR_VALUE_RED));
        assertFalse(
                "Scalar value datas are not thoses expected.",
                lValueDatasResult.contains(GpmTestValues.CATEGORY_COLOR_VALUE_GREY));
        assertTrue("Scalar value datas are not thoses expected.",
                lValueDatasResult.contains(UsableFieldsManager.NOT_SPECIFIED));

        lPossibleValuesResult =
                lUsableFieldDatas.get("CAT_usage").getPossibleValues();

        lSize = lPossibleValuesResult.size();
        lExpectedSize = 4;
        assertEquals("Method updateUsableFieldData returns " + lSize
                + " StringValueData instead of " + lExpectedSize, lSize,
                lExpectedSize);

        lValueDatasResult = lPossibleValuesResult;

        assertTrue(
                "Scalar value datas are not thoses expected.",
                lValueDatasResult.contains(GpmTestValues.CATEGORY_USAGE_VALUE_SELF_DEFENSE));
        assertTrue(
                "Scalar value datas are not thoses expected.",
                lValueDatasResult.contains(GpmTestValues.CATEGORY_USAGE_VALUE_POLICE));
        assertTrue("Scalar value datas are not thoses expected.",
                lValueDatasResult.contains("Children"));
        assertTrue("Scalar value datas are not thoses expected.",
                lValueDatasResult.contains(UsableFieldsManager.NOT_SPECIFIED));
    }
}
