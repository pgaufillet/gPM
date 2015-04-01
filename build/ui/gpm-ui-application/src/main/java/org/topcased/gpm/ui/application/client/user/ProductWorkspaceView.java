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

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.I18N;
import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.application.client.common.workspace.WorkspaceView;
import org.topcased.gpm.ui.application.client.user.detail.SheetDetailDisplay;
import org.topcased.gpm.ui.application.client.user.listing.SheetListingDisplay;
import org.topcased.gpm.ui.application.client.user.navigation.SheetNavigationDisplay;
import org.topcased.gpm.ui.component.client.button.GpmImageButton;
import org.topcased.gpm.ui.component.client.field.GpmDropDownListBoxWidget;
import org.topcased.gpm.ui.component.client.layout.GpmWorkspaceLayoutPanel;
import org.topcased.gpm.ui.component.client.menu.GpmMenuTitle;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;

/**
 * View for a product workspace that contain navigation, listing and detail zone
 * for a specific product.
 * 
 * @author tpanuel
 */
public class ProductWorkspaceView extends WorkspaceView implements
        ProductWorkspaceDisplay {
    /** LISTING_PANEL_HEIGHT */
    private static final int LISTING_PANEL_HEIGHT = 200;

    /** NAVIGATION_PANEL_WIDTH */
    private static final int NAVIGATION_PANEL_WIDTH = 300;

    private final GpmMenuTitle productTitle;

    private final GpmDropDownListBoxWidget<String> roleList;

    private final FlowPanel rolePanel;

    private String productName;

    private ResizableLayoutPanelWrapper navigationPanel;

    private ResizableLayoutPanelWrapper listingPanel;

    private ResizableLayoutPanelWrapper detailPanel;

    private GpmImageButton navigationMinimizeButton;

    private GpmImageButton listingMinimizeButton;

    private GpmImageButton detailMinimizeButton;

    /**
     * Create a product workspace view.
     */
    public ProductWorkspaceView() {
        super();
        setContent(new GpmWorkspaceLayoutPanel());
        productTitle = new GpmMenuTitle(true);
        roleList = new GpmDropDownListBoxWidget<String>();
        roleList.setTitle(CONSTANTS.workspaceUserSelectRole());
        getMenu().addTitle(productTitle);

        // Will handle the show/hide Label or DropDownList behavior
        rolePanel = new FlowPanel();
        rolePanel.addStyleName(INSTANCE.css().gpmMenuListBox());
        InlineLabel lLabel = new InlineLabel();
        lLabel.setStyleName(INSTANCE.css().gpmDropDownTextReadOnly());
        lLabel.getElement().getStyle().setBorderStyle(BorderStyle.NONE);
        rolePanel.add(lLabel);
        getMenu().addTitle(rolePanel);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.ProductWorkspaceDisplay#setProductName(java.lang.String)
     */
    @Override
    public void setProductName(final String pProductName) {
        productName = pProductName;
        productTitle.setHTML(I18N.getConstant(productName));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.ProductWorkspaceDisplay#addAvailableRole(java.lang.String)
     */
    @Override
    public void addAvailableRole(final Translation pRole) {
        roleList.addItem('(' + pRole.getTranslatedValue() + ')',
                pRole.getTranslatedValue(), pRole.getValue());
        if (roleList.getItems().size() > 1) { // If more than one role: display the selector
            rolePanel.clear();
            rolePanel.add(roleList);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.ProductWorkspaceDisplay#selectRole(java.lang.String)
     */
    @Override
    public void selectRole(final String pCurrentRole) {
        roleList.setValue(pCurrentRole);
        if (rolePanel.getWidget(0) != roleList) {
            ((Label) rolePanel.getWidget(0)).setText("(" + pCurrentRole + ")");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.ProductWorkspaceDisplay#setContent(org.topcased.gpm.ui.application.client.user.navigation.SheetNavigationDisplay,
     *      org.topcased.gpm.ui.application.client.user.listing.SheetListingDisplay,
     *      org.topcased.gpm.ui.application.client.user.detail.SheetDetailDisplay)
     */
    @Override
    public void setContent(SheetNavigationDisplay pSheetNavigationDisplay,
            SheetListingDisplay pSheetListingDisplay,
            SheetDetailDisplay pSheetDetailDisplay) {
        final GpmWorkspaceLayoutPanel lWorkspace =
                (GpmWorkspaceLayoutPanel) getContent();

        navigationPanel =
                new ResizableLayoutPanelWrapper(pSheetNavigationDisplay);
        listingPanel = new ResizableLayoutPanelWrapper(pSheetListingDisplay);
        detailPanel = new ResizableLayoutPanelWrapper(pSheetDetailDisplay);
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
     * @see org.topcased.gpm.ui.application.client.common.tab.TabElementDisplay#getTabTitle()
     */
    @Override
    public String getTabTitle() {
        return productName;
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
     * @see org.topcased.gpm.ui.application.client.user.ProductWorkspaceDisplay#addRoleChangeHandler(com.google.gwt.event.logical.shared.ValueChangeHandler)
     */
    @Override
    public void addRoleChangeHandler(final ValueChangeHandler<String> pHandler) {
        roleList.addValueChangeHandler(pHandler);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.ProductWorkspaceDisplay#getSelectedRole()
     */
    @Override
    public String getSelectedRole() {
        return roleList.getValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.ProductWorkspaceDisplay#getDetailRestoreButton()
     */
    @Override
    public HasClickHandlers getDetailRestoreButton() {
        return detailMinimizeButton;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.ProductWorkspaceDisplay#getListingRestoreButton()
     */
    @Override
    public HasClickHandlers getListingRestoreButton() {
        return listingMinimizeButton;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.ProductWorkspaceDisplay#getNavigationRestoreButton()
     */
    @Override
    public HasClickHandlers getNavigationRestoreButton() {
        return navigationMinimizeButton;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.ProductWorkspaceDisplay#rollBackSelectedRole()
     */
    @Override
    public void rollBackSelectedRole() {
        roleList.rollBack();
    }
}