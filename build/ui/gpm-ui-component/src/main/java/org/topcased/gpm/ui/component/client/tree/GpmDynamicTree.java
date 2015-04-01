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

import org.topcased.gpm.ui.component.client.field.tree.GpmTreeChoiceFieldItem;
import org.topcased.gpm.ui.component.client.resources.ComponentCssResource;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * A tree with a dynamic building.
 * 
 * @author tpanuel
 * @param <I>
 *            The type of item.
 */
public class GpmDynamicTree<I extends GpmDynamicTreeItem> extends Tree {
    protected final static ComponentCssResource CSS =
            ComponentResources.INSTANCE.css();

    private HandlerRegistration openHandlerRegistration;

    private GpmDynamicTreeManager<I> manager;

    /**
     * Create a dynamic tree.
     */
    public GpmDynamicTree() {
        super(ComponentResources.INSTANCE.images(), true);
        setStylePrimaryName(ComponentResources.INSTANCE.css().gpmTree());
        // filtered events
        sinkEvents(Event.ONCLICK | Event.ONDBLCLICK | Event.ONMOUSEOVER
                | Event.ONMOUSEOUT | Event.ONMOUSEDOWN | Event.ONMOUSEUP);
    }

    /**
     * Set the root items.
     * 
     * @param pRootItems
     *            The root items.
     */
    public void setRootItems(final List<I> pRootItems) {
        for (final I lRooItem : pRootItems) {
            addItem(lRooItem);
            // Add temporary sub item if not a leaf
            if (!lRooItem.isLeaf() && lRooItem.getChildCount() == 0) {
                lRooItem.addItem("");
            }
        }
        DeferredCommand.addCommand(new Command() {
            @Override
            public void execute() {
                for (int i = 1; i <= getItemCount(); i++) {
                    // TODO Uncomment and correct for CSS style
                    //                    setStyleName(
                    //                            (Element) getElement().getChild(i).getChild(0).getChild(
                    //                                    0).getChild(0).getChild(0).getChild(0),
                    //                            ComponentResources.INSTANCE.css().gpmTreeButton());
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.Tree#onBrowserEvent(com.google.gwt.user.client.Event)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onBrowserEvent(final Event pEvent) {

        if (Event.ONCLICK == DOM.eventGetType(pEvent)) {
            //Bypass super method to avoid focus move
            DomEvent.fireNativeEvent(pEvent, this, this.getElement());
        }
        else {
            super.onBrowserEvent(pEvent);
        }

        final I lTreeItem = (I) findItem(DOM.eventGetTarget(pEvent));

        if (lTreeItem != null && lTreeItem.isClickable()) {
            switch (DOM.eventGetType(pEvent)) {
                case Event.ONMOUSEDOWN:
                case Event.ONMOUSEUP:
                case Event.ONMOUSEOVER:
                    setStyleName(lTreeItem.getContent(),
                            CSS.gpmTreeItemHover(), true);
                    break;
                case Event.ONCLICK:
                    setStyleName(lTreeItem.getContent(),
                            CSS.gpmTreeItemHover(), false);
                    if (manager != null) {
                        manager.onSelection(lTreeItem);
                    }
                    break;
                case Event.ONDBLCLICK:
                    if (manager != null) {
                        manager.onDoubleClick(lTreeItem);
                    }
                    break;
                case Event.ONMOUSEOUT:
                    setStyleName(lTreeItem.getContent(),
                            CSS.gpmTreeItemHover(), false);
                    break;
                default:
                    //Do nothing
            }
        }
    }

    private GpmDynamicTreeItem findItem(final Element pElement) {
        for (int i = 0; i < getItemCount(); i++) {
            final TreeItem lRootItem = getItem(i);

            if (lRootItem instanceof GpmDynamicTreeItem) {
                final GpmDynamicTreeItem lFoundSubItem =
                        ((GpmDynamicTreeItem) lRootItem).searchElement(pElement);

                if (lFoundSubItem != null) {
                    return lFoundSubItem;
                }
            }
        }

        return null;
    }

    /**
     * Reset the tree.
     */
    public void resetTree() {
        if (openHandlerRegistration != null) {
            openHandlerRegistration.removeHandler();
            openHandlerRegistration = null;
        }
        manager = null;
        removeItems();
    }

    /**
     * {@inheritDoc}. Overhidden to filter values that can not be selected
     * 
     * @see com.google.gwt.user.client.ui.Tree#getSelectedItem()
     */
    @SuppressWarnings("unchecked")
    @Override
    public I getSelectedItem() {
        // TODO Auto-generated method stub
        I lItem = (I) super.getSelectedItem();
        if (lItem != null && lItem.isSelectable()) {
            return lItem;
        }
        return null;
    }

    /**
     * Set the manager used to create dynamic tree.
     * 
     * @param pManager
     *            The manager.
     */
    public void setDynamicTreeManager(final GpmDynamicTreeManager<I> pManager) {
        manager = pManager;
        openHandlerRegistration = addHandler(pManager, OpenEvent.getType());
    }

    /**
     * Opens the tree to the value
     * 
     * @param pValue
     *            the value to expand to
     */
    public void expandTo(GpmTreeChoiceFieldItem pValue) {
        if (pValue.getParent() != null) {
            expandTo(pValue.getParent());
        }
        pValue.setState(true, true);
    }
}