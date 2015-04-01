/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.ws.v2.client.AttributeData;
import org.topcased.gpm.ws.v2.client.GDMException_Exception;
import org.topcased.gpm.ws.v2.client.SheetType;

/**
 * TestWSAttributesSheets
 * 
 * @author mfranche
 */
public class TestWSAttributesSheets extends AbstractWSTestCase {
    /** The expected existing global attribute name */
    private static final String GLOBAL_ATTR_NAME = "authentication";

    /** The expected existing global attribute value */
    private static final String GLOBAL_ATTR_VALUE = "internal";

    /** The new global attribute name */
    private static final String NEW_GLOBAL_ATTR_NAME = "attribute";

    /** The new global attribute first value */
    private static final String NEW_GLOBAL_ATTR_VALUE1 = "val1";

    /** The new global attribute snd value */
    private static final String NEW_GLOBAL_ATTR_VALUE2 = "val2";

    /** The cat sheet type name */
    private static final String CAT_SHEET_TYPE = "Cat";

    private static final String CAT_ATTR_NAME = "revisionEnabled";

    /**
     * Tests the get methods on global attributes
     */
    public void testGetGlobalAttributesNormalCase() {
        try {
            // Get the existing global attribute names
            List<String> lGlobalAttrNames = staticServices.getGlobalAttrNames();

            assertNotNull(
                    "The method getGlobalAttrNames returns an incorrect result",
                    lGlobalAttrNames);
            assertTrue(
                    "The method getGlobalAttrNames returns an incorrect result",
                    lGlobalAttrNames.size() >= 1);
            assertTrue(
                    "The method getGlobalAttrNames returns an incorrect result",
                    lGlobalAttrNames.contains(GLOBAL_ATTR_NAME));

            // Construct a list containing only the global attribute name
            List<String> lStringAttrNameList = new ArrayList<String>();
            lStringAttrNameList.add(GLOBAL_ATTR_NAME);

            // Get the existing global attribute value
            List<AttributeData> lGlobalAttributesValue =
                    staticServices.getGlobalAttributes(lStringAttrNameList);

            assertNotNull(
                    "The method getGlobalAttributes returns an incorrect result",
                    lGlobalAttributesValue);
            assertTrue(
                    "The method getGlobalAttributes returns an incorrect result",
                    lGlobalAttributesValue.size() == 1);

            // Get the associated AttributeData
            AttributeData lGlobalAttributeData = lGlobalAttributesValue.get(0);

            assertNotNull(
                    "The attributeData returned by the method getGlobalAttributes is incorrect",
                    lGlobalAttributeData);
            assertEquals(
                    "The attributeData returned by the method getGlobalAttributes is incorrect",
                    lGlobalAttributeData.getName(), GLOBAL_ATTR_NAME);
            assertNotNull(
                    "The attributeData returned by the method getGlobalAttributes is incorrect",
                    lGlobalAttributeData.getValues());
            assertTrue(
                    "The attributeData returned by the method getGlobalAttributes is incorrect",
                    lGlobalAttributeData.getValues().size() == 1);
            assertNotNull(
                    "The attributeData returned by the method getGlobalAttributes is incorrect",
                    lGlobalAttributeData.getValues().get(0));
            assertEquals(
                    "The attributeData returned by the method getGlobalAttributes is incorrect",
                    lGlobalAttributeData.getValues().get(0), GLOBAL_ATTR_VALUE);
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Tests the addition of global attributes
     */
    public void testAdditionOfGlobalAttributes() {
        try {
            // Get the current number of global attributes
            int lInitialGlobalAttributesNumber =
                    staticServices.getGlobalAttrNames().size();

            // Construct a list with the new attribute data
            List<AttributeData> lNewAttributeDataList =
                    constructAttributeDataList();

            // Add this new global attribute
            staticServices.setGlobalAttributes(roleToken, lNewAttributeDataList);

            // get all the global attribute names
            List<String> lGlobalAttributeName =
                    staticServices.getGlobalAttrNames();

            // Verify that the new global attribute name list is correct
            assertNotNull(
                    "The method getGlobalAttrNames returns an incorrect result",
                    lGlobalAttributeName);
            assertTrue(
                    "The method getGlobalAttrNames returns an incorrect result",
                    lGlobalAttributeName.size() == lInitialGlobalAttributesNumber + 1);
            assertTrue(
                    "The method getGlobalAttrNames returns an incorrect result",
                    lGlobalAttributeName.contains(NEW_GLOBAL_ATTR_NAME));

            // Construct a list containing only the new global attribute name
            List<String> lStringAttrNameList = new ArrayList<String>();
            lStringAttrNameList.add(NEW_GLOBAL_ATTR_NAME);

            // Get the existing global attribute value
            List<AttributeData> lGlobalAttributesValue =
                    staticServices.getGlobalAttributes(lStringAttrNameList);

            // Check that the returned list is correct
            checkAddedAttribute(lGlobalAttributesValue);
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Check that the attribute data list contains only the global attribute
     * data
     * 
     * @param pAttributeDataList
     *            The attribute data list
     */
    protected void checkAddedAttribute(List<AttributeData> pAttributeDataList) {
        assertNotNull("The AttributeData list is incorrect.",
                pAttributeDataList);
        assertTrue("The AttributeData list is incorrect.",
                pAttributeDataList.size() == 1);

        // Get the associated AttributeData
        AttributeData lGlobalAttributeData = pAttributeDataList.get(0);

        assertNotNull("The attributeData is incorrect", lGlobalAttributeData);
        assertEquals("The attributeData is incorrect",
                lGlobalAttributeData.getName(), NEW_GLOBAL_ATTR_NAME);
        assertNotNull("The attributeData is incorrect",
                lGlobalAttributeData.getValues());
        assertTrue("The attributeData is incorrect",
                lGlobalAttributeData.getValues().size() == 2);
        assertTrue("The attributeData is incorrect",
                lGlobalAttributeData.getValues().contains(
                        NEW_GLOBAL_ATTR_VALUE1));
        assertTrue("The attributeData is incorrect",
                lGlobalAttributeData.getValues().contains(
                        NEW_GLOBAL_ATTR_VALUE2));
    }

    /**
     * Construct the attribute data list with a new attribute data
     * 
     * @return the creates attribute data list
     */
    protected List<AttributeData> constructAttributeDataList() {
        // Create a new attribute Data
        AttributeData lAttributeData = new AttributeData();
        lAttributeData.setName(NEW_GLOBAL_ATTR_NAME);
        lAttributeData.getValues().add(NEW_GLOBAL_ATTR_VALUE1);
        lAttributeData.getValues().add(NEW_GLOBAL_ATTR_VALUE2);

        // Create a list containing this new attribute data
        List<AttributeData> lNewAttributeDataList =
                new ArrayList<AttributeData>();
        lNewAttributeDataList.add(lAttributeData);

        return lNewAttributeDataList;
    }

    /**
     * Tests the getAllAttributes method on normal case
     */
    public void testGetAllAttributesNormalCase() {
        try {
            // Get all the sheet types
            List<SheetType> lSheetTypes =
                    staticServices.getSheetTypes(roleToken,
                            DEFAULT_PROCESS_NAME);

            // Get the cat sheet type
            SheetType lCatSheetType = getCatSheetType(lSheetTypes);

            // Get all attributes
            List<AttributeData> lAttributeDataList =
                    staticServices.getAllAttributes(lCatSheetType.getId());

            // Check the returned attributes list
            checkAttributeDataListConformity(lAttributeDataList);
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Tests the getAttributes method on normal case
     */
    public void testGetAttributesNormalCase() {
        try {
            // Get all the sheet types
            List<SheetType> lSheetTypes =
                    staticServices.getSheetTypes(roleToken,
                            DEFAULT_PROCESS_NAME);

            // Get the cat sheet type
            SheetType lCatSheetType = getCatSheetType(lSheetTypes);

            // Get all attribute names
            List<String> lAttrNames =
                    staticServices.getAttrNames(lCatSheetType.getId());

            // Check the returned attribute names list
            assertNotNull("The method getAttrNames returs an incorrect result",
                    lAttrNames);
            assertTrue("The method getAttrNames returs an incorrect result",
                    lAttrNames.contains(CAT_ATTR_NAME));

            // Get attributes
            List<AttributeData> lAttributeDataList =
                    staticServices.getAttributes(lCatSheetType.getId(),
                            lAttrNames);

            // Check the returned attributes list
            checkAttributeDataListConformity(lAttributeDataList);
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Check that the attribute data list is correct
     * 
     * @param pAttributeDataList
     *            The attribute data list
     */
    protected void checkAttributeDataListConformity(
            List<AttributeData> pAttributeDataList) {
        assertNotNull("The returned AttributeData list is incorrect",
                pAttributeDataList);
        assertTrue("The returned AttributeData list is incorrect",
                pAttributeDataList.size() >= 1);

        List<String> lAttributeDataName = new ArrayList<String>();
        for (AttributeData lAttributeData : pAttributeDataList) {
            lAttributeDataName.add(lAttributeData.getName());
        }

        assertTrue(lAttributeDataName.contains(CAT_ATTR_NAME));

    }

    /**
     * Tests the add and remove attributes methods
     */
    public void testAddAndRemoveAttributes() {
        try {
            // Get all the sheet types
            List<SheetType> lSheetTypes =
                    staticServices.getSheetTypes(roleToken,
                            DEFAULT_PROCESS_NAME);

            // Get the cat sheet type
            SheetType lCatSheetType = getCatSheetType(lSheetTypes);

            // Get the initial attribute number
            int lInitialAttributeNumber =
                    staticServices.getAttrNames(lCatSheetType.getId()).size();

            // Construct a attribute data list with the new attribute data
            List<AttributeData> lNewAttributeDataList =
                    constructAttributeDataList();

            // Add this new attribute
            staticServices.setAttributes(lCatSheetType.getId(),
                    lNewAttributeDataList);

            // get attribute names
            List<String> lAttrNames =
                    staticServices.getAttrNames(lCatSheetType.getId());

            assertNotNull(
                    "The method getAttrNames returns an incorrect result",
                    lAttrNames);
            assertTrue("The method getAttrNames returns an incorrect result",
                    lAttrNames.size() == lInitialAttributeNumber + 1);
            assertTrue("The method getAttrNames returns an incorrect result",
                    lAttrNames.contains(NEW_GLOBAL_ATTR_NAME));

            // Construct a list containing only the new global attribute name
            List<String> lStringAttrNameList = new ArrayList<String>();
            lStringAttrNameList.add(NEW_GLOBAL_ATTR_NAME);

            List<AttributeData> lAttributeDataList =
                    staticServices.getAttributes(lCatSheetType.getId(),
                            lStringAttrNameList);

            // Check the added attribute
            checkAddedAttribute(lAttributeDataList);

            // Then remove the attribute
            staticServices.removeAllAttributes(lCatSheetType.getId());

            // Check that the getAllAttributes list is empty
            lAttributeDataList =
                    staticServices.getAllAttributes(lCatSheetType.getId());
            assertTrue(
                    "The method getAllAttributes should have return an empty list",
                    lAttributeDataList.size() == 0);

            //Check that the getAttrNames list is empty
            List<String> lAttNames =
                    staticServices.getAttrNames(lCatSheetType.getId());
            assertEquals(
                    "The method getAttrNames should have return an empty list",
                    lAttNames.size(), 0);
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Retrieve the cat SheetType among a SheetType list
     * 
     * @param pSheetTypes
     *            The list of sheet types
     * @return the sheetType with name "Cat"
     */
    protected SheetType getCatSheetType(List<SheetType> pSheetTypes) {
        for (SheetType lSheetType : pSheetTypes) {
            if (lSheetType.getName().equals(CAT_SHEET_TYPE)) {
                return lSheetType;
            }
        }
        return null;
    }
}
