/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.filter.edit;

import net.customware.gwt.dispatch.shared.Result;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.facade.shared.filter.summary.UiFilterSummaries;

/**
 * SaveFilterResult
 * 
 * @author nveillet
 */
public class SaveFilterResult implements Result {

    /** serialVersionUID */
    private static final long serialVersionUID = 466796051597007612L;

    private String filterId;

    private UiFilterSummaries filterSummaries;

    private FilterType filterType;

    private String productName;

    /**
     * Empty constructor for serialization.
     */
    public SaveFilterResult() {
    }

    /**
     * Create SaveFilterResult with value
     * 
     * @param pProductName
     *            the current product name
     * @param pFilterType
     *            the filter type
     * @param pFilterId
     *            the filter Id.
     * @param pFilterSummaries
     *            filter summary
     */
    public SaveFilterResult(String pProductName, FilterType pFilterType,
            String pFilterId, UiFilterSummaries pFilterSummaries) {
        productName = pProductName;
        filterType = pFilterType;
        filterId = pFilterId;
        filterSummaries = pFilterSummaries;
    }

    /**
     * Get saved filter Id.
     * 
     * @return filter Id.
     */
    public String getFilterId() {
        return filterId;
    }

    /**
     * Get filter summaries to reload filter list.
     * 
     * @return filter summaries.
     */
    public UiFilterSummaries getFilters() {
        return filterSummaries;
    }

    /**
     * get filter type
     * 
     * @return the filter type
     */
    public FilterType getFilterType() {
        return filterType;
    }

    /**
     * get product name
     * 
     * @return the product name
     */
    public String getProductName() {
        return productName;
    }

}
