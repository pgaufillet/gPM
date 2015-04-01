/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.service.ActionData;
import org.topcased.gpm.business.extensions.service.CommandData;
import org.topcased.gpm.business.extensions.service.ExtensionsService;
import org.topcased.gpm.business.extensions.service.ScriptData;

/**
 * TestJavaActionExtensionPointService
 * 
 * @author ahaugomm
 */
public class TestJavaActionExtensionPoint extends
        AbstractBusinessServiceTestCase {

    /** The extension service */
    private ExtensionsService extensionsService;

    /** The class name */
    private final static String CLASS_NAME =
            "org.topcased.gpm.business.sheet.ChangeCatRef";

    /** The action name */
    private final static String ACTION_NAME = "changeCatRef_java";

    /** The class name for the command that throws an exception */
    private final static String FAILING_ACTION_CLASS_NAME =
            "org.topcased.gpm.business.sheet.FailingExtensionCommand";

    /**
     *
     */
    public void testCreateScriptCommandWithJavaActionName() {
        extensionsService = serviceLocator.getExtensionsService();
        sheetService = serviceLocator.getSheetService();

        ActionData lActionData = new ActionData();
        lActionData.setName(ACTION_NAME);
        lActionData.setClassName(CLASS_NAME);
        // Creating java action for extension point
        extensionsService.createCommand(lActionData);

        // Create script command with same name

        ScriptData lScriptData = new ScriptData();
        lScriptData.setName(ACTION_NAME);
        lScriptData.setCode("code");
        lScriptData.setLanguage("beanshell");

        try {
            extensionsService.createCommand(lScriptData);
            fail("A GDMException should have been thrown.");
        }
        catch (GDMException e) {
        }

    }

    /**
     *
     */
    public void testUpdateJavaAction() {
        extensionsService = serviceLocator.getExtensionsService();
        sheetService = serviceLocator.getSheetService();

        ActionData lActionData = new ActionData();
        lActionData.setName(ACTION_NAME);
        lActionData.setClassName(CLASS_NAME);
        // Creating java action for extension point
        extensionsService.createCommand(lActionData);

        lActionData.setClassName(FAILING_ACTION_CLASS_NAME);
        extensionsService.createCommand(lActionData);

        CommandData lCommandData = extensionsService.getCommand(ACTION_NAME);

        assertTrue("The command is not an action anymore.",
                lCommandData instanceof ActionData);

        assertEquals("The class name has not been updated : "
                + ((ActionData) lCommandData).getClassName(),
                FAILING_ACTION_CLASS_NAME,
                ((ActionData) lCommandData).getClassName());

    }

}
