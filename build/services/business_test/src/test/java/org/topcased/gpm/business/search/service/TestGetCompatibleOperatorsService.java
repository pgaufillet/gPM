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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.FieldTypes;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.criterias.impl.VirtualFieldData;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>getCompatibleOperator<CODE> of the Search Service.
 * 
 * @author ahaugomm
 */
public class TestGetCompatibleOperatorsService extends
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

        Iterator<UsableFieldData> lIterator =
                lUsableFieldDatas.values().iterator();
        assertTrue("No Usable fields found", lIterator.hasNext());

        UsableFieldData lUsableFieldData = lIterator.next();

        assertNotNull("Usable Field data is null", lUsableFieldData);

        Collection<String> lOperators =
                searchService.getCompatibleOperators(lUsableFieldData);

        if (lUsableFieldData.getFieldType().equals(FieldTypes.ATTACHED_FIELD)
                || lUsableFieldData.getFieldType().equals(
                        FieldTypes.SIMPLE_STRING_FIELD)) {
            assertTrue("operator EQ not present.",
                    lOperators.contains(Operators.EQ));
            assertTrue("operator LIKE not present.",
                    lOperators.contains(Operators.LIKE));
            assertTrue("operator NEQ not present.",
                    lOperators.contains(Operators.NEQ));
        }
        else if (lUsableFieldData.getFieldType().equals(
                FieldTypes.SIMPLE_BOOLEAN_FIELD)
                || lUsableFieldData.getFieldType().equals(
                        FieldTypes.CHOICE_FIELD)) {
            assertTrue("operator EQ not present.",
                    lOperators.contains(Operators.EQ));
            assertTrue("operator NEQ not present.",
                    lOperators.contains(Operators.NEQ));
        }
        else if (lUsableFieldData.getFieldType().equals(
                FieldTypes.SIMPLE_DATE_FIELD)
                || lUsableFieldData.getFieldType().equals(
                        FieldTypes.SIMPLE_REAL_FIELD)
                || lUsableFieldData.getFieldType().equals(
                        FieldTypes.SIMPLE_INTEGER_FIELD)) {
            assertTrue("operator EQ not present.",
                    lOperators.contains(Operators.EQ));
            assertTrue("operator GT not present.",
                    lOperators.contains(Operators.GT));
            assertTrue("operator GE not present.",
                    lOperators.contains(Operators.GE));
            assertTrue("operator LT not present.",
                    lOperators.contains(Operators.LT));
            assertTrue("operator LE not present.",
                    lOperators.contains(Operators.LE));
            assertTrue("operator NEQ not present.",
                    lOperators.contains(Operators.NEQ));
        }
        else if (lUsableFieldData instanceof VirtualFieldData) {
            VirtualFieldData lVirtualFieldData =
                    (VirtualFieldData) lUsableFieldData;

            if (lVirtualFieldData.getVirtualFieldType().equals(
                    FieldTypes.ATTACHED_FIELD)
                    || lVirtualFieldData.getVirtualFieldType().equals(
                            FieldTypes.SIMPLE_STRING_FIELD)) {
                assertTrue("operator EQ not present.",
                        lOperators.contains(Operators.EQ));
                assertTrue("operator LIKE not present.",
                        lOperators.contains(Operators.LIKE));
                assertTrue("operator NEQ not present.",
                        lOperators.contains(Operators.NEQ));
            }
            else if (lVirtualFieldData.getVirtualFieldType().equals(
                    FieldTypes.SIMPLE_BOOLEAN_FIELD)
                    || lVirtualFieldData.getVirtualFieldType().equals(
                            FieldTypes.CHOICE_FIELD)) {
                assertTrue("operator EQ not present.",
                        lOperators.contains(Operators.EQ));
                assertTrue("operator NEQ not present.",
                        lOperators.contains(Operators.NEQ));
            }
            else if (lVirtualFieldData.getVirtualFieldType().equals(
                    FieldTypes.SIMPLE_DATE_FIELD)
                    || lVirtualFieldData.getVirtualFieldType().equals(
                            FieldTypes.SIMPLE_REAL_FIELD)
                    || lVirtualFieldData.getVirtualFieldType().equals(
                            FieldTypes.SIMPLE_INTEGER_FIELD)) {
                assertTrue("operator EQ not present.",
                        lOperators.contains(Operators.EQ));
                assertTrue("operator GT not present.",
                        lOperators.contains(Operators.GT));
                assertTrue("operator GE not present.",
                        lOperators.contains(Operators.GE));
                assertTrue("operator LT not present.",
                        lOperators.contains(Operators.LT));
                assertTrue("operator LE not present.",
                        lOperators.contains(Operators.LE));
                assertTrue("operator NEQ not present.",
                        lOperators.contains(Operators.NEQ));
            }

        }
    }

}
