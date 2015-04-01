/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.menu;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import org.topcased.gpm.ui.component.client.menu.GpmToolBar;

import com.google.gwt.user.client.Command;

/**
 * A builder for the menu of the table tab of a navigation panel.
 * 
 * @author tpanuel
 */
public abstract class AbstractNavigationTableMenuBuilder {
    private final Command addFilter;

    private final Command minimizeNavigation;

    /**
     * Create a builder for the menu of the table tab of a navigation panel.
     * 
     * @param pAddFilterCommand
     *            The add filter command.
     * @param pMinimizeNavigationCommand
     *            The minimize command.
     */
    public AbstractNavigationTableMenuBuilder(final Command pAddFilterCommand,
            final Command pMinimizeNavigationCommand) {
        super();
        addFilter = pAddFilterCommand;
        minimizeNavigation = pMinimizeNavigationCommand;
    }

    /**
     * Build the menu.
     * 
     * @param pAddingEnable
     *            If the filter adding is enabled.
     * @return The menu.
     */
    public GpmToolBar buildMenu(final boolean pAddingEnable) {
        final GpmToolBar lToolBar = new GpmToolBar();

        // Add filter
        lToolBar.addSeparatedToolBar();
        if (pAddingEnable) {
            lToolBar.addEntry(INSTANCE.images().filterAdd(),
                    CONSTANTS.sheetFilterCreateButton(), addFilter);
        }

        // Minimize
        lToolBar.addSeparatedToolBar();
        lToolBar.addEntry(INSTANCE.images().minimizeLeft(),
                CONSTANTS.minimize(), minimizeNavigation);

        return lToolBar;
    }
}