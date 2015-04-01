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

import org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeItem;
import org.topcased.gpm.ui.facade.shared.filter.result.tree.UiFilterTreeResultLeaf;
import org.topcased.gpm.ui.facade.shared.filter.result.tree.UiFilterTreeResultNode;

/**
 * Item for tree filters.
 * 
 * @author tpanuel
 */
public class TreeFilterItem extends GpmDynamicTreeItem {
    private final UiFilterTreeResultNode node;

    /**
     * Create tree filter item from UI object.
     * 
     * @param pNode
     */
    public TreeFilterItem(final UiFilterTreeResultNode pNode) {
        super(pNode.getValue(), false, pNode instanceof UiFilterTreeResultLeaf);
        if (pNode.getDescription() != null) {
            setTitle(pNode.getDescription());
        }
        node = pNode;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeItem#isLeaf()
     */
    @Override
    public boolean isLeaf() {
        return node.getResultNodes() == null || node.getResultNodes().isEmpty();
    }

    /**
     * Get node info.
     * 
     * @return The node.
     */
    public UiFilterTreeResultNode getNode() {
        return node;
    }

    /**
     * Get the node id.
     * 
     * @return The node id.
     */
    public String getId() {
        if (node instanceof UiFilterTreeResultLeaf) {
            return ((UiFilterTreeResultLeaf) node).getId();
        }
        else {
            return node.getValue();
        }
    }
}