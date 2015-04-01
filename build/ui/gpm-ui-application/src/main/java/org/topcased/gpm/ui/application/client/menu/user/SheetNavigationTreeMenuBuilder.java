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
import org.topcased.gpm.ui.application.client.command.user.filter.DeleteSheetTreeFilterCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.EditSheetTreeFilterCommand;
import org.topcased.gpm.ui.application.client.command.user.filter.ExecuteSheetTreeFilterCommand;
import org.topcased.gpm.ui.application.client.command.user.workspace.MinimizeSheetNavigationCommand;
import org.topcased.gpm.ui.application.client.menu.AbstractNavigationTreeMenuBuilder;

import com.google.inject.Inject;

/**
 * A builder for the menu of the tree tab of the sheet navigation panel.
 * 
 * @author tpanuel
 */
public class SheetNavigationTreeMenuBuilder extends
        AbstractNavigationTreeMenuBuilder {
    /**
     * Create a builder for the menu of the tree tab of the sheet navigation
     * panel.
     * 
     * @param pRefreshFilter
     *            The refresh filter command.
     * @param pEditTreeFilterCommand
     *            The edit filter command.
     * @param pDeleteTreeFilterCommand
     *            The delete filter command.
     * @param pAddFilterCommand
     *            The add filter command.
     * @param pMinimizeNavigationCommand
     *            The minimize command.
     */
    @Inject
    public SheetNavigationTreeMenuBuilder(
            final ExecuteSheetTreeFilterCommand pRefreshFilter,
            final EditSheetTreeFilterCommand pEditTreeFilterCommand,
            final DeleteSheetTreeFilterCommand pDeleteTreeFilterCommand,
            final AddSheetFilterCommand pAddFilterCommand,
            final MinimizeSheetNavigationCommand pMinimizeNavigationCommand) {
        super(pRefreshFilter, pEditTreeFilterCommand, pDeleteTreeFilterCommand,
                pAddFilterCommand, pMinimizeNavigationCommand);
    }
}