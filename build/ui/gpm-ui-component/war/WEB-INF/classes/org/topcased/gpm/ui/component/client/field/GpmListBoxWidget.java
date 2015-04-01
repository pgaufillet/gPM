/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field;

import java.util.HashMap;
import java.util.Map;

import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.user.client.ui.ListBox;

/**
 * List box that store values to skip HTML modification.
 * 
 * @author tpanuel
 */
public class GpmListBoxWidget extends ListBox {
    /* Store values to skip HTML modification */
    private final Map<Integer, String> values;

    /**
     * Create a GpmListBoxWidget.
     * 
     * @param pMultivalued
     *            If multivalued.
     */
    public GpmListBoxWidget(boolean pMultivalued) {
        super(pMultivalued);
        values = new HashMap<Integer, String>();
        addStyleName(ComponentResources.INSTANCE.css().gpmTextArea());
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.ListBox#addItem(java.lang.String)
     */
    @Override
    public void addItem(final String pValue) {
        addItem(pValue, pValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.ListBox#addItem(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void addItem(final String pItem, final String pValue) {
        values.put(getItemCount(), pValue);
        super.addItem(pItem, pValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.ListBox#getValue(int)
     */
    @Override
    public String getValue(final int pIndex) {
        return values.get(pIndex);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.ListBox#clear()
     */
    @Override
    public void clear() {
        super.clear();
        values.clear();
    }
}