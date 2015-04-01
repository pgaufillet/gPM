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
import org.topcased.gpm.ui.application.client.command.admin.product.product.EditProductAttributesCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.ExportProductOnXmlCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.OpenProductOnVisualizationCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.UpdateProductCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.workspace.MaximizeProductDetailCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.workspace.MinimizeProductDetailCommand;
import org.topcased.gpm.ui.application.client.command.builder.AddProductLinkCommandBuilder;
import org.topcased.gpm.ui.application.client.command.builder.DeleteProductLinkCommandBuilder;
import org.topcased.gpm.ui.application.shared.util.ExtendedActionType;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.facade.shared.action.UiAction;

import com.google.inject.Inject;

/**
 * A builder for the menu of the product edition detail panel.
 * 
 * @author tpanuel
 */
public class ProductDetailEditionMenuBuilder extends
        AbstractProductDetailMenuBuilder {
    static {
        IMAGES.put(ActionName.PRODUCT_SAVE,
                ComponentResources.INSTANCE.images().sheetSave());
        IMAGES.put(ActionName.PRODUCT_DISPLAY,
                ComponentResources.INSTANCE.images().sheetVisu());
        IMAGES.put(ActionName.PRODUCT_DELETE,
                ComponentResources.INSTANCE.images().sheetDelete());
        IMAGES.put(ActionName.PRODUCT_LINK,
                ComponentResources.INSTANCE.images().linkAdd());
        IMAGES.put(ActionName.PRODUCT_UNLINK,
                ComponentResources.INSTANCE.images().linkDelete());
        IMAGES.put(ActionName.PRODUCT_EXPORT,
                ComponentResources.INSTANCE.images().sheetExport());
        IMAGES.put(ActionName.PRODUCT_ATTRIBUTES_EDIT,
                ComponentResources.INSTANCE.images().sheetState());
    }

    /**
     * Create a builder for the menu of the product edition detail panel.
     * 
     * @param pUpdateCommand
     *            The update command.
     * @param pDeleteCommand
     *            The delete command.
     * @param pVisualizationCommand
     *            The visualize command.
     * @param pAddLinkBuilder
     *            The add link command builder.
     * @param pDeleteLinkBuilder
     *            The delete link command builder.
     * @param pExportXml
     *            The export XML command.
     * @param pAttributesCommand
     *            The edit attributes command.
     * @param pMinimizePanel
     *            The minimize panel command.
     * @param pMaximizePanel
     *            The maximize panel command.
     */
    @Inject
    public ProductDetailEditionMenuBuilder(
            final UpdateProductCommand pUpdateCommand,
            final DeleteProductCommand pDeleteCommand,
            final OpenProductOnVisualizationCommand pVisualizationCommand,
            final AddProductLinkCommandBuilder pAddLinkBuilder,
            final DeleteProductLinkCommandBuilder pDeleteLinkBuilder,
            final ExportProductOnXmlCommand pExportXml,
            final EditProductAttributesCommand pAttributesCommand,
            final MinimizeProductDetailCommand pMinimizePanel,
            final MaximizeProductDetailCommand pMaximizePanel) {
        super(pMinimizePanel, pMaximizePanel);
        pVisualizationCommand.forceRefresh();
        registerStandardCommand(ActionName.PRODUCT_SAVE, pUpdateCommand);
        registerStandardCommand(ActionName.PRODUCT_DISPLAY,
                pVisualizationCommand);
        registerStandardCommand(ActionName.PRODUCT_DELETE, pDeleteCommand);
        registerDynamicCommandBuilder(ActionName.PRODUCT_LINK, pAddLinkBuilder);
        registerDynamicCommandBuilder(ActionName.PRODUCT_UNLINK,
                pDeleteLinkBuilder);
        registerStandardCommand(ActionName.PRODUCT_EXPORT
                + ActionName.EXPORT_XML, pExportXml);
        registerStandardCommand(ActionName.PRODUCT_ATTRIBUTES_EDIT,
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
            final List<UiAction> lToolBarE = new ArrayList<UiAction>();

            // Create the first tool bar
            lToolBarA.add(pActions.get(ActionName.PRODUCT_SAVE));

            // Create the second tool bar
            lToolBarB.add(pActions.get(ActionName.PRODUCT_DISPLAY));
            lToolBarB.add(pActions.get(ActionName.PRODUCT_DELETE));

            // Create the third tool bar
            lToolBarC.add(pActions.get(ActionName.PRODUCT_LINK));
            lToolBarC.add(pActions.get(ActionName.PRODUCT_UNLINK));

            // Create the fourth tool bar
            lToolBarD.add(pActions.get(ActionName.PRODUCT_EXPORT));

            // Create the fifth tool bar
            lToolBarE.add(pActions.get(ActionName.PRODUCT_ATTRIBUTES_EDIT));

            addToolBar(lToolBarA);
            addToolBar(lToolBarB);
            addToolBar(lToolBarC);
            addToolBar(lToolBarD);
            addToolBar(lToolBarE);
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