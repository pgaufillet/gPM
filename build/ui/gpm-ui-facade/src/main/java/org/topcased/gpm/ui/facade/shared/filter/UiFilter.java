/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.filter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.facade.shared.filter.field.criteria.UiFilterCriteriaGroup;
import org.topcased.gpm.ui.facade.shared.filter.field.result.UiFilterResultField;
import org.topcased.gpm.ui.facade.shared.filter.field.sort.UiFilterSortingField;

/**
 * UiFilter
 * 
 * @author nveillet
 */
public class UiFilter implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -6771331154988979149L;

    /** The id of business object FilterData (used for conversion) */
    private String businessFilterDataId;

    private Map<String, List<String>> categoryValues;

    private List<UiFilterContainerType> containerTypes;

    private List<UiFilterCriteriaGroup> criteriaGroups;

    private String description;

    private boolean editable;

    private FilterType filterType;

    private Boolean hidden;

    private String id;

    private String name;

    private List<UiFilterResultField> resultFields;

    private List<UiFilterScope> scopes;

    private List<UiFilterSortingField> sortingFields;

    private UiFilterUsage usage;

    private UiFilterVisibility visibility;

    //Indicate if the container list has been changed
    // and the user has chosen to reset fields 
    private boolean isResetNeeded;

    /**
     * Constructor
     */
    public UiFilter() {
        categoryValues = new HashMap<String, List<String>>();
    }

    /**
     * get business FilterData Id (used for conversion)
     * 
     * @return the business FilterData Id
     */
    public String getBusinessFilterDataId() {
        return businessFilterDataId;
    }

    /**
     * Get category values.
     * 
     * @return category values.
     */
    public Map<String, List<String>> getCategoryValues() {
        return categoryValues;
    }

    /**
     * get container types
     * 
     * @return the container types
     */
    public List<UiFilterContainerType> getContainerTypes() {
        return containerTypes;
    }

    /**
     * get criteria groups
     * 
     * @return the criteria groups
     */
    public List<UiFilterCriteriaGroup> getCriteriaGroups() {
        return criteriaGroups;
    }

    /**
     * get description
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * get filter type
     * 
     * @return the filter type
     */
    public FilterType getFilterType() {
        return filterType;
    }

    /**
     * get hidden
     * 
     * @return the hidden
     */
    public Boolean getHidden() {
        return hidden;
    }

    /**
     * get identifier
     * 
     * @return the identifier
     */
    public String getId() {
        return id;
    }

    /**
     * get name
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * get result fields
     * 
     * @return the result fields
     */
    public List<UiFilterResultField> getResultFields() {
        return resultFields;
    }

    /**
     * get scopes
     * 
     * @return the scopes
     */
    public List<UiFilterScope> getScopes() {
        return scopes;
    }

    /**
     * get sorting fields
     * 
     * @return the sorting fields
     */
    public List<UiFilterSortingField> getSortingFields() {
        return sortingFields;
    }

    /**
     * get usage
     * 
     * @return the usage
     */
    public UiFilterUsage getUsage() {
        return usage;
    }

    /**
     * get visibility
     * 
     * @return the visibility
     */
    public UiFilterVisibility getVisibility() {
        return visibility;
    }

    /**
     * get editable access
     * 
     * @return the editable access
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * set business FilterData Id (used for conversion)
     * 
     * @param pBusinessFilterDataId
     *            the business FilterData Id to set
     */
    public void setBusinessFilterDataId(String pBusinessFilterDataId) {
        businessFilterDataId = pBusinessFilterDataId;
    }

    /**
     * Set category values.
     * 
     * @param pCategoryValues
     *            category values.
     */
    public void setCategoryValues(Map<String, List<String>> pCategoryValues) {
        categoryValues = pCategoryValues;
    }

    /**
     * set container types
     * 
     * @param pContainerTypes
     *            the container types to set
     */
    public void setContainerTypes(List<UiFilterContainerType> pContainerTypes) {
        containerTypes = pContainerTypes;
    }

    /**
     * set criteria groups
     * 
     * @param pCriteriaGroups
     *            the criteria groups to set
     */
    public void setCriteriaGroups(List<UiFilterCriteriaGroup> pCriteriaGroups) {
        criteriaGroups = pCriteriaGroups;
    }

    /**
     * set description
     * 
     * @param pDescription
     *            the description to set
     */
    public void setDescription(String pDescription) {
        description = pDescription;
    }

    /**
     * set editable access
     * 
     * @param pEditable
     *            the editable access to set
     */
    public void setEditable(boolean pEditable) {
        editable = pEditable;
    }

    /**
     * set filter type
     * 
     * @param pFilterType
     *            the filter type to set
     */
    public void setFilterType(FilterType pFilterType) {
        filterType = pFilterType;
    }

    /**
     * set hidden
     * 
     * @param pHidden
     *            the hidden to set
     */
    public void setHidden(Boolean pHidden) {
        hidden = pHidden;
    }

    /**
     * set identifier
     * 
     * @param pId
     *            the identifier to set
     */
    public void setId(String pId) {
        id = pId;
    }

    /**
     * set name
     * 
     * @param pName
     *            the name to set
     */
    public void setName(String pName) {
        name = pName;
    }

    /**
     * set result fields
     * 
     * @param pResultFields
     *            the result fields to set
     */
    public void setResultFields(List<UiFilterResultField> pResultFields) {
        resultFields = pResultFields;
    }

    /**
     * set scopes
     * 
     * @param pScopes
     *            the scopes to set
     */
    public void setScopes(List<UiFilterScope> pScopes) {
        scopes = pScopes;
    }

    /**
     * set sorting fields
     * 
     * @param pSortingFields
     *            the sorting fields to set
     */
    public void setSortingFields(List<UiFilterSortingField> pSortingFields) {
        sortingFields = pSortingFields;
    }

    /**
     * set usage
     * 
     * @param pUsage
     *            the usage to set
     */
    public void setUsage(UiFilterUsage pUsage) {
        usage = pUsage;
    }

    /**
     * set visibility
     * 
     * @param pVisibility
     *            the visibility to set
     */
    public void setVisibility(UiFilterVisibility pVisibility) {
        visibility = pVisibility;
    }

    /**
     * get isResetNeeded
     * 
     * @return the isResetNeeded
     */
    public boolean isResetNeeded() {
        return isResetNeeded;
    }

    /**
     * set isResetNeeded
     * 
     * @param pResetNeeded
     *            the isResetNeeded to set
     */
    public void setResetNeeded(boolean pResetNeeded) {
        this.isResetNeeded = pResetNeeded;
    }

}
