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
 * DeleteFilterResult
 * 
 * @author nveillet
 */
public class DeleteFilterResult implements Result {

    /** serialVersionUID */
    private static final long serialVersionUID = -164867961137769983L;

    private UiFilterSummaries filters;

    /**
     * Empty constructor for serialization.
     */
    public DeleteFilterResult() {
    }

    /**
     * Constructor with values
     * 
     * @param pFilters
     *            the filters summaries
     */
    public DeleteFilterResult(UiFilterSummaries pFilters) {
        filters = pFilters;
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
     * set filters
     * 
     * @param pFilters
     *            the filters to set
     */
    public void setFilters(UiFilterSummaries pFilters) {
        filters = pFilters;
    }

}
