/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.cache;

import java.util.HashMap;
import java.util.Map;

import org.topcased.gpm.ui.facade.shared.filter.UiFilter;

/**
 * A cache for filters
 * 
 * @author nveillet
 */
public class FilterCache {

    private UiFilter popupFilter;

    private UiFilter productFilter;

    private final Map<String, UiFilter> sheetFilters;

    /**
     * Constructor
     */
    public FilterCache() {
        sheetFilters = new HashMap<String, UiFilter>();
    }

    /**
     * Get a filter in cache
     * 
     * @param pProductName
     *            the product name
     * @param pFilterId
     *            the filter identifier
     * @return the filter in cache, null if not found
     */
    public UiFilter getFilter(String pProductName, String pFilterId) {
        UiFilter lFilter = null;

        if (popupFilter != null && popupFilter.getId().equals(pFilterId)) {
            lFilter = popupFilter;
        }
        else if (sheetFilters.get(pProductName) != null
                && sheetFilters.get(pProductName).getId().equals(pFilterId)) {
            lFilter = sheetFilters.get(pProductName);
        }
        else if (productFilter != null
                && productFilter.getId().equals(pFilterId)) {
            lFilter = productFilter;
        }

        return lFilter;
    }

    /**
     * get popup filter
     * 
     * @return the popup filter
     */
    public UiFilter getPopupFilter() {
        return popupFilter;
    }

    /**
     * get product filter
     * 
     * @return the product filter
     */
    public UiFilter getProductFilter() {
        return productFilter;
    }

    /**
     * Get the sheet filters.
     * 
     * @return The sheet filters.
     */
    public Map<String, UiFilter> getSheetFilters() {
        return sheetFilters;
    }

    /**
     * set popup filter
     * 
     * @param pPopupFilter
     *            the popup filter to set
     */
    public void setPopupFilter(UiFilter pPopupFilter) {
        popupFilter = pPopupFilter;
    }

    /**
     * set product filter
     * 
     * @param pProductFilter
     *            the product filter to set
     */
    public void setProductFilter(UiFilter pProductFilter) {
        productFilter = pProductFilter;
    }
}
