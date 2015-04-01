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

import org.topcased.gpm.business.util.FieldType;
import org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeItem;
import org.topcased.gpm.ui.facade.shared.filter.field.UiFilterUsableField;

/**
 * Item for container tree in filter edition.
 * 
 * @author jlouisy
 */
public class TreeFilterEditionUsableFieldItem extends GpmDynamicTreeItem {

    private final UiFilterUsableField node;

    /**
     * Create tree filter item from UI object.
     * 
     * @param pNode
     *            node.
     */
    public TreeFilterEditionUsableFieldItem(final UiFilterUsableField pNode) {
        super(
                pNode.getTranslatedName(),
                false,
                !FieldType.MULTIPLE.equals(pNode.getFieldType())
                        && (pNode.getSubFields() == null || pNode.getSubFields().isEmpty()));
        node = pNode;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeItem#isLeaf()
     */
    @Override
    public boolean isLeaf() {
        return node.getSubFields() == null || node.getSubFields().isEmpty();
    }

    /**
     * Get node info.
     * 
     * @return The node.
     */
    public UiFilterUsableField getNode() {
        return node;
    }

    /**
     * Get the node id.
     * 
     * @return The node id.
     */
    public String getId() {
        return node.getId();
    }

}