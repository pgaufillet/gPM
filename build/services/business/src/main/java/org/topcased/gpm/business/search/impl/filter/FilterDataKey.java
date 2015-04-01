/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.filter;

import org.topcased.gpm.business.cache.CacheKey;

/**
 * Key used to access on the cache of the FilterDataManager.
 * 
 * @author tpanuel
 */
public class FilterDataKey extends CacheKey {
    private static final long serialVersionUID = -146983732766963890L;

    private final String filterId;

    /**
     * Create a key to access on the cache of the FilterDataManager. All the
     * fields are final.
     * 
     * @param pFilterId
     *            The id of the filter.
     */
    public FilterDataKey(final String pFilterId) {
        super(pFilterId);
        filterId = pFilterId;
    }

    /**
     * Get the filter id.
     * 
     * @return The filter id.
     */
    public String getFilterId() {
        return filterId;
    }
}