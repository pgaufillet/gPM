/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.service.ExtensionsService;

/**
 * TestGetCommandsService
 * 
 * @author ahaugomm
 */
public class TestRemoveCommandsService extends AbstractBusinessServiceTestCase {

    /** The extensions service */
    private ExtensionsService extensionsService;

    /** The commands */
    private static final String[] COMMANDS =
            { "CAT_initRef", "DOG_initRef", "MOUSE_initRef" };

    /**
     * Test the method in a normal way.
     */
    public void testNormalCase() {
        extensionsService = serviceLocator.getExtensionsService();

        extensionsService.removeCommands(COMMANDS, true);

        for (String lCommandName : COMMANDS) {
            try {
                extensionsService.getCommand(lCommandName);
                fail("The command '" + lCommandName
                        + "' should have been removed.");
            }
            catch (GDMException e) {
            }
        }
    }

    /**
     * Test the method without deleting in extension points
     */
    public void testWithoutDeletingAll() {
        extensionsService = serviceLocator.getExtensionsService();
        try {
            extensionsService.removeCommands(COMMANDS, false);
            fail("The command should not have been removed.");
        }
        catch (GDMException e) {
        }

    }
}
