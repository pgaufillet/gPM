/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
//
// Attention: Generated code! Do not modify by hand!
// Generated by: ValueObject.vsl in andromda-java-cartridge.
//
package org.topcased.gpm.business.search.criterias;

/**
 * @author Atos
 */
public class FilterData implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public FilterData() {
    }

    /**
     * Constructor taking all properties.
     */
    public FilterData(
            final java.lang.String pId,
            final java.lang.String pLabelKey,
            final boolean pIsFilterModel,
            final java.lang.String[] pFieldsContainerIds,
            final org.topcased.gpm.business.search.criterias.FilterTypeData pType,
            final org.topcased.gpm.business.search.criterias.CriteriaData pCriteriaData,
            final org.topcased.gpm.business.search.service.FilterVisibilityConstraintData pFilterVisibilityConstraintData) {
        this.id = pId;
        this.labelKey = pLabelKey;
        this.isFilterModel = pIsFilterModel;
        this.fieldsContainerIds = pFieldsContainerIds;
        this.type = pType;
        this.criteriaData = pCriteriaData;
        this.filterVisibilityConstraintData = pFilterVisibilityConstraintData;
    }

    /**
     * Copies constructor from other FilterData
     */
    public FilterData(FilterData pOtherBean) {
        if (pOtherBean != null) {
            this.id = pOtherBean.getId();
            this.labelKey = pOtherBean.getLabelKey();
            this.isFilterModel = pOtherBean.isIsFilterModel();
            this.fieldsContainerIds = pOtherBean.getFieldsContainerIds();
            this.type = pOtherBean.getType();
            this.criteriaData = pOtherBean.getCriteriaData();
            this.filterVisibilityConstraintData =
                    pOtherBean.getFilterVisibilityConstraintData();
        }
    }

    private java.lang.String id;

    /**
     * 
     */
    public java.lang.String getId() {
        return this.id;
    }

    public void setId(java.lang.String pId) {
        this.id = pId;
    }

    private java.lang.String labelKey;

    /**
     * 
     */
    public java.lang.String getLabelKey() {
        return this.labelKey;
    }

    public void setLabelKey(java.lang.String pLabelKey) {
        this.labelKey = pLabelKey;
    }

    private boolean isFilterModel;

    /**
     * 
     */
    public boolean isIsFilterModel() {
        return this.isFilterModel;
    }

    public void setIsFilterModel(boolean pIsFilterModel) {
        this.isFilterModel = pIsFilterModel;
    }

    private java.lang.String[] fieldsContainerIds;

    /**
     * 
     */
    public java.lang.String[] getFieldsContainerIds() {
        return this.fieldsContainerIds;
    }

    public void setFieldsContainerIds(java.lang.String[] pFieldsContainerIds) {
        this.fieldsContainerIds = pFieldsContainerIds;
    }

    private org.topcased.gpm.business.search.criterias.FilterTypeData type;

    /**
     * 
     */
    public org.topcased.gpm.business.search.criterias.FilterTypeData getType() {
        return this.type;
    }

    public void setType(
            org.topcased.gpm.business.search.criterias.FilterTypeData pType) {
        this.type = pType;
    }

    private org.topcased.gpm.business.search.criterias.CriteriaData criteriaData;

    /**
     * Get the criteriaData
     */
    public org.topcased.gpm.business.search.criterias.CriteriaData getCriteriaData() {
        return this.criteriaData;
    }

    /**
     * Set the criteriaData
     */
    public void setCriteriaData(
            org.topcased.gpm.business.search.criterias.CriteriaData pCriteriaData) {
        this.criteriaData = pCriteriaData;
    }

    private org.topcased.gpm.business.search.service.FilterVisibilityConstraintData filterVisibilityConstraintData;

    /**
     * Get the filterVisibilityConstraintData
     */
    public org.topcased.gpm.business.search.service.FilterVisibilityConstraintData getFilterVisibilityConstraintData() {
        return this.filterVisibilityConstraintData;
    }

    /**
     * Set the filterVisibilityConstraintData
     */
    public void setFilterVisibilityConstraintData(
            org.topcased.gpm.business.search.service.FilterVisibilityConstraintData pFilterVisibilityConstraintData) {
        this.filterVisibilityConstraintData = pFilterVisibilityConstraintData;
    }

}