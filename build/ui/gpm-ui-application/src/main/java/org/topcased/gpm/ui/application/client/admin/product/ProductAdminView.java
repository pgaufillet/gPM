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

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import org.topcased.gpm.ui.application.client.admin.product.detail.ProductDetailDisplay;
import org.topcased.gpm.ui.application.client.admin.product.listing.ProductListingDisplay;
import org.topcased.gpm.ui.application.client.admin.product.navigation.ProductNavigationDisplay;
import org.topcased.gpm.ui.application.client.common.workspace.WorkspaceView;
import org.topcased.gpm.ui.application.client.user.ResizableLayoutPanelWrapper;
import org.topcased.gpm.ui.component.client.button.GpmImageButton;
import org.topcased.gpm.ui.component.client.layout.GpmWorkspaceLayoutPanel;
import org.topcased.gpm.ui.component.client.menu.GpmMenuTitle;

import com.google.gwt.event.dom.client.HasClickHandlers;

/**
 * View for the product administration space.
 * 
 * @author tpanuel
 */
public class ProductAdminView extends WorkspaceView implements
        ProductAdminDisplay {
    /** LISTING_PANEL_HEIGHT */
    private static final int LISTING_PANEL_HEIGHT = 200;

    /** NAVIGATION_PANEL_WIDTH */
    private static final int NAVIGATION_PANEL_WIDTH = 300;

    private ResizableLayoutPanelWrapper navigationPanel;

    private ResizableLayoutPanelWrapper listingPanel;

    private ResizableLayoutPanelWrapper detailPanel;

    private GpmImageButton navigationMinimizeButton;

    private GpmImageButton listingMinimizeButton;

    private GpmImageButton detailMinimizeButton;

    /**
     * Create a product administration space.
     */
    public ProductAdminView() {
        super();
        getMenu().addTitle(
                new GpmMenuTitle(CONSTANTS.adminProductTitle(), true));
        setContent(new GpmWorkspaceLayoutPanel());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.product.ProductAdminDisplay#setContent(org.topcased.gpm.ui.application.client.admin.product.navigation.ProductNavigationDisplay,
     *      org.topcased.gpm.ui.application.client.admin.product.listing.ProductListingDisplay,
     *      org.topcased.gpm.ui.application.client.admin.product.detail.ProductDetailDisplay)
     */
    @Override
    public void setContent(
            final ProductNavigationDisplay pProductNavigationDisplay,
            final ProductListingDisplay pProductListingDisplay,
            final ProductDetailDisplay pProductDetailDisplay) {
        final GpmWorkspaceLayoutPanel lWorkspace =
                (GpmWorkspaceLayoutPanel) getContent();

        navigationPanel =
                new ResizableLayoutPanelWrapper(pProductNavigationDisplay);
        listingPanel = new ResizableLayoutPanelWrapper(pProductListingDisplay);
        detailPanel = new ResizableLayoutPanelWrapper(pProductDetailDisplay);

        navigationMinimizeButton =
                new GpmImageButton(INSTANCE.images().minimizeRight());
        navigationMinimizeButton.setTitle(CONSTANTS.restore());
        navigationPanel.setMinimizedButton(navigationMinimizeButton);

        listingMinimizeButton =
                new GpmImageButton(INSTANCE.images().minimizeDown());
        listingMinimizeButton.setTitle(CONSTANTS.restore());
        listingPanel.setMinimizedButton(listingMinimizeButton);

        detailMinimizeButton =
                new GpmImageButton(INSTANCE.images().minimizeUp());
        detailMinimizeButton.setTitle(CONSTANTS.restore());
        detailPanel.setMinimizedButton(detailMinimizeButton);

        lWorkspace.setNavigationPanel(navigationPanel, NAVIGATION_PANEL_WIDTH);
        lWorkspace.setListingPanel(listingPanel, LISTING_PANEL_HEIGHT);
        lWorkspace.setDetailPanel(detailPanel);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.ProductWorkspaceDisplay#setMinimizedContent(org.topcased.gpm.ui.component.client.button.GpmImageButton,
     *      org.topcased.gpm.ui.component.client.button.GpmImageButton,
     *      org.topcased.gpm.ui.component.client.button.GpmImageButton)
     */
    @Override
    public void setMinimizedContent(final GpmImageButton pNavigationButton,
            final GpmImageButton pListingButton,
            final GpmImageButton pDetailButton) {
        navigationPanel.setMinimizedButton(pNavigationButton);
        listingPanel.setMinimizedButton(pListingButton);
        detailPanel.setMinimizedButton(pDetailButton);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.product.ProductAdminDisplay#getDetailRestoreButton()
     */
    @Override
    public HasClickHandlers getDetailRestoreButton() {
        return detailMinimizeButton;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.product.ProductAdminDisplay#getListingRestoreButton()
     */
    @Override
    public HasClickHandlers getListingRestoreButton() {
        return listingMinimizeButton;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.product.ProductAdminDisplay#getNavigationRestoreButton()
     */
    @Override
    public HasClickHandlers getNavigationRestoreButton() {
        return navigationMinimizeButton;
    }
}