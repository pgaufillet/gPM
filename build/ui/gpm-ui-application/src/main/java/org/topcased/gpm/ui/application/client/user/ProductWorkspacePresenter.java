/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.user;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.application.client.command.OpenCloseWorkspacePanelAction;
import org.topcased.gpm.ui.application.client.command.connection.ChangeRoleCommand;
import org.topcased.gpm.ui.application.client.common.tab.TabElementPresenter;
import org.topcased.gpm.ui.application.client.common.workspace.OpenCloseWorkspacePanelHandler;
import org.topcased.gpm.ui.application.client.common.workspace.WorkspacePanelAction;
import org.topcased.gpm.ui.application.client.common.workspace.WorkspacePanelType;
import org.topcased.gpm.ui.application.client.event.LocalEvent;
import org.topcased.gpm.ui.application.client.menu.user.ProductWorkspaceMenuBuilder;
import org.topcased.gpm.ui.application.client.user.detail.SheetDetailPresenter;
import org.topcased.gpm.ui.application.client.user.listing.SheetListingPresenter;
import org.topcased.gpm.ui.application.client.user.navigation.SheetNavigationPresenter;
import org.topcased.gpm.ui.application.shared.command.authorization.ConnectResult;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.inject.Inject;

/**
 * The presenter for the ProductWorkspaceView.
 * 
 * @author tpanuel
 */
public class ProductWorkspacePresenter extends
        TabElementPresenter<ProductWorkspaceDisplay> {
    private final SheetNavigationPresenter navigation;

    private final SheetListingPresenter listing;

    private final SheetDetailPresenter detail;

    private final ProductWorkspaceMenuBuilder menuBuilder;

    private final ChangeRoleCommand changeRole;

    private boolean initWithSheet;

    private String productName;

    /**
     * Create a presenter for the ProductWorkspaceView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pNavigation
     *            The navigation presenter.
     * @param pListing
     *            The listing presenter.
     * @param pDetail
     *            The detail presenter.
     * @param pMenuBuilder
     *            The menu builder.
     * @param pChangeRole
     *            The command to change role.
     */
    @Inject
    public ProductWorkspacePresenter(final ProductWorkspaceDisplay pDisplay,
            final EventBus pEventBus,
            final SheetNavigationPresenter pNavigation,
            final SheetListingPresenter pListing,
            final SheetDetailPresenter pDetail,
            final ProductWorkspaceMenuBuilder pMenuBuilder,
            final ChangeRoleCommand pChangeRole) {
        super(pDisplay, pEventBus);
        navigation = pNavigation;
        listing = pListing;
        detail = pDetail;
        menuBuilder = pMenuBuilder;
        changeRole = pChangeRole;
    }

    /**
     * Initialize a product workspace with connection information.
     * 
     * @param pConnectResult
     *            The connection information.
     * @param pInitWithSheet
     *            If the workspace is open with at less one sheet.
     */
    public void init(final ConnectResult pConnectResult,
            final Boolean pInitWithSheet) {
        // Set the product name
        productName = pConnectResult.getProductName();
        navigation.setProductName(productName);
        listing.setProductName(productName);
        detail.setProductName(productName);
        getDisplay().setProductName(productName);

        // Set the roles
        for (final Translation lRole : pConnectResult.getRoles()) {
            getDisplay().addAvailableRole(lRole);
        }
        getDisplay().selectRole(pConnectResult.getRoleName());

        // Set the filters
        navigation.setTableProcessFilters(pConnectResult.getFilters().getTableProcessFilters());
        navigation.setTableProductFilters(pConnectResult.getFilters().getTableProductFilters());
        navigation.setTableUserFilters(pConnectResult.getFilters().getTableUserFilters());
        navigation.setTreeFilters(pConnectResult.getFilters().getTreeFilters());

        // Add create filter button
        navigation.setAddingFilterEnable(pConnectResult.isFilterCreatable());

        // Set the welcome page
        if (pConnectResult.getHomePageUrl() != null) {
            listing.getDisplay().setIFrameURL(pConnectResult.getHomePageUrl());
        }

        // Set the menu
        getDisplay().setToolBar(
                menuBuilder.buildMenu(pConnectResult.getActions(),
                        pConnectResult.getExtendedActions()));

        // If at less a sheet is displayed
        initWithSheet = pInitWithSheet;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.tab.TabElementPresenter#getTabId()
     */
    @Override
    public String getTabId() {
        return productName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    @Override
    protected void onBind() {
        // Add handler for open or close sub panels
        addEventHandler(
                LocalEvent.OPEN_CLOSE_SHEET_WORKSPACE.getType(productName),
                new OpenCloseWorkspacePanelHandler(getDisplay()));
        // Add handler on change role
        getDisplay().addRoleChangeHandler(changeRole);
        // Display sub panels
        getDisplay().setContent(navigation.getDisplay(), listing.getDisplay(),
                detail.getDisplay());
        // Tool bars for minimized displays
        display.getNavigationRestoreButton().addClickHandler(
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent pEvent) {
                        fireEvent(
                                LocalEvent.OPEN_CLOSE_SHEET_WORKSPACE.getType(productName),
                                new OpenCloseWorkspacePanelAction(
                                        WorkspacePanelType.NAVIGATION,
                                        WorkspacePanelAction.RESTORE));
                    }
                });
        display.getListingRestoreButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent pEvent) {
                fireEvent(
                        LocalEvent.OPEN_CLOSE_SHEET_WORKSPACE.getType(productName),
                        new OpenCloseWorkspacePanelAction(
                                WorkspacePanelType.LISTING,
                                WorkspacePanelAction.MAXIMIZE_RESTORE));
            }
        });
        display.getDetailRestoreButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent pEvent) {
                fireEvent(
                        LocalEvent.OPEN_CLOSE_SHEET_WORKSPACE.getType(productName),
                        new OpenCloseWorkspacePanelAction(
                                WorkspacePanelType.DETAIL,
                                WorkspacePanelAction.MAXIMIZE_RESTORE));
            }
        });
        // Bind sub panels
        navigation.bind();
        listing.bind();
        detail.bind();
        if (!initWithSheet) {
            // Detail is hidden because without tab, deferred to let it initialize first
            DeferredCommand.addCommand(new Command() {
                @Override
                public void execute() {
                    fireEvent(
                            LocalEvent.OPEN_CLOSE_SHEET_WORKSPACE.getType(productName),
                            new OpenCloseWorkspacePanelAction(
                                    WorkspacePanelType.DETAIL,
                                    WorkspacePanelAction.HIDE));
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.AbstractPresenter#onUnbind()
     */
    @Override
    protected void onUnbind() {
        navigation.unbind();
        listing.unbind();
        detail.unbind();
    }

    /**
     * Get the navigation presenter.
     * 
     * @return The navigation presenter.
     */
    public SheetNavigationPresenter getNavigation() {
        return navigation;
    }

    /**
     * Get the listing presenter.
     * 
     * @return The listing presenter.
     */
    public SheetListingPresenter getListing() {
        return listing;
    }

    /**
     * Get the detail presenter.
     * 
     * @return The detail presenter.
     */
    public SheetDetailPresenter getDetail() {
        return detail;
    }

    /**
     * Get the selected role.
     * 
     * @return The selected role.
     */
    public String getSelectedRole() {
        return getDisplay().getSelectedRole();
    }

    /**
     * Cancel the displayed user selected role
     */
    public void rollBackSelectedRole() {
        getDisplay().rollBackSelectedRole();
    }
}