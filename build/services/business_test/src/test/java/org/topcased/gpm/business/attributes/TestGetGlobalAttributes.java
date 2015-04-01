/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Mimoun Mennad (Atos)
 ******************************************************************/
package org.topcased.gpm.business.attributes;

import java.util.HashMap;
import java.util.Map;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.attributes.service.AttributesService;

/**
 * This class tests the getGlobalAttributes(String[]) method from the
 * AttributesService implementation.
 * 
 * @author mmennad
 */
public class TestGetGlobalAttributes extends AbstractBusinessServiceTestCase {

    // These attributes always exist, whatever attributes have been defined in the instance creation
    private static final String[][] GENERAL_STANDARD_ATTRIBUTES =
            { { "authentication", "internal" },
             { "autolocking", "READ_WRITE" }, { "filterFieldsMaxDepth", "3" },
             {"loginCaseSensitive", "true" }};

    // Same as above but wrong
    private static final String[][] GENERAL_STANDARD_ATTRIBUTES2 =
            { { "authenticahtion", "internal" },
             { "autolockyuoing", "READ_WRITE" }, 
             { "filterFields", "3" }};

    // New attributes: Test cases where attribute length is 0, 1 and 3.
    private static final Map<String, String[]> GENERAL_ATTRIBUTES =
            new HashMap<String, String[]>();
    static {
        GENERAL_ATTRIBUTES.put("hasCats", new String[] { "true" });
        GENERAL_ATTRIBUTES.put("empty", new String[] {});
        GENERAL_ATTRIBUTES.put("Colors",
                new String[] { "Red", "Green", "Blue" });
    }

    /**
     * Tests the method with correct Attributes Names
     */
    public void testNormalCase() {

        AttributesService lAttributesService =
                serviceLocator.getAttributesService();

        // Set up attributes
        for (Map.Entry<String, String[]> lEntry : GENERAL_ATTRIBUTES.entrySet()) {
            AttributeData[] lDatas = new AttributeData[1];
            lDatas[0] = new AttributeData(lEntry.getKey(), lEntry.getValue());
            lAttributesService.setGlobalAttributes(adminRoleToken, lDatas);
        }

        // Get all existing attributes values
        String[] lAttrNames = lAttributesService.getGlobalAttrNames();
        AttributeData[] lGlobalAttributes =
                lAttributesService.getGlobalAttributes(lAttrNames);

        // Create the map of all expected attributes
        Map<String, String[]> lExpectedAttributes =
                new HashMap<String, String[]>();
        for (String[] lData : GENERAL_STANDARD_ATTRIBUTES) {
            lExpectedAttributes.put(lData[0], new String[] { lData[1] });
        }
        for (Map.Entry<String, String[]> lData : GENERAL_ATTRIBUTES.entrySet()) {
            if (lData.getValue().length != 0) { // Empty entries are discarded
                lExpectedAttributes.put(lData.getKey(), lData.getValue());
            }
        }

        // Check result
        assertNotNull(lGlobalAttributes);
        assertEquals(lExpectedAttributes.size(), lGlobalAttributes.length);

        for (AttributeData lGlobalAttribute : lGlobalAttributes) {
            String[] lValues =
                    lExpectedAttributes.get(lGlobalAttribute.getName());
            assertNotNull(lValues);
            assertEquals(lValues.length, lGlobalAttribute.getValues().length);
            assertEquals(lValues[0].toString(),
                    lGlobalAttribute.getValues()[0].toString());
        }
    }

    /**
     * Tests the method with no names
     */

    public void testNoParameterCase() {

        AttributesService lAttributesService =
                serviceLocator.getAttributesService();

        AttributeData[] lGlobalAttributes =
                lAttributesService.getGlobalAttributes(new String[0]);

        assertTrue(lGlobalAttributes != null && lGlobalAttributes.length == 0);
    }

    /**
     * Tests the method with wrong names
     */
    public void testAbnormalCase() {

        // Set up attributes
        AttributesService lAttributesService =
                serviceLocator.getAttributesService();

        // Get all existing attributes values
        String[] lAttrNames = lAttributesService.getGlobalAttrNames();
        AttributeData[] lGlobalAttributes =
                lAttributesService.getGlobalAttributes(lAttrNames);

        // Create the map of all expected attributes
        Map<String, String[]> lExpectedAttributes =
                new HashMap<String, String[]>();
        for (String[] lData : GENERAL_STANDARD_ATTRIBUTES2) {
            lExpectedAttributes.put(lData[0], new String[] { lData[1] });

            assertNotNull(lGlobalAttributes);

            assertNotSame(lExpectedAttributes, lGlobalAttributes);

            //Check
            for (AttributeData lGlobalAttribute : lGlobalAttributes) {
                String[] lValues =
                        lExpectedAttributes.get(lGlobalAttribute.getName());

                assertNull(lValues);

            }

        }
    }
}
