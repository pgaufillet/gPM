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

import java.util.List;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;

/**
 * SelectResultFieldAction
 * 
 * @author nveillet
 */
public class SelectResultFieldAction extends
        AbstractCommandEditFilterAction<SelectResultFieldResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 6916667982119319137L;

    private boolean loadContainerHierarchy;

    private boolean resetNeeded = false;

    private List<UiFilterContainerType> filterContainerTypes;

    /**
     * create action
     */
    public SelectResultFieldAction() {
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
     * @param pFilterContainerTypes
     *            the container types.
     * @param pLoadContainerHierarchy
     *            load container hierarchy?
     * @param pResetNeeded
     *            true if the reset of result fields is needed
     */
    public SelectResultFieldAction(String pProductName, FilterType pFilterType,
            String pFilterId,
            List<UiFilterContainerType> pFilterContainerTypes,
            boolean pLoadContainerHierarchy, boolean pResetNeeded) {
        super(pProductName, pFilterType, pFilterId);
        filterContainerTypes = pFilterContainerTypes;
        loadContainerHierarchy = pLoadContainerHierarchy;
        resetNeeded = pResetNeeded;
    }

    /**
     * create action with product name
     * 
     * @param pProductName
     *            the product name
     * @param pFilterType
     *            the filter type
     * @param pLoadContainerHierarchy
     *            load container hierarchy?
     */
    public SelectResultFieldAction(String pProductName, FilterType pFilterType,
            boolean pLoadContainerHierarchy) {
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
    public SelectResultFieldAction(String pProductName, FilterType pFilterType,
            String pFilterId, boolean pLoadContainerHierarchy) {
        super(pProductName, pFilterType, pFilterId);
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
     * @param pFilterContainerTypes
     *            the container types.
     * @param pLoadContainerHierarchy
     *            load container hierarchy?
     */
    public SelectResultFieldAction(String pProductName, FilterType pFilterType,
            String pFilterId,
            List<UiFilterContainerType> pFilterContainerTypes,
            boolean pLoadContainerHierarchy) {
        super(pProductName, pFilterType, pFilterId);
        filterContainerTypes = pFilterContainerTypes;
        loadContainerHierarchy = pLoadContainerHierarchy;
    }

    /**
     * Get container type Ids.
     * 
     * @return container type Ids.
     */
    public List<UiFilterContainerType> getFilterContainerTypes() {
        return filterContainerTypes;
    }

    /**
     * Set container type Ids.
     * 
     * @param pFilterContainerTypes
     *            the container type Ids.
     */
    public void setFilterContainerTypes(
            List<UiFilterContainerType> pFilterContainerTypes) {
        filterContainerTypes = pFilterContainerTypes;
    }

    /**
     * load container hierarchy?
     * 
     * @return load container hierarchy?
     */
    public boolean loadContainerHierarchy() {
        return loadContainerHierarchy;
    }

    /**
     * get resetNeeded
     * 
     * @return the resetNeeded
     */
    public boolean isResetNeeded() {
        return resetNeeded;
    }

    /**
     * set resetNeeded
     * 
     * @param pResetNeeded
     *            the resetNeeded to set
     */
    public void setResetNeeded(boolean pResetNeeded) {
        this.resetNeeded = pResetNeeded;
    }

}
