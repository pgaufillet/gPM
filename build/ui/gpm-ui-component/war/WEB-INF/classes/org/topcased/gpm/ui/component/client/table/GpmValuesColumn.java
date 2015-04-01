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

import org.topcased.gpm.business.util.search.FilterResult;

import com.google.gwt.user.cellview.client.TextColumn;

/**
 * An text column for the gPM table.
 * 
 * @author tpanuel
 */
public class GpmValuesColumn extends TextColumn<FilterResult> {
    private final static String EMPTY = "&nbsp;";

    final private int index;

    /**
     * Create a GpmValuesColumn.
     * 
     * @param pIndex
     *            The index of the values column.
     */
    public GpmValuesColumn(int pIndex) {
        index = pIndex;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.cellview.client.Column#getValue(java.lang.Object)
     */
    public String getValue(final FilterResult pObject) {
        final String lValue = pObject.getValues().get(index);

        if (lValue == null || lValue.trim().isEmpty()) {
            return EMPTY;
        }
        else {
            return lValue;
        }
    }
}