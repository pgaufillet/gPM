/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.user;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import org.topcased.gpm.ui.application.client.admin.user.listing.UserListingDisplay;
import org.topcased.gpm.ui.application.client.common.workspace.EmptyResizablePanel;
import org.topcased.gpm.ui.application.client.common.workspace.WorkspaceView;
import org.topcased.gpm.ui.application.client.user.ResizableLayoutPanelWrapper;
import org.topcased.gpm.ui.component.client.button.GpmImageButton;
import org.topcased.gpm.ui.component.client.layout.GpmWorkspaceLayoutPanel;
import org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel;
import org.topcased.gpm.ui.component.client.menu.GpmMenuTitle;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;

import com.google.gwt.user.client.ui.Widget;

/**
 * View for the user administration space.
 * 
 * @author tpanuel
 */
public class UserAdminView extends WorkspaceView implements UserAdminDisplay {

    private static final int DETAIL_PANEL_HEIGHT = 200;

    private static final int LISTING_PANEL_WIDTH = 300;

    private ResizableLayoutPanelWrapper detailPanel;

    private ResizableLayoutPanelWrapper listingPanel;

    /**
     * Create a product user space.
     */
    public UserAdminView() {
        super();
        getMenu().addTitle(new GpmMenuTitle(CONSTANTS.adminUserTitle(), true));
        setContent(new GpmWorkspaceLayoutPanel());
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.widget.WidgetDisplay#asWidget()
     */
    @Override
    public Widget asWidget() {
        return this;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.user.UserAdminDisplay#setContent(org.topcased.gpm.ui.application.client.admin.user.listing.UserListingDisplay,
     *      org.topcased.gpm.ui.application.client.admin.user.detail.UserEditionDetailDisplay)
     */
    @Override
    public void setContent(UserListingDisplay pUserListingDisplay,
            IResizableLayoutPanel pUserDetailDisplay) {
        final GpmWorkspaceLayoutPanel lWorkspace =
                (GpmWorkspaceLayoutPanel) getContent();

        listingPanel = new ResizableLayoutPanelWrapper(pUserListingDisplay);
        detailPanel = new ResizableLayoutPanelWrapper(pUserDetailDisplay);

        lWorkspace.setNavigationPanel(listingPanel, LISTING_PANEL_WIDTH);
        lWorkspace.setListingPanel(detailPanel, DETAIL_PANEL_HEIGHT);

        lWorkspace.setDetailPanel(new ResizableLayoutPanelWrapper(
                new EmptyResizablePanel()));

        hideDetailPanel();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.user.UserAdminDisplay#setDetailContent(org.topcased.gpm.ui.application.client.admin.user.detail.UserDetailDisplay)
     */
    @Override
    public void setDetailContent(IResizableLayoutPanel pUserDetailDisplay) {
        final GpmWorkspaceLayoutPanel lWorkspace =
                (GpmWorkspaceLayoutPanel) getContent();

        detailPanel = new ResizableLayoutPanelWrapper(pUserDetailDisplay);

        lWorkspace.setListingPanel(detailPanel, DETAIL_PANEL_HEIGHT);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.WorkspaceDisplay#setMinimizedContent(org.topcased.gpm.ui.component.client.button.GpmImageButton,
     *      org.topcased.gpm.ui.component.client.button.GpmImageButton,
     *      org.topcased.gpm.ui.component.client.button.GpmImageButton)
     */
    @Override
    public void setMinimizedContent(GpmImageButton pNavigationButton,
            GpmImageButton pListingButton, GpmImageButton pDetailButton) {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.user.UserAdminDisplay#setToolBar(org.topcased.gpm.ui.component.client.menu.GpmToolBar)
     */
    @Override
    public void setToolBar(GpmToolBar pToolBar) {
        getMenu().setToolBar(pToolBar);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Display#startProcessing()
     */
    @Override
    public void startProcessing() {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Display#stopProcessing()
     */
    @Override
    public void stopProcessing() {
        // Nothing to do
    }
}