/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.tree;

import java.util.List;

import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Manager used by a dynamic tree.
 * 
 * @author tpanuel
 * @param <I>
 *            The type of item.
 */
public abstract class GpmDynamicTreeManager<I extends GpmDynamicTreeItem>
        implements OpenHandler<I> {
    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.logical.shared.OpenHandler#onOpen(com.google.gwt.event.logical.shared.OpenEvent)
     */
    @Override
    public void onOpen(final OpenEvent<I> pEvent) {
        final I lItem = pEvent.getTarget();

        if (!lItem.isInit()) {
            // Close the item immediately
            lItem.setState(false, false);
            // Initialized the item
            for (final I lSubItem : createSubItems(lItem)) {
                // Add temporary sub item if not a leaf
                if (!lSubItem.isLeaf()) {
                    lSubItem.addItem("");
                }
                lItem.addItem(lSubItem);
            }
            lItem.setInit(true);
            // Remove first item : the temporary item previously set
            lItem.getChild(0).remove();
            // Open the initialized item
            lItem.setState(true, false);

            // Add style on image button of all sub elements
            final Element lSubElements =
                    (Element) lItem.getElement().getChild(1);

            for (int i = 0; i < lSubElements.getChildCount(); i++) {
                if (!((GpmDynamicTreeItem) lItem.getChild(i)).isLeaf()) {
                    DOM.setElementProperty(
                            (Element) lSubElements.getChild(i).getChild(0).getChild(
                                    0).getChild(0).getChild(0).getChild(0),
                            "className",
                            ComponentResources.INSTANCE.css().gpmTreeButton());
                }
            }
        }
    }

    /**
     * Action on selection. To handle double clicks, override
     * <CODE>onDoubleClick(...)</CODE> method
     * 
     * @param pItem
     *            The item.
     */
    abstract public void onSelection(final I pItem);

    /**
     * Action on double click.
     * 
     * @param pItem
     *            The item.
     */
    public void onDoubleClick(final I pItem) {
        // Does nothing but can be overriden by sub classes
    }

    /**
     * Create the sub items of a parent item.
     * 
     * @param pItem
     *            The parent item.
     * @return The sub items.
     */
    public abstract List<I> createSubItems(final I pItem);
}