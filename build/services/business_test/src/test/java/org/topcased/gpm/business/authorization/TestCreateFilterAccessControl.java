/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: XXX (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;

/**
 * TestCreateFilterAccessControl
 */
public class TestCreateFilterAccessControl extends
        AbstractBusinessServiceTestCase {

    //    private static final String CONSTRAINT_NAME_1 = "constraint1";

    //    private static final String INSTANCE_FILE =
    //            "authorization/TestCreateFilterAccessControl.xml";

    //    private AuthorizationService authorizationService;

    @Override
    protected void setUp() {
        super.setUp();
        authorizationService = serviceLocator.getAuthorizationService();
    }

    //    public void testCreateConstraint() {
    //        instantiate(getProcessName(), INSTANCE_FILE);
    //
    //        FilterAccessControlConstraint lConstraint =
    //                authorizationService.getFilterAccessControlConstraint(
    //                        adminRoleToken, CONSTRAINT_NAME_1);
    //        assertNotNull(lConstraint);
    //        assertEquals(CONSTRAINT_NAME_1, lConstraint.getName());
    //        assertNotNull(lConstraint.getCriteria());
    //
    //        OperationData lOperationData =
    //                (OperationData) lConstraint.getCriteria();
    //        for (CriteriaData lCriteriaData : lOperationData.getCriteriaDatas()) {
    //            if (lCriteriaData instanceof CriteriaFieldData) {
    //                CriteriaFieldData lCriteriaFieldData =
    //                        (CriteriaFieldData) lCriteriaData;
    //                assertEquals("$SHEET_STATE",
    //                        lCriteriaFieldData.getUsableFieldData().getFieldName());
    //                assertEquals("!=", lCriteriaFieldData.getOperator());
    //                assertEquals("field_value3",
    //                        lCriteriaFieldData.getScalarValueData().getValue());
    //            }
    //            else {//instanceof OperationData
    //                OperationData lGroup2 = (OperationData) lCriteriaData;
    //                assertEquals(2, lGroup2.getCriteriaDatas().length);
    //                CriteriaFieldData lCriteriaFieldData1 =
    //                        (CriteriaFieldData) lGroup2.getCriteriaDatas()[0];
    //                assertEquals("SheetWithSomeConfidentialFields_Field1",
    //                        lCriteriaFieldData1.getUsableFieldData().getFieldName());
    //                assertEquals("=", lCriteriaFieldData1.getOperator());
    //                assertEquals("field_value1",
    //                        lCriteriaFieldData1.getScalarValueData().getValue());
    //
    //                CriteriaFieldData lCriteriaFieldData2 =
    //                        (CriteriaFieldData) lGroup2.getCriteriaDatas()[1];
    //                assertEquals("SheetLinkWithSomeConfidentialFields_Field3",
    //                        lCriteriaFieldData2.getUsableFieldData().getFieldName());
    //                assertEquals("like", lCriteriaFieldData2.getOperator());
    //                assertEquals(0,
    //                        lCriteriaFieldData2.getScalarValueData().getValue());
    //            }
    //        }
    //    }
}
