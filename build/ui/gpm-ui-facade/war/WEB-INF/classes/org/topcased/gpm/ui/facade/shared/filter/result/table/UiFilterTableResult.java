/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.filter.result.table;

import java.io.Serializable;
import java.util.List;

import org.topcased.gpm.business.util.search.FilterResult;
import org.topcased.gpm.ui.facade.shared.filter.result.UiFilterExecutionReport;

/**
 * UiFilterTableResult
 * 
 * @author nveillet
 */
public class UiFilterTableResult implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 6175492725976786407L;

    private List<String> columnNames;

    private UiFilterExecutionReport filterExecutionReport;

    private String filterId;

    private List<FilterResult> resultValues;

    /**
     * Create UiFilterTableResult
     */
    public UiFilterTableResult() {
    }

    /**
     * Create UiFilterTableResult with values
     * 
     * @param pColumnNames
     *            the column names
     * @param pResultValues
     *            the resultValues
     * @param pFilterExecutionReport
     *            the filter execution report
     * @param pFilterId
     *            the filter Id
     */
    public UiFilterTableResult(List<String> pColumnNames,
            List<FilterResult> pResultValues,
            UiFilterExecutionReport pFilterExecutionReport, String pFilterId) {
        columnNames = pColumnNames;
        resultValues = pResultValues;
        filterExecutionReport = pFilterExecutionReport;
        filterId = pFilterId;
    }

    /**
     * get column names
     * 
     * @return the column names
     */
    public List<String> getColumnNames() {
        return columnNames;
    }

    /**
     * get filter execution report
     * 
     * @return the filter execution report
     */
    public UiFilterExecutionReport getFilterExecutionReport() {
        return filterExecutionReport;
    }

    /**
     * get filter Id
     * 
     * @return the filter Id
     */
    public String getFilterId() {
        return filterId;
    }

    /**
     * get resultValues
     * 
     * @return the resultValues
     */
    public List<FilterResult> getResultValues() {
        return resultValues;
    }

    /**
     * set column names
     * 
     * @param pColumnNames
     *            the column names to set
     */
    public void setColumnNames(List<String> pColumnNames) {
        columnNames = pColumnNames;
    }

    /**
     * set filter execution report
     * 
     * @param pFilterExecutionReport
     *            the filter execution report to set
     */
    public void setFilterExecutionReport(
            UiFilterExecutionReport pFilterExecutionReport) {
        filterExecutionReport = pFilterExecutionReport;
    }

    /**
     * set filter Id
     * 
     * @param pFilterId
     *            the filter Id to set
     */
    public void setFilterId(String pFilterId) {
        filterId = pFilterId;
    }

    /**
     * set result values
     * 
     * @param pResultValues
     *            the result values to set
     */
    public void setResultValues(List<FilterResult> pResultValues) {
        resultValues = pResultValues;
    }

}
