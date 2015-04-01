/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.extensions.service.ActionData;
import org.topcased.gpm.business.extensions.service.ExtensionsService;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Test creation of a new extension point, attach commands to this point, and
 * invoke it.
 * 
 * @author llatil
 */
public class TestDefineAndCallExtension extends AbstractBusinessServiceTestCase {

    private static final String SHEET_TYPE_NAME =
            GpmTestValues.SHEET_TYPE_SIMPLE_SHEET_TYPE1;

    /**
     * Define a new extension point named 'checkContext', create an action
     * command that checks the content of the execution context, and finally
     * invoke the extension point with the sheetTypeData id.
     */
    public void testDefineAndCallCheckContext() {
        ExtensionsService lExtService = serviceLocator.getExtensionsService();

        ActionData lActionData = new ActionData();
        lActionData.setName("CheckContext");
        lActionData.setClassName(CheckContextAction.class.getName());

        lExtService.createCommand(lActionData);

        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        DEFAULT_PROCESS_NAME, SHEET_TYPE_NAME,
                        CacheProperties.IMMUTABLE);

        List<String> lCmdNames = new ArrayList<String>(1);
        lCmdNames.add("CheckContext");
        lExtService.setExtension(adminRoleToken, lType.getId(), "CheckContext",
                lCmdNames);

        lExtService.execute(adminRoleToken, lType.getId(), "CheckContext", null);
    }

    /**
     * Define a new extension point named 'checkContext' - method with
     * attributes - create an action command that checks the content of the
     * execution context, and finally invoke the extension point with the
     * sheetTypeData id.
     */
    public void testDefineAndCallCheckContext2() {
        ExtensionsService lExtService = serviceLocator.getExtensionsService();

        ActionData lActionData = new ActionData();
        lActionData.setName("CheckContext");
        lActionData.setClassName(CheckContextAction.class.getName());

        lExtService.createCommand(lActionData);

        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        DEFAULT_PROCESS_NAME, SHEET_TYPE_NAME,
                        CacheProperties.IMMUTABLE);

        List<String> lCmdNames = new ArrayList<String>(1);
        lCmdNames.add("CheckContext");
        lExtService.setExtension(adminRoleToken, lType.getId(), "CheckContext",
                lCmdNames, null);

        lExtService.execute(adminRoleToken, lType.getId(), "CheckContext", null);
    }

    /**
     * Test the method with an incorrect id
     */
    public void testIncorrectIdCase() {
        ExtensionsService lExtService = serviceLocator.getExtensionsService();

        ActionData lActionData = new ActionData();
        lActionData.setName("CheckContext");
        lActionData.setClassName(CheckContextAction.class.getName());

        lExtService.createCommand(lActionData);

        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        DEFAULT_PROCESS_NAME, SHEET_TYPE_NAME,
                        CacheProperties.IMMUTABLE);

        List<String> lCmdNames = new ArrayList<String>(1);
        lCmdNames.add("CheckContext");
        lExtService.setExtension(adminRoleToken, lType.getId(), "CheckContext",
                lCmdNames);

        try {
            lExtService.execute(adminRoleToken, "", "CheckContext", null);
            fail("The exception has not been thrown.");
        }
        catch (InvalidIdentifierException lInvalidIdentifierException) {
            // ok
        }
    }
}
