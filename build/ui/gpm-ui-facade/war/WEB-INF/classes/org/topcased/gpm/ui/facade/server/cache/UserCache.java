/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.cache;

import java.util.HashMap;
import java.util.Map;

import org.topcased.gpm.business.fields.impl.CacheableInputData;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.ui.facade.server.FacadeCommand;

/**
 * A cache for user.
 * 
 * @author tpanuel
 */
public class UserCache {

    private final Map<String, FacadeCommand> exportCache;

    private final FilterCache filterCache;

    private final Map<String, CacheableInputData> inputDataCache;

    private final Map<String, CacheableProduct> productCache;

    private final Map<String, CacheableSheet> sheetCache;

    /**
     * Create a user cache.
     */
    public UserCache() {
        sheetCache = new HashMap<String, CacheableSheet>();
        productCache = new HashMap<String, CacheableProduct>();
        exportCache = new HashMap<String, FacadeCommand>();
        inputDataCache = new HashMap<String, CacheableInputData>();
        filterCache = new FilterCache();
    }

    /**
     * Get the exported files cache.
     * 
     * @return The exported files cache.
     */
    public Map<String, FacadeCommand> getExportCache() {
        return exportCache;
    }

    /**
     * Get the filter cache.
     * 
     * @return The filter cache.
     */
    public FilterCache getFilterCache() {
        return filterCache;
    }

    /**
     * get inputData cache.
     * 
     * @return the inputData cache.
     */
    public Map<String, CacheableInputData> getInputDataCache() {
        return inputDataCache;
    }

    /**
     * Get the product cache.
     * 
     * @return The product cache.
     */
    public Map<String, CacheableProduct> getProductCache() {
        return productCache;
    }

    /**
     * Get the sheet cache.
     * 
     * @return The sheet cache.
     */
    public Map<String, CacheableSheet> getSheetCache() {
        return sheetCache;
    }

}
