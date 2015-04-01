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

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.I18N;

import org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeItem;
import org.topcased.gpm.ui.facade.shared.container.product.UiProductHierarchy;

/**
 * Item for tree products.
 * 
 * @author tpanuel
 */
public class TreeProductItem extends GpmDynamicTreeItem {
    private final UiProductHierarchy node;

    /**
     * Create tree product item from UI object.
     * 
     * @param pNode
     *            tree node
     */
    public TreeProductItem(final UiProductHierarchy pNode) {
        super(I18N.getConstant(pNode.getProductName()), pNode.getDescription(),
                false, pNode.isSelectable());
        node = pNode;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeItem#isLeaf()
     */
    @Override
    public boolean isLeaf() {
        return node.getChildren() == null || node.getChildren().isEmpty();
    }

    /**
     * Get node info.
     * 
     * @return The node.
     */
    public UiProductHierarchy getNode() {
        return node;
    }

    /**
     * Get the node id.
     * 
     * @return The node id.
     */
    public String getId() {
        return node.getProductName();
    }
}