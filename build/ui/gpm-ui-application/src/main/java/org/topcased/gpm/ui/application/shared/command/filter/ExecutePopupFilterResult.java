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

import java.util.List;

import org.topcased.gpm.business.util.search.FilterResult;

/**
 * ExecutePopupFilterResult
 * 
 * @author nveillet
 */
public class ExecutePopupFilterResult extends AbstractCommandFilterResult {

    /** serialVersionUID */
    private static final long serialVersionUID = -7957512511848324495L;

    private List<String> columnNames;

    private String filterId;

    private List<FilterResult> resultValues;

    private String typeName;

    /**
     * Empty constructor for serialization.
     */
    public ExecutePopupFilterResult() {
    }

    /**
     * Create ExecuteLinkFilterResult with values
     * 
     * @param pColumnNames
     *            the column names
     * @param pResultValues
     *            the result values
     * @param pTypeName
     *            the type name
     * @param pFilterId
     *            the filter identifier
     */
    public ExecutePopupFilterResult(List<String> pColumnNames,
            List<FilterResult> pResultValues, String pTypeName, String pFilterId) {
        columnNames = pColumnNames;
        resultValues = pResultValues;
        typeName = pTypeName;
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
     * get filter identifier
     * 
     * @return the filter identifier
     */
    public String getFilterId() {
        return filterId;
    }

    /**
     * get result values
     * 
     * @return the result values
     */
    public List<FilterResult> getResultValues() {
        return resultValues;
    }

    /**
     * Get the type name.
     * 
     * @return The type name.
     */
    public String getTypeName() {
        return typeName;
    }
}