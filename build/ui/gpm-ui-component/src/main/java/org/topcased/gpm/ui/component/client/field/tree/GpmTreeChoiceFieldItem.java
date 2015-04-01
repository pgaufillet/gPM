/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field.tree;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeItem;

/**
 * GpmTreeChoiceFieldItem
 * 
 * @author jeballar
 */
public class GpmTreeChoiceFieldItem extends GpmDynamicTreeItem {

    private final static String SEPARATOR_CHAR = "->";

    /** The real value of the element */
    private final String value;

    /** The displayed value of the element */
    private final String displayedValue;

    /** Sub items */
    private List<GpmTreeChoiceFieldItem> subItems =
            new ArrayList<GpmTreeChoiceFieldItem>();

    /** Parent if exists */
    private GpmTreeChoiceFieldItem parent = null;

    private int index = -1;

    /**
     * Constructor.
     * 
     * @param pDisplayedValue
     *            the text value
     * @param pValue
     *            the represented value
     * @param pSelectable
     *            Indicate if the node is a selectable value
     */
    public GpmTreeChoiceFieldItem(String pDisplayedValue, String pValue,
            boolean pSelectable) {
        super(pDisplayedValue, pSelectable, pSelectable);
        value = pValue;
        displayedValue = pDisplayedValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.tree.GpmDynamicTreeItem#isLeaf()
     */
    @Override
    public boolean isLeaf() {
        return subItems.size() == 0;
    }

    /**
     * get displayedValue
     * 
     * @return the displayedValue
     */
    public String getDisplayedValue() {
        return displayedValue;
    }

    public String getValue() {
        return value;
    }

    /**
     * Add a sub item to this item
     * 
     * @param pItem
     *            the sub item to add
     */
    public void addSubItem(GpmTreeChoiceFieldItem pItem) {
        pItem.parent = this;
        subItems.add(pItem);
    }

    /**
     * get subitems
     * 
     * @return the subitems
     */
    public List<GpmTreeChoiceFieldItem> getSubItems() {
        return subItems;
    }

    /**
     * get parent
     * 
     * @return the parent
     */
    public GpmTreeChoiceFieldItem getParent() {
        return parent;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.UIObject#toString()
     */
    @Override
    public String toString() {
        if (parent == null) {
            return getDisplayedValue();
        }
        else {
            return parent.toString() + " " + SEPARATOR_CHAR + " "
                    + getDisplayedValue();
        }
    }

    /**
     * Get the index.
     * 
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * set index
     * 
     * @param pIndex
     *            the index to set
     */
    public void setIndex(int pIndex) {
        index = pIndex;
    }
}
