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
import org.topcased.gpm.ui.facade.shared.filter.result.tree.UiFilterTreeResultNode;

/**
 * Manager for tree filters.
 * 
 * @author tpanuel
 */
public abstract class TreeFilterManager extends
        GpmDynamicTreeManager<TreeFilterItem> {
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeManager#createSubItems(org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeItem)
     */
    @Override
    public List<TreeFilterItem> createSubItems(final TreeFilterItem pItem) {
        final List<TreeFilterItem> lSubItems = new ArrayList<TreeFilterItem>();

        for (final UiFilterTreeResultNode lSubNode : pItem.getNode().getResultNodes()) {
            lSubItems.add(new TreeFilterItem(lSubNode));
        }

        return lSubItems;
    }
}