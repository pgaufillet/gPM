/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.topcased.gpm.business.util.search.FilterResultId;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Node;

/**
 * An check box cell for the gPM table.
 * 
 * @author tpanuel
 */
public class GpmCheckBoxCell extends AbstractCell<FilterResultId> {
    private final static String CHECKED = "checked";

    private final static String CLICK_EVENT = "click";

    private final Set<String> displayedElements;

    private final Set<String> selected;

    private GpmCheckBoxHandler handler;

    private Element father;

    /**
     * Create a check box cell.
     */
    public GpmCheckBoxCell() {
        selected = new HashSet<String>();
        displayedElements = new HashSet<String>();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.cell.client.AbstractCell#onBrowserEvent(com.google.gwt.dom.client.Element,
     *      java.lang.Object, java.lang.Object,
     *      com.google.gwt.dom.client.NativeEvent,
     *      com.google.gwt.cell.client.ValueUpdater)
     */
    @Override
    public Object onBrowserEvent(final Element pParent,
            final FilterResultId pValue, final Object pViewData,
            final NativeEvent pEvent,
            final ValueUpdater<FilterResultId> pValueUpdater) {
        if (pEvent.getType().equals(CLICK_EVENT)) {
            final boolean lIsSelected =
                    searchFirstLeaf(pParent).getPropertyBoolean(CHECKED);

            if (pValue != null) {
                if (lIsSelected) {
                    selected.add(pValue.getId());
                }
                else {
                    selected.remove(pValue.getId());
                }
            }
            if (handler != null) {
                handler.onClick(!lIsSelected);
            }
        }

        return pViewData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.cell.client.AbstractCell#render(java.lang.Object,
     *      java.lang.Object, java.lang.StringBuilder)
     */
    @Override
    public void render(final FilterResultId pData, final Object pViewData,
            final StringBuilder pStringBuilder) {
        pStringBuilder.append("<input type=\"checkbox\"/>");
        if (pData != null) {
            displayedElements.add(pData.getId());
        }
    }

    /**
     * Select or unselect all.
     * 
     * @param pSelectAll
     *            Select or unselect.
     */
    public void selectAll(final boolean pSelectAll) {
        selected.clear();
        if (pSelectAll) {
            selected.addAll(displayedElements);
        }
        if (father != null) {
            for (int i = 0; i < father.getChildCount(); i++) {
                searchFirstLeaf(father.getChild(i)).setPropertyBoolean(CHECKED,
                        pSelectAll);
            }
        }
    }

    private Element searchFirstLeaf(final Node pNode) {
        if (pNode.hasChildNodes()) {
            return (Element) searchFirstLeaf(pNode.getChild(0));
        }
        else {
            return (Element) pNode;
        }
    }

    /**
     * Clear the displayed elements list
     */
    public void clearDisplayedElements() {
        displayedElements.clear();
    }

    /**
     * Get the selected elements id.
     * 
     * @return The selected elements id.
     */
    public List<String> getSelectedIds() {
        return new ArrayList<String>(selected);
    }

    /**
     * Set the handler.
     * 
     * @param pHandler
     *            The handler.
     */
    public void setHandler(final GpmCheckBoxHandler pHandler) {
        handler = pHandler;
    }

    /**
     * Set the father element
     * 
     * @param pFather
     *            The father element.
     */
    public void setFather(final Element pFather) {
        father = pFather;
    }
}