/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Mimoun Mennad (ATOS), Pierre Hubert Tsaan
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.util.Arrays;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * This class tests the getSheetTypes(String, String, CacheProperties) method
 * from the SheetService implementation.
 * 
 * @author mmennad
 */
public class TestGetSheetTypes extends AbstractBusinessServiceTestCase {

    /**
     * Test the method with correct parameters
     */
    public void testNormalCase() {

        sheetService = serviceLocator.getSheetService();

        CacheProperties lCacheProperties = new CacheProperties(false);
        List<CacheableSheetType> lListSheetTypes =
                sheetService.getSheetTypes(adminRoleToken,
                        GpmTestValues.PROCESS_NAME, lCacheProperties);

        // check list was found
        assertNotNull(lListSheetTypes);

        List<String> lDefinedSheetTypes =
                Arrays.asList(GpmTestValues.SHEET_TYPE_NAMES);
        // check size
        assertEquals(lDefinedSheetTypes.size(), lListSheetTypes.size());

        // test content
        for (CacheableSheetType lCacheableSheetType : lListSheetTypes) {
            final String lName = lCacheableSheetType.getName();
            assertTrue(lDefinedSheetTypes.contains(lName));
        }
    }

    /**
     * Test the method if the process name is not correct
     */
    public void testInvalidProcessNameCase() {

        sheetService = serviceLocator.getSheetService();

        CacheProperties lCacheProperties = new CacheProperties(false);
        List<CacheableSheetType> lListSheetTypes =
                sheetService.getSheetTypes(adminRoleToken,
                        GpmTestValues.PROCESS_INVALID_NAME, lCacheProperties);

        // check size - empty list
        assertEquals(0, lListSheetTypes.size());
    }

    /**
     * Test the method if the role is not correct
     */
    public void testInvalidRoleCase() {

        sheetService = serviceLocator.getSheetService();

        CacheProperties lCacheProperties = new CacheProperties(false);
        List<CacheableSheetType> lListSheetTypes =
                sheetService.getSheetTypes(GpmTestValues.USER_NOROLE,
                        GpmTestValues.PROCESS_INVALID_NAME, lCacheProperties);

        // check size - empty list
        assertEquals(0, lListSheetTypes.size());
    }
}
