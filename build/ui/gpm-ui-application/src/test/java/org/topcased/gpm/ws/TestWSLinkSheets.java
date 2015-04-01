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
import java.util.Arrays;
import java.util.List;

import org.topcased.gpm.ws.v2.client.Field;
import org.topcased.gpm.ws.v2.client.GDMException_Exception;
import org.topcased.gpm.ws.v2.client.Link;
import org.topcased.gpm.ws.v2.client.LinkType;
import org.topcased.gpm.ws.v2.client.MultipleField;
import org.topcased.gpm.ws.v2.client.SheetData;
import org.topcased.gpm.ws.v2.client.SheetType;

/**
 * TestWSLinkSheets
 * 
 * @author mfranche
 */
public class TestWSLinkSheets extends AbstractWSTestCase {
    /** Link type name for testGetLinkedElementsIds */
    private static final String LINK_TYPE_NAME_1 = "Cat-Cat";

    /** Sheet reference for testGetLinkedElementsIds */
    private static final String ORIGIN_REF_1 = "Garfield";

    /** Linked elements references for testGetLinkedElementsIds */
    private static final String[] LINKED_REFS_1 =
            new String[] { "Gros Minet", "Tom" };

    /** productName for testGetLinkedElementsIds */
    private static final String PRODUCT_NAME_1 = "Happy Mouse";

    /**
     * Tests method getLinkedElements IDS
     * 
     * @throws GDMException_Exception
     *             WS Exception
     */
    public void testGetLinkedElementsIds() throws GDMException_Exception {
        SheetData lSheetData =
                staticServices.getSheetByRef(roleToken, getProcessName(),
                        PRODUCT_NAME_1, ORIGIN_REF_1);
        String lSheetId = lSheetData.getId();

        List<String> lLinkedElementsIds =
                staticServices.getLinkedElementsIds(roleToken, lSheetId,
                        LINK_TYPE_NAME_1);

        assertNotNull("No Linked elements found", lLinkedElementsIds);
        assertEquals(lLinkedElementsIds.size()
                + " linked elements found instead of " + LINKED_REFS_1.length
                + ".", LINKED_REFS_1.length, lLinkedElementsIds.size());

        List<SheetData> lSheets =
                staticServices.getSheetsByKeys(roleToken, lLinkedElementsIds);
        List<String> lLinkedElementsRefs =
                new ArrayList<String>(lLinkedElementsIds.size());
        for (SheetData lSheet : lSheets) {
            lLinkedElementsRefs.add(lSheet.getReference());
        }

        assertTrue(lLinkedElementsRefs.containsAll(Arrays.asList(LINKED_REFS_1)));
    }

    /** The cat sheet type name */
    private static final String CAT_SHEET_TYPE = "Cat";

    /** The link type to create. */
    private static final String SHEET_TYPE_LINK_NAME = "CAT_PRICE";

    /** The origin sheet reference. */
    private static final String SHEET_REF_ORIGIN = "Garfield";

    /** The origin sheet product name. */
    private static final String ORIGIN_SHEET_PRODUCT = "Happy Mouse";

    /** The destination sheet reference. */
    private static final String SHEET_REF_DESTINATION = "Price1";

    /** The destination sheet product name. */
    private static final String DEST_SHEET_PRODUCT = "Bernard's store";

    /** The new link sheet field value. */
    private static final String NEW_LINK_FIELD_VALUE = "UpdatedValue";

