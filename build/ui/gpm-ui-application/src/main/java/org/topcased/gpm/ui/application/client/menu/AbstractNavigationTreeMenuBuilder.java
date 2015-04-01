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
 * A builder for the menu of the tree tab of a navigation panel.
 * 
 * @author tpanuel
 */
public abstract class AbstractNavigationTreeMenuBuilder {
    private final Command refreshFilter;

    private final Command editTreeFilter;

    private final Command deleteTreeFilter;

    private final Command addFilter;

    private final Command minimizeNavigation;

    /**
     * Create a builder for the menu of the tree tab of a navigation panel.
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
    public AbstractNavigationTreeMenuBuilder(final Command pRefreshFilter,
            final Command pEditTreeFilterCommand,
            final Command pDeleteTreeFilterCommand,
            final Command pAddFilterCommand,
            final Command pMinimizeNavigationCommand) {
        super();
        refreshFilter = pRefreshFilter;
        editTreeFilter = pEditTreeFilterCommand;
        deleteTreeFilter = pDeleteTreeFilterCommand;
        addFilter = pAddFilterCommand;
        minimizeNavigation = pMinimizeNavigationCommand;
    }

    /**
     * Build the menu.
     * 
     * @param pRefreshEnable
     *            If the filter refreshing is enabled.
     * @param pEditionEnable
     *            If the filter edition is enabled.
     * @param pDeletionEnable
     *            If the filter deletion is enabled.
     * @param pAddingEnable
     *            If the filter adding is enabled.
     * @return The menu.
     */
    public GpmToolBar buildMenu(final boolean pRefreshEnable,
            final boolean pEditionEnable, final boolean pDeletionEnable,
            final boolean pAddingEnable) {
        final GpmToolBar lToolBar = new GpmToolBar();

        // Refresh filter
        lToolBar.addSeparatedToolBar();
        if (pRefreshEnable) {
            lToolBar.addEntry(INSTANCE.images().refresh(),
                    CONSTANTS.sheetFilterRefreshButton(), refreshFilter);
        }

        // Edit filter
        lToolBar.addSeparatedToolBar();
        if (pEditionEnable) {
            lToolBar.addEntry(INSTANCE.images().filterEdit(),
                    CONSTANTS.sheetFilterEditButton(), editTreeFilter);
        }

        // Delete filter
        if (pDeletionEnable) {
            lToolBar.addEntry(INSTANCE.images().filterDelete(),
                    CONSTANTS.sheetFilterDeleteButton(), deleteTreeFilter);
        }

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