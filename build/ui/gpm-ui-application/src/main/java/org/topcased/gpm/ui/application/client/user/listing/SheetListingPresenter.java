/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.user.listing;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.OpenCloseWorkspacePanelAction;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.FilterEditionExecuteSheetFilterCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.OpenSheetOnEditionCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.OpenSheetOnVisualizationCommand;
import org.topcased.gpm.ui.application.client.common.workspace.listing.ListingPresenter;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.client.event.LocalEvent;
import org.topcased.gpm.ui.application.client.menu.user.SheetListingMenuBuilder;
import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterResult;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;

/**
 * The presenter for the SheetListingView.
 * 
 * @author tpanuel
 */
public class SheetListingPresenter extends
        ListingPresenter<SheetListingDisplay> {
    private String productName;

    /**
     * Create a presenter for the SheetListingView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pMenuBuilder
     *            The menu builder.
     * @param pVisuCommand
     *            Command to open a sheet on visualization.
     * @param pEditCommand
     *            Command to open a sheet on edition.
     * @param pFilterEditionExecuteSheetFilterCommand
     *            The command to execute a sheet filter in edition mode.
     */
    @Inject
    public SheetListingPresenter(
            final SheetListingDisplay pDisplay,
            final EventBus pEventBus,
            final SheetListingMenuBuilder pMenuBuilder,
            final OpenSheetOnVisualizationCommand pVisuCommand,
            final OpenSheetOnEditionCommand pEditCommand,
            final FilterEditionExecuteSheetFilterCommand pFilterEditionExecuteSheetFilterCommand) {
        super(pDisplay, pEventBus, pMenuBuilder, pVisuCommand, pEditCommand,
                pFilterEditionExecuteSheetFilterCommand);
    }

    /**
     * Set the product name.
     * 
     * @param pProductName
     *            The product name.
     */
    public void setProductName(final String pProductName) {
        productName = pProductName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.listing.ListingPresenter#getExecuteTableFilterType()
     */
    @Override
    protected Type<ActionEventHandler<AbstractCommandFilterResult>> getExecuteTableFilterType() {
        return LocalEvent.EXECUTE_SHEET_TABLE_FILTER.getType(productName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.listing.ListingPresenter#getOpenCloseWorkspaceType()
     */
    @Override
    protected Type<ActionEventHandler<OpenCloseWorkspacePanelAction>> getOpenCloseWorkspaceType() {
        return LocalEvent.OPEN_CLOSE_SHEET_WORKSPACE.getType(productName);
    }
}