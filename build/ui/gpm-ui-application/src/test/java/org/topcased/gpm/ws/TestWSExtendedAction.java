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

import java.util.Arrays;
import java.util.List;

import org.topcased.gpm.ws.v2.client.ExtendedActionResult;
import org.topcased.gpm.ws.v2.client.ExtendedActionSummary;
import org.topcased.gpm.ws.v2.client.FieldValueData;
import org.topcased.gpm.ws.v2.client.GDMException_Exception;
import org.topcased.gpm.ws.v2.client.GuiContext;
import org.topcased.gpm.ws.v2.client.InputData;
import org.topcased.gpm.ws.v2.client.InputDataType;
import org.topcased.gpm.ws.v2.client.SheetType;

/**
 * TestWSExtendedAction
 * 
 * @author nveillet
 */
public class TestWSExtendedAction extends AbstractWSTestCase {

    private static final String EA_ALWAYS_INSTANCE_NAME = "testInputData";

    private static final int EA_ALWAYS_INSTANCE_COUNT = 2;

    private static final String EA_ALWAYS_INSTANCE_MENU = "TestInputData";

    private static final String EA_ALWAYS_INSTANCE_MENU_PARENT = "Interface";

    private static final String SHEET_REFERENCE =
            "sheet_with_some_confidential_fields_01";

    private static final String PRODUCT_NAME = "store1";

    private static final String EA_LIST_SHEET_NAME =
            "SheetWithSomeConfidentialFields_ExtentedAction";

    private static final int EA_LIST_SHEET_COUNT = 1;

    private static final String EA_LIST_SHEET_MENU =
            "SheetWithSomeConfidentialFields_ExtentedAction";

    private static final String EA_LIST_SHEET_MENU_PARENT = "Sheet";

    private static final String INPUT_DATA_TYPE_NAME = "InputDataTypeTest";

    private static final int EA_SHEET_CREATE_COUNT = 1;

    private static final List<String> EA_SHEET_CREATE =
            Arrays.asList("ExtendedAction_CF_018_Create_Sheet");

    private static final String EA_SHEET_CREATE_TEST_SHEET_TYPE =
            "SheetWithSomeConfidentialFields";

    /**
     * Tests method getAvailableExtendedActions for instance
     * 
     * @throws GDMException_Exception
     *             WS Exception
     */
    public void testGetAvailableExtendedActionsInstance()
        throws GDMException_Exception {

        List<ExtendedActionSummary> lExtendedActions =
                staticServices.getAvailableExtendedActions(roleToken,
                        Arrays.asList(GuiContext.ALWAYS), null);

        assertEquals(EA_ALWAYS_INSTANCE_COUNT, lExtendedActions.size());

        boolean lActionAvailable = false;
        for (ExtendedActionSummary lExtendedActionSummary : lExtendedActions) {
            if (EA_ALWAYS_INSTANCE_NAME.equals(lExtendedActionSummary.getExtendedActionName())) {
                lActionAvailable = true;

                // Check entry name
                assertEquals("MenuEntryName type of the action '"
                        + EA_ALWAYS_INSTANCE_NAME + "' not '"
                        + EA_ALWAYS_INSTANCE_MENU + "'.",
                        EA_ALWAYS_INSTANCE_MENU,
                        lExtendedActionSummary.getMenuEntryName());
                assertEquals("MenuEntryParentName type of the action '"
                        + EA_ALWAYS_INSTANCE_NAME + "' not '"
                        + EA_ALWAYS_INSTANCE_MENU_PARENT + "'.",
                        EA_ALWAYS_INSTANCE_MENU_PARENT,
                        lExtendedActionSummary.getMenuEntryParentName());
            }
        }
        assertTrue("Action '" + EA_ALWAYS_INSTANCE_NAME + "' not available",
                lActionAvailable);
    }

    /**
     * Tests method getAvailableExtendedActions for sheet
     * 
     * @throws GDMException_Exception
     *             WS Exception
     */
    public void testGetAvailableExtendedActionsSheet()
        throws GDMException_Exception {
        // Get a sheet
        String lSheetId =
                staticServices.getSheetByRef(roleToken, getProcessName(),
                        PRODUCT_NAME, SHEET_REFERENCE).getId();

        List<ExtendedActionSummary> lExtendedActions =
                staticServices.getAvailableExtendedActions(roleToken,
                        Arrays.asList(GuiContext.SHEET_LIST), lSheetId);

        assertEquals(EA_LIST_SHEET_COUNT, lExtendedActions.size());

        boolean lActionAvailable = false;
        for (ExtendedActionSummary lExtendedActionSummary : lExtendedActions) {
            if (EA_LIST_SHEET_NAME.equals(lExtendedActionSummary.getExtendedActionName())) {
                lActionAvailable = true;

                // Check entry name
                assertEquals("MenuEntryName type of the action '"
                        + EA_LIST_SHEET_NAME + "' not '" + EA_LIST_SHEET_MENU
                        + "'.", EA_LIST_SHEET_MENU,
                        lExtendedActionSummary.getMenuEntryName());
                assertEquals("MenuEntryParentName type of the action '"
                        + EA_LIST_SHEET_NAME + "' not '"
                        + EA_LIST_SHEET_MENU_PARENT + "'.",
                        EA_LIST_SHEET_MENU_PARENT,
                        lExtendedActionSummary.getMenuEntryParentName());
            }
        }
        assertTrue("Action '" + EA_LIST_SHEET_NAME + "' not available",
                lActionAvailable);
    }

