/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.menu.admin.product;

import java.util.List;
import java.util.Map;

import org.topcased.gpm.ui.application.client.command.admin.product.workspace.MaximizeProductDetailCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.workspace.MinimizeProductDetailCommand;
import org.topcased.gpm.ui.application.client.menu.AbstractMenuBuilder;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.facade.shared.action.UiAction;

/**
 * An abstract builder for the menu of the product detail panel.
 * 
 * @author tpanuel
 */
public abstract class AbstractProductDetailMenuBuilder extends
        AbstractMenuBuilder {
    private final MinimizeProductDetailCommand minimizePanel;

    private final MaximizeProductDetailCommand maximizePanel;

    /**
     * Create an abstract builder for the menu of the product detail panel.
     * 
     * @param pMinimizePanel
     *            The minimize panel command.
     * @param pMaximizePanel
     *            The maximize panel command.
     */
    public AbstractProductDetailMenuBuilder(
            final MinimizeProductDetailCommand pMinimizePanel,
            final MaximizeProductDetailCommand pMaximizePanel) {
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

        // Add actions
        doBuildMenu(pActions);

        // Add minimize-maximize actions
        toolBar.addSeparatedToolBar();
        toolBar.addEntry(ComponentResources.INSTANCE.images().minimizeDown(),
                "Minimize", minimizePanel);
        toolBar.addEntry(ComponentResources.INSTANCE.images().maximize(),
                "Maximize", maximizePanel);

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