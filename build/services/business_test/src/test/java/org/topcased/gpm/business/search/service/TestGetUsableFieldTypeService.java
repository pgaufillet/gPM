/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import java.util.Map;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>getUsableFieldType<CODE> of the Search Service.
 * 
 * @author ahaugomm
 */
public class TestGetUsableFieldTypeService extends
        AbstractBusinessServiceTestCase {

    /** The Search Service. */
    private SearchService searchService;

    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_CAT;

    /**
     * Test the method in a normal way
     */
    public void testNormalCase() {
        searchService = serviceLocator.getSearchService();

        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);

        assertNotNull("SheetType id is null", lType.getId());

        String[] lId = { lType.getId() };

        Map<String, UsableFieldData> lUsableFieldDatas =
                searchService.getUsableFields(adminRoleToken, lId,
                        getProcessName());
        assertNotNull("get usable fields returns null", lUsableFieldDatas);

        for (UsableFieldData lUsableFieldData : lUsableFieldDatas.values()) {

            assertNotNull("Usable Field data is null", lUsableFieldData);

            // Main test
            FieldType lFieldType = lUsableFieldData.getFieldType();
            assertEquals(lUsableFieldData.getFieldType(), lFieldType);
        }
    }
}
