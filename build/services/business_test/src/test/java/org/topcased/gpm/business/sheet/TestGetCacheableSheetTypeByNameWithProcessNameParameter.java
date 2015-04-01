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

import java.util.Iterator;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * This class tests the getCacheableSheetTypeByName method from the SheetService
 * implementation.
 * 
 * @author mmennad
 */
public class TestGetCacheableSheetTypeByNameWithProcessNameParameter extends
        AbstractBusinessServiceTestCase {

    /**
     * Test the method with correct parameters
     */
    public void testNormalCase() {

        List<CacheableSheetType> lListSheetTypes =
                sheetService.getSheetTypes(adminRoleToken,
                        GpmTestValues.PROCESS_NAME, CacheProperties.IMMUTABLE);
        //Check
        CacheableSheetType lCacheableSheetTypeByName =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), GpmTestValues.SHEET_TYPE_CAT,
                        CacheProperties.MUTABLE);
        assertNotNull(lCacheableSheetTypeByName);

        boolean lFound = false;
        for (Iterator<CacheableSheetType> lIterator =
                lListSheetTypes.iterator(); lIterator.hasNext();) {
            CacheableSheetType lCacheableSheetType =
                    (CacheableSheetType) lIterator.next();

            if (lCacheableSheetType.getId().equals(lCacheableSheetTypeByName.getId())) {
                lFound = true;
                break;
            }

        }
        assertTrue(lFound);

    }

    /**
     * Test the method with a wrong sheet type name parameters
     */
    public void testWrongNameCase() {

        //Check
        CacheableSheetType lCacheableSheetTypeByName = null;
        try {
            lCacheableSheetTypeByName =
                    sheetService.getCacheableSheetTypeByName(adminRoleToken,
                            getProcessName(), INVALID_CONTAINER_ID,
                            CacheProperties.MUTABLE);
            assertNull(lCacheableSheetTypeByName);
        }
        catch (InvalidNameException e) {
            assertTrue("InvalidNameException", true);
        }
        finally {
            assertNull(lCacheableSheetTypeByName);
        }

    }

}