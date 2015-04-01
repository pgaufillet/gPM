/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.lifecycle;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.lifecycle.service.LifeCycleService;
import org.topcased.gpm.business.lifecycle.service.ProcessInformation;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestSetCurrentState
 * 
 * @author llatil
 */
public class TestSetCurrentState extends AbstractBusinessServiceTestCase {

    /** The lifeCycle service */
    private LifeCycleService lifeCycleService;

    /**
     * Test the 'setCurrentState' method
     */
    public void testSetCurrentState() {
        lifeCycleService = serviceLocator.getLifeCycleService();

        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), GpmTestValues.SHEET_TYPE_SHEETTYPE1,
                        CacheProperties.IMMUTABLE);

        CacheableSheet lSheet =
                sheetService.getCacheableSheetModel(normalRoleToken, lType,
                        getProductName(), null);
        String lSheetId =
                sheetService.createSheet(normalRoleToken, lSheet, null);

        // Try to call the method using an unprivileged role
        try {
            lifeCycleService.setCurrentState(normalRoleToken, lSheetId,
                    GpmTestValues.SHEET_TYPE1_FINAL_STATE);

            fail("setCurrentState() not throwing exception when called by a 'not admin' user");
        }
        catch (AuthorizationException e) {
            // Ok, got the expected exception
        }

        // Call it with 'admin' role.
        lifeCycleService.setCurrentState(adminRoleToken, lSheetId,
                GpmTestValues.SHEET_TYPE1_FINAL_STATE);

        // Check the sheet state has been changed.
        ProcessInformation lStateInfo;
        lStateInfo =
                lifeCycleService.getProcessInstanceInformation(normalRoleToken,
                        lSheetId);

        assertEquals(GpmTestValues.SHEET_TYPE1_FINAL_STATE,
                lStateInfo.getCurrentState());
    }
}
