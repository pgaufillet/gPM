/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.impl.filter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.topcased.gpm.business.search.impl.fields.UsableTypeData;

/**
 * Report describing the restrictions applied on a filter execution.
 * 
 * @author tpanuel
 */
public class FilterExecutionReport implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 3515278336946796438L;

    /** PRODUCT_NAMES */
    private final Set<String> nonExecutableProducts;

    /** Map<ROLE_NAME, PRODUCT_NAMES> */
    private final Map<String, Set<String>> executableProducts;

    /** Map<TYPE, Map<ROLE_NAME, CONSTRAINTS> */
    private final Map<UsableTypeData, Map<String, Set<FilterAccessContraint>>>
            additionalConstraints;

    /**
     * Create a report listing the details of a filter execution.
     */
    public FilterExecutionReport() {
        nonExecutableProducts = new HashSet<String>();
        executableProducts = new HashMap<String, Set<String>>();
        additionalConstraints =
                new HashMap<UsableTypeData, Map<String, Set<FilterAccessContraint>>>();
    }

    /**
     * Get the non executable products.
     * 
     * @return The non executable products.
     */
    public Set<String> getNonExecutableProducts() {
        return nonExecutableProducts;
    }

    /**
     * Get the executable products by role.
     * 
     * @return The executable products.
     */
    public Map<String, Set<String>> getExecutableProducts() {
        return executableProducts;
    }

    /**
     * Get the additional constraints by type and role.
     * 
     * @return The additional constraints.
     */
    public Map<UsableTypeData, Map<String, Set<FilterAccessContraint>>> getAdditionalConstraints() {
        return additionalConstraints;
    }
}