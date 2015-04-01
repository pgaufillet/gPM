/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.extendedaction;

import org.topcased.gpm.ui.facade.shared.filter.UiFilter;
import org.topcased.gpm.ui.facade.shared.filter.result.table.UiFilterTableResult;

/**
 * UiFilterEAResult
 * 
 * @author nveillet
 */
public class UiFilterEAResult extends AbstractUiExtendedActionResult {

    private UiFilter filter;

    private UiFilterTableResult filterResult;

    /**
     * Constructor
     * 
     * @param pFilterResult
     *            the filter result
     * @param pFilter
     *            the filter used
     */
    public UiFilterEAResult(UiFilterTableResult pFilterResult, UiFilter pFilter) {
        filterResult = pFilterResult;
        filter = pFilter;
    }

    /**
     * get filter
     * 
     * @return the filter
     */
    public UiFilter getFilter() {
        return filter;
    }

    /**
     * get filter result
     * 
     * @return the filter result
     */
    public UiFilterTableResult getFilterResult() {
        return filterResult;
    }

    /**
     * set filter
     * 
     * @param pFilter
     *            the filter to set
     */
    public void setFilter(UiFilter pFilter) {
        filter = pFilter;
    }

    /**
     * set filter result
     * 
     * @param pFilterResult
     *            the filter result to set
     */
    public void setFilterResult(UiFilterTableResult pFilterResult) {
        filterResult = pFilterResult;
    }

}
