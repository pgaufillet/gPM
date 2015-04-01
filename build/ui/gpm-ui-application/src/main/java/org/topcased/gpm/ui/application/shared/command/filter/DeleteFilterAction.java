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
 * DeleteFilterAction
 * 
 * @author nveillet
 */
public class DeleteFilterAction extends
        AbstractCommandFilterAction<DeleteFilterResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 8886123583771225602L;

    private String filterId;

    /**
     * Create action
     */
    public DeleteFilterAction() {
    }

    /**
     * Create action with values
     * 
     * @param pProductName
     *            the product name
     * @param pFilterType
     *            the filter type
     * @param pFilterId
     *            the filter identifier
     */
    public DeleteFilterAction(String pProductName, FilterType pFilterType,
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
