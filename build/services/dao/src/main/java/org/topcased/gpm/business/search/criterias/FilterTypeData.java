/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.criterias;

import org.topcased.gpm.domain.search.FilterType;

/**
 * Enumeration of Filter Type Data.
 * 
 * @author ogehin
 */
public enum FilterTypeData {
    SHEET, PRODUCT, LINK, OTHER;

    /**
     * fromString method
     * 
     * @param pValue
     *            the value.
     * @return filterTypeData
     */
    public static FilterTypeData fromString(String pValue) {
        return valueOf(pValue);
    }

    /**
     * Converts a FilterTypeData to a FilterType
     * 
     * @return FilterType corresponding to this FilterTypeData
     */
    public FilterType toFilterType() {
        return FilterType.fromString(name());
    }
}
