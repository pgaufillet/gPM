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
import org.topcased.gpm.ui.application.client.command.admin.product.product.DeleteProductCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.DisplayProductAttributesCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.ExportProductOnXmlCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.OpenProductOnEditionCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.OpenProductOnVisualizationCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.workspace.MaximizeProductDetailCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.workspace.MinimizeProductDetailCommand;
import org.topcased.gpm.ui.application.shared.util.ExtendedActionType;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.facade.shared.action.UiAction;

import com.google.inject.Inject;

/**
 * A builder for the menu of the product visualization detail panel.
 * 
 * @author tpanuel
 */
public class ProductDetailVisualizationMenuBuilder extends
        AbstractProductDetailMenuBuilder {
    static {
        IMAGES.put(ActionName.PRODUCT_REFRESH,
                ComponentResources.INSTANCE.images().refresh());
        IMAGES.put(ActionName.PRODUCT_EDIT,
                ComponentResources.INSTANCE.images().sheetEdit());
        IMAGES.put(ActionName.PRODUCT_DELETE,
                ComponentResources.INSTANCE.images().sheetDelete());
        IMAGES.put(ActionName.PRODUCT_EXPORT,
                ComponentResources.INSTANCE.images().sheetExport());
        IMAGES.put(ActionName.PRODUCT_ATTRIBUTES_DISPLAY,
                ComponentResources.INSTANCE.images().sheetState());
    }

    /**
     * Create a builder for the menu of the product visualization detail panel.
     * 
     * @param pVisualizationCommand
     *            The visualization command.
     * @param pEdititionCommand
     *            The edition command.
     * @param pDeleteCommand
     *            The delete command.
     * @param pExportXml
     *            The export XML command.
     * @param pMinimizePanel
     *            The minimize panel command.
     * @param pMaximizePanel
     *            The maximize panel command.
     * @param pAttributesCommand
     *            The display attributes command.
     */
    @Inject
    public ProductDetailVisualizationMenuBuilder(
            final MinimizeProductDetailCommand pMinimizePanel,
            final MaximizeProductDetailCommand pMaximizePanel,
            final OpenProductOnVisualizationCommand pVisualizationCommand,
            final OpenProductOnEditionCommand pEdititionCommand,
            final DeleteProductCommand pDeleteCommand,
            final ExportProductOnXmlCommand pExportXml,
            final DisplayProductAttributesCommand pAttributesCommand) {
        super(pMinimizePanel, pMaximizePanel);
        pVisualizationCommand.forceRefresh();
        pEdititionCommand.forceRefresh();
        registerStandardCommand(ActionName.PRODUCT_REFRESH,
                pVisualizationCommand);
        registerStandardCommand(ActionName.PRODUCT_EDIT, pEdititionCommand);
        registerStandardCommand(ActionName.PRODUCT_DELETE, pDeleteCommand);
        registerStandardCommand(ActionName.PRODUCT_EXPORT
                + ActionName.EXPORT_XML, pExportXml);
        registerStandardCommand(ActionName.PRODUCT_ATTRIBUTES_DISPLAY,
                pAttributesCommand);
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
            final List<UiAction> lToolBarB = new ArrayList<UiAction>();
            final List<UiAction> lToolBarC = new ArrayList<UiAction>();
            final List<UiAction> lToolBarD = new ArrayList<UiAction>();

            // Create the first tool bar
            lToolBarA.add(pActions.get(ActionName.PRODUCT_REFRESH));

            // Create the second tool bar
            lToolBarB.add(pActions.get(ActionName.PRODUCT_EDIT));
            lToolBarB.add(pActions.get(ActionName.PRODUCT_DELETE));

            // Create the third tool bar
            lToolBarC.add(pActions.get(ActionName.PRODUCT_EXPORT));

            // Create the fourth tool bar
            lToolBarD.add(pActions.get(ActionName.PRODUCT_ATTRIBUTES_DISPLAY));

            addToolBar(lToolBarA);
            addToolBar(lToolBarB);
            addToolBar(lToolBarC);
            addToolBar(lToolBarD);
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