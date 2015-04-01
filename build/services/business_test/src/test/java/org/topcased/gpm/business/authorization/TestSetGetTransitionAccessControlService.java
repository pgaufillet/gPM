/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin), Sébastien René
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.authorization.service.TransitionAccessControlData;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the methods <CODE>getTransitionAccessControl<CODE> and
 * <CODE>setTransitionAccessControl<CODE>
 * of the Authorization Service.
 * 
 * @author nsamson
 */
public class TestSetGetTransitionAccessControlService extends
        AbstractBusinessServiceTestCase {

    /** The Product Service. */
    private AuthorizationService authorizationService;

    /** The transition name. */
    private static final String TRANSITION_NAME = "Close";

    /** The new role name. */
    private static final String ROLE_NAME = GpmTestValues.USER_ADMIN;

    /** The ovverridden role name. */
    private static final String OVERRIDDEN_ROLE_NAME = "restricted";

    /** The new role name. */
    private static final String SHEET_TYPE_NAME = GpmTestValues.SHEET_TYPE_DOG;

    /** The sheet */
    private static final String SHEET_REF = "Medor";

    /** The new role name. */
    private static final String STATE_NAME = "Open";

    private static final String XML_INSTANCE_TEST =
            "authorization/TestTransitionAccessControlExtendedAttributes.xml";

    private static final String EXTENDED_ATTRIBUTE_TRANSITIONNAME =
            "TestACTransition1";

    private static final String EXTENDED_ATTRIBUTE_SHEETTYPE =
            GpmTestValues.SHEET_TYPE_CONTROL_TYPE2;

    private static final String EXTENDED_ATTRIBUTE_ATTRIBUTES_FROM = "sheet1";

    private static final String EXTENDED_ATTRIBUTE_ATTRIBUTES_TO = "sheet2";

    private String containerId;

    protected void setUp() {
        super.setUp();

        // Get an attribute container.  (we use a sheet type id for this).
        SheetService lSheetService = serviceLocator.getSheetService();

        CacheableSheetType lTypeData =
                lSheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE_NAME,
                        CacheProperties.IMMUTABLE);

        containerId = lTypeData.getId();
    }

    /**
     * Tests the getTransitionAccessControl and setTransitionAccessControl
     * methods.
     */
    public void testNormalCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Creating the Sheet Access Control
        TransitionAccessControlData lAccessControl =
                new TransitionAccessControlData(true, TRANSITION_NAME);
        lAccessControl.setBusinessProcessName(getProcessName());
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setProductName(getProductName());
        lAccessControlContextData.setRoleName(ROLE_NAME);
        lAccessControlContextData.setContainerTypeId(containerId);
        lAccessControlContextData.setStateName(STATE_NAME);
        lAccessControl.setContext(lAccessControlContextData);
        authorizationService.setTransitionAccessControl(lAccessControl);

        // Retrieving the Transition Access Control
        lAccessControl =
                authorizationService.getTransitionAccessControl(adminRoleToken,
                        lAccessControlContextData, TRANSITION_NAME);

        assertNotNull("The method getTransitionAccessControl returns null.",
                lAccessControl);

        // Verifies the fields
        String lProcessName = lAccessControl.getBusinessProcessName();
        assertEquals("Process name expected = " + getProcessName()
                + ". Process name actual = " + lProcessName + ".",
                getProcessName(), lProcessName);

        String lRoleName = lAccessControl.getContext().getRoleName();
        assertEquals("Role name expected = " + ROLE_NAME
                + ". Role name actual = " + lRoleName + ".", ROLE_NAME,
                lRoleName);

        String lSheetTypeId = lAccessControl.getContext().getContainerTypeId();
        assertEquals("Sheet type id expected = " + containerId
                + ". Sheet type id actual = " + lSheetTypeId + ".",
                containerId, lSheetTypeId);

        String lStateName = lAccessControl.getContext().getStateName();
        assertEquals("State name expected = " + STATE_NAME
                + ". State name actual = " + lStateName + ".", STATE_NAME,
                lStateName);

        String lTypeId = lAccessControl.getContext().getContainerTypeId();
        assertEquals("Type id name expected = " + containerId
                + ". Type id actual = " + lTypeId + ".", containerId, lTypeId);

        boolean lAllowed = lAccessControl.getAllowed();
        assertTrue("The access control have been set be allowed, "
                + "the retrieving transition access which allowed.", lAllowed);
    }

    /**
     * Tests the getTransitionAccessControl with extended attributes method.
     */
    public void testNormalWithExtendedAttribute() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // set field access control with extended attributes
        instantiate(getProcessName(), XML_INSTANCE_TEST);

        // Retrieving the Transition Access Control
        String lContainerId =
                getFieldsContainerService().getFieldsContainerId(
                        adminRoleToken, EXTENDED_ATTRIBUTE_SHEETTYPE);
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setContainerTypeId(lContainerId);
        TransitionAccessControlData lTransitionAccessControlData =
                authorizationService.getTransitionAccessControl(adminRoleToken,
                        lAccessControlContextData,
                        EXTENDED_ATTRIBUTE_TRANSITIONNAME);

        assertNotNull("The method getTransitionAccessControl returns null.",
                lTransitionAccessControlData);

        // Verifies the fields
        String lProcessName =
                lTransitionAccessControlData.getBusinessProcessName();

        assertEquals("Process name expected = " + getProcessName()
                + ". Process name actual = " + lProcessName + ".",
                getProcessName(), lProcessName);

        String lTransitionName =
                lTransitionAccessControlData.getTransitionName();
        assertEquals("Transition name", EXTENDED_ATTRIBUTE_TRANSITIONNAME,
                lTransitionName);

        assertNull("The state name must be null",
                lTransitionAccessControlData.getContext().getStateName());
        assertNull("The product name must be null",
                lTransitionAccessControlData.getContext().getProductName());

        String lACContainerTypeId =
                lTransitionAccessControlData.getContext().getContainerTypeId();
        assertEquals("Container type id is not correct.", lContainerId,
                lACContainerTypeId);

        assertNotNull(
                "The transition access control must have extended attributes",
                lTransitionAccessControlData.getExtendedAttributes());
        assertFalse(
                "The transition access control must have extended attributes",
                lTransitionAccessControlData.getExtendedAttributes().length < 1);

        assertEquals(
                EXTENDED_ATTRIBUTE_ATTRIBUTES_FROM,
                lTransitionAccessControlData.getExtendedAttributes()[0].getValues()[0]);
        assertEquals(
                EXTENDED_ATTRIBUTE_ATTRIBUTES_TO,
                lTransitionAccessControlData.getExtendedAttributes()[1].getValues()[0]);
    }

    /**
     * Tests the getTransitionAccessControl method with overridden role.
     */
    public void testWithOverriddenRole() {
        String lSheetId =
                sheetService.getSheetIdByReference(getProcessName(),
                        GpmTestValues.PRODUCT_BERNARD_STORE_NAME, SHEET_REF);

        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Creating the Transition Access Control
        TransitionAccessControlData lAccessControl =
                new TransitionAccessControlData(true, TRANSITION_NAME);
        lAccessControl.setBusinessProcessName(getProcessName());
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setProductName(getProductName());
        lAccessControlContextData.setRoleName(ROLE_NAME);
        lAccessControlContextData.setContainerTypeId(containerId);
        lAccessControlContextData.setStateName(STATE_NAME);
        lAccessControlContextData.setValuesContainerId(lSheetId);
        lAccessControl.setContext(lAccessControlContextData);
        authorizationService.setTransitionAccessControl(lAccessControl);

        TransitionAccessControlData lAccessControl2 =
                new TransitionAccessControlData(true, TRANSITION_NAME);
        lAccessControl2.setBusinessProcessName(getProcessName());
        lAccessControl2.setAllowed(false);
        AccessControlContextData lAccessControlContextData2 =
                new AccessControlContextData();
        lAccessControlContextData2.setProductName(getProductName());
        lAccessControlContextData2.setRoleName(OVERRIDDEN_ROLE_NAME);
        lAccessControlContextData2.setContainerTypeId(containerId);
        lAccessControlContextData2.setStateName(STATE_NAME);
        lAccessControlContextData.setValuesContainerId(lSheetId);
        lAccessControl2.setContext(lAccessControlContextData2);
        authorizationService.setTransitionAccessControl(lAccessControl2);

        // Setting the overridden role
        authorizationService.setOverriddenRole(adminRoleToken, lSheetId,
                ADMIN_LOGIN[0], ROLE_NAME, OVERRIDDEN_ROLE_NAME);

        // Retrieving the Transition Access Control
        lAccessControl =
                authorizationService.getTransitionAccessControl(adminRoleToken,
                        lAccessControlContextData, TRANSITION_NAME);

        assertNotNull(
                "The method getTransitionActionAccessControl returns null.",
                lAccessControl);

        // Verifies the fields
        String lProcessName = lAccessControl.getBusinessProcessName();
        assertEquals("Process name expected = " + getProcessName()
                + ". Process name actual = " + lProcessName + ".",
                getProcessName(), lProcessName);

        String lRoleName = lAccessControl.getContext().getRoleName();
        assertEquals("Role name expected = " + OVERRIDDEN_ROLE_NAME
                + ". Role name actual = " + lRoleName + ".",
                OVERRIDDEN_ROLE_NAME, lRoleName);

        String lSheetTypeId = lAccessControl.getContext().getContainerTypeId();
        assertEquals("Sheet type id expected = " + containerId
                + ". Sheet type id actual = " + lSheetTypeId + ".",
                containerId, lSheetTypeId);

        String lStateName = lAccessControl.getContext().getStateName();
        assertEquals("State name expected = " + STATE_NAME
                + ". State name actual = " + lStateName + ".", STATE_NAME,
                lStateName);

        String lTypeId = lAccessControl.getContext().getContainerTypeId();
        assertEquals("Type id name expected = " + containerId
                + ". Type id actual = " + lTypeId + ".", containerId, lTypeId);

        boolean lAllowed = lAccessControl.getAllowed();
        assertFalse("The access control have been set not allowed, "
                + "the retrieving transition access which allowed.", lAllowed);

    }
}