/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos),Mimoun Mennad (Atos)
 ******************************************************************/
package org.topcased.gpm.business.attributes;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.product.service.ProductService;

/**
 * This class tests the atomicIncrement (String, String, String, long) method
 * from the AttributesService implementation.
 * 
 * @author mmennad
 */

public class TestAtomicIncrement extends AbstractBusinessServiceTestCase {

    private final static String COUNTER_ATTR_NAME = "AtomicCounter";

    private final static long COUNTER_INIT_VALUE = 10;

    private final static long DUMMY_COUNTER = -1337;

    /**
     * Tests the method with correct parameters
     */
    public void testNormalCase() {

        AttributesService lAttributesService =
                serviceLocator.getAttributesService();
        ProductService lProductTypeService = serviceLocator.getProductService();
        //Set up parameter
        String lElemId =
                lProductTypeService.getProductId(adminRoleToken,
                        GpmTestValues.PRODUCT_STORE1_NAME);

        // Increment the counter attribute.
        // As this attribute does not exist yet, it must be set to the initial value.
        long lValueFirstIncrement =
                lAttributesService.atomicIncrement(adminRoleToken, lElemId,
                        COUNTER_ATTR_NAME, COUNTER_INIT_VALUE);

        // Increment the counter attribute again. The new value must be 'initial + 1'.
        long lValueSecondIncrement =
                lAttributesService.atomicIncrement(adminRoleToken, lElemId,
                        COUNTER_ATTR_NAME, COUNTER_INIT_VALUE);

        assertEquals(lValueFirstIncrement + 1, lValueSecondIncrement);

    }

    /**
     * Tests the method with a wrong id
     */
    public void testWrongProductCase() {

        AttributesService lAttributesService =
                serviceLocator.getAttributesService();

        long lValueFirstIncrement = DUMMY_COUNTER;

        try {

            lValueFirstIncrement =
                    lAttributesService.atomicIncrement(adminRoleToken,
                            INVALID_CONTAINER_ID, COUNTER_ATTR_NAME,
                            COUNTER_INIT_VALUE);
        }
        catch (InvalidIdentifierException e) {
            assertTrue("InvalidIdentifierException", true);
        }
        finally {
            assertEquals(DUMMY_COUNTER, lValueFirstIncrement);

        }

    }

}
