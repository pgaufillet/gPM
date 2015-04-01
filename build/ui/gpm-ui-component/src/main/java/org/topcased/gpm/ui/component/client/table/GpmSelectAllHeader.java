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

import org.topcased.gpm.business.util.search.FilterResultId;

import com.google.gwt.user.cellview.client.Header;

/**
 * Header for select on unselect all.
 * 
 * @author tpanuel
 */
public class GpmSelectAllHeader extends Header<FilterResultId> {

    private final GpmCheckBoxCell cell;

    /**
     * Create a select all header.
     */
    public GpmSelectAllHeader() {
        this(new GpmCheckBoxCell());
    }

    private GpmSelectAllHeader(final GpmCheckBoxCell pCell) {
        super(pCell);
        cell = pCell;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.cellview.client.Header#getValue()
     */
    @Override
    public FilterResultId getValue() {
        return null;
    }

    /**
     * Get the cell.
     * 
     * @return The cell.
     */
    public GpmCheckBoxCell getCell() {
        return cell;
    }
}