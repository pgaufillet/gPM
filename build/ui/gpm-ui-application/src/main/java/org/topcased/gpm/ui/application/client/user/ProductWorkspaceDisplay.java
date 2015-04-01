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

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.application.client.common.tab.TabElementDisplay;
import org.topcased.gpm.ui.application.client.common.workspace.WorkspaceDisplay;
import org.topcased.gpm.ui.application.client.user.detail.SheetDetailDisplay;
import org.topcased.gpm.ui.application.client.user.listing.SheetListingDisplay;
import org.topcased.gpm.ui.application.client.user.navigation.SheetNavigationDisplay;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

/**
 * Display interface for the ProductWorkspaceView.
 * 
 * @author tpanuel
 */
public interface ProductWorkspaceDisplay extends TabElementDisplay,
        WorkspaceDisplay {
    /**
     * Set the product name.
     * 
     * @param pProductName
     *            The product name.
     */
    public void setProductName(String pProductName);

    /**
     * Add an available role.
     * 
     * @param pRole
     *            The role to add.
     */
    public void addAvailableRole(final Translation pRole);

    /**
     * Select a role.
     * 
     * @param pCurrentRole
     *            The current role.
     */
    public void selectRole(final String pCurrentRole);

    /**
     * Set the three sub views.
     * 
     * @param pSheetNavigationDisplay
     *            The navigation view.
     * @param pSheetListingDisplay
     *            The listing view.
     * @param pSheetDetailDisplay
     *            The detail view.
     */
    public void setContent(SheetNavigationDisplay pSheetNavigationDisplay,
            SheetListingDisplay pSheetListingDisplay,
            SheetDetailDisplay pSheetDetailDisplay);

    /**
     * Add handler on role change.
     * 
     * @param pHandler
     *            The handler.
     */
    public void addRoleChangeHandler(ValueChangeHandler<String> pHandler);

    /**
     * Get the selected role.
     * 
     * @return The selected role.
     */
    public String getSelectedRole();

    /**
     * Restore click handler for the navigation panel
     * 
     * @return Click handler for navigation panel
     */
    public HasClickHandlers getNavigationRestoreButton();

    /**
     * Restore click handler for the listing panel
     * 
     * @return Click handler for listing panel
     */
    public HasClickHandlers getListingRestoreButton();

    /**
     * Restore click handler for the detail panel
     * 
     * @return Click handler for detail panel
     */
    public HasClickHandlers getDetailRestoreButton();

    /**
     * Undo the previous role name change done by user
     */
    public void rollBackSelectedRole();
}