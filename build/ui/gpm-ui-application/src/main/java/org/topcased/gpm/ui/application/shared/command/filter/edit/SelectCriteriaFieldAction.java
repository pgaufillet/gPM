/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.filter.edit;

import org.topcased.gpm.business.util.FilterType;

/**
 * SelectCriteriaFieldAction
 * 
 * @author nveillet
 */
public class SelectCriteriaFieldAction extends
        AbstractCommandEditFilterAction<SelectCriteriaFieldResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -5372074742353497231L;

    private boolean loadContainerHierarchy;

    /**
     * create action
     */
    public SelectCriteriaFieldAction() {
    }

    /**
     * create action with product and filter identifier
     * 
     * @param pProductName
     *            the product name
     * @param pFilterType
     *            the filter type
     * @param pLoadContainerHierarchy
     *            load container hierarchy?
     */
    public SelectCriteriaFieldAction(String pProductName,
            FilterType pFilterType, boolean pLoadContainerHierarchy) {
        super(pProductName, pFilterType);
        loadContainerHierarchy = pLoadContainerHierarchy;
    }

    /**
     * create action with product and filter identifier
     * 
     * @param pProductName
     *            the product name
     * @param pFilterType
     *            the filter type
     * @param pFilterId
     *            the filter identifier
     * @param pLoadContainerHierarchy
     *            load container hierarchy?
     */
    public SelectCriteriaFieldAction(String pProductName,
            FilterType pFilterType, String pFilterId,
            boolean pLoadContainerHierarchy) {
        super(pProductName, pFilterType, pFilterId);
        loadContainerHierarchy = pLoadContainerHierarchy;
    }

    /**
     * load container hierarchy?
     * 
     * @return load container hierarchy?
     */
    public boolean loadContainerHierarchy() {
        return loadContainerHierarchy;
    }
}
