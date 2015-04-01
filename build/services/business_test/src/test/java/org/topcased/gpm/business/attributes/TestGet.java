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
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.product.service.ProductService;

/**
 * This class tests the get(String, String[]) method from the AttributesService
 * implementation.
 * 
 * @author mmennad
 */
public class TestGet extends AbstractBusinessServiceTestCase {

    /**
     * Tests the method in case of correct parameters were sent
     */
    public void testNormalCase() {

        AttributesService lAttributesService =
                serviceLocator.getAttributesService();
        ProductService lProductTypeService = serviceLocator.getProductService();

        //Setup parameters
        String lElemId =
                lProductTypeService.getProductId(adminRoleToken,
                        GpmTestValues.PRODUCT_STORE1_NAME);

        //Calling the method
        AttributeData[] lAttributesData =
                lAttributesService.get(lElemId,
                        GpmTestValues.PRODUCT_ATTR_NAMES);

        //Be sure that the object attribute data is not null, being found
        assertNotNull("Values found", lAttributesData);

        //Be sure that two attributes were found
        assertEquals(GpmTestValues.PRODUCT_ATTR_NAMES.length,
                lAttributesData.length);

        //Be sure that the first attribute has one value
        assertEquals(GpmTestValues.PRODUCT_ATTR1_VALUES.length,
                lAttributesData[0].getValues().length);

        //Be sure that the second attribute has two values
        assertEquals(GpmTestValues.PRODUCT_ATTR2_VALUES.length,
                lAttributesData[1].getValues().length);

        // Control that the attributes are the good ones, see
        //org.topcased.gpm.business.GpmTestValues and 
        //org.topcased.gpm.business.attributes.TestGetAttrs for more details
        assertEquals(lAttributesData[0].getValues()[0],
                GpmTestValues.PRODUCT_ATTR1_VALUES[0]);
        assertEquals(lAttributesData[1].getValues()[0],
                GpmTestValues.PRODUCT_ATTR2_VALUES[0]);
        assertEquals(lAttributesData[1].getValues()[1],
                GpmTestValues.PRODUCT_ATTR2_VALUES[1]);

    }

    /**
     * Tests the method with a wrong ID was sent
     */
    public void testWrongIdValueCase() {

        //Instantiation
        AttributesService lAttributesService =
                serviceLocator.getAttributesService();
        ProductService lProductService = serviceLocator.getProductService();

        //Setup a wrong product id
        String lElemId =
                lProductService.getProductId(adminRoleToken,
                        INVALID_CONTAINER_ID);

        AttributeData[] lAttributesData = null;

        try {
            lAttributesData =
                    lAttributesService.get(lElemId,
                            GpmTestValues.PRODUCT_ATTR_NAMES);
        }
        catch (IllegalArgumentException e) {
            assertTrue("IllegalArgumentException", true);
        }
        finally {
            assertNull(lAttributesData);
        }

    }

    /**
     * Tests the method with wrong attributes names were sent
     */
    public void testWrongAttributesNamesValuesCase() {

        AttributesService lAttributesService =
                serviceLocator.getAttributesService();
        ProductService lProductTypeService = serviceLocator.getProductService();

        //Setup parameters
        String lElemId =
                lProductTypeService.getProductId(adminRoleToken,
                        GpmTestValues.PRODUCT_STORE1_NAME);
        String[] lAttrNames = GpmTestValues.FALSE_PRODUCT_ATTR_NAMES;

        //Check
        assertNotNull(lElemId);

        //Setting null value for attributes object
        AttributeData[] lAttributesData = null;

        lAttributesData = lAttributesService.get(lElemId, lAttrNames);

        //Check
        assertNull("The first attribute data should be null.",
                lAttributesData[0]);
        assertNull("The second attribute data should be null.",
                lAttributesData[1]);

    }

    /**
     * Tests the method without parameters
     */
    public void testEmptyAllValuesCase() {
        AttributesService lAttributesService =
                serviceLocator.getAttributesService();

        //Setup
        String lElemId = "";
        String[] lAttrNames = { "", "" };
        AttributeData[] lAttributesData = null;

        try {
            lAttributesData = lAttributesService.get(lElemId, lAttrNames);
        }
        catch (InvalidIdentifierException e) {
            assertTrue("InvalidIdentifierException", true);
        }
        finally {
            assertNull(lAttributesData);
        }
    }

}
