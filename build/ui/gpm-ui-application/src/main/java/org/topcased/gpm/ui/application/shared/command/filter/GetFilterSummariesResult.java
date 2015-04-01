/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.filter;

import net.customware.gwt.dispatch.shared.Result;

import org.topcased.gpm.ui.facade.shared.filter.summary.UiFilterSummaries;

/**
 * GetFilterSummariesResult
 * 
 * @author nveillet
 */
public class GetFilterSummariesResult implements Result {

    /** serialVersionUID */
    private static final long serialVersionUID = 3970837002671465349L;

    private boolean filterCreatable;

    private UiFilterSummaries filters;

    /**
     * Empty constructor for serialization.
     */
    public GetFilterSummariesResult() {
    }

    /**
     * Constructor with values
     * 
     * @param pFilters
     *            The filters summaries
     * @param pFilterCreatable
     *            filter creatable access
     */
    public GetFilterSummariesResult(UiFilterSummaries pFilters,
            boolean pFilterCreatable) {
        filters = pFilters;
        filterCreatable = pFilterCreatable;
    }

    /**
     * get filters
     * 
     * @return the filters
     */
    public UiFilterSummaries getFilters() {
        return filters;
    }

    /**
     * get filter creatable access
     * 
     * @return the filter creatable access
     */
    public boolean isFilterCreatable() {
        return filterCreatable;
    }

}
