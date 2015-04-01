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
import java.util.Map;

import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.filter.result.table.UiFilterTableResult;

/**
 * ExecuteTableFilterResult
 * 
 * @author nveillet
 */
public class ExecuteTableFilterResult extends AbstractCommandFilterResult {

    /** serialVersionUID */
    private static final long serialVersionUID = 8432077548270976290L;

    private Map<String, UiAction> actions;

    private List<UiAction> extendedActions;

    private String filterId;

    private String filterDescription;

    private String filterName;

    private UiFilterTableResult filterResult;

    /**
     * Empty constructor for serialization.
     */
    public ExecuteTableFilterResult() {
    }

    /**
     * Create ExecuteTableFilterResult with values
     * 
     * @param pFilterResult
     *            The filter result
     * @param pFilterId
     *            The filter id
     * @param pFilterName
     *            The filter name
     * @param pFilterDescription
     *            The filter description
     * @param pActions
     *            the actions
     * @param pExtendedActions
     *            the extended actions
     */
    public ExecuteTableFilterResult(UiFilterTableResult pFilterResult,
            String pFilterId, String pFilterName, String pFilterDescription,
            Map<String, UiAction> pActions, List<UiAction> pExtendedActions) {
        filterResult = pFilterResult;
        filterId = pFilterId;
        filterName = pFilterName;
        filterDescription = pFilterDescription;
        actions = pActions;
        extendedActions = pExtendedActions;
    }

    /**
     * get actions
     * 
     * @return the actions
     */
    public Map<String, UiAction> getActions() {
        return actions;
    }

    /**
     * get extended actions
     * 
     * @return the extended actions
     */
    public List<UiAction> getExtendedActions() {
        return extendedActions;
    }

    /**
     * get filter description
     * 
     * @return the filter description
     */
    public String getFilterDescription() {
        return filterDescription;
    }

    /**
     * get filter id
     * 
     * @return the filter id
     */
    public String getFilterId() {
        return filterId;
    }

    /**
     * get filter name
     * 
     * @return the filter name
     */
    public String getFilterName() {
        return filterName;
    }

    /**
     * get filter result
     * 
     * @return the filter result
     */
    public UiFilterTableResult getFilterResult() {
        return filterResult;
    }
}
