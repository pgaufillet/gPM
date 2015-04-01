/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.product;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.admin.product.detail.ProductDetailPresenter;
import org.topcased.gpm.ui.application.client.admin.product.listing.ProductListingPresenter;
import org.topcased.gpm.ui.application.client.admin.product.navigation.ProductNavigationPresenter;
import org.topcased.gpm.ui.application.client.command.OpenCloseWorkspacePanelAction;
import org.topcased.gpm.ui.application.client.common.AbstractPresenter;
import org.topcased.gpm.ui.application.client.common.workspace.OpenCloseWorkspacePanelHandler;
import org.topcased.gpm.ui.application.client.common.workspace.WorkspacePanelAction;
import org.topcased.gpm.ui.application.client.common.workspace.WorkspacePanelType;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.menu.admin.product.ProductAdminMenuBuilder;
import org.topcased.gpm.ui.application.shared.command.authorization.OpenAdminModuleResult;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.inject.Inject;

/**
 * The presenter for the ProductAdminView.
 * 
 * @author tpanuel
 */
public class ProductAdminPresenter extends
        AbstractPresenter<ProductAdminDisplay> {
    private final ProductNavigationPresenter navigation;

    private final ProductListingPresenter listing;

    private final ProductDetailPresenter detail;

    private final ProductAdminMenuBuilder menuBuilder;

    /**
     * Create a presenter for the ProductAdminView.
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
     */
    @Inject
    public ProductAdminPresenter(final ProductAdminDisplay pDisplay,
            final EventBus pEventBus,
            final ProductNavigationPresenter pNavigation,
            final ProductListingPresenter pListing,
            final ProductDetailPresenter pDetail,
            final ProductAdminMenuBuilder pMenuBuilder) {
        super(pDisplay, pEventBus);
        navigation = pNavigation;
        listing = pListing;
        detail = pDetail;
        menuBuilder = pMenuBuilder;
    }

    /**
     * Initialize the navigation.
     * 
     * @param pResult
     *            The filter list.
     */
    public void init(final OpenAdminModuleResult pResult) {
        // Add the menu tool bar
        getDisplay().setToolBar(
                menuBuilder.buildMenu(pResult.getProductActions(), null));

        // Set the filters
        navigation.setTableProcessFilters(pResult.getFilters().getTableProcessFilters());
        navigation.setTableProductFilters(pResult.getFilters().getTableProductFilters());
        navigation.setTableUserFilters(pResult.getFilters().getTableUserFilters());
        navigation.setTreeFilters(pResult.getFilters().getTreeFilters());

        // Add create filter button
        navigation.setAddingFilterEnable(pResult.isFilterCreatable());
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    @Override
    protected void onBind() {
        // Add handler for open or close sub panels
        addEventHandler(GlobalEvent.OPEN_CLOSE_PRODUCT_WORKSPACE.getType(),
                new OpenCloseWorkspacePanelHandler(getDisplay()));
        // Display sub panels
        getDisplay().setContent(navigation.getDisplay(), listing.getDisplay(),
                detail.getDisplay());
        // Tool bars for minimized displays
        display.getNavigationRestoreButton().addClickHandler(
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent pEvent) {
                        fireEvent(
                                GlobalEvent.OPEN_CLOSE_PRODUCT_WORKSPACE.getType(),
                                new OpenCloseWorkspacePanelAction(
                                        WorkspacePanelType.NAVIGATION,
                                        WorkspacePanelAction.RESTORE));
                    }
                });
        display.getListingRestoreButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent pEvent) {
                fireEvent(GlobalEvent.OPEN_CLOSE_PRODUCT_WORKSPACE.getType(),
                        new OpenCloseWorkspacePanelAction(
                                WorkspacePanelType.LISTING,
                                WorkspacePanelAction.MAXIMIZE_RESTORE));
            }
        });
        display.getDetailRestoreButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent pEvent) {
                fireEvent(GlobalEvent.OPEN_CLOSE_PRODUCT_WORKSPACE.getType(),
                        new OpenCloseWorkspacePanelAction(
                                WorkspacePanelType.DETAIL,
                                WorkspacePanelAction.MAXIMIZE_RESTORE));
            }
        });
        // Bind sub panels
        navigation.bind();
        listing.bind();
        detail.bind();
        // Detail is hidden because without tab, deferred to let it initialize first
        DeferredCommand.addCommand(new Command() {
            @Override
            public void execute() {
                fireEvent(GlobalEvent.OPEN_CLOSE_PRODUCT_WORKSPACE.getType(),
                        new OpenCloseWorkspacePanelAction(
                                WorkspacePanelType.DETAIL,
                                WorkspacePanelAction.HIDE));
            }
        });
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onUnbind()
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
    public ProductNavigationPresenter getNavigation() {
        return navigation;
    }

    /**
     * Get the listing presenter.
     * 
     * @return The listing presenter.
     */
    public ProductListingPresenter getListing() {
        return listing;
    }

    /**
     * Get the detail presenter.
     * 
     * @return The detail presenter.
     */
    public ProductDetailPresenter getDetail() {
        return detail;
    }
}