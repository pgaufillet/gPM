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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.util.action.ActionName;
import org.topcased.gpm.ui.application.client.command.admin.product.product.CreateProductCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.workspace.MaximizeProductDetailCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.workspace.MinimizeProductDetailCommand;
import org.topcased.gpm.ui.application.shared.util.ExtendedActionType;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.facade.shared.action.UiAction;

import com.google.inject.Inject;

/**
 * A builder for the menu of the product creation detail panel.
 * 
 * @author tpanuel
 */
public class ProductDetailCreationMenuBuilder extends
        AbstractProductDetailMenuBuilder {
    static {
        IMAGES.put(ActionName.PRODUCT_SAVE,
                ComponentResources.INSTANCE.images().sheetSave());
    }

    /**
     * Create a builder for the menu of the product creation detail panel.
     * 
     * @param pCreateCommand
     *            The creation command.
     * @param pMinimizePanel
     *            The minimize panel command.
     * @param pMaximizePanel
     *            The maximize panel command.
     */
    @Inject
    public ProductDetailCreationMenuBuilder(
            final CreateProductCommand pCreateCommand,
            final MinimizeProductDetailCommand pMinimizePanel,
            final MaximizeProductDetailCommand pMaximizePanel) {
        super(pMinimizePanel, pMaximizePanel);
        registerStandardCommand(ActionName.PRODUCT_SAVE, pCreateCommand);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.menu.user.AbstractSheetDetailMenuBuilder#doBuildMenu(java.util.Map)
     */
    @Override
    protected void doBuildMenu(final Map<String, UiAction> pActions) {
        // Add actions buttons
        if (pActions != null) {
            final List<UiAction> lToolBarA = new ArrayList<UiAction>();

            // Create the first tool bar
            lToolBarA.add(pActions.get(ActionName.PRODUCT_SAVE));

            addToolBar(lToolBarA);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.menu.AbstractMenuBuilder#getExtendedActionType()
     */
    @Override
    protected ExtendedActionType getExtendedActionType() {
        return null;
    }
}