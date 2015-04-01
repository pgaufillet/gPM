/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.product.listing;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.OpenCloseWorkspacePanelAction;
import org.topcased.gpm.ui.application.client.command.admin.product.product.OpenProductOnEditionCommand;
import org.topcased.gpm.ui.application.client.command.admin.product.product.OpenProductOnVisualizationCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.FilterEditionExecuteProductFilterCommand;
import org.topcased.gpm.ui.application.client.common.workspace.listing.ListingPresenter;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.menu.admin.product.ProductListingMenuBuilder;
import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterResult;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;

/**
 * The presenter for the ProductListingView.
 * 
 * @author tpanuel
 */
public class ProductListingPresenter extends
        ListingPresenter<ProductListingDisplay> {
    /**
     * Create a presenter for the ProductListingView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pMenuBuilder
     *            The menu builder.
     * @param pVisuCommand
     *            The open product on visualization command.
     * @param pEditCommand
     *            The open product on edition command.
     * @param pFilterEditionExecuteProductFilterCommand
     *            The command to execute a product filter in edition mode.
     */
    @Inject
    public ProductListingPresenter(
            final ProductListingDisplay pDisplay,
            final EventBus pEventBus,
            final ProductListingMenuBuilder pMenuBuilder,
            final OpenProductOnVisualizationCommand pVisuCommand,
            final OpenProductOnEditionCommand pEditCommand,
            final FilterEditionExecuteProductFilterCommand pFilterEditionExecuteProductFilterCommand) {
        super(pDisplay, pEventBus, pMenuBuilder, pVisuCommand, pEditCommand,
                pFilterEditionExecuteProductFilterCommand);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.listing.ListingPresenter#getExecuteTableFilterType()
     */
    @Override
    protected Type<ActionEventHandler<AbstractCommandFilterResult>> getExecuteTableFilterType() {
        return GlobalEvent.EXECUTE_PRODUCT_TABLE_FILTER.getType();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.listing.ListingPresenter#getOpenCloseWorkspaceType()
     */
    @Override
    protected Type<ActionEventHandler<OpenCloseWorkspacePanelAction>> getOpenCloseWorkspaceType() {
        return GlobalEvent.OPEN_CLOSE_PRODUCT_WORKSPACE.getType();
    }
}