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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeManager;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerHierarchy;

/**
 * Manager for filter edition container tree.
 * 
 * @author jlouisy
 */
public abstract class TreeFilterEditionContainerManager extends
        GpmDynamicTreeManager<TreeFilterEditionContainerItem> {

    private Map<String, UiFilterContainerHierarchy> hierarchy;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeManager#createSubItems(org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeItem)
     */
    @Override
    public List<TreeFilterEditionContainerItem> createSubItems(
            final TreeFilterEditionContainerItem pItem) {

        final List<TreeFilterEditionContainerItem> lSubItems =
                new ArrayList<TreeFilterEditionContainerItem>();

        if (pItem.isLink()) {
            lSubItems.add(new TreeFilterEditionContainerItem(
                    hierarchy.get(pItem.getDestinationContainerId()),
                    pItem.getDepth(), null));
        }
        else {
            for (String[] lSubContainer : hierarchy.get(pItem.getId()).getChildren()) {
                lSubItems.add(new TreeFilterEditionContainerItem(
                        hierarchy.get(lSubContainer[0]), pItem.getDepth() - 1,
                        lSubContainer[1]));
            }
        }

        return lSubItems;
    }

    public void setHierarchy(Map<String, UiFilterContainerHierarchy> pHierarchy) {
        hierarchy = pHierarchy;
    }

}