    /**
     * Tests the link sheet in a normal mode
     */
    public void testLinkSheetNormalCase() {
        try {
            // Get all the sheet types
            List<SheetType> lSheetTypes =
                    staticServices.getSheetTypes(roleToken,
                            DEFAULT_PROCESS_NAME);

            // Get the cat sheet type
            SheetType lCatSheetType = getCatSheetType(lSheetTypes);

            // Get the possible links key for cat.
            List<String> lPossibleLinkTypes =
                    staticServices.getPossibleLinkTypes(roleToken,
                            lCatSheetType.getId());
            assertNotNull(
                    "The list returned by the method getPossibleLinkTypes is incorrect.",
                    lPossibleLinkTypes);
            assertTrue(
                    "The list returned by the method getPossibleLinkTypes is incorrect.",
                    lPossibleLinkTypes.size() != 0);

            // Get the possible link types for cat.
            List<LinkType> lLinkTypes =
                    staticServices.getLinkTypesByKeys(roleToken,
                            lPossibleLinkTypes);
            assertNotNull(
                    "The list returned by the method getLinkTypesByKeys is incorrect.",
                    lLinkTypes);
            assertEquals(
                    "The list returned by the method getLinkTypesByKeys is incorrect.",
                    lPossibleLinkTypes.size(), lLinkTypes.size());

            // Get the origin sheet data
            SheetData lOriginSheetData = getOriginSheetData();

            // Get the destination sheet data
            SheetData lDestSheetData = getDestinationSheetData();

            // Get the existing links on the origin sheet data
            List<Link> lLinkListBefore =
                    staticServices.getLinks(roleToken, lOriginSheetData.getId());
            assertNotNull("The origin sheet data must contain existing links.",
                    lLinkListBefore);
            assertTrue("The origin sheet data must contain existing links.",
                    lLinkListBefore.size() >= 1);

            // Find expected link between origin and destination
            Link lLinkBefore = null;
            for (Link lLink : lLinkListBefore) {
                if (lLink.getDestinationId().equals(lDestSheetData.getId())) {
                    lLinkBefore = lLink;
                }
            }
            assertNotNull(lLinkBefore);

            // Tests the update method
            testUpdate(lLinkBefore);

            lLinkListBefore =
                    staticServices.getLinks(roleToken, lOriginSheetData.getId());

            if (lLinkListBefore.size() != 0) {
                List<String> lLinkListKeyToDelete = new ArrayList<String>();
                for (Link lLink : lLinkListBefore) {
                    lLinkListKeyToDelete.add(lLink.getId());
                }

                // Delete the existing links
                staticServices.deleteLinks(roleToken, lLinkListKeyToDelete);

                // Get the links list on the same origin sheet data : this list must be empty
                lLinkListBefore =
                        staticServices.getLinks(roleToken,
                                lOriginSheetData.getId());
                assertTrue("The list returned by the method getLinks must "
                        + "be empty after the deletion.",
                        lLinkListBefore.size() == 0);
            }

            // Create the sheet link
            Link lCreatedLink =
                    staticServices.createSheetLink(roleToken,
                            SHEET_TYPE_LINK_NAME, lOriginSheetData.getId(),
                            lDestSheetData.getId());

            // Get the links
            List<Link> lLinkList =
                    staticServices.getLinks(roleToken, lOriginSheetData.getId());

            assertNotNull(
                    "The link List returned by the method getLinks is incorrect.",
                    lLinkList);
            assertTrue(
                    "The link List returned by the method getLinks is incorrect.",
                    lLinkList.size() == 1);
            // Get the single link
            Link lLink = lLinkList.get(0);

            checkLink(lLink, lCreatedLink, lOriginSheetData, lDestSheetData);

            List<String> lKeyList = new ArrayList<String>();
            lKeyList.add(lCreatedLink.getId());
            List<Link> lLinkListByKeys =
                    staticServices.getLinksByKeys(roleToken, lKeyList);

            assertNotNull(
                    "The link list returned by the method getLinksByKeys is incorrect.",
                    lLinkListByKeys);
            assertTrue(
                    "The link list returned by the method getLinksByKeys is incorrect.",
                    lLinkListByKeys.size() == 1);
            // Get the single link
            lLink = lLinkListByKeys.get(0);

            checkLink(lLink, lCreatedLink, lOriginSheetData, lDestSheetData);

        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Tests the update method
     * 
     * @param pLink
     *            The link to be updated
     * @throws GDMException_Exception
     *             An GDM Exception
     */
    protected void testUpdate(Link pLink) throws GDMException_Exception {
        // Change the field value
        pLink.getFieldValues().get(0).getFieldValues().get(0).setValue(
                NEW_LINK_FIELD_VALUE);

        // Update the link
        List<Link> lListLink = new ArrayList<Link>();
        lListLink.add(pLink);
        staticServices.updateLinks(roleToken, lListLink);

        // Retrieve the modified link by its key
        List<String> lListKey = new ArrayList<String>();
        lListKey.add(pLink.getId());
        lListLink = staticServices.getLinksByKeys(roleToken, lListKey);

        Link lModifiedLink = lListLink.get(0);

        assertEquals(
                "The link has not correctly been updated. ",
                lModifiedLink.getFieldValues().get(0).getFieldValues().get(0).getValue(),
                NEW_LINK_FIELD_VALUE);
        assertEquals("The link has not correctly been updated. ",
                lModifiedLink.getId(), pLink.getId());
        assertEquals("The link has not correctly been updated. ",
                lModifiedLink.getOriginId(), pLink.getOriginId());
        assertEquals("The link has not correctly been updated. ",
                lModifiedLink.getDestinationId(), pLink.getDestinationId());
    }

    /**
     * Check the link
     * 
     * @param pLink
     *            The link to be checked
     * @param pCreatedLink
     *            The created link
     * @param pOriginSheetData
     *            the origin sheet data
     * @param pDestSheetData
     *            the destination sheet data
     */
    protected void checkLink(Link pLink, Link pCreatedLink,
            SheetData pOriginSheetData, SheetData pDestSheetData) {
        assertEquals("The returned link has an incorrect id.", pLink.getId(),
                pCreatedLink.getId());
        assertEquals("The returned link has an incorrect origin id.",
                pLink.getOriginId(), pOriginSheetData.getId());
        assertEquals("The returned link has an incorrect destination id.",
                pLink.getDestinationId(), pDestSheetData.getId());
        assertEquals("The returned link has an incorrect type.",
                pLink.getType(), SHEET_TYPE_LINK_NAME);
    }

    /**
     * Get the origin sheet data
     * 
     * @return The sheet data
     * @throws GDMException_Exception
     *             A GDM Exception
     */
    protected SheetData getOriginSheetData() throws GDMException_Exception {
        List<String> lCatRefs = new ArrayList<String>();
        lCatRefs.add(SHEET_REF_ORIGIN);
        List<SheetData> lCatSheetDataList =
                staticServices.getSheetsByRefs(roleToken, DEFAULT_PROCESS_NAME,
                        ORIGIN_SHEET_PRODUCT, lCatRefs);
        return lCatSheetDataList.get(0);
    }

    /**
     * Get the destination sheet data
     * 
     * @return The sheet data
     * @throws GDMException_Exception
     *             A GDM Exception
     */
    protected SheetData getDestinationSheetData() throws GDMException_Exception {
        List<String> lPriceRefs = new ArrayList<String>();
        lPriceRefs.add(SHEET_REF_DESTINATION);
        List<SheetData> lPriceSheetDataList =
                staticServices.getSheetsByRefs(roleToken, DEFAULT_PROCESS_NAME,
                        DEST_SHEET_PRODUCT, lPriceRefs);
        return lPriceSheetDataList.get(0);

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
            if (lSheetType.getName().compareTo(CAT_SHEET_TYPE) == 0) {
                return lSheetType;
            }
        }
        return null;
    }

    private static final String PRICE_SHEET_TYPE = "Price";

    private static final String LINK_FIELD_OFFICIAL_REFERENCE =
            "CAT_PRICE_officialreference";

    private static final String LINK_FIELD_AUTHOR = "CAT_PRICE_author";

    /**
     * Tests method getLinkType
     * 
     * @throws GDMException_Exception
     *             WS Exception
     */
    public void testGetLinkType() throws GDMException_Exception {
        // get 'Cat' sheet type
        SheetType lCatSheetType =
                staticServices.getSheetTypeWithAccessControls(roleToken,
                        getProcessName(), getProductName(), null,
                        CAT_SHEET_TYPE);

        // get 'Price' sheet type
        SheetType lPriceSheetType =
                staticServices.getSheetTypeWithAccessControls(roleToken,
                        getProcessName(), getProductName(), null,
                        PRICE_SHEET_TYPE);

        // get the ID of 'CAT_PRICE' link type
        String lLinkTypeId = null;

        List<String> lLinkTypeIds =
                staticServices.getPossibleLinkTypes(roleToken,
                        lCatSheetType.getId());

        for (String lLinkTypeIdTemp : lLinkTypeIds) {
            LinkType lLinkType =
                    staticServices.getLinkTypesByKeys(roleToken,
                            Arrays.asList(lLinkTypeIdTemp)).get(0);
            if (SHEET_TYPE_LINK_NAME.equals(lLinkType.getName())) {
                lLinkTypeId = lLinkTypeIdTemp;
                break;
            }
        }

        // Get link type from 'cat' sheet
        LinkType lLinkType =
                staticServices.getLinkType(roleToken, lLinkTypeId,
                        lCatSheetType.getId());

        Field lLinkFieldOfficialReference =
                getField(lLinkType.getFields(), LINK_FIELD_OFFICIAL_REFERENCE);
        Field lLinkFieldAuthor =
                getField(lLinkType.getFields(), LINK_FIELD_AUTHOR);

        assertEquals("The field '" + LINK_FIELD_OFFICIAL_REFERENCE
                + "' is not confidential from sheet type '" + CAT_SHEET_TYPE
                + "'.", Boolean.TRUE,
                lLinkFieldOfficialReference.isConfidential());
        assertEquals(
                "The field '" + LINK_FIELD_AUTHOR
                        + "' is confidential from sheet type '"
                        + CAT_SHEET_TYPE + "'.", Boolean.FALSE,
                lLinkFieldAuthor.isConfidential());

        // Get link type from 'price' sheet
        lLinkType =
                staticServices.getLinkType(roleToken, lLinkTypeId,
                        lPriceSheetType.getId());

        lLinkFieldOfficialReference =
                getField(lLinkType.getFields(), LINK_FIELD_OFFICIAL_REFERENCE);
        lLinkFieldAuthor = getField(lLinkType.getFields(), LINK_FIELD_AUTHOR);

        assertEquals(
                "The field '" + LINK_FIELD_OFFICIAL_REFERENCE
                        + "' is confidential from sheet type '"
                        + CAT_SHEET_TYPE + "'.", Boolean.FALSE,
                lLinkFieldOfficialReference.isConfidential());
        assertEquals("The field '" + LINK_FIELD_AUTHOR
                + "' is not confidential from sheet type '" + CAT_SHEET_TYPE
                + "'.", Boolean.TRUE, lLinkFieldAuthor.isConfidential());
    }

    /**
     * Get a field by name from a list
     * 
     * @param pFields
     *            The field list
     * @param pFieldName
     *            The field name
     */
    private Field getField(List<Field> pFields, String pFieldName) {
        Field lField = null;
        for (Field lFieldTemp : pFields) {
            if (pFieldName.equals(lFieldTemp.getLabelKey())) {
                lField = lFieldTemp;
            }
            else if (lFieldTemp instanceof MultipleField) {
                lField =
                        getField(((MultipleField) lFieldTemp).getFields(),
                                pFieldName);
            }

            if (lField != null) {
                break;
            }
        }
        return lField;
    }
}