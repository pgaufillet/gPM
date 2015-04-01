/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/

package org.topcased.gpm.business.authorization;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.authorization.service.FieldAccessControlData;
import org.topcased.gpm.business.fields.FieldAccessData;
import org.topcased.gpm.business.fields.FieldTypeData;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * @author llatil
 */
public class TestAccessControlsHierarchy extends
        AbstractBusinessServiceTestCase {

    private static final String PROCESS_NAME = "test_process";

    private static final String SHEET_TYPE_NAME = "CtlHierarchyType";

    private static final String FIELD1_NAME = "field1";

    private static final String INSTANCE_RESOURCE =
            "authorization/TestControlsHierarchy.xml";

    private static final String ROLE_NAME = "normal_access";

    public void testNormalCase() {
        FieldAccessData lAccess;

        // launch base update
        instantiate(PROCESS_NAME, INSTANCE_RESOURCE);

        CacheableSheetType lSheetTypeData =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        PROCESS_NAME, SHEET_TYPE_NAME,
                        CacheProperties.IMMUTABLE);
        FieldTypeData lField1Data =
                fieldsService.getField(adminRoleToken, lSheetTypeData.getId(),
                        FIELD1_NAME);

        // Get a role session as 'normal_access'
        String lNormalAccessRoleToken =
                authorizationService.selectRole(adminUserToken, ROLE_NAME,
                        null, PROCESS_NAME);

        // Get the access controls of field1 for 'normal_access' for
        // sheets in 'FirstState' state.
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setRoleName(ROLE_NAME);
        lAccessControlContextData.setStateName("Open");
        lAccessControlContextData.setContainerTypeId(lSheetTypeData.getId());
        lAccessControlContextData.setVisibleTypeId(null);
        FieldAccessControlData lFacd;
        lFacd =
                authorizationService.getFieldAccessControl(
                        lNormalAccessRoleToken, lAccessControlContextData,
                        lField1Data.getId());

        assertFalse(lFacd.getAccess().getConfidential());
        assertTrue(lFacd.getAccess().getUpdatable());

        // Get the access controls of field1 for 'normal_access' for
        // sheets in 'EndState' state.
        AccessControlContextData lAccessControlContextData2 =
                new AccessControlContextData();
        lAccessControlContextData2.setRoleName(ROLE_NAME);
        lAccessControlContextData2.setStateName("Closed");
        lAccessControlContextData2.setContainerTypeId(lSheetTypeData.getId());
        lFacd =
                authorizationService.getFieldAccessControl(
                        lNormalAccessRoleToken, lAccessControlContextData2,
                        lField1Data.getId());
        assertFalse(lFacd.getAccess().getConfidential());
        assertFalse(lFacd.getAccess().getUpdatable());

        // Get the access controls of field1 for 'normal_access' without
        // specifying sheet state  (the access control should still apply).
        AccessControlContextData lAccessControlContextData3 =
                new AccessControlContextData();
        lAccessControlContextData3.setRoleName(ROLE_NAME);
        lAccessControlContextData3.setContainerTypeId(lSheetTypeData.getId());
        lFacd =
                authorizationService.getFieldAccessControl(
                        lNormalAccessRoleToken, lAccessControlContextData3,
                        lField1Data.getId());
        assertFalse(lFacd.getAccess().getConfidential());
        assertFalse(lFacd.getAccess().getUpdatable());

        // Get the an access controls for the 'admin' role
        // (no specific access control are defined for the 'admin' role,
        // so the access controls returned are the default access for the field.
        AccessControlContextData lAccessControlContextData4 =
                new AccessControlContextData();
        lAccessControlContextData4.setRoleName(GpmTestValues.USER_ADMIN);
        lAccessControlContextData4.setStateName("Closed");
        lAccessControlContextData4.setContainerTypeId(lSheetTypeData.getId());
        lFacd =
                authorizationService.getFieldAccessControl(adminRoleToken,
                        lAccessControlContextData4, lField1Data.getId());
        lAccess = lFacd.getAccess();
        assertTrue(lAccess.getConfidential() && !lAccess.getExportable());

    }
}
