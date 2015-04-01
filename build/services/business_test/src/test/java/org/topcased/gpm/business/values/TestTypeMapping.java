/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.values;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.extensions.values.MappingExtensionPointTest;
import org.topcased.gpm.business.serialization.data.AttachedFieldValueData;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.impl.SheetServiceImpl;
import org.topcased.gpm.util.bean.CacheProperties;
import org.topcased.gpm.util.bean.GpmPair;
import org.topcased.gpm.util.iterator.GpmPairIterator;

/**
 * Tests the type mapping used on the Values Service.
 * 
 * @author tpanuel
 */
public class TestTypeMapping extends AbstractBusinessServiceTestCase {
    private final static String FIELD_MAPPING_XML =
            "values/TestFieldMapping.xml";

    private final static String VALUE_MAPPING_XML =
            "values/TestValueMapping.xml";;

    private final static String EXTENSION_POINT_EXECUTION_ON_MAPPING_XML =
            "values/TestExtensionPointExecutionOnMapping.xml";

    private final static String PRODUCT_NAME =
            GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME;

    private final static String SHEET_TYPE_NAME =
            GpmTestValues.SHEET_TYPE_SHEETTYPE1;

    private final static String SHEET_REFERENCE = "Sheet24:7";

    private SheetServiceImpl sheetService =
            (SheetServiceImpl) ContextLocator.getContext().getBean(
                    "sheetServiceImpl");

    /**
     * Test the mapping between fields.
     */
    public void testFieldMapping() {
        instantiate(getProcessName(), FIELD_MAPPING_XML);

        final CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        SHEET_TYPE_NAME, CacheProperties.IMMUTABLE);
        final String lSourceId =
                sheetService.getSheetIdByReference(getProcessName(),
                        PRODUCT_NAME, SHEET_REFERENCE);
        final CacheableSheet lSource =
                sheetService.getCacheableSheet(adminRoleToken, lSourceId,
                        CacheProperties.IMMUTABLE);
        final CacheableSheet lDestination =
                sheetService.getCacheableSheetModel(adminRoleToken, lType,
                        PRODUCT_NAME, null);

        sheetService.initializeSheetValues(adminRoleToken, lType, lSource,
                lType, lDestination, null);

