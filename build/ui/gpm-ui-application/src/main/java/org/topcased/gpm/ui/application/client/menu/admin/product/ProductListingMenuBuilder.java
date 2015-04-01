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
import org.topcased.gpm.ui.application.client.command.admin.product.filter.EditProductTableFilterCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.filter.ExecuteProductTableFilterCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.products.DeleteProductsCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.products.ExportProductsOnXmlCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.workspace.MaximizeProductListingCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.workspace.MinimizeProductListingCommand;
import org.topcased.gpm.ui.application.client.menu.AbstractMenuBuilder;
import org.topcased.gpm.ui.application.shared.util.ExtendedActionType;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.facade.shared.action.UiAction;

import com.google.inject.Inject;

/**
 * A builder for the menu of the product listing panel.
 * 
 * @author tpanuel
 */
public class ProductListingMenuBuilder extends AbstractMenuBuilder {
    static {
        IMAGES.put(ActionName.FILTER_PRODUCT_REFRESH,
                ComponentResources.INSTANCE.images().refresh());
        IMAGES.put(ActionName.FILTER_PRODUCT_EDIT,
                ComponentResources.INSTANCE.images().filterEdit());
        IMAGES.put(ActionName.PRODUCTS_DELETE,
                ComponentResources.INSTANCE.images().sheetDelete());
        IMAGES.put(ActionName.PRODUCTS_EXPORT,
                ComponentResources.INSTANCE.images().sheetExport());
    }

    private final MinimizeProductListingCommand minimizePanel;

    private final MaximizeProductListingCommand maximizePanel;

    /**
     * Create a builder for the menu of the product listing panel.
     * 
     * @param pRefreshCommand
     *            The refresh command.
     * @param pEditCommand
     *            The edit command.
     * @param pDeleteCommand
     *            The delete command.
     * @param pExportXmlCommand
     *            The export XML command.
     * @param pMinimizePanel
     *            The minimize panel command.
     * @param pMaximizePanel
     *            The maximize panel command.
     */
    @Inject
    public ProductListingMenuBuilder(
            final ExecuteProductTableFilterCommand pRefreshCommand,
            final EditProductTableFilterCommand pEditCommand,
            final DeleteProductsCommand pDeleteCommand,
            final ExportProductsOnXmlCommand pExportXmlCommand,
            final MinimizeProductListingCommand pMinimizePanel,
            final MaximizeProductListingCommand pMaximizePanel) {
        super();
        minimizePanel = pMinimizePanel;
        maximizePanel = pMaximizePanel;
        registerStandardCommand(ActionName.FILTER_PRODUCT_REFRESH,
                pRefreshCommand);
        registerStandardCommand(ActionName.FILTER_PRODUCT_EDIT, pEditCommand);
        registerStandardCommand(ActionName.PRODUCTS_DELETE, pDeleteCommand);
        registerStandardCommand(ActionName.PRODUCTS_EXPORT
                + ActionName.EXPORT_XML, pExportXmlCommand);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.menu.AbstractMenuBuilder#buildMenu(java.util.Map,
     *      java.util.List)
     */
    @Override
    public GpmToolBar buildMenu(final Map<String, UiAction> pActions,
            final List<UiAction> pExtendedActions) {
        // Clean tool bar
        resetToolBar();

        // Add actions buttons
        if (pActions != null) {
            final List<UiAction> lToolBarA = new ArrayList<UiAction>();
            final List<UiAction> lToolBarB = new ArrayList<UiAction>();
            final List<UiAction> lToolBarC = new ArrayList<UiAction>();

            // Create the first tool bar
            lToolBarA.add(pActions.get(ActionName.FILTER_PRODUCT_REFRESH));

            // Create the second tool bar
            lToolBarB.add(pActions.get(ActionName.FILTER_PRODUCT_EDIT));

            // Create the third tool bar
            lToolBarC.add(pActions.get(ActionName.PRODUCTS_DELETE));
            lToolBarC.add(pActions.get(ActionName.PRODUCTS_EXPORT));

            addToolBar(lToolBarA);
            addToolBar(lToolBarB);
            addToolBar(lToolBarC);
        }

        // Add minimize-maximize actions
        toolBar.addSeparatedToolBar();
        toolBar.addEntry(ComponentResources.INSTANCE.images().minimizeUp(),
                "Minimize", minimizePanel);
        toolBar.addEntry(ComponentResources.INSTANCE.images().maximize(),
                "Maximize", maximizePanel);

        return toolBar;
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