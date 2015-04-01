/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin), Sébastien René
 * (Atos Origin), Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization;

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.authorization.service.TypeAccessControlData;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the methods
 * <CODE>getSheetTypeAccessControl<CODE> and <CODE>setSheetTypeAccessControl<CODE>
 * of the Authorization Service.
 * 
 * @author nsamson
 */
public class TestSetGetSheetTypeAccessControlService extends
        AbstractBusinessServiceTestCase {

    /** The Product Service. */
    private AuthorizationService authorizationService;

    /** The new role name. */
    private static final String ROLE_NAME = GpmTestValues.USER_ADMIN;

    /** The overridden role name. */
    private static final String OVERRIDDEN_ROLE_NAME = "restricted";

    /** The new role name. */
    private static final String SHEET_TYPE_NAME = GpmTestValues.SHEET_TYPE_DOG;

    /** The new role name. */
    private static final String STATE_NAME = "Closed";

    /** The sheet */
    private static final String SHEET_REF = "Medor";

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
     * Tests the getSheetTypeAccessControl and setSheetTypeAccessControl
     * methods.
     */
    public void testNormalCase() {
        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Creating the Application Access Control
        TypeAccessControlData lAccessControl =
                new TypeAccessControlData(true, true, true, true);
        lAccessControl.setBusinessProcessName(getProcessName());
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setProductName(getProductName());
        lAccessControlContextData.setStateName(STATE_NAME);
        lAccessControlContextData.setRoleName(ROLE_NAME);
        lAccessControlContextData.setContainerTypeId(containerId);
        lAccessControl.setContext(lAccessControlContextData);
        authorizationService.setSheetTypeAccessControl(lAccessControl);

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE_NAME);
        assertFalse("No sheets for type " + SHEET_TYPE_NAME,
                lSheetSummary.isEmpty());
        SheetSummaryData lSheetSummaryData = lSheetSummary.get(0);
        lAccessControl =
                authorizationService.getSheetAccessControl(adminRoleToken,
                        lAccessControlContextData, lSheetSummaryData.getId());

        // assertTrue("test not valid", false);

        assertNotNull("The method getSheetAccessControl returns null.",
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
        assertEquals("SheetType id expected = " + containerId
                + ". SheetType id actual = " + lSheetTypeId + ".", containerId,
                lSheetTypeId);

        String lStateName = lAccessControl.getContext().getStateName();
        assertEquals("State name expected = " + STATE_NAME
                + ". State name actual = " + lStateName + ".", STATE_NAME,
                lStateName);

        assertTrue("The sheet type must be updatable",
                lAccessControl.getUpdatable());
        assertTrue("The sheet type must be confidential",
                lAccessControl.getConfidential());
        assertTrue("The sheet type must be deletable",
                lAccessControl.getDeletable());
        assertTrue("The sheet type must be creatable",
                lAccessControl.getCreatable());
    }

    /**
     * Test the getSheetTypeAccessControl method with a defined overridden role.
     */
    public void testWithOverridenRole() {
        String lSheetId =
                sheetService.getSheetIdByReference(getProcessName(),
                        GpmTestValues.PRODUCT_BERNARD_STORE_NAME, SHEET_REF);

        // Gets the authorization service.
        authorizationService = serviceLocator.getAuthorizationService();

        // Set the overridden role.
        authorizationService.setOverriddenRole(adminRoleToken, lSheetId,
                ADMIN_LOGIN[0], ROLE_NAME, OVERRIDDEN_ROLE_NAME);

        // Creating the Application Access Control
        TypeAccessControlData lAccessControl =
                new TypeAccessControlData(true, true, true, true);
        lAccessControl.setBusinessProcessName(getProcessName());
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setProductName(getProductName());
        lAccessControlContextData.setStateName(STATE_NAME);
        lAccessControlContextData.setRoleName(ROLE_NAME);
        lAccessControlContextData.setContainerTypeId(containerId);
        lAccessControl.setContext(lAccessControlContextData);
        authorizationService.setSheetTypeAccessControl(lAccessControl);

        // Retrieving the Application Access Control

        lAccessControl =
                authorizationService.getSheetAccessControl(adminRoleToken,
                        lAccessControlContextData, lSheetId);

        // assertTrue("test not valid", false);

        assertNotNull("The method getSheetAccessControl returns null.",
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
        assertEquals("SheetType id expected = " + containerId
                + ". SheetType id actual = " + lSheetTypeId + ".", containerId,
                lSheetTypeId);

        String lStateName = lAccessControl.getContext().getStateName();
        assertEquals("State name expected = " + STATE_NAME
                + ". State name actual = " + lStateName + ".", STATE_NAME,
                lStateName);

        assertTrue("The sheet type must be updatable",
                lAccessControl.getUpdatable());
        assertFalse("The sheet type must be not confidential",
                lAccessControl.getConfidential());
        assertTrue("The sheet type must be deletable",
                lAccessControl.getDeletable());
        assertTrue("The sheet type must be creatable",
                lAccessControl.getCreatable());
    }
}