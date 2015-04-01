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
import org.topcased.gpm.ui.component.client.util.GpmBasicActionHandler;

import com.google.gwt.user.cellview.client.Column;

/**
 * An image column for the gPM table.
 * 
 * @author tpanuel
 */
public class GpmImageColumn extends Column<FilterResult, FilterResultId> {
    /**
     * Create a GpmImageColumn.
     * 
     * @param pVisu
     *            If it's to open the element on visu mode.
     */
    public GpmImageColumn(final boolean pVisu) {
        super(new GpmImageCell(pVisu));
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

    /**
     * Set the handler call when the image is clicked.
     * 
     * @param pHandler
     *            The handler.
     */
    public void setHandler(final GpmBasicActionHandler<String> pHandler) {
        ((GpmImageCell) getCell()).setHandler(pHandler);
    }
}