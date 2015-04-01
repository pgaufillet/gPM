/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Vincent Hemery (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.service.SheetData;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * TestTransformCacheableSheet. Test getting a serialization sheet data from a
 * cacheable sheet, itself constructed from a service sheet data. This scenario
 * happens when exporting modified sheets different from those in database (in
 * case of concurrent saving)
 * 
 * @author vhemery
 */
public class TestTransformCacheableSheet extends
        AbstractBusinessServiceTestCase {

    /** The Type of the sheet. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_DOG;

    /**
     * Tests the method in normal conditions
     */
    public void testNormalCase() {

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lId = lSheetSummary.get(0).getId();

        // Retrieving the sheet
        SheetData lSheetData = sheetService.getSheetByKey(adminRoleToken, lId);

        // Transform into a cacheable sheet
        CacheableSheet lCacheable =
                serviceLocator.getDataTransformationService().getCacheableSheetFromSheetData(
                        adminRoleToken, lSheetData);

        // creation of the sheetData that will be serialized
        org.topcased.gpm.business.serialization.data.SheetData lSerialSheetData =
                new org.topcased.gpm.business.serialization.data.SheetData();
        lCacheable.marshal(lSerialSheetData);

        // compare with original
        org.topcased.gpm.business.serialization.data.SheetData lOriginalSerialSheet =
                sheetService.getSerializableSheet(adminRoleToken, lId);

        assertTrue("Sheet has been altered during cacheable transformation",
                compare(lOriginalSerialSheet, lSerialSheetData));
    }

    private boolean compare(
            org.topcased.gpm.business.serialization.data.SheetData pSerialSheet1,
            org.topcased.gpm.business.serialization.data.SheetData pSerialSheet2) {
        if (!pSerialSheet1.getReference().equals(pSerialSheet2.getReference())) {
            return false;
        }

        // compare field value data : only field with null values should differ
        List<FieldValueData> lValuesD1 = pSerialSheet1.getFieldValues();
        List<FieldValueData> lValuesD2 = pSerialSheet2.getFieldValues();
        for (FieldValueData lValueD1 : lValuesD1) {
            if (!lValuesD2.remove(lValueD1) && lValueD1.getValue() != null) {
                return false;
            }
        }
        for (FieldValueData lValueD2 : lValuesD2) {
            if (lValueD2.getValue() != null) {
                return false;
            }
        }
        return true;
    }
}
