/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.tree;

import org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeItem;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerHierarchy;

/**
 * Item for container tree in filter edition.
 * 
 * @author jlouisy
 */
public class TreeFilterEditionContainerItem extends GpmDynamicTreeItem {

    private final UiFilterContainerHierarchy node;

    private int depth;

    private boolean isLink;

    private String destinationContainerId;

    /**
     * Create tree filter item from UI object.
     * 
     * @param pNode
     *            node.
     * @param pDepth
     *            maximum depth - current node depth.
     * @param pDestinationContainerId
     *            destination container id for links.
     */
    public TreeFilterEditionContainerItem(
            final UiFilterContainerHierarchy pNode, int pDepth,
            String pDestinationContainerId) {
        super(pNode.getContainerTranslatedName(), true, true);
        node = pNode;
        depth = pDepth;
        if (pDestinationContainerId != null) {
            destinationContainerId = pDestinationContainerId;
            isLink = true;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeItem#isLeaf()
     */
    @Override
    public boolean isLeaf() {
        return !isLink
                && (depth == 0 || node.getChildren() == null || node.getChildren().isEmpty());
    }

    /**
     * Get node info.
     * 
     * @return The node.
     */
    public UiFilterContainerHierarchy getNode() {
        return node;
    }

    /**
     * Get the node id.
     * 
     * @return The node id.
     */
    public String getId() {
        return node.getContainerId();
    }

    public int getDepth() {
        return depth;
    }

    public boolean isLink() {
        return isLink;
    }

    public String getDestinationContainerId() {
        return destinationContainerId;
    }

}