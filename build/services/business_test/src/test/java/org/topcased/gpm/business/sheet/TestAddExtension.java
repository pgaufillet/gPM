/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Mimoun Mennad (ATOS), Nicolas Samson (ATOS)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.extensions.service.ActionData;
import org.topcased.gpm.business.extensions.service.ExtensionsService;
import org.topcased.gpm.business.serialization.data.SheetData;
import org.topcased.gpm.business.serialization.data.SheetType;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * Tests the method AddExtensionPoint of the Sheet Service.
 * 
 * @author mmennad
 */
public class TestAddExtension extends AbstractBusinessServiceTestCase {

    /** The extension service */
    private ExtensionsService extensionsService;

    /** The class name */
    private final static String CLASS_NAME =
        "org.topcased.gpm.business.sheet.ChangeCatRef";

    /** The action name */
    private final static String ACTION_NAME = "changeCatRef_java";

    /** The extension point name */
    private final static String EXTENSION_POINT_NAME = "preUpdate";

    /** The expected sheet reference - See the class above to define it */
    private final static String EXPECTED_REF = "TEST";

    /**
     * Tests the method in case of correct parameters were sent
     * 
     * CME : CLASS_NAME does not exist. The test must fail.
     */
    public void testNormalCase() {
        try {
            // Gets the services.
            sheetService = serviceLocator.getSheetService();

            extensionsService = serviceLocator.getExtensionsService();

            // Admin login
            String lAdminUserToken =
                authorizationService.login(GpmTestValues.USER_ADMIN,
                        GpmTestValues.USER_ADMIN);
            String lAdminRoleToken =
                authorizationService.selectRole(lAdminUserToken,
                        GpmTestValues.USER_ADMIN, getProductName(),
                        getProcessName());

            ActionData lActionData = new ActionData();
            lActionData.setName(ACTION_NAME);
            lActionData.setClassName(CLASS_NAME);

            // Creating java action for extension point ...
            extensionsService.createCommand(lActionData);

            // Gets the sheet types
            SheetType lSheetTypeData =
                sheetService.getSerializableSheetTypeByName(lAdminRoleToken,
                        getProcessName(), GpmTestValues.SHEET_TYPE_CAT);

            assertNotNull("The sheet type '" + GpmTestValues.SHEET_TYPE_CAT
                    + "' is null.", lSheetTypeData);

            List<String> lCommandNames = new ArrayList<String>(1);
            lCommandNames.add(ACTION_NAME);

            sheetService.addExtension(adminRoleToken, lSheetTypeData.getId(),
                    EXTENSION_POINT_NAME, lCommandNames);

            //Retrieving sheet data
            List<SheetSummaryData> lSheetSummary =
                sheetService.getSheetsByType(getProcessName(),
                        GpmTestValues.SHEET_TYPE_CAT);
            assertNotNull("Sheet type " + GpmTestValues.SHEET_TYPE_CAT
                    + " not found.", lSheetSummary);

            SheetData lSheetData =
                sheetService.getSerializableSheet(adminRoleToken,
                        lSheetSummary.get(0).getId());

            //Updating the sheet
            sheetService.updateSheet(lAdminRoleToken, GpmTestValues.PROCESS_NAME,
                    lSheetData, null);

            //Retrieving the sheet after update
            lSheetData =
                sheetService.getSerializableSheet(adminRoleToken,
                        lSheetSummary.get(0).getId());

            fail("error");

            //Check
            assertEquals("The extension point has not been correctly executed.",
                    lSheetData.getReference(), EXPECTED_REF);
        }
        catch (InvalidNameException ex) {
            // Ok
        }

    }

}