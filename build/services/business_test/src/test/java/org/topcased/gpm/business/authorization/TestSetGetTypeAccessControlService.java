/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.authorization.service.TypeAccessControlData;

/**
 * Tests the methods
 * <CODE>getTypeAccessControl<CODE> and <CODE>setTypeAccessControl<CODE>
 * of the Authorization Service.
 * 
 * @author mkargbo
 */
public class TestSetGetTypeAccessControlService extends
        AbstractBusinessServiceTestCase {

    private static final String TYPE_NAME =
            GpmTestValues.SHEET_TYPE_CONTROL_TYPE;

    private static final String TYPE_NAME_WITH_NO_AC =
            GpmTestValues.SHEET_TYPE_CONTROL_TYPE2;

    private static final String XML_INSTANCE_TEST =
            "authorization/TestTypeAccessControlExtendedAttributes.xml";

    private static final String EXTENDED_ATTRIBUTE_TYPENAME =
            GpmTestValues.SHEET_TYPE_CONTROL_TYPE2;

    private static final String EXTENDED_ATTRIBUTE_ATTRIBUTES_TYPE = "product";

    private static final String[] EXTENDED_ATTRIBUTE_ATTRIBUTES_FIELDLIST =
            { "field1", "field2" };

    /**
     * Test get and set type access control
     */
    public void testNormalCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Creating the Application Access Control
        //Get type id
        fieldsService = serviceLocator.getFieldsService();
        String lTypeId =
                getFieldsContainerService().getFieldsContainerId(
                        adminRoleToken, TYPE_NAME);

        TypeAccessControlData lAccessControl =
                new TypeAccessControlData(true, true, true, true);

        lAccessControl.setBusinessProcessName(getProcessName());
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setProductName(null);
        lAccessControlContextData.setRoleName(null);
        lAccessControlContextData.setContainerTypeId(lTypeId);
        lAccessControlContextData.setStateName(null);
        lAccessControl.setContext(lAccessControlContextData);
        authorizationService.setTypeAccessControl(lAccessControl);

        // Retrieving the Type Access Control
        lAccessControl =
                authorizationService.getTypeAccessControl(adminRoleToken,
                        lAccessControlContextData);

        assertNotNull("The method getTypeAccessControl returns null.",
                lAccessControl);

        // Verifies the fields
        String lProcessName = lAccessControl.getBusinessProcessName();
        assertEquals("Process name expected = " + getProcessName()
                + ". Process name actual = " + lProcessName + ".",
                getProcessName(), lProcessName);

        String lRetrivingTypeId =
                lAccessControl.getContext().getContainerTypeId();
        assertEquals("Type id expected = " + lTypeId + ". Type id actual = "
                + lRetrivingTypeId + ".", lTypeId, lRetrivingTypeId);

        assertTrue(lAccessControl.getCreatable());
        assertTrue(lAccessControl.getUpdatable());
        assertTrue(lAccessControl.getDeletable());
        assertTrue(lAccessControl.getConfidential());
    }

    /**
     * Test get type access control for a type with no access control.
     */
    public void testTypeWithNoAC() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        //Get type id
        fieldsService = serviceLocator.getFieldsService();
        String lTypeId =
                getFieldsContainerService().getFieldsContainerId(
                        adminRoleToken, TYPE_NAME_WITH_NO_AC);

        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setProductName(null);
        lAccessControlContextData.setRoleName(null);
        lAccessControlContextData.setContainerTypeId(lTypeId);
        lAccessControlContextData.setStateName(null);

        // Retrieving the Type Access Control
        TypeAccessControlData lAccessControl =
                authorizationService.getTypeAccessControl(adminRoleToken,
                        lAccessControlContextData);

        assertNotNull("The method getTypeAccessControl returns null.",
                lAccessControl);

        // Verifies the fields
        String lProcessName = lAccessControl.getBusinessProcessName();
        assertEquals("Process name expected = " + getProcessName()
                + ". Process name actual = " + lProcessName + ".",
                getProcessName(), lProcessName);

        String lRetrivingTypeId =
                lAccessControl.getContext().getContainerTypeId();
        assertEquals("Type id expected = " + lTypeId + ". Type id actual = "
                + lRetrivingTypeId + ".", lTypeId, lRetrivingTypeId);

        assertTrue(lAccessControl.getCreatable());
        assertTrue(lAccessControl.getUpdatable());
        assertTrue(lAccessControl.getDeletable());
        assertFalse(lAccessControl.getConfidential());
    }

    /**
     * Tests the getTypeAccessControl with extended attributes method.
     */
    public void testNormalWithExtendedAttribute() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // set field access control with extended attributes
        instantiate(getProcessName(), XML_INSTANCE_TEST);

        // Retrieving the Application Access Control
        String lContainerId =
                getFieldsContainerService().getFieldsContainerId(
                        adminRoleToken, EXTENDED_ATTRIBUTE_TYPENAME);
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setContainerTypeId(lContainerId);
        TypeAccessControlData lTypeAccessControlData =
                authorizationService.getTypeAccessControl(adminRoleToken,
                        lAccessControlContextData);

        assertNotNull("The method getTypeAccessControl returns null.",
                lTypeAccessControlData);

        // Verifies the fields
        String lProcessName = lTypeAccessControlData.getBusinessProcessName();

        assertEquals("Process name expected = " + getProcessName()
                + ". Process name actual = " + lProcessName + ".",
                getProcessName(), lProcessName);

        assertNull("The state name must be null",
                lTypeAccessControlData.getContext().getStateName());
        assertNull("The product name must be null",
                lTypeAccessControlData.getContext().getProductName());

        String lACContainerTypeId =
                lTypeAccessControlData.getContext().getContainerTypeId();
        assertEquals("Container type id is not correct.", lContainerId,
                lACContainerTypeId);

        assertNotNull(
                "The transition access control must have extended attributes",
                lTypeAccessControlData.getExtendedAttributes());
        assertFalse(
                "The transition access control must have extended attributes",
                lTypeAccessControlData.getExtendedAttributes().length < 1);

        assertEquals(
                EXTENDED_ATTRIBUTE_ATTRIBUTES_TYPE,
                lTypeAccessControlData.getExtendedAttributes()[1].getValues()[0]);
        assertEquals(
                EXTENDED_ATTRIBUTE_ATTRIBUTES_FIELDLIST[0],
                lTypeAccessControlData.getExtendedAttributes()[0].getValues()[0]);
        assertEquals(
                EXTENDED_ATTRIBUTE_ATTRIBUTES_FIELDLIST[1],
                lTypeAccessControlData.getExtendedAttributes()[0].getValues()[1]);
    }
}
