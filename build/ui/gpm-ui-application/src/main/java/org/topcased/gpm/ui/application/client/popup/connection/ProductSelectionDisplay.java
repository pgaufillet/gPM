/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.connection;

import java.util.List;

import org.topcased.gpm.ui.application.client.common.tree.TreeProductItem;
import org.topcased.gpm.ui.application.client.common.tree.TreeProductManager;
import org.topcased.gpm.ui.application.client.popup.PopupDisplay;
import org.topcased.gpm.ui.facade.shared.container.product.UiProduct;

import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Display interface for the ProductSelectionView.
 * 
 * @author tpanuel
 */
public interface ProductSelectionDisplay extends PopupDisplay {
    /**
     * Set the products on the list tab.
     * 
     * @param pList
     *            The products.
     * @param pHandler
     *            The handler.
     */
    public void setProductList(List<UiProduct> pList, ClickHandler pHandler);

    /**
     * Set the tree product hierarchy manager.
     * 
     * @param pProductHierarchy
     *            The product names.
     * @param pManager
     *            The manager.
     */
    public void setProductHierarchy(List<TreeProductItem> pProductHierarchy,
            TreeProductManager pManager);
}