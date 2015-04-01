/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.attributes;

import org.apache.commons.lang.ArrayUtils;

/**
 * TestGlobalAttributesTestCase
 * 
 * @author ahaugomm
 */
public class TestGlobalAttributes extends AttrTestBase {

    final static String ATTR1_NAME = "global_attr1";

    final static String ATTR2_NAME = "global_attr2";

    final static String[] ATTR1_VALUES = new String[] { "val1", "val2" };

    final static String[] ATTR2_VALUES = new String[] { "val3" };

    /**
     * Tests the method in normal conditions
     */
    public void testNormalCase() {
        // Create global attributes:
        // attr1 with values val1 and val2
        // attr2 with value val3
        AttributeData[] lAttrs =
                new AttributeData[] {
                                     new AttributeData(ATTR1_NAME, ATTR1_VALUES),
                                     new AttributeData(ATTR2_NAME, ATTR2_VALUES) };
        getAttributesService().setGlobalAttributes(adminRoleToken, lAttrs);

        // Find names for global attributes
        String[] lNamesFound = getAttributesService().getGlobalAttrNames();
        assertTrue("Less than 2 global attributes", lNamesFound.length >= 2);

        assertTrue(ATTR1_NAME + " not found.", ArrayUtils.contains(lNamesFound,
                ATTR1_NAME));
        assertTrue(ATTR2_NAME + " not found.", ArrayUtils.contains(lNamesFound,
                ATTR2_NAME));

        // Find values for a global attribute
        AttributeData[] lAttrDatas =
                getAttributesService().getGlobalAttributes(
                        new String[] { ATTR1_NAME, ATTR2_NAME });
        assertTrue("Invalid results count", lAttrDatas.length == 2);

        assertEquals("Invalid name", ATTR1_NAME, lAttrDatas[0].getName());
        assertEquals("Invalid name", ATTR2_NAME, lAttrDatas[1].getName());

        assertEqualsOrdered(ATTR1_VALUES, lAttrDatas[0].getValues());
        assertEqualsOrdered(ATTR2_VALUES, lAttrDatas[1].getValues());
    }
}
