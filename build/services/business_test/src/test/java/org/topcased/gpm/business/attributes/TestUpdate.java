/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ** Contributors: Laurent Latil (Atos),
 * Michael Kargbo (Atos),
 * Mimoun Mennad (Atos)
 ******************************************************************/
package org.topcased.gpm.business.attributes;

import org.apache.commons.lang.ArrayUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.product.service.ProductService;

/**
 * This class tests the update(String, AttributeData[]) method from
 * AttributesService implementation.
 * 
 * @author mmennad
 */

public class TestUpdate extends AbstractBusinessServiceTestCase {

    private static final String INSTANCE_FILE_UPDATE =
            "attributes/TestUpdateAttributes.xml";

    private static final String PRODUCT_NAME_UPDATE = "testAttr";

    private static final int EXPECTED_ATTRIBUTES_SIZE_BEFORE = 1;

    private static final int EXPECTED_ATTRIBUTES_SIZE_AFTER = 2;

    private static final String EXPECTED_ATTRIBUTE_ATTR2 = "attr2_update";

    private static final String[] EXPECTED_ATTRIBUTE_ATTR2_VALUES =
            new String[] { "value21" };

    private static final String EXPECTED_ATTRIBUTE_ATTR1 = "attr1_update";

    private static final String[] EXPECTED_ATTRIBUTE_ATTR1_VALUES =
            new String[] { "value1", "value3" };

    /**
     * Tests the method with correct parameters
     */

    public void testNormalCase() {

        AttributesService lAttributesService =
                serviceLocator.getAttributesService();

        instantiate(getProcessName(), INSTANCE_FILE_UPDATE);
        ProductService lProductService = serviceLocator.getProductService();

        //Setup
        String lProductId =
                lProductService.getProductId(adminRoleToken,
                        PRODUCT_NAME_UPDATE);

        //Check before
        AttributeData[] lAttributes = lAttributesService.getAll(lProductId);
        assertEquals(EXPECTED_ATTRIBUTES_SIZE_BEFORE, lAttributes.length);
        AttributeData[] lAttrs =
                new AttributeData[] {
                                     new AttributeData(
                                             EXPECTED_ATTRIBUTE_ATTR2,
                                             EXPECTED_ATTRIBUTE_ATTR2_VALUES),
                                     new AttributeData(
                                             EXPECTED_ATTRIBUTE_ATTR1,
                                             EXPECTED_ATTRIBUTE_ATTR1_VALUES) };
        lAttributesService.update(lProductId, lAttrs);

        assertNotSame(lAttributes, lAttrs);

        //Check after
        lAttributes = lAttributesService.getAll(lProductId);
        assertEquals(EXPECTED_ATTRIBUTES_SIZE_AFTER, lAttributes.length);

        AttributeData lAttr1 = lAttributes[0];
        assertEquals(EXPECTED_ATTRIBUTE_ATTR1, lAttr1.getName());
        assertTrue(ArrayUtils.isEquals(EXPECTED_ATTRIBUTE_ATTR1_VALUES,
                lAttr1.getValues()));

        AttributeData lAttr2 = lAttributes[1];
        assertEquals(EXPECTED_ATTRIBUTE_ATTR2, lAttr2.getName());
        assertTrue(ArrayUtils.isEquals(EXPECTED_ATTRIBUTE_ATTR2_VALUES,
                lAttr2.getValues()));

    }

    /**
     * Tests the method with a wrong product id
     */
    public void testWrongIdCase() {

        AttributesService lAttributesService =
                serviceLocator.getAttributesService();

        AttributeData[] lAttrs =
                new AttributeData[] {
                                     new AttributeData(
                                             EXPECTED_ATTRIBUTE_ATTR2,
                                             EXPECTED_ATTRIBUTE_ATTR2_VALUES),
                                     new AttributeData(
                                             EXPECTED_ATTRIBUTE_ATTR1,
                                             EXPECTED_ATTRIBUTE_ATTR1_VALUES) };
        //Check
        try {
            lAttributesService.update(INVALID_CONTAINER_ID, lAttrs);

            AttributeData[] lAttributes =
                    lAttributesService.getAll(INVALID_CONTAINER_ID);
            assertEquals(EXPECTED_ATTRIBUTES_SIZE_AFTER, lAttributes.length);

            AttributeData lAttr1 = lAttributes[0];
            assertEquals(EXPECTED_ATTRIBUTE_ATTR1, lAttr1.getName());
            assertTrue(ArrayUtils.isEquals(EXPECTED_ATTRIBUTE_ATTR1_VALUES,
                    lAttr1.getValues()));

            AttributeData lAttr2 = lAttributes[1];
            assertEquals(EXPECTED_ATTRIBUTE_ATTR2, lAttr2.getName());
            assertTrue(ArrayUtils.isEquals(EXPECTED_ATTRIBUTE_ATTR2_VALUES,
                    lAttr2.getValues()));
        }

        catch (InvalidIdentifierException e) {
            assertTrue("InvalidIdentifierException", true);
        }

    }

}
