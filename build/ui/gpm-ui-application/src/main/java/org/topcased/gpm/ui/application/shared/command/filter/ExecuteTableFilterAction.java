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

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;

/**
 * ExecuteExistingTableFilterAction
 * 
 * @author nveillet
 */
public class ExecuteTableFilterAction extends
        AbstractCommandFilterAction<AbstractCommandFilterResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 4182887991251215196L;

    private UiFilter filter;

    private String filterId;

    private boolean useCache;

    /**
     * create action
     */
    public ExecuteTableFilterAction() {
    }

    /**
     * create action with filter identifier (for existing filter)
     * 
     * @param pProductName
     *            The product name
     * @param pFilterType
     *            the filter type
     * @param pFilterId
     *            The filter identifier
     * @param pUseCache
     *            if command must use the filter cache
     */
    public ExecuteTableFilterAction(String pProductName,
            FilterType pFilterType, String pFilterId, boolean pUseCache) {
        super(pProductName, pFilterType);
        filterId = pFilterId;
        useCache = pUseCache;
    }

    /**
     * create action with filter
     * 
     * @param pProductName
     *            The product name
     * @param pFilter
     *            The filter
     */
    public ExecuteTableFilterAction(String pProductName, UiFilter pFilter) {
        super(pProductName, pFilter.getFilterType());
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
     * get filter identifier
     * 
     * @return the filter identifier
     */
    public String getFilterId() {
        return filterId;
    }

    /**
     * get useCache
     * 
     * @return the useCache
     */
    public boolean isUseCache() {
        return useCache;
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
     * set filter identifier
     * 
     * @param pFilterId
     *            the filter identifier to set
     */
    public void setFilterId(String pFilterId) {
        filterId = pFilterId;
    }

    /**
     * set useCache
     * 
     * @param pUseCache
     *            the useCache to set
     */
    public void setUseCache(boolean pUseCache) {
        useCache = pUseCache;
    }

}
