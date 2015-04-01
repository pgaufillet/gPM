/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.serialization.data.SheetType;

/**
 * TestGetCreatableSerializableSheetTypesService
 * 
 * @author mfranche
 */
public class TestGetCreatableSerializableSheetTypesService extends
        AbstractBusinessServiceTestCase {

    /** Cat type */
    private static final String CAT_TYPE = GpmTestValues.SHEET_TYPE_CAT;

    /** Dog type */
    private static final String DOG_TYPE = GpmTestValues.SHEET_TYPE_DOG;

    /** Mouse type */
    private static final String MOUSE_TYPE = GpmTestValues.SHEET_TYPE_MOUSE;

    /** SheetType1 type */
    private static final String SHEET_TYPE1_TYPE =
            GpmTestValues.SHEET_TYPE_SHEETTYPE1;

    /** SimpleSheetType1 type */
    private static final String SIMPLE_SHEET_TYPE1 =
            GpmTestValues.SHEET_TYPE_SIMPLE_SHEET_TYPE1;

    /** SimpleSheetType2 type */
    private static final String SIMPLE_SHEET_TYPE2 =
            GpmTestValues.SHEET_TYPE_SIMPLE_SHEET_TYPE2;

    /**
     * Tests the method in a normal case.
     */
    public void testNormalCase() {
        sheetService = serviceLocator.getSheetService();

        List<SheetType> lSheetTypeList =
                sheetService.getCreatableSerializableSheetTypes(adminRoleToken,
                        getProcessName());

        assertNotNull("The sheet type list should not be null.", lSheetTypeList);

        List<String> lSheetTypeNames = new ArrayList<String>();
        for (SheetType lSheetType : lSheetTypeList) {
            lSheetTypeNames.add(lSheetType.getName());
        }

        assertTrue(CAT_TYPE + " should be in the list.",
                lSheetTypeNames.contains(CAT_TYPE));
        assertTrue(DOG_TYPE + " should be in the list.",
                lSheetTypeNames.contains(DOG_TYPE));
        assertTrue(MOUSE_TYPE + " should be in the list.",
                lSheetTypeNames.contains(MOUSE_TYPE));
        assertTrue(SHEET_TYPE1_TYPE + " should be in the list.",
                lSheetTypeNames.contains(SHEET_TYPE1_TYPE));
        assertTrue(SIMPLE_SHEET_TYPE1 + " should be in the list.",
                lSheetTypeNames.contains(SIMPLE_SHEET_TYPE1));
        assertTrue(SIMPLE_SHEET_TYPE2 + " should be in the list.",
                lSheetTypeNames.contains(SIMPLE_SHEET_TYPE2));
    }

}
