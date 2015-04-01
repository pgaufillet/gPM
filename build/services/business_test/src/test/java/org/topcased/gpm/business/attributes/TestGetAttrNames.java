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
 * This class tests the getAttrNames(String) method from the AttributesService
 * implementation.
 * 
 * @author mmennad
 */
public class TestGetAttrNames extends AbstractBusinessServiceTestCase {

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

        String[] lAttributesNames = lAttributesService.getAttrNames(lElemId);

        //Check 
        assertNotNull("Values found", lAttributesNames);
        assertEquals(GpmTestValues.PRODUCT_ATTR_NAMES.length,
                lAttributesNames.length);
        assertEquals(GpmTestValues.PRODUCT_ATTR_NAMES[0], lAttributesNames[0]);
        assertEquals(GpmTestValues.PRODUCT_ATTR_NAMES[1], lAttributesNames[1]);
    }

    /**
     * Tests the method if a wrong Id is sent
     */
    public void testWrongIdCase() {

        AttributesService lAttributesService =
                serviceLocator.getAttributesService();
        ProductService lProductTypeService = serviceLocator.getProductService();

        //Setup
        String lElemId =
                lProductTypeService.getProductId(adminRoleToken,
                        INVALID_CONTAINER_ID);
        String[] lAttributesNames = null;
        //Check
        try {
            lAttributesNames = lAttributesService.getAttrNames(lElemId);
        }
        catch (IllegalArgumentException e) {
            assertTrue("IllegalArgumentException", true);
        }
        finally {
            assertNull(lAttributesNames);
        }

    }

    /**
     * Tests the method if no parameter was sent
     */
    public void testNoIdCase() {

        AttributesService lAttributesService =
                serviceLocator.getAttributesService();
        ProductService lProductTypeService = serviceLocator.getProductService();

        //Check
        String lElemId = lProductTypeService.getProductId(adminRoleToken, null);
        String[] lAttributesNames = null;
        try {
            lAttributesNames = lAttributesService.getAttrNames(lElemId);
        }
        catch (IllegalArgumentException e) {
            assertTrue("IllegalArgumentException", true);
        }
        finally {
            assertNull(lAttributesNames);
        }

    }

}