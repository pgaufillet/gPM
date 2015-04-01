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

import org.topcased.gpm.ui.application.client.command.admin.product.filter.AddProductFilterCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.workspace.MinimizeProductNavigationCommand;
import org.topcased.gpm.ui.application.client.menu.AbstractNavigationTableMenuBuilder;

import com.google.inject.Inject;

/**
 * A builder for the menu of the table tab of the sheet navigation panel.
 * 
 * @author tpanuel
 */
public class ProductNavigationTableMenuBuilder extends
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
    public ProductNavigationTableMenuBuilder(
            final AddProductFilterCommand pAddFilterCommand,
            final MinimizeProductNavigationCommand pMinimizeNavigationCommand) {
        super(pAddFilterCommand, pMinimizeNavigationCommand);
    }
}