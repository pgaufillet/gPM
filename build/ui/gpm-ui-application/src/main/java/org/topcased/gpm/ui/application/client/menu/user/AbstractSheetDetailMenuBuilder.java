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

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import java.util.List;
import java.util.Map;

import org.topcased.gpm.ui.application.client.command.user.workspace.MaximizeSheetDetailCommand;
import org.topcased.gpm.ui.application.client.command.user.workspace.MinimizeSheetDetailCommand;
import org.topcased.gpm.ui.application.client.menu.AbstractMenuBuilder;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;
import org.topcased.gpm.ui.facade.shared.action.UiAction;

/**
 * An abstract builder for the menu of the sheet detail panel.
 * 
 * @author tpanuel
 */
public abstract class AbstractSheetDetailMenuBuilder extends
        AbstractMenuBuilder {
    private final MinimizeSheetDetailCommand minimizePanel;

    private final MaximizeSheetDetailCommand maximizePanel;

    /**
     * Create an abstract builder for the menu of the sheet detail panel.
     * 
     * @param pMinimizePanel
     *            The minimize panel command.
     * @param pMaximizePanel
     *            The maximize panel command.
     */
    public AbstractSheetDetailMenuBuilder(
            final MinimizeSheetDetailCommand pMinimizePanel,
            final MaximizeSheetDetailCommand pMaximizePanel) {
        super();
        minimizePanel = pMinimizePanel;
        maximizePanel = pMaximizePanel;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.menu.AbstractMenuBuilder#buildMenu(java.util.Map,
     *      java.util.List)
     */
    @Override
    public final GpmToolBar buildMenu(final Map<String, UiAction> pActions,
            final List<UiAction> pExtendedActions) {
        // Clean tool bar
        resetToolBar();

        // Replace Standard actions with Extended Actions when corresponding
        mergeExtendedActionsWithActions(pActions, pExtendedActions);

        // Add standard actions
        doBuildMenu(pActions);

        // Add remaining extended actions
        if (pExtendedActions != null && pExtendedActions.size() > 0) {
            addToolBar(pExtendedActions);
        }

        // Add minimize-maximize actions
        toolBar.addSeparatedToolBar();
        toolBar.addEntry(INSTANCE.images().minimizeDown(),
                CONSTANTS.minimize(), minimizePanel);
        toolBar.addEntry(INSTANCE.images().maximize(), CONSTANTS.maximize(),
                maximizePanel);

        return toolBar;
    }

    /**
     * Build the menu.
     * 
     * @param pActions
     *            The actions. Can be null.
     */
    abstract protected void doBuildMenu(final Map<String, UiAction> pActions);
}