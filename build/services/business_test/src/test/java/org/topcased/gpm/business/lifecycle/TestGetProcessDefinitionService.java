/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.lifecycle;

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.lifecycle.service.LifeCycleService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.domain.process.ProcessDefinition;

/**
 * TestGetProcessDefinitionService
 * 
 * @author mfranche
 */
public class TestGetProcessDefinitionService extends
        AbstractBusinessServiceTestCase {

    /** The sheet type used. */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_CAT;

    /** The lifecycle service */
    private LifeCycleService lifecycleService;

    /**
     * Test the method in normal conditions
     */
    public void testNormalCase() {
        lifecycleService = serviceLocator.getLifeCycleService();

        // Gets a Id
        List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE);
        assertNotNull(
                "getSheets returns null instead of a list of id of sheets",
                lSheetSummary);
        assertFalse("getSheets returns no sheet", lSheetSummary.isEmpty());
        String lId = lSheetSummary.get(0).getId();

        try {
            ProcessDefinition lProcessDefinition =
                    lifecycleService.getProcessDefinition(lId);
            assertNotNull("The process definition is incorrect.",
                    lProcessDefinition);
        }
        catch (Exception ex) {
            fail("Fail with the getProcessDefinition method.");
        }
    }
}
