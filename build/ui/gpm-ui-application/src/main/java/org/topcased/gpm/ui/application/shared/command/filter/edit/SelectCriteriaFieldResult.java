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
import java.util.Map;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterResult;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerHierarchy;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;
import org.topcased.gpm.ui.facade.shared.filter.field.criteria.UiFilterCriteriaGroup;

/**
 * SelectCriteriaFieldResult
 * 
 * @author nveillet
 */
public class SelectCriteriaFieldResult extends AbstractCommandFilterResult {

    /** serialVersionUID */
    private static final long serialVersionUID = 3322070389659782042L;

    private Map<String, UiFilterContainerHierarchy> containerHierarchy;

    private int maxLevel;

    private List<UiFilterCriteriaGroup> criteriaGroups;

    private Map<String, List<String>> categoryValues;

    private List<UiFilterContainerType> rootContainers;

    /**
     * Empty constructor for serialization.
     */
    public SelectCriteriaFieldResult() {
    }

    /**
     * Create SelectCriteriaFieldResult with values
     * 
     * @param pFilterId
     *            the filter Id
     * @param pFilterType
     *            Filter type.
     * @param pContainerHierarchy
     *            the container hierarchy
     * @param pMaxLevel
     *            the max level
     * @param pCriteriaGroups
     *            the criteria groups
     * @param pCategoryValues
     *            category values.
     * @param pRootContainers
     *            the tree root(s)
     */
    public SelectCriteriaFieldResult(String pFilterId, FilterType pFilterType,
            Map<String, UiFilterContainerHierarchy> pContainerHierarchy,
            int pMaxLevel, List<UiFilterCriteriaGroup> pCriteriaGroups,
            Map<String, List<String>> pCategoryValues,
            List<UiFilterContainerType> pRootContainers) {
        super(pFilterId, pFilterType);
        containerHierarchy = pContainerHierarchy;
        maxLevel = pMaxLevel;
        criteriaGroups = pCriteriaGroups;
        categoryValues = pCategoryValues;
        rootContainers = pRootContainers;
    }

    /**
     * get criteriaGroups
     * 
     * @return the criteria groups
     */
    public List<UiFilterCriteriaGroup> getCriteriaGroups() {
        return criteriaGroups;
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
     * get container hierarchy
     * 
     * @return the container hierarchy
     */
    public Map<String, UiFilterContainerHierarchy> getContainerHierarchy() {
        return containerHierarchy;
    }

    /**
     * get container hierarchy
     * 
     * @return the container hierarchy
     */
    public List<UiFilterContainerType> getRootContainers() {
        return rootContainers;
    }

    /**
     * get max level
     * 
     * @return the max level
     */
    public int getMaxLevel() {
        return maxLevel;
    }

}
