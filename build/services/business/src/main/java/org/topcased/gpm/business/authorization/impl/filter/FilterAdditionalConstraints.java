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

import java.util.Map;

import org.topcased.gpm.business.search.criterias.CriteriaData;

/**
 * Describe the additional constraints applied for filter execution.
 * 
 * @author tpanuel
 */
public class FilterAdditionalConstraints {
    private final FilterExecutionReport executionReport;

    /** Map<TYPE_ID, ADDITIONAL_CONSTRAINTS> */
    private final Map<String, CriteriaData> additionalConstraints;

    /**
     * Constructor initializing all final fields.
     * 
     * @param pExecutionReport
     *            The filter execution report.
     * @param pAdditionalConstraints
     *            The additional constraints.
     */
    public FilterAdditionalConstraints(
            final FilterExecutionReport pExecutionReport,
            final Map<String, CriteriaData> pAdditionalConstraints) {
        executionReport = pExecutionReport;
        additionalConstraints = pAdditionalConstraints;
    }

    /**
     * Get the filter execution report.
     * 
     * @return The filter execution report.
     */
    public FilterExecutionReport getExecutionReport() {
        return executionReport;
    }

    /**
     * Get the additional constraints sorted by types 'full' id.
     * 
     * @return The additional constraints.
     */
    public Map<String, CriteriaData> getAdditionalConstraints() {
        return additionalConstraints;
    }
}