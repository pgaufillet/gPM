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
import java.util.Collection;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.extensions.service.ActionData;
import org.topcased.gpm.business.extensions.service.ExtensionsService;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestGetExtensionCommandNamesService
 * 
 * @author mfranche
 */
public class TestGetExtensionCommandNamesService extends
        AbstractBusinessServiceTestCase {

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
        lExtService.setExtension(adminRoleToken, lType.getId(), EXTENSION_NAME,
                lCmdNames);

        Collection<String> lExtCommandNames =
                lExtService.getExtensionCommandNames(lType.getId(),
                        EXTENSION_NAME);

        assertNotNull(
                "The result of getExtensionCommandNames should not be null",
                lExtCommandNames);
        assertTrue("The result of getExtensionCommandNames is incorrect",
                lExtCommandNames.size() >= 1);
        assertTrue("The result of getExtensionCommandNames is incorrect",
                lExtCommandNames.contains(ACTION_NAME));
    }
}
