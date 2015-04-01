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

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestGetFieldNamesService
 * 
 * @author mfranche
 */
public class TestGetFieldNamesService extends AbstractBusinessServiceTestCase {

    /** The sheet type used. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_CAT;

    /** The Fields Service. */
    private FieldsService fieldsService;

    private static final String CAT_NAME_FIELD = "CAT_name";

    private static final String CAT_REF_FIELD = "CAT_ref";

    private static final String CAT_BIRTHDATE_FIELD = "CAT_birthdate";

    private static final String CAT_COLOR_FIELD = "CAT_color";

    private static final String CAT_FURLENGTH_FIELD = "CAT_furlength";

    private static final String CAT_PICTURE_FIELD = "CAT_picture";

    private static final String CAT_DESCRIPTION_FIELD = "CAT_description";

    private static final String CAT_ISHAPPY_FIELD = "CAT_ishappy";

    /**
     * Tests the method in a normal case
     */
    public void testNormalCase() {
        fieldsService = serviceLocator.getFieldsService();

        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);

        String[] lFieldNames =
                fieldsService.getFieldNames(adminRoleToken, lType.getId());

        assertNotNull(
                "Method getFieldNames should not have returned a null value.",
                lFieldNames);

        List<String> lFieldNamesList = new ArrayList<String>();
        for (String lFieldName : lFieldNames) {
            lFieldNamesList.add(lFieldName);
        }

        assertTrue(CAT_NAME_FIELD + " is not in the field names list.",
                lFieldNamesList.contains(CAT_NAME_FIELD));
        assertTrue(CAT_REF_FIELD + " is not in the field names list.",
                lFieldNamesList.contains(CAT_REF_FIELD));
        assertTrue(CAT_BIRTHDATE_FIELD + " is not in the field names list.",
                lFieldNamesList.contains(CAT_BIRTHDATE_FIELD));
        assertTrue(CAT_COLOR_FIELD + " is not in the field names list.",
                lFieldNamesList.contains(CAT_COLOR_FIELD));
        assertTrue(CAT_FURLENGTH_FIELD + " is not in the field names list.",
                lFieldNamesList.contains(CAT_FURLENGTH_FIELD));
        assertTrue(CAT_PICTURE_FIELD + " is not in the field names list.",
                lFieldNamesList.contains(CAT_PICTURE_FIELD));
        assertTrue(CAT_DESCRIPTION_FIELD + " is not in the field names list.",
                lFieldNamesList.contains(CAT_DESCRIPTION_FIELD));
        assertTrue(CAT_ISHAPPY_FIELD + " is not in the field names list.",
                lFieldNamesList.contains(CAT_ISHAPPY_FIELD));
    }
}
