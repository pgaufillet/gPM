/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin), 
 *  Vincent Hemery (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.topcased.gpm.ws.v2.client.FieldSummaryData;
import org.topcased.gpm.ws.v2.client.FilterScope;
import org.topcased.gpm.ws.v2.client.GDMException_Exception;
import org.topcased.gpm.ws.v2.client.Product;
import org.topcased.gpm.ws.v2.client.ProductNode;
import org.topcased.gpm.ws.v2.client.ProductSummaryData;

/**
 * Test product services through web services.
 * 
 * @author tpanuel
 */
public class TestWSProducts extends AbstractWSTestCase {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(TestWSProducts.class);

    /** The Product Name. */
    private static final String PRODUCT_NAME = "Bernard's store";

    /** Expected filtering product names. */
    private static final String[] PRODUCTS_NAMES =
            { "Bernard's store", "environment test store", "Happy Mouse",
             "productWithNoUsers", "product1", "product1_1", "product1_2",
             "product2", "product3", "store1", "store1_1", "store2", "substore" };

    /** Name of the default product filter to execute. */
    private static final String PRODUCT_FILTER_NAME = "ProductFilter01";

    /** Name of filters with same name and different scopes */
    private static final String FILTER_WITH_SAME_NAME =
            "ProductFilterWithSameName";

    /**
     * Name of the only field label key for filters with same name, for each
     * visibility scope
     */
    private static final Map<FilterScope, String[]> FILTER_WITH_SAME_NAME_FIELD =
            new HashMap<FilterScope, String[]>(3);
    static {
        FILTER_WITH_SAME_NAME_FIELD.put(FilterScope.USER_FILTER,
                new String[] { "product_name" });
        FILTER_WITH_SAME_NAME_FIELD.put(FilterScope.PRODUCT_FILTER,
                new String[] { "product_name", "product_storetype" });
        FILTER_WITH_SAME_NAME_FIELD.put(FilterScope.INSTANCE_FILTER,
                new String[] { "product_storetype" });
    }

    /**
     * Test the getProducts method in normal conditions.
     */
    public void testGetProductsNormalCase() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("WS : Get products in normal conditions");
        }
        try {
            List<Product> lProducts =
                    staticServices.getProducts(userToken, DEFAULT_PROCESS_NAME);
            assertNotNull("The method getProducts returns null.", lProducts);
            int lSize = lProducts.size();
            int lExpectedSize = PRODUCTS_NAMES.length;
            assertEquals("The method getProducts returns " + lSize
                    + " products instead of " + lExpectedSize + ".",
                    lExpectedSize, lSize);
            List<String> lProductNames = new ArrayList<String>();
            for (Product lProduct : lProducts) {
                lProductNames.add(lProduct.getName());
            }
            assertTrue(
                    "The method getProduct returns not all expected products",
                    lProductNames.containsAll(Arrays.asList(PRODUCTS_NAMES)));
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the getProductTypesByKeys method in normal conditions.
     */
    public void testGetProductTypesByKeysNormalCase() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("WS : Get product types in normal conditions");
        }
        try {
            List<Product> lProducts =
                    staticServices.getProducts(userToken, DEFAULT_PROCESS_NAME);
            assertNotNull("The method getProducts returns null.", lProducts);
            List<String> lProductsKeys = new ArrayList<String>();
            for (Product lProduct : lProducts) {
                lProductsKeys.add(lProduct.getType());
            }
            int lSize = lProducts.size();
            int lExpectedSize = PRODUCTS_NAMES.length;
            assertEquals("The method getProducts returns " + lSize
                    + "process names instead of " + lExpectedSize + ".",
                    lExpectedSize, lSize);
            List<String> lProductNames = new ArrayList<String>();
            for (Product lProduct : lProducts) {
                lProductNames.add(lProduct.getName());
            }
            assertTrue(
                    "The method getProduct returns not all expected products",
                    lProductNames.containsAll(Arrays.asList(PRODUCTS_NAMES)));
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the getProduct and getProductByKey method in normal conditions.
     */
    public void testGetProductNormalCase() {
        try {
            Product lProduct =
                    staticServices.getProduct(roleToken, getProcessName(),
                            PRODUCT_NAME);

            assertNotNull("Product with name " + PRODUCT_NAME
                    + " is not in DB - getProduct service failed", lProduct);

            String lProductId = lProduct.getId();

            lProduct = staticServices.getProductByKey(roleToken, lProductId);

            assertNotNull("Product #" + lProductId
                    + " is not in DB - getProductByKey service failed",
                    lProduct);
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the executeProductFilter method in normal conditions.
     */
    public void testExecuteProductFilterNormalCase() {
        try {
            List<ProductSummaryData> lProductSummaryDatas =
                    staticServices.executeProductFilter(roleToken,
                            DEFAULT_PROCESS_NAME, DEFAULT_PRODUCT_NAME,
                            getLogin()[0], PRODUCT_FILTER_NAME);
            int lSize = lProductSummaryDatas.size();
            int lExpectedSize = PRODUCTS_NAMES.length;
            assertEquals("The method executeProductFilter returns " + lSize
                    + " results instead of " + lExpectedSize + ".",
                    lExpectedSize, lSize);
            List<String> lProductNames = new ArrayList<String>();
            for (ProductSummaryData lProductSummaryData : lProductSummaryDatas) {
                lProductNames.add(lProductSummaryData.getName());
            }
            assertTrue("",
                    lProductNames.containsAll(Arrays.asList(PRODUCTS_NAMES)));
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the executeProductFilterWithScope method with different filters with
     * same name and different scopes.
     * 
     * @author vhemery
     */
    public void testExecuteProductFilterWithScopeNormalCase() {
        try {
            for (FilterScope lScope : FilterScope.values()) {
                List<ProductSummaryData> lProductSummaryDatas =
                        staticServices.executeProductFilterWithScope(roleToken,
                                -1, 0, DEFAULT_PRODUCT_NAME,
                                FILTER_WITH_SAME_NAME, lScope);

                int lSize = lProductSummaryDatas.size();
                int lExpectedSize = PRODUCTS_NAMES.length;
                assertEquals(
                        "The method executeProductFilterWithScope returns "
                                + lSize + " results instead of "
                                + lExpectedSize + " with scope "
                                + lScope.toString(), lExpectedSize, lSize);
                // PRODUCTS_NAMES.length > 0
                assertTrue(
                        "The summaries returned by method "
                                + "executeProductFilterWithScope contain no field with scope "
                                + lScope.toString(),
                        lProductSummaryDatas.get(0).getFieldSummaryDatas().size() > 0);
                List<String> lFilterFieldNames = new ArrayList<String>(2);
                List<FieldSummaryData> lFieldSummaries =
                        lProductSummaryDatas.get(0).getFieldSummaryDatas();
                for (FieldSummaryData lFieldSum : lFieldSummaries) {
                    lFilterFieldNames.add(lFieldSum.getLabelKey());

                }
                List<String> lReferenceList =
                        Arrays.asList(FILTER_WITH_SAME_NAME_FIELD.get(lScope));
                assertTrue(
                        "The method executeProductFilterWithScope does "
                                + "not execute the right filter with scope "
                                + lScope.toString(),
                        lFilterFieldNames.size() == FILTER_WITH_SAME_NAME_FIELD.get(lScope).length
                                && lFilterFieldNames.containsAll(lReferenceList));
            }
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the getProductAsTree method
     */
    public void testGetProductsAsTree() {
        List<ProductNode> lOriginalNodes =
                staticServices.getProductsAsTree(roleToken,
                        DEFAULT_PROCESS_NAME);
        StringBuilder lNodeWrappersToString = new StringBuilder();
        for (ProductNode lNode : lOriginalNodes) {
            lNodeWrappersToString.append(new ProductNodeWrapper(lNode).toString());
        }

        assertEquals(
                "Check that Product Tree is as expected",
                "[Bernard's store,([store1,([store1_1,(), enabled]), enabled]"
                        + "[store2,(), enabled][substore,(), enabled]), enabled]"
                        + "[Happy Mouse,(), enabled]"
                        + "[environment test store,([substore,(), enabled]), enabled]"
                        + "[product1,([product1_1,(), enabled][product1_2,(), enabled]), enabled]"
                        + "[product2,(), enabled][product3,(), enabled]"
                        + "[productWithNoUsers,(), enabled]",
                lNodeWrappersToString.toString());
    }

    /**
     * Class for test purpose, adding a string representation
     */
    private static class ProductNodeWrapper extends ProductNode {

        public String toString() {
            StringBuilder lRet =
                    new StringBuilder("[" + getProductName() + ",(");

            for (ProductNode lChild : getChildren()) {
                lRet.append(lChild.toString());
            }

            if (isEnabled()) {
                lRet.append("), enabled]");
            }
            else {
                lRet.append("), disabled]");
            }
            return lRet.toString();
        }

        public ProductNodeWrapper(ProductNode pNode) {
            setProductName(pNode.getProductName());
            setEnabled(pNode.isEnabled());
            getChildren().addAll(wrapProductNodes(pNode.getChildren()));
        }
    }

    private static List<ProductNodeWrapper> wrapProductNodes(
            List<ProductNode> pProductNodes) {
        List<ProductNodeWrapper> lRet = new ArrayList<ProductNodeWrapper>();
        for (ProductNode lNode : pProductNodes) {
            lRet.add(new ProductNodeWrapper(lNode));
        }
        return lRet;
    }

}
