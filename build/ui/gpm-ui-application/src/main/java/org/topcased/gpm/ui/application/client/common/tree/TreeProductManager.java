/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.tree;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeManager;
import org.topcased.gpm.ui.facade.shared.container.product.UiProductHierarchy;

/**
 * Manager for tree products.
 * 
 * @author tpanuel
 */
public abstract class TreeProductManager extends
        GpmDynamicTreeManager<TreeProductItem> {
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeManager#createSubItems(org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeItem)
     */
    @Override
    public List<TreeProductItem> createSubItems(final TreeProductItem pItem) {
        final List<TreeProductItem> lSubItems =
                new ArrayList<TreeProductItem>();

        for (final UiProductHierarchy lSubNode : pItem.getNode().getChildren()) {
            lSubItems.add(new TreeProductItem(lSubNode));
        }

        return lSubItems;
    }
}