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

import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.user.cellview.client.Column;

/**
 * A super column with sub columns for the gPM table.
 * 
 * @author tpanuel
 */
public class GpmCompositeColumn extends Column<FilterResult, FilterResult> {
    /**
     * Create a GpmCompositeColumn.
     */
    public GpmCompositeColumn() {
        super(new CompositeCell<FilterResult>());
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.cellview.client.Column#getValue(java.lang.Object)
     */
    @Override
    public FilterResult getValue(final FilterResult pObject) {
        return pObject;
    }

    /**
     * Add a sub column.
     * 
     * @param pSubCell
     *            The Sub cell.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addSubColumn(final HasCell<FilterResult, ?> pSubCell) {
        ((CompositeCell) getCell()).addHasCell(pSubCell);
    }
}