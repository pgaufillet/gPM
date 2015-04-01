/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin),
 *  Vincent Hemery (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.ws.v2.client.AttachedFieldValueData;
import org.topcased.gpm.ws.v2.client.Attribute;
import org.topcased.gpm.ws.v2.client.Field;
import org.topcased.gpm.ws.v2.client.FieldValueData;
import org.topcased.gpm.ws.v2.client.FilterScope;
import org.topcased.gpm.ws.v2.client.FilterTypeData;
import org.topcased.gpm.ws.v2.client.FilterUsageEnum;
import org.topcased.gpm.ws.v2.client.GDMException_Exception;
import org.topcased.gpm.ws.v2.client.ProcessInformation;
import org.topcased.gpm.ws.v2.client.SheetData;
import org.topcased.gpm.ws.v2.client.SheetSummaryData;
import org.topcased.gpm.ws.v2.client.SheetType;

/**
 * Test the filter methods and sheet getter methods, accessing through web
 * services.
 * 
 * @author ogehin
 */
public class TestWSGetSheets extends AbstractWSTestCase {
    /** Expected filter names. */
    private static final String[] FILTER_NAMES =
            { "TEST_FILTER_2", "HMI - FilterOnSeveralLevel", "OPENED_CATS",
             "SHEETS_WITH_POINTERS", "SHEETTYPE1",
             "TEST_FILTER_WITH_SAME_NAME", "TEST_FILTER_WITH_SAME_NAME",
             "TEST_FILTER_WITH_SAME_NAME",
             "Test_RemoveFilter_CF_004_FILTER_INSTANCE", "TestMigration",
             "type_vfd_sheet_state_Filter", "type_vfd_sheet_type_Filter",
             "type_vfd_sheet_reference_Filter" };

    /** Name of filters with same name and different scopes */
    private static final String FILTER_WITH_SAME_NAME =
            "TEST_FILTER_WITH_SAME_NAME";

    /**
     * Name of the only field label key for filters with same name, for each
     * visibility scope
     */
    private static final Map<FilterScope, String> FILTER_WITH_SAME_NAME_FIELD =
            new HashMap<FilterScope, String>(3);
    static {
        FILTER_WITH_SAME_NAME_FIELD.put(FilterScope.USER_FILTER,
                "CAT_description");
        FILTER_WITH_SAME_NAME_FIELD.put(FilterScope.PRODUCT_FILTER,
                "$PRODUCT_NAME");
        FILTER_WITH_SAME_NAME_FIELD.put(FilterScope.INSTANCE_FILTER,
                "$SHEET_REFERENCE");
    }

    /** Expected filtering reference sheets */
    private static final String[] REFERENCE_SHEETS =
            { "Garfield", "Tom", "Gros Minet", "testSheet", "testLargeString",
             "Cat1", "Cat2", "Cat3" };

    /** Expected current state for Garfield sheet */
    private static final String CURRENT_STATE = "Temporary";

    /** Expected available transitions for Garfield sheet */
    private static final String[] AVAILABLE_TRANSITIONS =
            { "Delete", "Validate" };

    /** Name of group with an attached file field */
    //private static final String ATTACHED_FILE_GROUP_NAME = "Identity";
    /** Attached file field name */
    private static final String ATTACHED_FILE_FIELD_NAME = "CAT_picture";

    private static final String DEFAULT_PRODUCT_NAME = "Happy Mouse";

    /** The sheet type used for linkable sheets type. */
    private static final String SHEET_TYPE = "Cat";

    /** The sheet type that is linked to Cat. */
    private static final String LINKED_SHEET_TYPE = "Price";

    /**
     * Test the getSheetTypes method in normal conditions.
     */
    public void testGetSheetTypesNormalCase() {
        try {
            List<SheetType> lSheetTypes =
                    staticServices.getSheetTypes(roleToken,
                            DEFAULT_PROCESS_NAME);
            int lSize = lSheetTypes.size();
            int lExpectedSize = GpmTestValues.SHEET_TYPE_NAMES.length;
            List<String> lSheetTypesNames = new ArrayList<String>();
            for (SheetType lSheetType : lSheetTypes) {
                lSheetTypesNames.add(lSheetType.getName());

                // Check States
                assertNotNull("Sheet type " + lSheetType.getName()
                        + " must have states.", lSheetType.getStates());
                assertTrue("Sheet type " + lSheetType.getName()
                        + " must more to one state.",
                        lSheetType.getStates().size() > 1);
            }

            assertEquals("The method getSheetTypes returns " + lSize
                    + "sheet types instead of " + lExpectedSize + ".",
                    lExpectedSize, lSize);

            assertTrue(lSheetTypesNames.containsAll(Arrays.asList(GpmTestValues.SHEET_TYPE_NAMES)));
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the getCreatableSheetTypes method in normal conditions.
     */
    public void testGetCreatableSheetTypesNormalCase() {
        try {
            List<SheetType> lSheetTypes =
                    staticServices.getCreatableSheetTypes(roleToken,
                            DEFAULT_PROCESS_NAME);
            int lSize = lSheetTypes.size();
            int lExpectedSize = GpmTestValues.SHEET_TYPE_NAMES.length;
            assertEquals("The method getCreatableSheetTypes returns " + lSize
                    + "sheet types instead of " + lExpectedSize + ".",
                    lExpectedSize, lSize);
            List<String> lSheetTypesNames = new ArrayList<String>();
            for (SheetType lSheetType : lSheetTypes) {
                lSheetTypesNames.add(lSheetType.getName());
            }
            assertTrue(
                    "The method getCreatableSheetTypes doesn't return all creatable sheet types",
                    lSheetTypesNames.containsAll(Arrays.asList(GpmTestValues.SHEET_TYPE_NAMES)));
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the getVisibleExecutableFilterNamesByFilterType method in normal
     * conditions.
     */
    public void testGetVisibleExecutableFilterNamesByFilterTypeNormalCase() {
        try {
            List<String> lFilterNames =
                    staticServices.getVisibleExecutableFilterNamesByFilterType(
                            roleToken, DEFAULT_PROCESS_NAME,
                            DEFAULT_PRODUCT_NAME, getLogin()[0],
                            FilterTypeData.SHEET, FilterUsageEnum.BOTH_VIEWS);
            int lSize = lFilterNames.size();
            int lExpectedSize = FILTER_NAMES.length;
            assertEquals("The method getBusinessProcessNames returns " + lSize
                    + "filter names instead of " + lExpectedSize + ".",
                    lExpectedSize, lSize);
            assertTrue("",
                    lFilterNames.containsAll(Arrays.asList(FILTER_NAMES)));
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the executeSheetFilter method in normal conditions.
     */
    public void testExecuteSheetFilterNormalCase() {
        try {
            List<SheetSummaryData> lSheetSummaryDatas =
                    staticServices.executeSheetFilter(roleToken,
                            DEFAULT_PROCESS_NAME, DEFAULT_PRODUCT_NAME,
                            getLogin()[0], FILTER_NAMES[0]);
            int lSize = lSheetSummaryDatas.size();
            int lExpectedSize = REFERENCE_SHEETS.length;
            assertEquals("The method executeSheetFilter returns " + lSize
                    + " results instead of " + lExpectedSize + ".",
                    lExpectedSize, lSize);
            List<String> lReferences = new ArrayList<String>();
            for (SheetSummaryData lSheetSummaryData : lSheetSummaryDatas) {
                lReferences.add(lSheetSummaryData.getSheetReference());
            }
            assertTrue(
                    "The method executeSheetFilter doesn't return all expected results",
                    lReferences.containsAll(Arrays.asList(REFERENCE_SHEETS)));
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the executeFilter method in normal conditions.
     */
    public void testExecuteFilterNormalCase() {
        try {
            List<String> lSheetIds =
                    staticServices.executeFilter(roleToken,
                            DEFAULT_PROCESS_NAME, null, null, FILTER_NAMES[0]);
            int lSize = lSheetIds.size();
            int lExpectedSize = REFERENCE_SHEETS.length;
            assertEquals("The method executeSheetFilter returns " + lSize
                    + " results instead of " + lExpectedSize + ".",
                    lExpectedSize, lSize);
            List<String> lReferences = new ArrayList<String>();
            Collection<SheetData> lSheets =
                    staticServices.getSheetsByKeys(roleToken, lSheetIds);
            for (SheetData lSheet : lSheets) {
                lReferences.add(lSheet.getReference());
            }
            assertTrue(
                    "The method executeFilter doesn't return all expected results",
                    lReferences.containsAll(Arrays.asList(REFERENCE_SHEETS)));
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the executeSheetFilterWithScope method with different filters with
     * same name and different scopes.
     * 
     * @author vhemery
     */
    public void testExecuteSheetFilterWithScopeNormalCase() {
        try {
            for (FilterScope lScope : FilterScope.values()) {
                List<SheetSummaryData> lSheetSummaryDatas =
                        staticServices.executeSheetFilterWithScope(roleToken,
                                -1, 0, DEFAULT_PRODUCT_NAME,
                                FILTER_WITH_SAME_NAME, lScope);

                int lSize = lSheetSummaryDatas.size();
                int lExpectedSize = REFERENCE_SHEETS.length;
                assertEquals("The method executeSheetFilterWithScope returns "
                        + lSize + " results instead of " + lExpectedSize
                        + " with scope " + lScope.toString(), lExpectedSize,
                        lSize);
                // REFERENCE_SHEETS.length > 0
                assertTrue(
                        "The summaries returned by method "
                                + "executeSheetFilterWithScope contain no field with scope "
                                + lScope.toString(),
                        lSheetSummaryDatas.get(0).getFieldSummaryDatas().size() > 0);
                String lFilterFieldName =
                        lSheetSummaryDatas.get(0).getFieldSummaryDatas().get(0).getLabelKey();
                assertEquals("The method executeSheetFilterWithScope does not "
                        + "execute the right filter with scope "
                        + lScope.toString(), lFilterFieldName,
                        FILTER_WITH_SAME_NAME_FIELD.get(lScope));
            }
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the getSheetByKey method in normal conditions.
     */
    public void testGetSheetByKeyNormalCase() {
        try {
            List<SheetSummaryData> lSheetSummaryDatas =
                    staticServices.executeSheetFilter(roleToken,
                            DEFAULT_PROCESS_NAME, DEFAULT_PRODUCT_NAME,
                            getLogin()[0], FILTER_NAMES[0]);
            String lSheetKey = "";
            for (SheetSummaryData lSheetSummaryData : lSheetSummaryDatas) {
                if (lSheetSummaryData.getSheetReference().equals(
                        REFERENCE_SHEETS[0])) {
                    lSheetKey = lSheetSummaryData.getId();
                }
            }
            assertNotNull("An error occcured when retriving a sheet key",
                    lSheetKey);
            List<String> lKeys = generateList(lSheetKey, null);
            List<SheetData> lSheets =
                    staticServices.getSheetsByKeys(roleToken, lKeys);
            assertNotNull("The getSheetByKey method failed with a correct key",
                    lSheets);
            assertEquals("The getSheetByKey method returns a bad reference",
                    REFERENCE_SHEETS[0], lSheets.get(0).getReference());
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the getSheetByKey method with an empty key list.
     */
    public void testGetSheetByKeyWithEmptyList() {
        try {
            List<String> lKeys = new ArrayList<String>();
            List<SheetData> lSheets =
                    staticServices.getSheetsByKeys(roleToken, lKeys);
            assertNotNull(
                    "The getSheetByKey method failed with an empty key list",
                    lSheets);
            assertEquals("The getSheetByKey method returns a bad sheets list",
                    lSheets.size(), 0);

        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the getSheetByRefmethod in normal conditions.
     */
    public void testGetSheetByRefNormalCase() {
        try {
            List<String> lRefs = generateList(REFERENCE_SHEETS[0], null);
            List<SheetData> lSheets =
                    staticServices.getSheetsByRefs(roleToken,
                            DEFAULT_PROCESS_NAME, DEFAULT_PRODUCT_NAME, lRefs);
            assertNotNull("The getSheetByRef method failed with a correct key",
                    lSheets);
            assertEquals("The getSheetByRef method returns a bad reference",
                    REFERENCE_SHEETS[0], lSheets.get(0).getReference());
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the getSheetProcessInformation method in normal conditions.
     */
    public void testGetSheetProcessInformationNormalCase() {
        try {
            List<SheetSummaryData> lSheetSummaryDatas =
                    staticServices.executeSheetFilter(roleToken,
                            DEFAULT_PROCESS_NAME, DEFAULT_PRODUCT_NAME,
                            getLogin()[0], FILTER_NAMES[0]);
            String lSheetKey = "";
            for (SheetSummaryData lSheetSummaryData : lSheetSummaryDatas) {
                if (lSheetSummaryData.getSheetReference().equals(
                        REFERENCE_SHEETS[0])) {
                    lSheetKey = lSheetSummaryData.getId();
                }
            }
            assertNotNull("An error occcured when retriving a sheet key",
                    lSheetKey);
            List<ProcessInformation> lProcessInformations =
                    staticServices.getSheetProcessInformations(roleToken,
                            generateList(lSheetKey, null));
            assertNotNull(
                    "The getSheetProcessInformation method failed with a correct key",
                    lProcessInformations.get(0));
            assertEquals(
                    "The getSheetProcessInformation method returns a bad current state",
                    lProcessInformations.get(0).getCurrentState(),
                    CURRENT_STATE);
            assertTrue(
                    "The getSheetProcessInformation method returns bad transitions",
                    lProcessInformations.get(0).getTransitions().containsAll(
                            Arrays.asList(AVAILABLE_TRANSITIONS)));
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test the getAttachedFileContent method in normal conditions.
     */
    public void testGetAttachedFileContentNormalCase() {
        try {
            List<SheetSummaryData> lSheetSummaryDatas =
                    staticServices.executeSheetFilter(roleToken,
                            DEFAULT_PROCESS_NAME, DEFAULT_PRODUCT_NAME,
                            getLogin()[0], FILTER_NAMES[0]);
            String lSheetKey = "";
            for (SheetSummaryData lSheetSummaryData : lSheetSummaryDatas) {
                if (lSheetSummaryData.getSheetReference().equals(
                        REFERENCE_SHEETS[1])) {
                    lSheetKey = lSheetSummaryData.getId();
                }
            }
            assertNotNull("An error occcured when retriving a sheet key",
                    lSheetKey);
            List<String> lKeys = generateList(lSheetKey, null);
            List<SheetData> lSheetDatas =
                    staticServices.getSheetsByKeys(roleToken, lKeys);
            List<FieldValueData> lFieldValueDatas =
                    lSheetDatas.get(0).getFieldValues();
            AttachedFieldValueData lFileField = null;
            for (FieldValueData lFieldValueData : lFieldValueDatas) {
                if (lFieldValueData.getName().equals(ATTACHED_FILE_FIELD_NAME)) {
                    lFileField = (AttachedFieldValueData) lFieldValueData;
                }
            }
            assertNotNull(
                    "An error occcured when retriving an AttachedFieldvalueData",
                    lFileField);
            String lFileId = lFileField.getId();
            assertNotNull("An error occcured when retriving a File Id", lFileId);

            byte[] lFile =
                    staticServices.getAttachedFileContent(roleToken, lFileId);
            assertNotNull("The getAttachedFileContent method failed", lFile);
        }
        catch (GDMException_Exception e) {
            fail("no Exception was expected instead of a "
                    + e.getClass().getName());
        }
    }

    /**
     * Test getter on sheet state
     */
    public void testGetSheetState() {
        try {
            List<String> lSheetRefs = new ArrayList<String>(1);
            lSheetRefs.add(REFERENCE_SHEETS[0]);
            SheetData lSheetData =
                    (staticServices.getSheetsByRefs(roleToken,
                            getProcessName(), DEFAULT_PRODUCT_NAME, lSheetRefs)).get(0);
            String lSheetState =
                    staticServices.getSheetState(lSheetData.getId());
            assertEquals(lSheetData.getState(), lSheetState);
        }
        catch (GDMException_Exception e) {
            fail("A GDM exception has been thrown.");
        }

    }

    private static final String CONTAINER_WITH_ACCESS_CONTROL =
            "TestPointerFields2";

    private static final String ACCESS_CONTROL_PRODUCT_NAME = "product1_2";

    private static final String ACCESS_CONTROL_ATTRIBUTE_NAME = "hidden";

    private static final String ACCESS_CONTROL_ATTRIBUTE_VALUE = "true";

    /**
     * test method getSheetTypeWithAccessControls
     */
    public void testGetSheetTypeWithAccessControls() {

        try {
            String lRoleToken =
                    staticServices.selectRole(userToken, DEFAULT_ROLE,
                            ACCESS_CONTROL_PRODUCT_NAME, getProcessName());

            // Add all sheet type in cache before calling.
            staticServices.getSheetTypes(lRoleToken, getProcessName());

            SheetType lSheetType =
                    staticServices.getSheetTypeWithAccessControls(lRoleToken,
                            getProcessName(), ACCESS_CONTROL_PRODUCT_NAME,
                            null, CONTAINER_WITH_ACCESS_CONTROL);
            assertTrue(lSheetType.isConfidential());
            assertFalse(lSheetType.isCreatable());
            assertFalse(lSheetType.isDeletable());
            assertFalse(lSheetType.isUpdatable());
            assertTrue(lSheetType.isSelectable());
            assertFalse(lSheetType.isVersionable());
            assertNotNull(lSheetType.getAttributes());
            boolean lIsFound = false;
            for (Attribute lAttribute : lSheetType.getAttributes()) {
                if (ACCESS_CONTROL_ATTRIBUTE_NAME.equals(lAttribute.getName())) {
                    lIsFound = true;
                    assertEquals(ACCESS_CONTROL_ATTRIBUTE_VALUE,
                            lAttribute.getValues().get(0));
                }
            }
            assertTrue("Type access control extended attributes "
                    + "have not been transmitted to sheet type.", lIsFound);

            // Check States
            assertNotNull("Sheet type " + lSheetType.getName()
                    + " must have states.", lSheetType.getStates());
            assertTrue("Sheet type " + lSheetType.getName()
                    + " must more to one state.",
                    lSheetType.getStates().size() > 1);

        }
        catch (GDMException_Exception e) {
            fail("A GDM exception has been thrown.");
        }
    }

    /** The Constant CHOICE_STRING_FIELD_NAME. */
    private static final String CHOICE_STRING_FIELD_NAME =
            "SHEETTYPE1_simpleChoiceStringMul";

    private static final String[] EXPECTED_CHOICES_STRING =
            new String[] { "Computed Choice #1", "Computed Choice #2" };

    /**
     * Test normal case.
     * 
     * @throws GDMException_Exception
     *             WS Exception
     */
    public void testGetChoiceStringDisplayHint() throws GDMException_Exception {
        SheetType lSheetType;
        lSheetType =
                staticServices.getSheetTypeWithAccessControls(roleToken,
                        GpmTestValues.PROCESS_NAME, null, null,
                        GpmTestValues.SHEET_TYPE_SHEETTYPE1);
        String lFieldId = null;
        for (Field lField : lSheetType.getFields()) {
            if (CHOICE_STRING_FIELD_NAME.equals(lField.getLabelKey())) {
                lFieldId = lField.getId();
                break;
            }
        }
        assertNotNull("Field cannot be found", lFieldId);
        List<String> lChoices;
        lChoices =
                staticServices.getChoiceStringList(roleToken,
                        lSheetType.getId(), lFieldId, null);

        assertEquals("Invalid choices count", EXPECTED_CHOICES_STRING.length,
                lChoices.size());
        assertEquals(EXPECTED_CHOICES_STRING[0], lChoices.get(0));
        assertEquals(EXPECTED_CHOICES_STRING[1], lChoices.get(1));
    }

    /**
     * Test getter on linkable sheet types
     * 
     * @throws GDMException_Exception
     *             A GDM Exception
     */
    public void testGetLinkableSheetTypes() throws GDMException_Exception {

        List<SheetType> lSheetTypes =
                staticServices.getSheetTypes(roleToken, getProcessName());

        // get THE sheet type
        SheetType lSheetType = null;
        SheetType lLinkedSheetType = null;
        for (int i = 0; i < lSheetTypes.size(); i++) {
            if (lSheetTypes.get(i).getName().equals(SHEET_TYPE)) {
                lSheetType = lSheetTypes.get(i);
            }
            else if (lSheetTypes.get(i).getName().equals(LINKED_SHEET_TYPE)) {
                lLinkedSheetType = lSheetTypes.get(i);
            }
        }

        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lSheetType);

        List<String> lLinkableSheetTypesIds =
                staticServices.getLinkableSheetTypes(roleToken,
                        lSheetType.getId());

        assertNotNull(
                "getLinkableSheetTypes returns null instead of a list of id of sheet types",
                lLinkableSheetTypesIds);

        assertFalse("getLinkableSheetTypes returns no link (\""
                + LINKED_SHEET_TYPE + "\" should be linked)",
                lLinkableSheetTypesIds.isEmpty());

        // We must have only one result !
        assertTrue("Sheet type " + SHEET_TYPE
                + " should only be linked at least to one sheet type \""
                + LINKED_SHEET_TYPE + "\"", lLinkableSheetTypesIds.size() >= 1);

        // Let's verifing that sheet type exists and that is
        // Price

        assertNotNull("Sheet type " + LINKED_SHEET_TYPE + " not found.",
                lLinkedSheetType);

        assertTrue("Sheet type " + SHEET_TYPE
                + " should be linked at least to " + LINKED_SHEET_TYPE,
                lLinkableSheetTypesIds.contains(lLinkedSheetType.getId()));
    }
}