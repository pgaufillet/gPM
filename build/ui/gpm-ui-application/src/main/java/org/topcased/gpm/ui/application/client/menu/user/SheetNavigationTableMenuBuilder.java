/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.menu.user;

import org.topcased.gpm.ui.application.client.command.user.filter.AddSheetFilterCommand;
import org.topcased.gpm.ui.application.client.command.user.workspace.MinimizeSheetNavigationCommand;
import org.topcased.gpm.ui.application.client.menu.AbstractNavigationTableMenuBuilder;

import com.google.inject.Inject;

/**
 * A builder for the menu of the table tab of the sheet navigation panel.
 * 
 * @author tpanuel
 */
public class SheetNavigationTableMenuBuilder extends
        AbstractNavigationTableMenuBuilder {
    /**
     * Create a builder for the menu of the table tab of the sheet navigation
     * panel.
     * 
     * @param pAddFilterCommand
     *            The add filter command.
     * @param pMinimizeNavigationCommand
     *            The minimize command.
     */
    @Inject
    public SheetNavigationTableMenuBuilder(
            final AddSheetFilterCommand pAddFilterCommand,
            final MinimizeSheetNavigationCommand pMinimizeNavigationCommand) {
        super(pAddFilterCommand, pMinimizeNavigationCommand);
    }
}