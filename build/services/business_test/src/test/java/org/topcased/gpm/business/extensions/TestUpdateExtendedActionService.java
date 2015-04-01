/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.extensions.service.ExtendedActionData;
import org.topcased.gpm.business.extensions.service.ExtensionsService;
import org.topcased.gpm.business.instance.service.InstanceService;
import org.topcased.gpm.common.extensions.GUIContext;

/**
 * TestCreateExtendedActionService
 * 
 * @author Anne Haugommard
 */
public class TestUpdateExtendedActionService extends
        AbstractBusinessServiceTestCase {

    private ExtensionsService extensionsService;

    private InstanceService instanceService;

    private static final String MENU_ENTRY = "menu Entry";

    private static final String PARENT_MENU_ENTRY = "parent menu Entry";

    private static final String NEW_MENU_ENTRY = "new menu Entry";

    private static final String EXT_POINT = "extensionPoint";

    private static final String NAME = "myExtendedAction";

    /**
     * Tests the method in a normal case
     */
    public void testNormalCase() {
        extensionsService = serviceLocator.getExtensionsService();
        instanceService = serviceLocator.getInstanceService();
        List<GUIContext> lContexts = new ArrayList<GUIContext>();
        lContexts.add(GUIContext.CTX_VIEW_SHEET);
        String lProcessId =
                instanceService.getBusinessProcessId(getProcessName());
        ExtendedActionData lExtendedActionData =
                new ExtendedActionData(NAME, EXT_POINT, lContexts, MENU_ENTRY,
                        PARENT_MENU_ENTRY, lProcessId, null, 1);
        extensionsService.createExtendedAction(adminRoleToken,
                getProcessName(), lExtendedActionData);

        ExtendedActionData lExtendedAction =
                extensionsService.getExtendedAction(lProcessId, NAME);

        lExtendedAction.setMenuEntryName(NEW_MENU_ENTRY);
        extensionsService.updateExtendedAction(adminRoleToken, lExtendedAction);

        lExtendedAction = extensionsService.getExtendedAction(lProcessId, NAME);

        assertEquals("Extended Action not updated.", NEW_MENU_ENTRY,
                lExtendedAction.getMenuEntryName());

    }

}
