/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.product.detail;

import org.topcased.gpm.ui.application.client.common.container.product.ProductDisplay;
import org.topcased.gpm.ui.application.client.common.workspace.detail.DetailView;

/**
 * View for a set of product displayed on the product management space.
 * 
 * @author tpanuel
 */
public class ProductDetailView extends DetailView<ProductDisplay> implements
        ProductDetailDisplay {
    /**
     * Create a detail view for products.
     */
    public ProductDetailView() {
        super();
    }
}