        assertValueEqual(lSource, "SHEETTYPE1_ref", lDestination,
                "SHEETTYPE1_simpleString");
        assertValueEqual(lSource, "SHEETTYPE1_simpleStringMul", lDestination,
                "SHEETTYPE1_simpleChoiceStringMul");
        assertValueEqual(lSource, "SHEETTYPE1_currentDate", lDestination,
                "SHEETTYPE1_simpleDate");
        assertValueEqual(lSource, "SHEETTYPE1_simpleDateMul", lDestination,
                "SHEETTYPE1_simpleDateMul");
        assertValueEqual(lSource, "SHEETTYPE1_attached", lDestination,
                "SHEETTYPE1_attached");
        assertValueEqual(lSource, "SHEETTYPE1_attachedMul", lDestination,
                "SHEETTYPE1_attachedMul");
        assertValueEqual(lSource, "SHEETTYPE1_choice", lDestination,
                "SHEETTYPE1_choice");
        assertValueEqual(lSource, "SHEETTYPE1_choiceMul", lDestination,
                "SHEETTYPE1_choiceMul");
        assertValueEqual(lSource, "SHEETTYPE1_multiple1", lDestination,
                "SHEETTYPE1_multiple1");
        assertValueEqual(lSource, "SHEETTYPE1_multiple1M", lDestination,
                "SHEETTYPE1_multiple1M");
    }

    private void assertValueEqual(final CacheableSheet pSheet1,
            final String pFieldLabel1, final CacheableSheet pSheet2,
            final String pFieldLabel2) {
        assertValueEqual(pSheet1.getValue(pFieldLabel1), pFieldLabel1,
                pSheet2.getValue(pFieldLabel2), pFieldLabel2);
    }

    @SuppressWarnings("unchecked")
    private void assertValueEqual(final Object pValue1,
            final String pFieldLabel1, final Object pValue2,
            final String pFieldLabel2) {
        final String lErrorMess =
                "The mapping between field " + pFieldLabel1 + " and field "
                        + pFieldLabel2 + " has failled.";

        if (pValue1 == null) {
            assertNull(lErrorMess, pValue2);
        }
        else {
            assertNotNull(lErrorMess, pValue2);
            if (pValue1 instanceof AttachedFieldValueData) {
                assertTrue(lErrorMess,
                        pValue2 instanceof AttachedFieldValueData);
                final AttachedFieldValueData lFile1 =
                        (AttachedFieldValueData) pValue1;
                final AttachedFieldValueData lFile2 =
                        (AttachedFieldValueData) pValue2;
                final byte[] lFileContent1 =
                        sheetService.getAttachedFileContent(adminRoleToken,
                                lFile1.getId());
                final byte[] lFileContent2 = lFile2.getNewContent();

                assertEquals(lErrorMess, lFile1.getName(), lFile2.getName());
                assertEquals(lErrorMess, lFile1.getMimeType(),
                        lFile2.getMimeType());
                assertEquals(lErrorMess, lFileContent1.length,
                        lFileContent2.length);
                for (int i = 0; i < lFileContent1.length; i++) {
                    assertEquals(lErrorMess, lFileContent1[i], lFileContent2[i]);
                }
            }
            else if (pValue1 instanceof FieldValueData) {
                assertTrue(lErrorMess, pValue2 instanceof FieldValueData);
                assertEquals(lErrorMess, ((FieldValueData) pValue1).getValue(),
                        ((FieldValueData) pValue2).getValue());
            }
            else if (pValue1 instanceof List) {
                final GpmPairIterator<Object, Object> lIterator =
                        new GpmPairIterator<Object, Object>(pValue1, pValue2);

                while (lIterator.hasNext()) {
                    final GpmPair<Object, Object> lValue = lIterator.next();

                    assertNotNull(lErrorMess, lValue.getFirst());
                    assertNotNull(lErrorMess, lValue.getSecond());
                    assertValueEqual(lValue.getFirst(), pFieldLabel1,
                            lValue.getSecond(), pFieldLabel2);
                }
            }
            else if (pValue1 instanceof Map) {
                assertTrue(lErrorMess, pValue2 instanceof Map);
                final Map<String, Object> lMap1 = (Map<String, Object>) pValue1;
                final Map<String, Object> lMap2 = (Map<String, Object>) pValue2;

                for (String lSubField : lMap1.keySet()) {
                    assertValueEqual(lMap1.get(lSubField), pFieldLabel1,
                            lMap2.get(lSubField), pFieldLabel2);
                }
            }
            else {
                fail(lErrorMess);
            }
        }
    }

    /**
     * Test the mapping between values.
     */
    public void testValueMapping() {
        instantiate(getProcessName(), VALUE_MAPPING_XML);

        final CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        SHEET_TYPE_NAME, CacheProperties.IMMUTABLE);
        final String lSourceId =
                sheetService.getSheetIdByReference(getProcessName(),
                        PRODUCT_NAME, SHEET_REFERENCE);
        final CacheableSheet lSource =
                sheetService.getCacheableSheet(adminRoleToken, lSourceId,
                        CacheProperties.IMMUTABLE);
        final CacheableSheet lDestination =
                sheetService.getCacheableSheetModel(adminRoleToken, lType,
                        PRODUCT_NAME, null);

        sheetService.initializeSheetValues(adminRoleToken, lType, lSource,
                lType, lDestination, null);

        checkFieldValues(lDestination, "SHEETTYPE1_simpleString",
                new String[] { "Sheet25" });
        checkFieldValues(lDestination, "SHEETTYPE1_simpleChoiceStringMul",
                new String[] { "C", "D" });
        checkFieldValues(lDestination, "SHEETTYPE1_choice",
                new String[] { GpmTestValues.CATEGORY_COLOR_VALUE_RED });
        checkFieldValues(lDestination, "SHEETTYPE1_choiceMul",
                new String[] { GpmTestValues.CATEGORY_COLOR_VALUE_RED,
                              GpmTestValues.CATEGORY_COLOR_VALUE_GREEN });
    }

    private void checkFieldValues(final CacheableSheet pSheet,
            final String pFieldName, final String[] pExpectedValues) {
        final Object lValues = pSheet.getValue(pFieldName);
        final GpmPairIterator<FieldValueData, String> lValueIterator =
                new GpmPairIterator<FieldValueData, String>(lValues,
                        Arrays.asList(pExpectedValues));
        final String lErrorMessage =
                "Wrong value mapping on field " + pFieldName + ".";

        while (lValueIterator.hasNext()) {
            final GpmPair<FieldValueData, String> lCurrentValue =
                    lValueIterator.next();

            assertNotNull(lErrorMessage, lCurrentValue.getFirst());
            assertNotNull(lErrorMessage, lCurrentValue.getSecond());
            assertEquals(lErrorMessage, lCurrentValue.getFirst().getValue(),
                    lCurrentValue.getSecond());
        }
    }

    /**
     * Test the execution of extension point defined on type mapping.
     */
    public void testExtensionPointExecutionOnMapping() {
        instantiate(getProcessName(), EXTENSION_POINT_EXECUTION_ON_MAPPING_XML);

        final CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        SHEET_TYPE_NAME, CacheProperties.IMMUTABLE);
        final String lSourceId =
                sheetService.getSheetIdByReference(getProcessName(),
                        PRODUCT_NAME, SHEET_REFERENCE);
        final CacheableSheet lSource =
                sheetService.getCacheableSheet(adminRoleToken, lSourceId,
                        CacheProperties.IMMUTABLE);
        final CacheableSheet lDestination =
                sheetService.getCacheableSheetModel(adminRoleToken, lType,
                        PRODUCT_NAME, null);

        sheetService.initializeSheetValues(adminRoleToken, lType, lSource,
                lType, lDestination, null);

        assertNotNull("Extension point not executed on mapping.",
                lDestination.getValue("SHEETTYPE1_simpleString"));
        assertEquals(
                "Extension point not executed on mapping.",
                ((FieldValueData) lDestination.getValue("SHEETTYPE1_simpleString")).getValue(),
                MappingExtensionPointTest.NEW_VALUE);
    }
}