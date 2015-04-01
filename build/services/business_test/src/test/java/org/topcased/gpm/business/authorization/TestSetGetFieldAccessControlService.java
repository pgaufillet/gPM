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
import org.topcased.gpm.business.authorization.service.FieldAccessControlData;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.fields.FieldAccessData;
import org.topcased.gpm.business.fields.FieldTypeData;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the methods
 * <CODE>getFieldAccessControl<CODE> and <CODE>setFieldAccessControl<CODE>
 * of the Authorization Service.
 * 
 * @author nsamson
 */
public class TestSetGetFieldAccessControlService extends
        AbstractBusinessServiceTestCase {

    /** The field name. */
    private static final String FIELD_NAME = "DOG_ref";

    private static final String CONTROL_TYPE_FIELD1 = "ControlType_Field1";

    private static final String FIELD2 = "Field2";

    /** The new role name. */
    private static final String ROLE_NAME = GpmTestValues.USER_ADMIN;

    /** The overridden role name. */
    private static final String OVERRIDDEN_ROLE_NAME = "restricted";

    /** The sheet type name. */
    private static final String SHEET_TYPE_NAME = GpmTestValues.SHEET_TYPE_DOG;

    private static final String CONTROL_TYPE =
            GpmTestValues.SHEET_TYPE_CONTROL_TYPE;

    private static final String CONTROL_TYPE2 =
            GpmTestValues.SHEET_TYPE_CONTROL_TYPE2;

    /** The sheet */
    private static final String SHEET_REF = "Medor";

    /** The state name. */
    private static final String STATE_NAME = "Closed";

    /** mandatory rights of the Field1 for Control Type */
    private static final Boolean CONTROL_TYPE_FIELD1_MANDATORY = true;

    /** updatable rights of the Field1 for Control Type */
    private static final Boolean CONTROL_TYPE_FIELD1_UPDATABLE = false;

    /** confidential rights of the Field1 for Control Type */
    private static final Boolean CONTROL_TYPE_FIELD1_CONFIDENTIAL = true;

    /** exportable rights of the Field1 for Control Type */
    private static final Boolean CONTROL_TYPE_FIELD1_EXPORTABLE = false;

    private static final String XML_INSTANCE_TEST =
            "authorization/TestFieldAccessControlExtendedAttributes.xml";

    private static final String EXTENDED_ATTRIBUTE_FIELDLABEL = "Field2";

    private static final String EXTENDED_ATTRIBUTE_CONTAINERTYPE =
            GpmTestValues.SHEET_TYPE_CONTROL_TYPE2;

    private static final String EXTENDED_ATTRIBUTE_ATTRIBUTES_CONTAINERTYPE =
            "sheet";

    private static final String EXTENDED_ATTRIBUTE_ATTRIBUTES_FIELDTYPE =
            "string";

    private String containerId;

    private String containerIdControlType;

    private String containerIdControlType2;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    protected void setUp() {
        super.setUp();

        // Get an attribute container.  (we use a sheet type id for this).
        SheetService lSheetService = serviceLocator.getSheetService();

        CacheableSheetType lType =
                lSheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE_NAME,
                        CacheProperties.IMMUTABLE);

        containerId = lType.getId();

        CacheableSheetType lTypeDataControlType =
                lSheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), CONTROL_TYPE,
                        CacheProperties.IMMUTABLE);

        containerIdControlType = lTypeDataControlType.getId();

        CacheableSheetType lTypeDataControlType2 =
                lSheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), CONTROL_TYPE2,
                        CacheProperties.IMMUTABLE);
        containerIdControlType2 = lTypeDataControlType2.getId();
    }

    /**
     * Tests the getFieldAccessControl and setFieldAccessControl methods.
     */
    public void testNormalCase() {
        // Creating the Application Access Control
        FieldAccessControlData lAccessControl = new FieldAccessControlData();
        FieldAccessData lAccess = new FieldAccessData();

        lAccess.setConfidential(false);
        lAccess.setExportable(true);
        lAccess.setMandatory(false);
        lAccess.setUpdatable(true);

        lAccessControl.setAccess(lAccess);

        lAccessControl.setFieldName(FIELD_NAME);
        lAccessControl.setBusinessProcessName(getProcessName());

        // Note: We cannot use a product name here, as the current role is
        // an admin one (not attached to a product)
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setRoleName(ROLE_NAME);
        lAccessControlContextData.setContainerTypeId(containerId);
        lAccessControlContextData.setStateName(STATE_NAME);
        lAccessControl.setContext(lAccessControlContextData);
        lAccessControlContextData.setVisibleTypeId(null);
        authorizationService.setFieldAccessControl(lAccessControl);

        // Retrieving the Application Access Control
        FieldsService lFieldService = serviceLocator.getFieldsService();
        FieldTypeData lFieldTypeData =
                lFieldService.getField(adminRoleToken, containerId, FIELD_NAME);

        assertNotNull("Cannot get data for field " + FIELD_NAME, lFieldTypeData);

        lAccessControl =
                authorizationService.getFieldAccessControl(adminRoleToken,
                        lAccessControlContextData, lFieldTypeData.getId());

        assertNotNull("The method getFieldAccessControl returns null.",
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

        FieldAccessData lRetrievedAccessData = lAccessControl.getAccess();
        assertNotNull(
                "The method getFieldAccessControl returns no filedAccess.",
                lRetrievedAccessData);
        assertFalse("FieldAccess must be not confidential",
                lRetrievedAccessData.getConfidential());
        assertTrue("FieldAccess must be exportable",
                lRetrievedAccessData.getExportable());
        assertFalse("FieldAccess must be not mandatory",
                lRetrievedAccessData.getMandatory());
        assertTrue("FieldAccess must be updatable",
                lRetrievedAccessData.getUpdatable());
    }

    /**
     * Test invalid token.
     */
    public void testInvalidToken() {
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setRoleName(ROLE_NAME);
        lAccessControlContextData.setProductName(null);
        lAccessControlContextData.setContainerTypeId(containerId);
        lAccessControlContextData.setStateName(STATE_NAME);
        lAccessControlContextData.setVisibleTypeId(null);

        FieldsService lFieldService = serviceLocator.getFieldsService();
        FieldTypeData lFieldTypeData =
                lFieldService.getField(adminRoleToken, containerId, FIELD_NAME);

        try {
            authorizationService.getFieldAccessControl(null,
                    lAccessControlContextData, lFieldTypeData.getId());
            fail("getFieldAccessControl method throw an invalid token exception "
                    + "when the roleToken is null");
        }
        catch (InvalidTokenException lE) {
            //ok
        }
    }

    /**
     * Test illegal argument 'field id' argument.
     */
    public void testIllegalArgumentFieldId() {
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setRoleName(ROLE_NAME);
        lAccessControlContextData.setProductName(null);
        lAccessControlContextData.setContainerTypeId(containerId);
        lAccessControlContextData.setStateName(STATE_NAME);
        lAccessControlContextData.setVisibleTypeId(null);

        try {
            authorizationService.getFieldAccessControl(adminRoleToken,
                    lAccessControlContextData, null);
            fail("getFieldAccessControl method throw an illegalArgument exception "
                    + "when the field id is null");
        }
        catch (IllegalArgumentException lE) {
            //ok
        }
    }

    /**
     * Test to get the default values for a field with no access control
     * Test_AccessControls_CF_001
     */
    public void testGetDefaultAccessControl() {
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setRoleName(ROLE_NAME);
        lAccessControlContextData.setProductName(null);
        lAccessControlContextData.setContainerTypeId(containerIdControlType);
        lAccessControlContextData.setStateName(null);
        lAccessControlContextData.setVisibleTypeId(null);

        FieldsService lFieldsService = serviceLocator.getFieldsService();
        FieldTypeData lFieldTypeData =
                lFieldsService.getField(adminRoleToken, containerIdControlType,
                        CONTROL_TYPE_FIELD1);

        FieldAccessControlData lFieldAccessControlData =
                authorizationService.getFieldAccessControl(adminRoleToken,
                        lAccessControlContextData, lFieldTypeData.getId());

        assertNotNull("For " + CONTROL_TYPE_FIELD1 + " of container type "
                + CONTROL_TYPE
                + ", the method getFieldAccessControl return null. "
                + "Must return default access control", lFieldAccessControlData);

        //Verify default access control
        assertEquals(CONTROL_TYPE_FIELD1_MANDATORY,
                lFieldAccessControlData.getAccess().getMandatory());
        assertEquals(CONTROL_TYPE_FIELD1_UPDATABLE,
                lFieldAccessControlData.getAccess().getUpdatable());
        assertEquals(CONTROL_TYPE_FIELD1_CONFIDENTIAL,
                lFieldAccessControlData.getAccess().getConfidential());
        assertEquals(CONTROL_TYPE_FIELD1_EXPORTABLE,
                lFieldAccessControlData.getAccess().getExportable());
    }

    /**
     * Test to get access rights according to given context
     * Test_AccessControls_CF_002
     */
    public void testByContext() {
        //First context
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setRoleName("normal");
        lAccessControlContextData.setProductName(GpmTestValues.PRODUCT1_NAME);
        lAccessControlContextData.setContainerTypeId(containerIdControlType);
        lAccessControlContextData.setStateName(null);
        lAccessControlContextData.setVisibleTypeId(null);

        FieldsService lFieldsService = serviceLocator.getFieldsService();
        FieldTypeData lFieldTypeData =
                lFieldsService.getField(adminRoleToken, containerIdControlType,
                        FIELD2);

        FieldAccessControlData lFieldAccessControlData =
                authorizationService.getFieldAccessControl(adminRoleToken,
                        lAccessControlContextData, lFieldTypeData.getId());

        assertNotNull(
                "For "
                        + FIELD2
                        + " of container type "
                        + CONTROL_TYPE
                        + ", the method getFieldAccessControl return null (first context).",
                lFieldAccessControlData);

        //Verify access control
        assertEquals(
                false,
                lFieldAccessControlData.getAccess().getMandatory().booleanValue());
        assertEquals(
                false,
                lFieldAccessControlData.getAccess().getUpdatable().booleanValue());
        assertEquals(
                true,
                lFieldAccessControlData.getAccess().getConfidential().booleanValue());
        assertEquals(
                true,
                lFieldAccessControlData.getAccess().getExportable().booleanValue());

        //Second context
        AccessControlContextData lAccessControlContextData2 =
                new AccessControlContextData();
        lAccessControlContextData2.setRoleName("normal");
        lAccessControlContextData2.setProductName(GpmTestValues.PRODUCT_PRODUCT2);
        lAccessControlContextData2.setContainerTypeId(containerIdControlType);
        lAccessControlContextData2.setStateName(null);

        FieldAccessControlData lFieldAccessControlData2 =
                authorizationService.getFieldAccessControl(adminRoleToken,
                        lAccessControlContextData2, lFieldTypeData.getId());

        assertNotNull(
                "For "
                        + FIELD2
                        + " of container type "
                        + CONTROL_TYPE
                        + ", the method getFieldAccessControl return null (second context).",
                lFieldAccessControlData2);

        //Verify access control
        assertEquals(
                false,
                lFieldAccessControlData2.getAccess().getMandatory().booleanValue());
        assertEquals(
                true,
                lFieldAccessControlData2.getAccess().getUpdatable().booleanValue());
        assertEquals(
                true,
                lFieldAccessControlData2.getAccess().getConfidential().booleanValue());
        assertEquals(
                true,
                lFieldAccessControlData2.getAccess().getExportable().booleanValue());

        //Third context
        AccessControlContextData lAccessControlContextData3 =
                new AccessControlContextData();
        lAccessControlContextData3.setRoleName("restricted");
        lAccessControlContextData3.setProductName(GpmTestValues.PRODUCT1_NAME);
        lAccessControlContextData3.setContainerTypeId(containerIdControlType);
        lAccessControlContextData3.setStateName(null);

        FieldAccessControlData lFieldAccessControlData3 =
                authorizationService.getFieldAccessControl(adminRoleToken,
                        lAccessControlContextData3, lFieldTypeData.getId());

        assertNotNull(
                "For "
                        + FIELD2
                        + " of container type "
                        + CONTROL_TYPE
                        + ", the method getFieldAccessControl return null (third context).",
                lFieldAccessControlData3);

        //Verify access control
        assertEquals(
                true,
                lFieldAccessControlData3.getAccess().getMandatory().booleanValue());
        assertEquals(
                false,
                lFieldAccessControlData3.getAccess().getUpdatable().booleanValue());
        assertEquals(
                true,
                lFieldAccessControlData3.getAccess().getConfidential().booleanValue());
        assertEquals(
                false,
                lFieldAccessControlData3.getAccess().getExportable().booleanValue());

        //Fourth context
        AccessControlContextData lAccessControlContextData4 =
                new AccessControlContextData();
        lAccessControlContextData4.setRoleName("viewer");
        lAccessControlContextData4.setProductName(GpmTestValues.PRODUCT1_NAME);
        lAccessControlContextData4.setContainerTypeId(containerIdControlType);
        lAccessControlContextData4.setStateName(null);

        FieldAccessControlData lFieldAccessControlData4 =
                authorizationService.getFieldAccessControl(adminRoleToken,
                        lAccessControlContextData4, lFieldTypeData.getId());

        assertNotNull(
                "For "
                        + FIELD2
                        + " of container type "
                        + CONTROL_TYPE
                        + ", the method getFieldAccessControl return null. (fourth context)",
                lFieldAccessControlData4);

        //Verify access control
        assertEquals(
                true,
                lFieldAccessControlData4.getAccess().getMandatory().booleanValue());
        assertEquals(
                true,
                lFieldAccessControlData4.getAccess().getUpdatable().booleanValue());
        assertEquals(
                false,
                lFieldAccessControlData4.getAccess().getConfidential().booleanValue());
        assertEquals(
                true,
                lFieldAccessControlData4.getAccess().getExportable().booleanValue());

        //Fifth context
        AccessControlContextData lAccessControlContextData5 =
                new AccessControlContextData();
        lAccessControlContextData5.setRoleName("normal");
        lAccessControlContextData5.setProductName(GpmTestValues.PRODUCT1_NAME);
        lAccessControlContextData5.setContainerTypeId(containerIdControlType2);
        lAccessControlContextData5.setStateName(null);

        FieldAccessControlData lFieldAccessControlData5 =
                authorizationService.getFieldAccessControl(adminRoleToken,
                        lAccessControlContextData5, lFieldTypeData.getId());

        assertNotNull(
                "For "
                        + FIELD2
                        + " of container type "
                        + CONTROL_TYPE
                        + ", the method getFieldAccessControl return null. (fifth context)",
                lFieldAccessControlData5);

        //Verify access control
        assertEquals(
                true,
                lFieldAccessControlData5.getAccess().getMandatory().booleanValue());
        assertEquals(
                true,
                lFieldAccessControlData5.getAccess().getUpdatable().booleanValue());
        assertEquals(
                false,
                lFieldAccessControlData5.getAccess().getConfidential().booleanValue());
        assertEquals(
                true,
                lFieldAccessControlData5.getAccess().getExportable().booleanValue());
    }

    /**
     * Tests the getFieldAccessControl with extended attributes method.
     */
    public void testNormalWithExtendedAttribute() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // set field access control with extended attributes
        instantiate(getProcessName(), XML_INSTANCE_TEST);

        // Retrieving the Application Access Control
        String lContainerId =
                getFieldsContainerService().getFieldsContainerId(
                        adminRoleToken, EXTENDED_ATTRIBUTE_CONTAINERTYPE);
        FieldTypeData lFieldTypeData =
                fieldsService.getField(adminRoleToken, lContainerId,
                        EXTENDED_ATTRIBUTE_FIELDLABEL);
        String lFieldId = lFieldTypeData.getId();
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setContainerTypeId(lContainerId);
        lAccessControlContextData.setVisibleTypeId(null);
        FieldAccessControlData lFieldAccessControlData =
                authorizationService.getFieldAccessControl(adminRoleToken,
                        lAccessControlContextData, lFieldId);

        assertNotNull("The method getFieldAccessControl returns null.",
                lFieldAccessControlData);

        // Verifies the fields
        String lProcessName = lFieldAccessControlData.getBusinessProcessName();

        assertEquals("Process name expected = " + getProcessName()
                + ". Process name actual = " + lProcessName + ".",
                getProcessName(), lProcessName);

        String lFieldName = lFieldAccessControlData.getFieldName();
        assertEquals("Field name", EXTENDED_ATTRIBUTE_FIELDLABEL, lFieldName);

        assertNull("The state name must be null",
                lFieldAccessControlData.getContext().getStateName());
        assertNull("The product name must be null",
                lFieldAccessControlData.getContext().getProductName());

        String lACContainerTypeId =
                lFieldAccessControlData.getContext().getContainerTypeId();
        assertEquals("Container type id is not correct.", lContainerId,
                lACContainerTypeId);

        assertNotNull("The field access control must have extended attributes",
                lFieldAccessControlData.getExtendedAttributes());
        assertFalse("The field access control must have extended attributes",
                lFieldAccessControlData.getExtendedAttributes().length < 1);

        assertEquals(
                EXTENDED_ATTRIBUTE_ATTRIBUTES_CONTAINERTYPE,
                lFieldAccessControlData.getExtendedAttributes()[0].getValues()[0]);
        assertEquals(
                EXTENDED_ATTRIBUTE_ATTRIBUTES_FIELDTYPE,
                lFieldAccessControlData.getExtendedAttributes()[1].getValues()[0]);
    }

    /**
     * Tests the getFieldAccessControl method with overridden role.
     */
    public void testWithOverriddenRole() {
        String lSheetId =
                sheetService.getSheetIdByReference(getProcessName(),
                        GpmTestValues.PRODUCT_BERNARD_STORE_NAME, SHEET_REF);

        // Creating the Application Access Control
        FieldAccessControlData lAccessControl = new FieldAccessControlData();
        FieldAccessData lAccess = new FieldAccessData();

        lAccess.setConfidential(false);
        lAccess.setExportable(true);
        lAccess.setMandatory(false);
        lAccess.setUpdatable(true);

        lAccessControl.setAccess(lAccess);

        lAccessControl.setFieldName(FIELD_NAME);
        lAccessControl.setBusinessProcessName(getProcessName());

        // Note: We cannot use a product name here, as the current role is
        // an admin one (not attached to a product)
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setRoleName(ROLE_NAME);
        lAccessControlContextData.setContainerTypeId(containerId);
        lAccessControlContextData.setStateName(STATE_NAME);
        lAccessControl.setContext(lAccessControlContextData);
        lAccessControlContextData.setVisibleTypeId(null);
        lAccessControlContextData.setValuesContainerId(lSheetId);
        authorizationService.setFieldAccessControl(lAccessControl);

        FieldAccessControlData lAccessControl2 = new FieldAccessControlData();
        FieldAccessData lAccess2 = new FieldAccessData();

        lAccess2.setConfidential(false);
        lAccess2.setExportable(true);
        lAccess2.setMandatory(true);
        lAccess2.setUpdatable(false);

        lAccessControl2.setAccess(lAccess);

        lAccessControl2.setFieldName(FIELD_NAME);
        lAccessControl2.setBusinessProcessName(getProcessName());

        // Note: We cannot use a product name here, as the current role is
        // an admin one (not attached to a product)
        AccessControlContextData lAccessControlContextData2 =
                new AccessControlContextData();
        lAccessControlContextData2.setRoleName(OVERRIDDEN_ROLE_NAME);
        lAccessControlContextData2.setContainerTypeId(containerId);
        lAccessControlContextData2.setStateName(STATE_NAME);
        lAccessControl2.setContext(lAccessControlContextData2);
        lAccessControlContextData2.setVisibleTypeId(null);
        lAccessControlContextData2.setValuesContainerId(lSheetId);
        authorizationService.setFieldAccessControl(lAccessControl2);

        // Setting the overridden role
        authorizationService.setOverriddenRole(adminRoleToken, lSheetId,
                ADMIN_LOGIN[0], ROLE_NAME, OVERRIDDEN_ROLE_NAME);

        // Retrieving the Application Access Control
        FieldsService lFieldService = serviceLocator.getFieldsService();
        FieldTypeData lFieldTypeData =
                lFieldService.getField(adminRoleToken, containerId, FIELD_NAME);

        assertNotNull("Cannot get data for field " + FIELD_NAME, lFieldTypeData);

        lAccessControl =
                authorizationService.getFieldAccessControl(adminRoleToken,
                        lAccessControlContextData, lFieldTypeData.getId());

        assertNotNull("The method getFieldAccessControl returns null.",
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

        FieldAccessData lRetrievedAccessData = lAccessControl.getAccess();
        assertNotNull(
                "The method getFieldAccessControl returns no filedAccess.",
                lRetrievedAccessData);
        assertFalse("FieldAccess must be not confidential",
                lRetrievedAccessData.getConfidential());
        assertTrue("FieldAccess must be exportable",
                lRetrievedAccessData.getExportable());
        assertFalse("FieldAccess must be mandatory",
                lRetrievedAccessData.getMandatory());
        assertTrue("FieldAccess must be not updatable",
                lRetrievedAccessData.getUpdatable());
    }
}