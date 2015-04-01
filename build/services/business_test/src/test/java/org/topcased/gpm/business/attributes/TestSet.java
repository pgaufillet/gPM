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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.product.service.ProductService;

/**
 * This class tests the getAll(String) method from the AttributesService
 * implementation.
 * 
 * @author mmennad
 */
public class TestSet extends AbstractBusinessServiceTestCase {

    private static final String FIRST_ATTRIBUTE_NAME =
            "First_New_Attribute_Name";

    private static final String[] FIRST_ATTRIBUTE_VALUE =
            { "First_New_Attribute_Value" };

    private static final String SECOND_ATTRIBUTE_NAME =
            "Zecond_New_Attribute_Name";

    private static final String[] SECOND_ATTRIBUTE_VALUES =
            { "Zecond_New_Attribute_Value1", "Zecond_New_Attribute_Value2",
             "Zecond_New_Attribute_Value3" };

    private static final int NUMBER_ATTRIBUTES_DATA_AFTER = 4;

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

        AttributeData[] lAttrs =
                new AttributeData[] {
                                     new AttributeData(FIRST_ATTRIBUTE_NAME,
                                             FIRST_ATTRIBUTE_VALUE),
                                     new AttributeData(SECOND_ATTRIBUTE_NAME,
                                             SECOND_ATTRIBUTE_VALUES) };

        //Check Before
        AttributeData[] lAttributesData = lAttributesService.getAll(lElemId);
        assertTrue(
                "There are two attributes now",
                GpmTestValues.PRODUCT_ATTR_NAMES.length == lAttributesData.length);

        lAttributesService.set(lElemId, lAttrs);

        // set attribute test list
        List<String> lTesteBaseAttibuteName =
                new ArrayList<String>(NUMBER_ATTRIBUTES_DATA_AFTER);

        for (AttributeData lD : lAttributesData) {
            lTesteBaseAttibuteName.add(lD.getName());
        }

