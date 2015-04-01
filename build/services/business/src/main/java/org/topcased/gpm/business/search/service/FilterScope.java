/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Vincent Hemery (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

/**
 * FilterScope This enumeration list the different scope types on which a filter
 * applies.
 * 
 * @author vhemery
 */
public enum FilterScope {
    /** The filter is an instance's filter. */
    INSTANCE_FILTER,

    /** The filter is a product's filter. */
    PRODUCT_FILTER,

    /** The filter is a user's filter. */
    USER_FILTER;

    public static FilterScope toFilterScope(String pVisibility) {
        String lVisibility = pVisibility.toUpperCase();
        if ("PROCESS".equals(lVisibility)) {
            return INSTANCE_FILTER;
        }
        else if ("PRODUCT".equals(lVisibility)) {
            return PRODUCT_FILTER;
        }
        else if ("USER".equals(lVisibility)) {
            return USER_FILTER;
        }
        else {
            throw new IllegalArgumentException(
                    "The visibility '"
                            + lVisibility
                            + "' is not handled. Use only 'PROCESS', 'PRODUCT' or 'USER'");
        }
    }
}
