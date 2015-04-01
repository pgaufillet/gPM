/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin),
 *               Yvan Ntsama (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization;

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.product.impl.ProductTreeNode;
import org.topcased.gpm.business.product.service.ProductData;

/**
 * Tests the method getProductsAsTree of the Authorization Service.
 * 
 * @author mfranche
 */
public class TestGetProductsAsTreeService extends
        AbstractBusinessServiceTestCase {

    /** The Happy Mouse product */
    private static final String HAPPY_MOUSE =
            GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME;

    /** The product1 product */
    private static final String PRODUCT1 = GpmTestValues.PRODUCT1_NAME;

    /** The store1 product */
    private static final String STORE1 = GpmTestValues.PRODUCT_STORE1_NAME;

    /** The store1_1 product */
    private static final String STORE1_1 = GpmTestValues.PRODUCT_STORE1_1_NAME;

    /** The store2 product */
    private static final String STORE2 = GpmTestValues.PRODUCT_STORE2_NAME;

    /**
     * Tests the method
     */
    public void testNormalCase() {
        // User2 login
        String lUserToken2 =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");

        // Get products as tree for user2
        List<ProductTreeNode> lProductTreeNodeList =
                authorizationService.getProductsAsTree(lUserToken2,
                        getProcessName());

        // Check if the list is correct
        checkTreeForUserToken2(lProductTreeNodeList);

        // User3 login
        String lUserToken3 =
                authorizationService.login(GpmTestValues.USER_USER3, "pwd3");

        // Get products as tree for user3
        lProductTreeNodeList =
                authorizationService.getProductsAsTree(lUserToken3,
                        getProcessName());

        // Check if the list is correct
        checkTreeForUserToken3(lProductTreeNodeList);

        // User4 login
        String lUserToken4 =
                authorizationService.login(GpmTestValues.USER_USER4, "pwd4");

        // Get products as tree for user4
        lProductTreeNodeList =
                authorizationService.getProductsAsTree(lUserToken4,
                        getProcessName());

        // Check if the list is correct
        checkTreeForUserToken4(lProductTreeNodeList);
    }

    /**
     * Check the products tree node list for user2 (Only Bernard's Store, Happy
     * Mouse and Product1 are accessible)
     * 
     * @param pProductTreeNodeList
     *            product tree node list
     */
    public void checkTreeForUserToken2(
            List<ProductTreeNode> pProductTreeNodeList) {
        assertTrue(
                "The product tree node List returned for the user2 has not the correct size.",
                pProductTreeNodeList.size() == 3);

        // Check the first product tree node : Bernard's store
        ProductTreeNode lFstProductTreeNode = pProductTreeNodeList.get(0);
        assertTrue(
                "The first product tree node returned for the user2 is incorrect",
                lFstProductTreeNode.getChildCount() == 0
                        && lFstProductTreeNode.isEnabled());

        ProductData lFstProductData = lFstProductTreeNode.getProductData();
        assertEquals(
                "The first product data returned for the user2 is incorrect",
                GpmTestValues.PRODUCT_BERNARD_STORE_NAME,
                lFstProductData.getName());

        // Check the snd product tree node : Happy Mouse
        ProductTreeNode lSndProductTreeNode = pProductTreeNodeList.get(1);
        assertTrue(
                "The snd product tree node returned for the user2 is incorrect",
                lSndProductTreeNode.getChildCount() == 0
                        && lSndProductTreeNode.isEnabled());

        ProductData lSndProductData = lSndProductTreeNode.getProductData();
        assertEquals(
                "The snd product data returned for the user2 is incorrect",
                HAPPY_MOUSE, lSndProductData.getName());

        // Check the third product tree node : product1
        ProductTreeNode lThirdProductTreeNode = pProductTreeNodeList.get(2);
        assertTrue(
                "The third product tree node returned for the user2 is incorrect",
                lThirdProductTreeNode.getChildCount() == 0
                        && lThirdProductTreeNode.isEnabled());

        ProductData lThirdProductData = lThirdProductTreeNode.getProductData();
        assertEquals(
                "The third product data returned for the user2 is incorrect",
                PRODUCT1, lThirdProductData.getName());
    }

    /**
     * Check the products tree node list for user3 (Only store1_1 and store2 are
     * accessible)
     * 
     * @param pProductTreeNodeList
     *            product tree node list
     */
    public void checkTreeForUserToken3(
            List<ProductTreeNode> pProductTreeNodeList) {
        assertTrue(
                "The product tree node List returned for the user3 has not the correct size.",
                pProductTreeNodeList.size() == 1);

        // Check the first product tree node : Bernard's Store (but not enabled)
        ProductTreeNode lProductTreeNode = pProductTreeNodeList.get(0);

        assertTrue("The product tree node returned for the user3 is incorrect",
                lProductTreeNode.getChildCount() == 2
                        && !lProductTreeNode.isEnabled());
        ProductData lProductData = lProductTreeNode.getProductData();

        assertEquals("The product data returned for the user3 is incorrect",
                GpmTestValues.PRODUCT_BERNARD_STORE_NAME,
                lProductData.getName());

        // Get the first child : store1 (but not enabled)
        ProductTreeNode lFirstChildProductTreeNode =
                (ProductTreeNode) lProductTreeNode.getChildAt(0);
        assertTrue("The first child returned for the user3 is incorrect",
                lFirstChildProductTreeNode.getChildCount() == 1
                        && !lFirstChildProductTreeNode.isEnabled());
        ProductData lFirstChildProductData =
                lFirstChildProductTreeNode.getProductData();

        assertEquals(
                "The first child product data returned for the user3 is incorrect",
                STORE1, lFirstChildProductData.getName());

        // Get the first sub child : store1_1
        ProductTreeNode lFirstSubChildProductTreeNode =
                (ProductTreeNode) lFirstChildProductTreeNode.getChildAt(0);
        assertNotNull(
                "The first sub child returned for the user3 is incorrect",
                lFirstSubChildProductTreeNode);
        assertTrue("The first sub child returned for the user3 is incorrect",
                lFirstSubChildProductTreeNode.getChildCount() == 0);
        assertTrue("The first sub child returned for the user3 is incorrect",
                lFirstSubChildProductTreeNode.isEnabled());
        ProductData lFirstSubChildProductData =
                lFirstSubChildProductTreeNode.getProductData();

        assertEquals(
                "The first sub product data returned for the user3 is incorrect",
                STORE1_1, lFirstSubChildProductData.getName());

        // Get the second child : store2
        ProductTreeNode lSndChildProductTreeNode =
                (ProductTreeNode) lProductTreeNode.getChildAt(1);

        assertTrue("The snd child returned for the user3 is incorrect",
                lSndChildProductTreeNode.getChildCount() == 0
                        && lSndChildProductTreeNode.isEnabled());
        ProductData lSndChildProductData =
                lSndChildProductTreeNode.getProductData();

        assertEquals(
                "The snd child product data returned for the user3 is incorrect",
                STORE2, lSndChildProductData.getName());
    }

    /**
     * Check the products tree node list for user4 (Only Bernard's store and
     * store1 are accessible)
     * 
     * @param pProductTreeNodeList
     *            product tree node list
     */
    public void checkTreeForUserToken4(
            List<ProductTreeNode> pProductTreeNodeList) {
        assertTrue(
                "The product tree node List returned for the user4 has not the correct size.",
                pProductTreeNodeList.size() == 1);

        // Check the first product tree node : Bernard's Store
        ProductTreeNode lProductTreeNode = pProductTreeNodeList.get(0);

        assertTrue("The product tree node returned for the user4 is incorrect",
                lProductTreeNode.getChildCount() == 1
                        && lProductTreeNode.isEnabled());

        ProductData lProductData = lProductTreeNode.getProductData();

        assertEquals("The product data returned for the user4 is incorrect",
                GpmTestValues.PRODUCT_BERNARD_STORE_NAME,
                lProductData.getName());

        // Get the first child : store1
        ProductTreeNode lFirstChildProductTreeNode =
                (ProductTreeNode) lProductTreeNode.getChildAt(0);

        assertTrue("The first child returned for the user4 is incorrect",
                lFirstChildProductTreeNode.getChildCount() == 0
                        && lFirstChildProductTreeNode.isEnabled());
        ProductData lFirstChildProductData =
                lFirstChildProductTreeNode.getProductData();

        assertEquals(
                "The first child product data returned for the user4 is incorrect",
                STORE1, lFirstChildProductData.getName());
    }
}
