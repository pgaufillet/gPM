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

import java.util.Collection;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.service.ActionData;
import org.topcased.gpm.business.extensions.service.CommandData;
import org.topcased.gpm.business.extensions.service.ExtensionsService;

/**
 * TestCreateAndGetCommandsService
 * 
 * @author mfranche
 */
public class TestCreateAndGetCommandsService extends
        AbstractBusinessServiceTestCase {

    /** The action name */
    private static final String ACTION_NAME = "CheckContext";

    /**
     * Tests the method get commands
     */
    public void testNormalCase() {
        ExtensionsService lExtService = serviceLocator.getExtensionsService();

        // Create an action data
        ActionData lActionData = new ActionData();
        lActionData.setName(ACTION_NAME);
        lActionData.setClassName(CheckContextAction.class.getName());

        // Create the associated commands
        lExtService.createCommands(new CommandData[] { lActionData });

        // Get the commands with the action data name
        Collection<CommandData> lCommandDataCol =
                lExtService.getCommands(new String[] { ACTION_NAME });

        // Check the result of getCommands
        assertNotNull(
                "The method getCommands should not return a null result.",
                lCommandDataCol);
        assertTrue("The size of getCommands collection is incorrect.",
                lCommandDataCol.size() == 1);
        CommandData lCommandData = (CommandData) lCommandDataCol.toArray()[0];
        assertEquals("The command data name is incorrect.",
                lCommandData.getName(), ACTION_NAME);
        assertTrue("The command data class is incorrect.",
                lCommandData instanceof ActionData);
        lActionData = (ActionData) lCommandData;
        assertEquals("The action data class name is incorrect.",
                lActionData.getClassName(), CheckContextAction.class.getName());
    }

    /**
     * Tests the method createCommands with an invalid CommandData
     */
    public void testIncorrectCommandDataCase() {
        ExtensionsService lExtService = serviceLocator.getExtensionsService();

        CommandData lCommandData = new CommandData(ACTION_NAME);

        try {
            lExtService.createCommands(new CommandData[] { lCommandData });
            fail("The exception has not been thrown.");
        }

        catch (GDMException lGDMException) {
            // ok
        }
    }
}
