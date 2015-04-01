/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Mimoun Mennad (ATOS)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * This class tests the getCacheableSheetType method from the SheetService
 * implementation.
 * 
 * @author mmennad
 */
public class TestGetCacheableSheetType extends AbstractBusinessServiceTestCase {

    /**
     * Test the method with correct parameters
     */
    public void testNormalCase() {
        sheetService = serviceLocator.getSheetService();

        List<CacheableSheetType> lListSheetTypes =
                sheetService.getSheetTypes(adminRoleToken,
                        GpmTestValues.PROCESS_NAME, CacheProperties.IMMUTABLE);

        //Check
        CacheableSheetType lCacheableSheetType =
                sheetService.getCacheableSheetType(adminRoleToken,
                        lListSheetTypes.get(0).getId(),
                        CacheProperties.IMMUTABLE);
        assertNotNull(lCacheableSheetType);
        assertEquals(lListSheetTypes.get(0).getId(),
                lCacheableSheetType.getId());
        assertEquals(lListSheetTypes.get(0).getBusinessProcessName(),
                lCacheableSheetType.getBusinessProcessName());
        assertEquals(lListSheetTypes.get(0).getDescription(),
                lCacheableSheetType.getDescription());
        assertEquals(lListSheetTypes.get(0).getInitialStateName(),
                lCacheableSheetType.getInitialStateName());
        assertEquals(lListSheetTypes.get(0).getName(),
                lCacheableSheetType.getName());
    }

    /**
     * Test the method with a wrong sheet type id
     */
    public void testWrongIdCase() {
        sheetService = serviceLocator.getSheetService();
        CacheableSheetType lCacheableSheetType = null;
        try {

            lCacheableSheetType =
                    sheetService.getCacheableSheetType(adminRoleToken,
                            INVALID_CONTAINER_ID, CacheProperties.IMMUTABLE);
        }
        catch (InvalidIdentifierException e) {
            assertTrue("InvalidIdentifierException", true);
        }
        finally {
            assertNull(lCacheableSheetType);
        }

    }

}