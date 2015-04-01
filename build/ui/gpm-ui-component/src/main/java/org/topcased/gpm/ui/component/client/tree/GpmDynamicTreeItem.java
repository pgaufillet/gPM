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

import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * An item that can be used on a dynamic tree.
 * 
 * @author tpanuel
 */
public abstract class GpmDynamicTreeItem extends TreeItem {
    private final Element content;

    private final boolean clickable;

    private final boolean selectable;

    private boolean init;

    /**
     * Create a dynamic tree item with an initial value.
     * 
     * @param pValue
     *            The value.
     * @param pSelectable
     *            If the item can be selected.
     * @param pClickable
     *            If the item can be clicked.
     */
    public GpmDynamicTreeItem(final String pValue, final boolean pSelectable,
            final boolean pClickable) {
        super(pValue);
        selectable = pSelectable;
        clickable = pClickable;
        content = DOM.getFirstChild(getElement());
        build(null);
    }

    /**
     * Create a dynamic tree item with an initial value.
     * 
     * @param pValue
     *            The value.
     * @param pTitle
     *            item tooltip
     * @param pSelectable
     *            If the item can be selected.
     * @param pClickable
     *            If the item can be clicked.
     */
    public GpmDynamicTreeItem(final String pValue, String pTitle,
            final boolean pSelectable, final boolean pClickable) {
        super(pValue);
        selectable = pSelectable;
        clickable = pClickable;
        content = DOM.getFirstChild(getElement());
        build(pTitle);
    }

    /**
     * Build the dynamic tree item with an initial value.
     * 
     * @param pTitle
     *            item tooltip
     */
    private void build(String pTitle) {
        setStylePrimaryName(content,
                ComponentResources.INSTANCE.css().gpmTreeItem());
        if (selectable || clickable) {
            setStyleName(content,
                    ComponentResources.INSTANCE.css().gpmTreeItemSelecteable(),
                    true);
        }
        content.setTitle(pTitle);
        init = false;
    }

    /**
     * Test if the item is a leaf.
     * 
     * @return If the item is a leaf.
     */
    public abstract boolean isLeaf();

    /**
     * Test if the item has been initialized.
     * 
     * @return If the item has been initialized.
     */
    public boolean isInit() {
        return init;
    }

    /**
     * Set if the item has been initialized.
     * 
     * @param pInit
     *            If the item has been initialized.
     */
    public void setInit(final boolean pInit) {
        init = pInit;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.TreeItem#setSelected(boolean)
     */
    @Override
    public void setSelected(final boolean pSelected) {
        super.setSelected(pSelected);
        if (selectable) {
            setStyleName(content,
                    ComponentResources.INSTANCE.css().gpmTreeItemSelected(),
                    pSelected);
        }
    }

    /**
     * Get the content.
     * 
     * @return The content.
     */
    public Element getContent() {
        return content;
    }

    /**
     * Test if the item is clickable.
     * 
     * @return If the item is clickable.
     */
    public boolean isClickable() {
        return clickable;
    }

    /**
     * Search the deep level tree item that contains a specific element.
     * 
     * @param pElement
     *            The element
     * @return The tree itme. Null if not found.
     */
    public GpmDynamicTreeItem searchElement(final Element pElement) {
        for (int i = 0; i < getChildCount(); i++) {
            final TreeItem lItem = getChild(i);

            // Search on sub level
            if (lItem instanceof GpmDynamicTreeItem) {
                final GpmDynamicTreeItem lFoundSubItem =
                        ((GpmDynamicTreeItem) lItem).searchElement(pElement);

                if (lFoundSubItem != null) {
                    return lFoundSubItem;
                }
            }
        }
        // If not on a child, search on it's level
        if (content.isOrHasChild(pElement)) {
            return this;
        }

        return null;
    }

    /**
     * get selectable
     * 
     * @return the selectable
     */
    public boolean isSelectable() {
        return selectable;
    }
}