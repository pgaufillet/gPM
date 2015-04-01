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
import org.topcased.gpm.ui.facade.shared.filter.field.result.UiFilterResultField;

/**
 * SelectResultFieldResult
 * 
 * @author nveillet
 */
public class SelectResultFieldResult extends AbstractCommandFilterResult {

    /** serialVersionUID */
    private static final long serialVersionUID = 3878552937064191062L;

    private Map<String, UiFilterContainerHierarchy> containerHierarchy;

    private int maxLevel;

    private List<UiFilterResultField> resultFields;

    private List<UiFilterContainerType> rootContainers;

    /**
     * Empty constructor for serialization.
     */
    public SelectResultFieldResult() {
    }

    /**
     * Create SelectResultFieldResult with values
     * 
     * @param pFilterId
     *            the filter Id
     * @param pFilterType
     *            Filter type.
     * @param pContainerHierarchy
     *            the container hierarchy
     * @param pMaxLevel
     *            the max level
     * @param pResultFields
     *            the result fields
     * @param pRootContainers
     *            the tree root(s)
     */
    public SelectResultFieldResult(String pFilterId, FilterType pFilterType,
            Map<String, UiFilterContainerHierarchy> pContainerHierarchy,
            int pMaxLevel, List<UiFilterResultField> pResultFields,
            List<UiFilterContainerType> pRootContainers) {
        super(pFilterId, pFilterType);
        containerHierarchy = pContainerHierarchy;
        maxLevel = pMaxLevel;
        resultFields = pResultFields;
        rootContainers = pRootContainers;
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

    /**
     * get result fields
     * 
     * @return the result fields
     */
    public List<UiFilterResultField> getResultFields() {
        return resultFields;
    }
}
