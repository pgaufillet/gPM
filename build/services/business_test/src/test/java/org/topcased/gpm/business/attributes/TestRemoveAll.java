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
 * This class tests the removeAll(String) method from the AttributesService
 * implementation.
 * 
 * @author mmennad
 */
public class TestRemoveAll extends AbstractBusinessServiceTestCase {

    private static final int NUMBER_ATTRIBUTES_DATA_AFTER = 0;

    /**
     * Tests the method with a correct id
     */
    public void testNormalCase() {

        AttributesService lAttributesService =
                serviceLocator.getAttributesService();
        ProductService lProductTypeService = serviceLocator.getProductService();

        //Setup
        String lElemId =
                lProductTypeService.getProductId(adminRoleToken,
                        GpmTestValues.PRODUCT_STORE1_NAME);

        //Check Before
        AttributeData[] lAttributesData = lAttributesService.getAll(lElemId);
        assertTrue(
                "There are two attributes now",
                GpmTestValues.PRODUCT_ATTR_NAMES.length == lAttributesData.length);
        assertNotNull("There is a value here", lAttributesData[0]);

        lAttributesService.removeAll(lElemId);

        //Check After
        lAttributesData = lAttributesService.getAll(lElemId);

        assertTrue("Now there is no attribute at all",
                NUMBER_ATTRIBUTES_DATA_AFTER == lAttributesData.length);
    }

    /**
     * Tests the method if a wrong Id was sent
     */
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
            lAttributesService.removeAll(lElemId);
            lAttributesData = lAttributesService.getAll(lElemId);

            assertTrue("there shouldn't be any attribute at all",
                    NUMBER_ATTRIBUTES_DATA_AFTER == lAttributesData.length);
        }

        catch (IllegalArgumentException e) {
            assertTrue("IllegalArgumentException", true);
        }
        finally {
            assertNull(lAttributesData);
        }

    }

    /**
     * Tests the method with no Id
     */
    public void testNullIdCase() {

        AttributesService lAttributesService =
                serviceLocator.getAttributesService();
        ProductService lProductTypeService = serviceLocator.getProductService();

        //Setup
        String lElemId = lProductTypeService.getProductId(adminRoleToken, null);

        AttributeData[] lAttributesData = null;

        //Check
        try {
            lAttributesService.removeAll(lElemId);
            lAttributesData = lAttributesService.getAll(lElemId);

            assertTrue("there shouldn't be any attribute at all",
                    NUMBER_ATTRIBUTES_DATA_AFTER == lAttributesData.length);
        }
        catch (IllegalArgumentException e) {
            assertTrue("IllegalArgumentException", true);
        }
        finally {
            assertNull(lAttributesData);
        }

    }

}
