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
import org.topcased.gpm.ui.application.client.command.admin.product.product.ImportProductCommand;
import org.topcased.gpm.ui.application.client.command.builder.OpenProductOnCreationCommandBuilder;
import org.topcased.gpm.ui.application.client.menu.AbstractMenuBuilder;
import org.topcased.gpm.ui.application.shared.util.ExtendedActionType;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.facade.shared.action.UiAction;

import com.google.inject.Inject;

/**
 * A builder for the main menu of the product administration.
 * 
 * @author tpanuel
 */
public class ProductAdminMenuBuilder extends AbstractMenuBuilder {
    static {
        IMAGES.put(ActionName.PRODUCT_CREATION,
                ComponentResources.INSTANCE.images().sheetAdd());
        IMAGES.put(ActionName.PRODUCTS_IMPORT,
                ComponentResources.INSTANCE.images().sheetImport());
    }

    /**
     * Create a builder for the main menu of the product administration.
     * 
     * @param pCreateCommandBuilder
     *            Command builder for creation command.
     * @param pImportCommand
     *            Command to import a product.
     */
    @Inject
    public ProductAdminMenuBuilder(
            final OpenProductOnCreationCommandBuilder pCreateCommandBuilder,
            final ImportProductCommand pImportCommand) {
        super();
        registerDynamicCommandBuilder(ActionName.PRODUCT_CREATION,
                pCreateCommandBuilder);
        registerStandardCommand(ActionName.PRODUCTS_IMPORT, pImportCommand);
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
        if (pActions != null) {
            final List<UiAction> lToolBarA = new ArrayList<UiAction>();
            final List<UiAction> lToolBarB = new ArrayList<UiAction>();

            // Create the first tool bar
            lToolBarA.add(pActions.get(ActionName.PRODUCT_CREATION));
            // Create the second tool bar
            lToolBarB.add(pActions.get(ActionName.PRODUCTS_IMPORT));

            addToolBar(lToolBarA);
            addToolBar(lToolBarB);
        }

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