    /**
     * Tests methods getInputDataType and getInputDataModel
     * 
     * @throws GDMException_Exception
     *             WS Exception
     */
    public void testInputData() throws GDMException_Exception {

        List<ExtendedActionSummary> lExtendedActions =
                staticServices.getAvailableExtendedActions(roleToken,
                        Arrays.asList(GuiContext.ALWAYS), null);

        ExtendedActionSummary lExtendedAction = null;
        for (ExtendedActionSummary lExtendedActionSummary : lExtendedActions) {
            if (EA_ALWAYS_INSTANCE_NAME.equals(lExtendedActionSummary.getExtendedActionName())) {
                lExtendedAction = lExtendedActionSummary;
            }
        }
        assertNotNull("Action '" + EA_ALWAYS_INSTANCE_NAME + "' not available",
                lExtendedAction);

        assertNotNull("Action '" + EA_ALWAYS_INSTANCE_NAME
                + "' don't have inputData.",
                lExtendedAction.getInputDataTypeId());

        // Check entry name
        assertEquals("MenuEntryName type of the action '"
                + EA_ALWAYS_INSTANCE_NAME + "' not '" + EA_ALWAYS_INSTANCE_MENU
                + "'.", EA_ALWAYS_INSTANCE_MENU,
                lExtendedAction.getMenuEntryName());
        assertEquals("MenuEntryParentName type of the action '"
                + EA_ALWAYS_INSTANCE_NAME + "' not '"
                + EA_ALWAYS_INSTANCE_MENU_PARENT + "'.",
                EA_ALWAYS_INSTANCE_MENU_PARENT,
                lExtendedAction.getMenuEntryParentName());

        InputDataType lInputDataType =
                staticServices.getInputDataType(roleToken,
                        lExtendedAction.getInputDataTypeId());

        assertNotNull("InputData type of the action '"
                + EA_ALWAYS_INSTANCE_NAME + "' not available", lInputDataType);

        assertEquals("InputData type of the action '" + EA_ALWAYS_INSTANCE_NAME
                + "' not '" + INPUT_DATA_TYPE_NAME + "'.",
                INPUT_DATA_TYPE_NAME, lInputDataType.getName());

        InputData lInputData =
                staticServices.getInputDataModel(roleToken,
                        lExtendedAction.getInputDataTypeId());
        assertNotNull("InputData of type '" + INPUT_DATA_TYPE_NAME
                + "' not initialized.", lInputData);

        for (FieldValueData lFieldValueData : lInputData.getFieldValues()) {
            assertNotNull("Value of inputData field '"
                    + lFieldValueData.getLabel() + "' not initialized.",
                    lFieldValueData.getValue());
        }
    }

    /**
     * Tests method executeExtendedActionByContainer
     * 
     * @throws GDMException_Exception
     *             WS Exception
     */
    public void testExecuteExtendedActionByContainer()
        throws GDMException_Exception {

        List<ExtendedActionSummary> lExtendedActions =
                staticServices.getAvailableExtendedActions(roleToken,
                        Arrays.asList(GuiContext.ALWAYS), null);

        assertEquals(EA_ALWAYS_INSTANCE_COUNT, lExtendedActions.size());

        for (ExtendedActionSummary lExtendedActionSummary : lExtendedActions) {
            if (EA_ALWAYS_INSTANCE_NAME.equals(lExtendedActionSummary.getExtendedActionName())) {
                InputData lInputData =
                        staticServices.getInputDataModel(roleToken,
                                lExtendedActionSummary.getInputDataTypeId());

                ExtendedActionResult lExtendedActionResult =
                        staticServices.executeExtendedAction(roleToken,
                                EA_ALWAYS_INSTANCE_NAME, null, null, null,
                                lInputData);

                assertEquals("The extended action '" + EA_ALWAYS_INSTANCE_NAME
                        + "' return a bad screen result.", "MESSAGE",
                        lExtendedActionResult.getResultingScreen());
                assertEquals("The extended action '" + EA_ALWAYS_INSTANCE_NAME
                        + "' return a bad screen result.", "InputData OK",
                        lExtendedActionResult.getMessage());
                break;
            }
        }
    }

    /**
     * Tests method getAvailableExtendedActions for sheet when creating sheet:<br />
     * Sheet ID null but existing sheet type ID
     * 
     * @throws GDMException_Exception
     *             WS Exception
     */
    public void testGetAvailableExtendedActionsForExtensionContainer()
        throws GDMException_Exception {
        // Get a the sheet type ID for EA_SHEET_CREATE_TEST_SHEET_TYPE
        String lTypeID = null;
        List<SheetType> lTypes =
                staticServices.getSheetTypes(roleToken, DEFAULT_PROCESS_NAME);
        for (SheetType lType : lTypes) {
            if (lType.getName().equals(EA_SHEET_CREATE_TEST_SHEET_TYPE)) {
                lTypeID = lType.getId();
                break;
            }
        }

        // Execute
        List<ExtendedActionSummary> lExtendedActions =
                staticServices.getAvailableExtendedActionsForExtensionContainer(
                        roleToken, Arrays.asList(GuiContext.CTX_CREATE_SHEET),
                        lTypeID);

        // Asserts
        assertEquals("Check expected count of Extended actions",
                EA_SHEET_CREATE_COUNT, lExtendedActions.size());

        for (ExtendedActionSummary lExtendedActionSummary : lExtendedActions) {
            if (!EA_SHEET_CREATE.contains(lExtendedActionSummary.getExtendedActionName())) {
                fail("Expected ExtendedAction \""
                        + lExtendedActionSummary.getExtendedActionName()
                        + "\" but it was not found");
            }
        }
    }
}
