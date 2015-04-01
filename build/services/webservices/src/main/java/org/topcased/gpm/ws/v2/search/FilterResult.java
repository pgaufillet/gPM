/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
package org.topcased.gpm.ws.v2.search;

import java.util.List;

import org.topcased.gpm.business.fields.SummaryData;

/**
 * Filter result class. Groups the result of the filter execution and the filter
 * execution report
 * 
 * @author jeballar
 */
public class FilterResult implements java.io.Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1945190639823198482L;

    /** Execution result */
    private List<SummaryData> filterExecutionResult = null;

    /** Content of the attributes of the original FilterExecutionReport class */
    private org.topcased.gpm.ws.v2.search.FilterExecutionReport executionReport =
            null;

    /**
     * Empty Constructor.
     */
    public FilterResult() {
    }

    /**
     * Build a FilterResult
     * 
     * @param pFilterExecutionResult
     *            The result data
     * @param pExecutionReport
     *            The execution report
     */
    public FilterResult(List<SummaryData> pFilterExecutionResult,
            FilterExecutionReport pExecutionReport) {
        super();
        filterExecutionResult = pFilterExecutionResult;
        executionReport = pExecutionReport;
    }

    /**
     * Get filterExecutionResult
     * 
     * @return the filterExecutionResult
     */
    public List<SummaryData> getFilterExecutionResult() {
        return filterExecutionResult;
    }

    /**
     * Get the executionReport. This objects originally contained Maps, which
     * are not allowed to transit over network. They are wrapped in objects as
     * two List, one for the keys and one for the values. The value associated
     * to a Key is placed in the values list at the same index as the key in the
     * keys list.
     * 
     * @return the executionReport
     */
    public org.topcased.gpm.ws.v2.search.FilterExecutionReport getExecutionReport() {
        return executionReport;
    }

    /**
     * Set filterExecutionResult
     * 
     * @param pFilterExecutionResult
     *            the filterExecutionResult to set
     */
    public void setFilterExecutionResult(
            List<SummaryData> pFilterExecutionResult) {
        filterExecutionResult = pFilterExecutionResult;
    }

    /**
     * Set executionReport
     * 
     * @param pExecutionReport
     *            the executionReport to set
     */
    public void setExecutionReport(
            org.topcased.gpm.ws.v2.search.FilterExecutionReport pExecutionReport) {
        executionReport = pExecutionReport;
    }
}
