/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos),
 * Michael Kargbo (Atos), Mimoun Mennad (Atos)
 ******************************************************************/
package org.topcased.gpm.business.attributes;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.product.service.ProductService;

/**
 * This class tests the atomicTestAndSet(String, String, String, String, String)
 * method from the AttributesService implementation.
 * 
 * @author mmennad
 */

public class TestAtomicTestAndSet extends AbstractBusinessServiceTestCase {

    private final static String COUNTER_ATTR_NAME_2 = "AtomicCounter2";

    /**
     * Tests the method with correct parameters
     */
    public void testNormalCase() {
        setUp();
        AttributesService lAttributesService =
                serviceLocator.getAttributesService();

        ProductService lProductService = serviceLocator.getProductService();

        //Setup parameters
        String lElemId =
                lProductService.getProductId(adminRoleToken,
                        GpmTestValues.PRODUCT_STORE1_NAME);
        // Set Attribute with null oldValue
        String lValueBeforeUpdate = null;
        String lNewValue = "test1";
        String lOldValue =
                lAttributesService.atomicTestAndSet(adminRoleToken, lElemId,
                        COUNTER_ATTR_NAME_2, lValueBeforeUpdate, lNewValue);
        assertEquals(lValueBeforeUpdate, lOldValue);

        // Set Attribute with "normal" values
        lValueBeforeUpdate = lNewValue;
        lNewValue = "test2";
        lOldValue =
                lAttributesService.atomicTestAndSet(adminRoleToken, lElemId,
                        COUNTER_ATTR_NAME_2, lValueBeforeUpdate, lNewValue);
        assertEquals(lValueBeforeUpdate, lOldValue);

        // Set attribute with bad oldValue => No change
        lOldValue =
                lAttributesService.atomicTestAndSet(adminRoleToken, lElemId,
                        COUNTER_ATTR_NAME_2, lValueBeforeUpdate, "test2");
        assertFalse(lValueBeforeUpdate.equals(lOldValue));
        assertEquals(lNewValue, lOldValue);

        // Set Attribute with null newValue
        lValueBeforeUpdate = lNewValue;
        lNewValue = null;
        lOldValue =
                lAttributesService.atomicTestAndSet(adminRoleToken, lElemId,
                        COUNTER_ATTR_NAME_2, lValueBeforeUpdate, lNewValue);
        assertEquals(lValueBeforeUpdate, lOldValue);

    }

    /**
     * Tests the method with a wrong product id, each call to the method throws
     * an exception
     */
    public void testWrongProductIdCase() {
        setUp();
        AttributesService lAttributesService =
                serviceLocator.getAttributesService();

        // Set Attribute with null oldValue
        String lValueBeforeUpdate = null;
        String lNewValue = "test1";
        String lOldValue = null;

        try {
            lOldValue =
                    lAttributesService.atomicTestAndSet(adminRoleToken,
                            INVALID_CONTAINER_ID, COUNTER_ATTR_NAME_2,
                            lValueBeforeUpdate, lNewValue);
        }
        catch (InvalidIdentifierException e) {
            assertTrue("InvalidIdentifierException", true);
        }
        finally {
            assertNull("the old value is still null", lValueBeforeUpdate);
            assertNull("the old value is still null", lOldValue);
            assertEquals("the new value has not changed", "test1", lNewValue);
        }

        try {
            assertEquals(lValueBeforeUpdate, lOldValue);
        }
        catch (InvalidIdentifierException e) {
            assertTrue("InvalidIdentifierException", true);
        }

        // Set Attribute with "normal" values
        try {
            lValueBeforeUpdate = lNewValue;
            lNewValue = "test2";
            lOldValue =
                    lAttributesService.atomicTestAndSet(adminRoleToken,
                            INVALID_CONTAINER_ID, COUNTER_ATTR_NAME_2,
                            lValueBeforeUpdate, lNewValue);
            assertEquals(lValueBeforeUpdate, lOldValue);
        }
        catch (InvalidIdentifierException e) {
            assertTrue("InvalidIdentifierException", true);
        }
        finally {
            assertEquals("the new value has not changed", "test2", lNewValue);
            assertNull("the old value is still null", lOldValue);
        }

        // Set attribute with bad oldValue => No change
        try {
            lOldValue =
                    lAttributesService.atomicTestAndSet(adminRoleToken,
                            INVALID_CONTAINER_ID, COUNTER_ATTR_NAME_2,
                            lValueBeforeUpdate, "test2");
            assertFalse(lValueBeforeUpdate.equals(lOldValue));
            assertEquals(lNewValue, lOldValue);
        }
        catch (InvalidIdentifierException e) {
            assertTrue("InvalidIdentifierException", true);
        }
        finally {
            assertNull("the old value is still null", lOldValue);
        }

        try {
            // Set Attribute with null newValue
            lValueBeforeUpdate = lNewValue;
            lNewValue = null;
            lOldValue =
                    lAttributesService.atomicTestAndSet(adminRoleToken,
                            INVALID_CONTAINER_ID, COUNTER_ATTR_NAME_2,
                            lValueBeforeUpdate, lNewValue);
            assertEquals(lValueBeforeUpdate, lOldValue);
        }

        catch (InvalidIdentifierException e) {
            assertTrue("InvalidIdentifierException", true);
        }
        finally {
            assertNull("the old value is still null", lOldValue);
            assertNull("the new value is null", lNewValue);
        }

    }
}