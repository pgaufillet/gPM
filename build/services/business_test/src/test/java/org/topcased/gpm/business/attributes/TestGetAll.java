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

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.product.service.ProductService;

/**
 * This class tests the getAll(String) method It takes 1 parameter : a string
 * which is an ID of an element.It gives an array of Attributes data
 * 
 * @author mmennad
 */
public class TestGetAll extends AbstractBusinessServiceTestCase {

    /**
     * Tests the method in case of correct Id was sent
     */
    public void testNormalCase() {

        AttributesService lAttributesService =
                serviceLocator.getAttributesService();
        ProductService lProductTypeService = serviceLocator.getProductService();

        //Setup
        String lElemId =
                lProductTypeService.getProductId(adminRoleToken,
                        GpmTestValues.PRODUCT_STORE1_NAME);

        AttributeData[] lAttributesData = lAttributesService.getAll(lElemId);

        //Check
        assertNotNull("Values found", lAttributesData);
        assertEquals(GpmTestValues.PRODUCT_ATTR_NAMES.length,
                lAttributesData.length);
        assertEquals(GpmTestValues.PRODUCT_ATTR1_VALUES.length,
                lAttributesData[0].getValues().length);
        assertEquals(GpmTestValues.PRODUCT_ATTR2_VALUES.length,
                lAttributesData[1].getValues().length);

        assertEquals(lAttributesData[0].getValues()[0],
                GpmTestValues.PRODUCT_ATTR1_VALUES[0]);
        assertEquals(lAttributesData[1].getValues()[0],
                GpmTestValues.PRODUCT_ATTR2_VALUES[0]);
        assertEquals(lAttributesData[1].getValues()[1],
                GpmTestValues.PRODUCT_ATTR2_VALUES[1]);
    }

    /**
     * Tests the method with a wrong Id
     **/
    public void testWrongIdCase() {

        AttributesService lAttributesService =
                serviceLocator.getAttributesService();
        ProductService lProductTypeService = serviceLocator.getProductService();

        //Setup
        String lElemId =
                lProductTypeService.getProductId(adminRoleToken,
                        INVALID_CONTAINER_ID);
        AttributeData[] lAttributesData = null;

        //Check
        try {
            lAttributesData = lAttributesService.getAll(lElemId);
        }
        catch (IllegalArgumentException e) {
            assertTrue("IllegalArgumentException", true);
        }
        finally {
            assertNull(lAttributesData);
        }

    }

    /**
     * Tests the method without parameters
     */
    public void testNoIdCase() {

        AttributesService lAttributesService =
                serviceLocator.getAttributesService();
        ProductService lProductTypeService = serviceLocator.getProductService();

        //Setup
        String lElemId = lProductTypeService.getProductId(adminRoleToken, null);
        AttributeData[] lAttributesData = null;
        //Check
        try {
            lAttributesData = lAttributesService.getAll(lElemId);
        }
        catch (IllegalArgumentException e) {
            assertTrue("IllegalArgumentException", true);
        }
        finally {
            assertNull(lAttributesData);
        }

    }

}