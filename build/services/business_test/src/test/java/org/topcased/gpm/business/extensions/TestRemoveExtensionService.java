/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.extensions.service.ActionData;
import org.topcased.gpm.business.extensions.service.ExtensionsService;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestRemoveExtensionService
 * 
 * @author mfranche
 */
public class TestRemoveExtensionService extends AbstractBusinessServiceTestCase {

    /** The sheet type where an extension is added */
    private static final String SHEET_TYPE_NAME =
            GpmTestValues.SHEET_TYPE_SIMPLE_SHEET_TYPE1;

    /** The action name */
    private static final String ACTION_NAME = "CheckContext";

    /** The extension name */
    private static final String EXTENSION_NAME = "extName";

    /**
     * Tests the method in a normal case.
     */
    public void testNormalCase() {
        ExtensionsService lExtService = serviceLocator.getExtensionsService();

        ActionData lActionData = new ActionData();
        lActionData.setName(ACTION_NAME);
        lActionData.setClassName(CheckContextAction.class.getName());

        lExtService.createCommand(lActionData);

        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        DEFAULT_PROCESS_NAME, SHEET_TYPE_NAME,
                        CacheProperties.IMMUTABLE);

        List<String> lCmdNames = new ArrayList<String>(1);
        lCmdNames.add(ACTION_NAME);

        // Set the extension
        lExtService.setExtension(adminRoleToken, lType.getId(), EXTENSION_NAME,
                lCmdNames);

        // Check that the extension has been set
        Map<String, List<String>> lAllExtensionsMapBeforeRemove =
                lExtService.getAllExtensions(lType.getId());
        assertNotNull(lAllExtensionsMapBeforeRemove);
        assertTrue(lAllExtensionsMapBeforeRemove.size() == 1);

        // Remove the extension
        lExtService.removeExtension(adminRoleToken, lType.getId(),
                EXTENSION_NAME);

        // Check that the extension has been removed
        Map<String, List<String>> lAllExtensionsMapAfterRemove =
                lExtService.getAllExtensions(lType.getId());
        assertNull(lAllExtensionsMapAfterRemove);

        // Try to remove a non existing extension.
        // The removeExtension() method does nothing in this case.
        lExtService.removeExtension(adminRoleToken, lType.getId(),
                EXTENSION_NAME);
    }
}
