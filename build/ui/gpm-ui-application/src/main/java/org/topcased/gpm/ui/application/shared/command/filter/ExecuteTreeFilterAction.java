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

import org.topcased.gpm.business.util.FilterType;

/**
 * ExecuteTreeFilterAction
 * 
 * @author nveillet
 */
public class ExecuteTreeFilterAction extends
        AbstractCommandFilterAction<AbstractCommandFilterResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -5561096038734563436L;

    private String filterId;

    /**
     * create action
     */
    public ExecuteTreeFilterAction() {
    }

    /**
     * create action with filter identifier
     * 
     * @param pProductName
     *            The product name
     * @param pFilterType
     *            the filter type
     * @param pFilterId
     *            The filter identifier
     */
    public ExecuteTreeFilterAction(String pProductName, FilterType pFilterType,
            String pFilterId) {
        super(pProductName, pFilterType);
        filterId = pFilterId;
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
     * set filter identifier
     * 
     * @param pFilterId
     *            the filter identifier to set
     */
    public void setFilterId(String pFilterId) {
        filterId = pFilterId;
    }
}
