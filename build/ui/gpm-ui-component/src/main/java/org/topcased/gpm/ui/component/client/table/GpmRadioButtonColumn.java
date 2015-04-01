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
import org.topcased.gpm.business.util.search.FilterResultId;

import com.google.gwt.user.cellview.client.Column;

/**
 * A column with radio button for the gPM table.
 * 
 * @author tpanuel
 */
public class GpmRadioButtonColumn extends Column<FilterResult, FilterResultId> {
    /**
     * Create a GpmRadioButtonColumn.
     */
    public GpmRadioButtonColumn() {
        super(new GpmRadioButtonCell());
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.cellview.client.Column#getValue(java.lang.Object)
     */
    @Override
    public FilterResultId getValue(final FilterResult pObject) {
        return pObject.getFilterResultId();
    }
}