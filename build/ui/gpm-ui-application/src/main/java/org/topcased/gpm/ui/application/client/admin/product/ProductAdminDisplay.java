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

import org.topcased.gpm.ui.application.client.admin.product.detail.ProductDetailDisplay;
import org.topcased.gpm.ui.application.client.admin.product.listing.ProductListingDisplay;
import org.topcased.gpm.ui.application.client.admin.product.navigation.ProductNavigationDisplay;
import org.topcased.gpm.ui.application.client.common.workspace.WorkspaceDisplay;

import com.google.gwt.event.dom.client.HasClickHandlers;

/**
 * Display interface for the ProductAdminView.
 * 
 * @author tpanuel
 */
public interface ProductAdminDisplay extends WorkspaceDisplay {
    /**
     * Set the three sub views.
     * 
     * @param pProductNavigationDisplay
     *            The navigation view.
     * @param pProductListingDisplay
     *            The listing view.
     * @param pProductDetailDisplay
     *            The detail view.
     */
    public void setContent(ProductNavigationDisplay pProductNavigationDisplay,
            ProductListingDisplay pProductListingDisplay,
            ProductDetailDisplay pProductDetailDisplay);

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
}