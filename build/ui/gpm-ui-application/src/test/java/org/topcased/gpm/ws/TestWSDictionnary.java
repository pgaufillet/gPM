/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.ws.v2.client.GDMException_Exception;

/**
 * TestWSDictionnary
 * 
 * @author nveillet
 */
public class TestWSDictionnary extends AbstractWSTestCase {

    private final static String PRODUCT_NAME_PRODUCT1 = getProductName();

    private final static String CATEGORY_NAME_PRODUCT1 = "Color";

    private final static List<String> CATEGORY_VALUES_PRODUCT1;
    static {
        CATEGORY_VALUES_PRODUCT1 = new ArrayList<String>();
        CATEGORY_VALUES_PRODUCT1.add("WHITE");
        CATEGORY_VALUES_PRODUCT1.add("GREY");
        CATEGORY_VALUES_PRODUCT1.add("RED");
        CATEGORY_VALUES_PRODUCT1.add("BLACK");
    }

    private final static String PRODUCT_NAME_PRODUCT2 = "Happy Mouse";

    private final static String CATEGORY_NAME_PRODUCT2 = "CAT_pedigre";

    private final static List<String> CATEGORY_VALUES_PRODUCT2;
    static {
        CATEGORY_VALUES_PRODUCT2 = new ArrayList<String>();
        CATEGORY_VALUES_PRODUCT2.add("ANGORA");
        CATEGORY_VALUES_PRODUCT2.add("BIRMAN");
        CATEGORY_VALUES_PRODUCT2.add("ABYSSINIAN");
    }

    /**
     * Tests method getCategoryValuesByProduct
     * 
     * @throws GDMException_Exception
     *             WS Exception
     */
    public void testGetCategoryValuesByProduct() throws GDMException_Exception {
        // Get category value to a product with one environment
        List<String> lCategoryValues =
                staticServices.getCategoryValues(roleToken,
                        PRODUCT_NAME_PRODUCT1, CATEGORY_NAME_PRODUCT1);
        assertEquals(CATEGORY_VALUES_PRODUCT1, lCategoryValues);

        // Get category value to a product with severals environments
        lCategoryValues =
                staticServices.getCategoryValues(roleToken,
                        PRODUCT_NAME_PRODUCT2, CATEGORY_NAME_PRODUCT2);
        assertEquals(CATEGORY_VALUES_PRODUCT2, lCategoryValues);
    }
}
