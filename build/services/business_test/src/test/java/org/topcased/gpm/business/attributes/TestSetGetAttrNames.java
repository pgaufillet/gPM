/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin),
 * Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.attributes;

import org.apache.commons.lang.ArrayUtils;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestSetGetAttrNames class
 * 
 * @author llatil
 */
public class TestSetGetAttrNames extends AttrTestBase {

    private final static String COUNTER_ATTR_NAME_2 = "AtomicCounter2";

    /**
     * testNormalCase
     */
    public void testNormalCase() {
        AttributeData[] lAttrs =
                new AttributeData[] {
                                     new AttributeData("attr1",
                                             new String[] { "val1", "val2" }),
                                     new AttributeData("attr2",
                                             new String[] { "val3" }) };

        getAttributesService().set(getContainerId(), lAttrs);
        String[] lAttrNames =
                getAttributesService().getAttrNames(getContainerId());

        assertTrue(lAttrNames.length >= 2);

        // Note: The method getAttrNames() returns the list sorted by name, so the
        // names position is known.
        assertEquals(lAttrNames[0], "attr1");
        assertEquals(lAttrNames[1], "attr2");

        // Remove one attribute and retest the names list.
        // Note: 'attr3' is useless here, just to test we can 'remove' a non existent attr.
        AttributeData[] lAttrs2 =
                new AttributeData[] { new AttributeData("attr2", null),
                                     new AttributeData("attr3", null) };

        getAttributesService().set(getContainerId(), lAttrs2);
        lAttrNames = getAttributesService().getAttrNames(getContainerId());

        assertTrue(lAttrNames.length >= 1);
        assertEquals(lAttrNames[0], "attr1");
    }

    /**
     * testEmptyListCase
     */
    public void testEmptyListCase() {
        String[] lAttrNames =
                getAttributesService().getAttrNames(getContainerId());

        assertTrue(lAttrNames.length >= 0);
    }

    //    /**
    //     * Test the atomic increment of an attribute value (normal case).
    //     */
    //    public void testAtomicIncrement() {
    //        SheetTypeData lSheetType =
    //                sheetService.getSheetTypeByName(adminRoleToken,
    //                        GpmTestValues.PROCESS_NAME,
    //                        GpmTestValues.SHEET_TYPE_SHEETTYPE1);
    //        String lContainerId = lSheetType.getId();
    //
    //        // Increment the counter attribute.
    //        // As this attribute does not exist yet, it must be set to the initial value.
    //        long lValueFirstIncrement =
    //                getAttributesService().atomicIncrement(adminRoleToken,
    //                        lContainerId, COUNTER_ATTR_NAME, COUNTER_INIT_VALUE);
    //
    //        // Increment the counter attribute again. The new value must be 'initial + 1'.
    //        long lValueSecondIncrement =
    //                getAttributesService().atomicIncrement(adminRoleToken,
    //                        lContainerId, COUNTER_ATTR_NAME, COUNTER_INIT_VALUE);
    //
    //        assertEquals(lValueFirstIncrement + 1, lValueSecondIncrement);
    //    }

    /**
     * Test the atomic test and set of an attribute value (normal case)
     */
    public void testAtomicTestAndSet() {

        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        GpmTestValues.PROCESS_NAME,
                        GpmTestValues.SHEET_TYPE_SHEETTYPE1,
                        CacheProperties.IMMUTABLE);
        String lContainerId = lType.getId();

        // Set Attribute with null oldValue
        String lValueBeforeUpdate = null;
        String lNewValue = "test1";
        String lOldValue =
                getAttributesService().atomicTestAndSet(adminRoleToken,
                        lContainerId, COUNTER_ATTR_NAME_2, lValueBeforeUpdate,
                        lNewValue);
        assertEquals(lValueBeforeUpdate, lOldValue);

        // Set Attribute with "normal" values
        lValueBeforeUpdate = lNewValue;
        lNewValue = "test2";
        lOldValue =
                getAttributesService().atomicTestAndSet(adminRoleToken,
                        lContainerId, COUNTER_ATTR_NAME_2, lValueBeforeUpdate,
                        lNewValue);
        assertEquals(lValueBeforeUpdate, lOldValue);

        // Set attribute with bad oldValue => No change
        lOldValue =
                getAttributesService().atomicTestAndSet(adminRoleToken,
                        lContainerId, COUNTER_ATTR_NAME_2, lValueBeforeUpdate,
                        "test2");
        assertFalse(lValueBeforeUpdate.equals(lOldValue));
        assertEquals(lNewValue, lOldValue);

        // Set Attribute with null newValue
        lValueBeforeUpdate = lNewValue;
        lNewValue = null;
        lOldValue =
                getAttributesService().atomicTestAndSet(adminRoleToken,
                        lContainerId, COUNTER_ATTR_NAME_2, lValueBeforeUpdate,
                        lNewValue);
        assertEquals(lValueBeforeUpdate, lOldValue);
    }

    private static final String INSTANCE_FILE_UPDATE =
            "attributes/TestUpdateAttributes.xml";

    private static final String PRODUCT_NAME_UPDATE = "testAttr";

    private static final int EXPECTED_ATTRIBUTES_SIZE = 2;

    private static final String EXPECTED_ATTRIBUTE_ATTR2 = "attr2_update";

    private static final String[] EXPECTED_ATTRIBUTE_ATTR2_VALUES =
            new String[] { "value21" };

    private static final String EXPECTED_ATTRIBUTE_ATTR1 = "attr1_update";

    private static final String[] EXPECTED_ATTRIBUTE_ATTR1_VALUES =
            new String[] { "value1", "value3" };

    /**
     * Update attributes
     */
    public void updateAttribute() {
        instantiate(getProcessName(), INSTANCE_FILE_UPDATE);
        ProductService lProductService = serviceLocator.getProductService();
        String lProductId =
                lProductService.getProductId(adminRoleToken,
                        PRODUCT_NAME_UPDATE);

        AttributeData[] lAttrs =
                new AttributeData[] {
                                     new AttributeData(
                                             EXPECTED_ATTRIBUTE_ATTR2,
                                             EXPECTED_ATTRIBUTE_ATTR2_VALUES),
                                     new AttributeData(
                                             EXPECTED_ATTRIBUTE_ATTR1,
                                             EXPECTED_ATTRIBUTE_ATTR1_VALUES) };
        getAttributesService().update(lProductId, lAttrs);

        AttributeData[] lAttributes = getAttributesService().getAll(lProductId);
        assertEquals(EXPECTED_ATTRIBUTES_SIZE, lAttributes.length);

        AttributeData lAttr1 = lAttributes[0];
        assertEquals(EXPECTED_ATTRIBUTE_ATTR1, lAttr1.getName());
        assertTrue(ArrayUtils.isEquals(EXPECTED_ATTRIBUTE_ATTR1_VALUES,
                lAttr1.getValues()));

        AttributeData lAttr2 = lAttributes[1];
        assertEquals(EXPECTED_ATTRIBUTE_ATTR2, lAttr2.getName());
        assertTrue(ArrayUtils.isEquals(EXPECTED_ATTRIBUTE_ATTR2_VALUES,
                lAttr2.getValues()));
    }
}
