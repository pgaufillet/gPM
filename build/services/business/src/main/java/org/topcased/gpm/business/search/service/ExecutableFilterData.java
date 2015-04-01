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
package org.topcased.gpm.business.search.service;

/**
 * @author Atos
 */
public class ExecutableFilterData implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public ExecutableFilterData() {
    }

    /**
     * Constructor without default values.
     */
    public ExecutableFilterData(
            final java.lang.String pId,
            final java.lang.String pLabelKey,
            final java.lang.String pDescription,
            final java.lang.String pUsage,
            final boolean pHidden,
            final org.topcased.gpm.business.search.result.summary.ResultSummaryData pResultSummaryData,
            final org.topcased.gpm.business.search.result.sorter.ResultSortingData pResultSortingData,
            final org.topcased.gpm.business.search.criterias.FilterData pFilterData,
            final org.topcased.gpm.business.search.service.FilterVisibilityConstraintData pFilterVisibilityConstraintData,
            final org.topcased.gpm.business.search.service.FilterProductScope[] pFilterProductScopes) {
        this.id = pId;
        this.labelKey = pLabelKey;
        this.description = pDescription;
        this.usage = pUsage;
        this.hidden = pHidden;
        this.resultSummaryData = pResultSummaryData;
        this.resultSortingData = pResultSortingData;
        this.filterData = pFilterData;
        this.filterVisibilityConstraintData = pFilterVisibilityConstraintData;
        this.filterProductScopes = pFilterProductScopes;
    }

    /**
     * Constructor taking all properties.
     */
    public ExecutableFilterData(
            final java.lang.String pId,
            final java.lang.String pLabelKey,
            final java.lang.String pDescription,
            final java.lang.String pUsage,
            final boolean pHidden,
            final boolean pExecutable,
            final boolean pEditable,
            final org.topcased.gpm.business.search.result.summary.ResultSummaryData pResultSummaryData,
            final org.topcased.gpm.business.search.result.sorter.ResultSortingData pResultSortingData,
            final org.topcased.gpm.business.search.criterias.FilterData pFilterData,
            final org.topcased.gpm.business.search.service.FilterVisibilityConstraintData pFilterVisibilityConstraintData,
            final org.topcased.gpm.business.search.service.FilterProductScope[] pFilterProductScopes) {
        this.id = pId;
        this.labelKey = pLabelKey;
        this.description = pDescription;
        this.usage = pUsage;
        this.hidden = pHidden;
        this.executable = pExecutable;
        this.editable = pEditable;
        this.resultSummaryData = pResultSummaryData;
        this.resultSortingData = pResultSortingData;
        this.filterData = pFilterData;
        this.filterVisibilityConstraintData = pFilterVisibilityConstraintData;
        this.filterProductScopes = pFilterProductScopes;
    }

    /**
     * Copies constructor from other ExecutableFilterData
     */
    public ExecutableFilterData(ExecutableFilterData pOtherBean) {
        if (pOtherBean != null) {
            this.id = pOtherBean.getId();
            this.labelKey = pOtherBean.getLabelKey();
            this.description = pOtherBean.getDescription();
            this.usage = pOtherBean.getUsage();
            this.hidden = pOtherBean.isHidden();
            this.executable = pOtherBean.isExecutable();
            this.editable = pOtherBean.isEditable();
            this.resultSummaryData = pOtherBean.getResultSummaryData();
            this.resultSortingData = pOtherBean.getResultSortingData();
            this.filterData = pOtherBean.getFilterData();
            this.filterVisibilityConstraintData =
                    pOtherBean.getFilterVisibilityConstraintData();
            this.filterProductScopes = pOtherBean.getFilterProductScopes();
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

    private java.lang.String description;

    /**
     * <p>
     * Translated value of description if exists. The description non translated
     * value otherwise.
     * </p>
     */
    public java.lang.String getDescription() {
        return this.description;
    }

    public void setDescription(java.lang.String pDescription) {
        this.description = pDescription;
    }

    private java.lang.String usage;

    /**
     * 
     */
    public java.lang.String getUsage() {
        return this.usage;
    }

    public void setUsage(java.lang.String pUsage) {
        this.usage = pUsage;
    }

    private boolean hidden;

    /**
     * 
     */
    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean pHidden) {
        this.hidden = pHidden;
    }

    private boolean executable = true;

    /**
     * 
     */
    public boolean isExecutable() {
        return this.executable;
    }

    public void setExecutable(boolean pExecutable) {
        this.executable = pExecutable;
    }

    private boolean editable = true;

    /**
     * 
     */
    public boolean isEditable() {
        return this.editable;
    }

    public void setEditable(boolean pEditable) {
        this.editable = pEditable;
    }

    private org.topcased.gpm.business.search.result.summary.ResultSummaryData resultSummaryData;

    /**
     * Get the resultSummaryData
     */
    public org.topcased.gpm.business.search.result.summary.ResultSummaryData getResultSummaryData() {
        return this.resultSummaryData;
    }

    /**
     * Set the resultSummaryData
     */
    public void setResultSummaryData(
            org.topcased.gpm.business.search.result.summary.ResultSummaryData pResultSummaryData) {
        this.resultSummaryData = pResultSummaryData;
    }

    private org.topcased.gpm.business.search.result.sorter.ResultSortingData resultSortingData;

    /**
     * Get the resultSortingData
     */
    public org.topcased.gpm.business.search.result.sorter.ResultSortingData getResultSortingData() {
        return this.resultSortingData;
    }

    /**
     * Set the resultSortingData
     */
    public void setResultSortingData(
            org.topcased.gpm.business.search.result.sorter.ResultSortingData pResultSortingData) {
        this.resultSortingData = pResultSortingData;
    }

    private org.topcased.gpm.business.search.criterias.FilterData filterData;

    /**
     * Get the filterData
     */
    public org.topcased.gpm.business.search.criterias.FilterData getFilterData() {
        return this.filterData;
    }

    /**
     * Set the filterData
     */
    public void setFilterData(
            org.topcased.gpm.business.search.criterias.FilterData pFilterData) {
        this.filterData = pFilterData;
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

    private org.topcased.gpm.business.search.service.FilterProductScope[] filterProductScopes;

    /**
     * Get the filterProductScopes
     */
    public org.topcased.gpm.business.search.service.FilterProductScope[] getFilterProductScopes() {
        return this.filterProductScopes;
    }

    /**
     * Set the filterProductScopes
     */
    public void setFilterProductScopes(
            org.topcased.gpm.business.search.service.FilterProductScope[] pFilterProductScopes) {
        this.filterProductScopes = pFilterProductScopes;
    }

}