        //Check After
        List<AttributeData> lAttributesDataList =
                Arrays.asList(lAttributesService.getAll(lElemId));
        assertTrue("There are four attributes now",
                NUMBER_ATTRIBUTES_DATA_AFTER == lAttributesDataList.size());
        boolean lIsFound = false;
        for (AttributeData lAtd : lAttributesData) {
            for (int i = 0; (i < lAttributesDataList.size() && !lIsFound); i++) {
                AttributeData lGetAt = lAttributesDataList.get(i);

                // the same name
                if ((lAtd.getName().equals(lGetAt.getName()))) {

                    // the same attribute values
                    String[] lAtdValues = lAtd.getValues();
                    List<String> lGetAtValues =
                            Arrays.asList(lGetAt.getValues());
                    for (String lAtdVal : lAtdValues) {
                        assertTrue(lGetAtValues.contains(lAtdVal));
                        // go to the next attribute
                        lIsFound = true;
                    }
                }
            }
            if (!lIsFound) {
                fail("error not found attribute " + lAtd.getName());
            }
        }

    }

    /**
     * Tests the method if two set of attributes sharing the same values are
     * sent Only one is registered
     */
    public void testNoReplacementCase() {

        AttributesService lAttributesService =
                serviceLocator.getAttributesService();
        ProductService lProductTypeService = serviceLocator.getProductService();

        //Setup
        String lElemId =
                lProductTypeService.getProductId(adminRoleToken,
                        GpmTestValues.PRODUCT_STORE1_NAME);

        AttributeData[] lAttrs =
                new AttributeData[] {
                                     new AttributeData(FIRST_ATTRIBUTE_NAME,
                                             FIRST_ATTRIBUTE_VALUE),
                                     new AttributeData(SECOND_ATTRIBUTE_NAME,
                                             SECOND_ATTRIBUTE_VALUES),
                                     new AttributeData(SECOND_ATTRIBUTE_NAME,
                                             SECOND_ATTRIBUTE_VALUES) };

        //Check Before
        AttributeData[] lAttributesData = lAttributesService.getAll(lElemId);
        assertTrue(
                "There are two attributes now",
                GpmTestValues.PRODUCT_ATTR_NAMES.length == lAttributesData.length);

        lAttributesService.set(lElemId, lAttrs);

        // set attribute test list
        List<String> lTesteBaseAttibuteName =
                new ArrayList<String>(NUMBER_ATTRIBUTES_DATA_AFTER);

        for (AttributeData lD : lAttributesData) {
            lTesteBaseAttibuteName.add(lD.getName());
        }

        //Check After
        List<AttributeData> lAttributesDataList =
                Arrays.asList(lAttributesService.getAll(lElemId));
        assertTrue("There are four attributes now",
                NUMBER_ATTRIBUTES_DATA_AFTER == lAttributesDataList.size());
        boolean lIsFound = false;
        for (AttributeData lAtd : lAttributesData) {
            for (int i = 0; (i < lAttributesDataList.size() && !lIsFound); i++) {
                AttributeData lGetAt = lAttributesDataList.get(i);

                // the same name
                if ((lAtd.getName().equals(lGetAt.getName()))) {

                    // the same attribute values
                    String[] lAtdValues = lAtd.getValues();
                    List<String> lGetAtValues =
                            Arrays.asList(lGetAt.getValues());
                    for (String lAtdVal : lAtdValues) {
                        assertTrue(lGetAtValues.contains(lAtdVal));
                        // go to the next attribute
                        lIsFound = true;
                    }
                }
            }
            if (!lIsFound) {
                fail("error not found attribute " + lAtd.getName());
            }
        }

    }

    /**
     * Tests the method in case a wrong Id was sent
     */
    public void testWrongIdCase() {

        AttributesService lAttributesService =
                serviceLocator.getAttributesService();
        ProductService lProductTypeService = serviceLocator.getProductService();

        AttributeData[] lAttrs =
                new AttributeData[] {
                                     new AttributeData(FIRST_ATTRIBUTE_NAME,
                                             FIRST_ATTRIBUTE_VALUE),
                                     new AttributeData(SECOND_ATTRIBUTE_NAME,
                                             SECOND_ATTRIBUTE_VALUES) };

        //Setup
        String lElemId =
                lProductTypeService.getProductId(adminRoleToken,
                        INVALID_CONTAINER_ID);
        AttributeData[] lAttributesData = null;

        //Check
        try {
            lAttributesService.set(lElemId, lAttrs);
        }
        catch (IllegalArgumentException e) {
            assertTrue("IllegalArgumentException", true);
        }
        finally {
            assertNull(lAttributesData);
        }

    }

    /**
     * Tests the method in case no Id parameter was sent
     */
    public void testNoIdCase() {

        AttributesService lAttributesService =
                serviceLocator.getAttributesService();
        ProductService lProductTypeService = serviceLocator.getProductService();

        AttributeData[] lAttrs =
                new AttributeData[] {
                                     new AttributeData(FIRST_ATTRIBUTE_NAME,
                                             FIRST_ATTRIBUTE_VALUE),
                                     new AttributeData(SECOND_ATTRIBUTE_NAME,
                                             SECOND_ATTRIBUTE_VALUES) };

        //Setup
        String lElemId = lProductTypeService.getProductId(adminRoleToken, null);
        AttributeData[] lAttributesData = null;
        //Check
        try {
            lAttributesService.set(lElemId, lAttrs);
        }
        catch (IllegalArgumentException e) {
            assertTrue("IllegalArgumentException", true);
        }
        finally {
            assertNull(lAttributesData);
        }

    }

    /**
     * Tests the method in case no attributes names were sent
     */
    public void testNoAttributesNamesCase() {

        AttributesService lAttributesService =
                serviceLocator.getAttributesService();
        ProductService lProductTypeService = serviceLocator.getProductService();

        AttributeData[] lAttrs = new AttributeData[] {

        new AttributeData(null, SECOND_ATTRIBUTE_VALUES) };

        //Setup
        String lElemId = lProductTypeService.getProductId(adminRoleToken, null);
        AttributeData[] lAttributesData = null;
        //Check
        try {
            lAttributesService.set(lElemId, lAttrs);
        }
        catch (IllegalArgumentException e) {
            assertTrue("IllegalArgumentException", true);
        }
        finally {
            assertNull(lAttributesData);
        }

    }

    /**
     * Tests the method if no attributes names were sent
     */
    public void testNoAttributesValuesCase() {

        AttributesService lAttributesService =
                serviceLocator.getAttributesService();
        ProductService lProductTypeService = serviceLocator.getProductService();

        AttributeData[] lAttrs = new AttributeData[] {

        new AttributeData(SECOND_ATTRIBUTE_NAME, null) };

        //Setup
        String lElemId = lProductTypeService.getProductId(adminRoleToken, null);
        AttributeData[] lAttributesData = null;
        //Check
        try {
            lAttributesService.set(lElemId, lAttrs);
        }
        catch (IllegalArgumentException e) {
            assertTrue("IllegalArgumentException", true);
        }
        finally {
            assertNull(lAttributesData);
        }

    }

}