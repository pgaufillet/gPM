/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.workspace;

import org.topcased.gpm.ui.component.client.layout.GpmLayoutPanelWithMenu;
import org.topcased.gpm.ui.component.client.layout.GpmWorkspaceLayoutPanel;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;

import com.google.gwt.user.client.ui.Widget;

/**
 * View for a workspace that contain navigation, listing and detail zone.
 * 
 * @author tpanuel
 */
public abstract class WorkspaceView extends GpmLayoutPanelWithMenu implements
        WorkspaceDisplay {
    /**
     * Create a workspace view.
     */
    public WorkspaceView() {
        super(false);
    }

    /**
     * Get the workspace.
     * 
     * @return The workspace.
     */
    public GpmWorkspaceLayoutPanel getWorkspace() {
        return (GpmWorkspaceLayoutPanel) getContent();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.ProductWorkspaceDisplay#setToolBar(org.topcased.gpm.ui.component.client.menu.GpmToolBar)
     */
    @Override
    public void setToolBar(final GpmToolBar pToolBar) {
        getMenu().setToolBar(pToolBar);
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

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.WorkspaceDisplay#maximizeOrRestoreDetailPanel()
     */
    @Override
    public void maximizeOrRestoreDetailPanel() {
        getWorkspace().maximizeOrRestoreDetailPanel();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.WorkspaceDisplay#maximizeOrRestoreListingPanel()
     */
    @Override
    public void maximizeOrRestoreListingPanel() {
        getWorkspace().maximizeOrRestoreListingPanel();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.WorkspaceDisplay#minimizeDetailPanel()
     */
    @Override
    public void minimizeDetailPanel() {
        getWorkspace().minimizeDetailPanel();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.WorkspaceDisplay#minimizeListingPanel()
     */
    @Override
    public void minimizeListingPanel() {
        getWorkspace().minimizeListingPanel();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.WorkspaceDisplay#minimizeNavigationPanel()
     */
    @Override
    public void minimizeNavigationPanel() {
        getWorkspace().minimizeNavigationPanel();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.WorkspaceDisplay#restoreDetailPanel()
     */
    @Override
    public void restoreDetailPanel() {
        getWorkspace().restoreDetailPanel();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.WorkspaceDisplay#restoreListingPanel()
     */
    @Override
    public void restoreListingPanel() {
        getWorkspace().restoreListingPanel();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.WorkspaceDisplay#restoreNavigationPanel()
     */
    @Override
    public void restoreNavigationPanel() {
        getWorkspace().restoreNavigationPanel();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.WorkspaceDisplay#hideDetailPanel()
     */
    @Override
    public void hideDetailPanel() {
        getWorkspace().hideDetailPanel();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.WorkspaceDisplay#showDetailPanel()
     */
    @Override
    public void showDetailPanel() {
        getWorkspace().showDetailPanel();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.workspace.WorkspaceDisplay#makeDetailPanelVisible()
     */
    @Override
    public void makeDetailPanelVisible() {
        getWorkspace().makeDetailPanelVisible();
    }
}