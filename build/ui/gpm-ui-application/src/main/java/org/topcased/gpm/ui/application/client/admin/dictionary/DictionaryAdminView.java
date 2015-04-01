/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.dictionary;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import org.topcased.gpm.ui.application.client.admin.dictionary.navigation.DictionaryNavigationDisplay;
import org.topcased.gpm.ui.application.client.common.workspace.WorkspaceView;
import org.topcased.gpm.ui.application.client.common.workspace.detail.EmptyDetailDisplay;
import org.topcased.gpm.ui.application.client.user.ResizableLayoutPanelWrapper;
import org.topcased.gpm.ui.component.client.button.GpmImageButton;
import org.topcased.gpm.ui.component.client.layout.GpmWorkspaceLayoutPanel;
import org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel;
import org.topcased.gpm.ui.component.client.menu.GpmMenuTitle;

import com.google.gwt.user.client.ui.Widget;

/**
 * View for the dictionary administration space.
 * 
 * @author tpanuel
 */
public class DictionaryAdminView extends WorkspaceView implements
        DictionaryAdminDisplay {

    private static final int LISTING_PANEL_HEIGHT = 100;

    private static final int NAVIGATION_PANEL_WIDTH = 300;

    private ResizableLayoutPanelWrapper detailPanel;

    private ResizableLayoutPanelWrapper listingPanel;

    private ResizableLayoutPanelWrapper navigationPanel;

    /**
     * Create a product administration space.
     */
    public DictionaryAdminView() {
        super();
        getMenu().addTitle(
                new GpmMenuTitle(CONSTANTS.adminDictionaryTitle(), true));
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
     * @see org.topcased.gpm.ui.application.client.admin.dictionary.DictionaryAdminDisplay#setContent(org.topcased.gpm.ui.application.client.admin.dictionary.navigation.DictionaryNavigationDisplay,
     *      org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel,
     *      org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel)
     */
    @Override
    public void setContent(
            DictionaryNavigationDisplay pDictionaryNavigationDisplay,
            IResizableLayoutPanel pListingDisplay,
            IResizableLayoutPanel pDetailDisplay) {
        final GpmWorkspaceLayoutPanel lWorkspace =
                (GpmWorkspaceLayoutPanel) getContent();

        navigationPanel =
                new ResizableLayoutPanelWrapper(pDictionaryNavigationDisplay);
        detailPanel = new ResizableLayoutPanelWrapper(pDetailDisplay);
        listingPanel = new ResizableLayoutPanelWrapper(pListingDisplay);

        lWorkspace.setNavigationPanel(navigationPanel, NAVIGATION_PANEL_WIDTH);
        lWorkspace.setListingPanel(listingPanel, LISTING_PANEL_HEIGHT);

        lWorkspace.setDetailPanel(detailPanel);

        if (pDetailDisplay instanceof EmptyDetailDisplay) {
            hideDetailPanel();
        }
        else {
            showDetailPanel();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.dictionary.DictionaryAdminDisplay#setDetailContent(org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel)
     */
    @Override
    public void setDetailContent(IResizableLayoutPanel pDetailDisplay) {
        final GpmWorkspaceLayoutPanel lWorkspace =
                (GpmWorkspaceLayoutPanel) getContent();

        detailPanel = new ResizableLayoutPanelWrapper(pDetailDisplay);

        lWorkspace.setDetailPanel(detailPanel);

        if (pDetailDisplay instanceof EmptyDetailDisplay) {
            hideDetailPanel();
        }
        else {
            showDetailPanel();
        }
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