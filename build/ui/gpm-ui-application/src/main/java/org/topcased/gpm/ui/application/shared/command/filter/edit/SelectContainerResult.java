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
import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterResult;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;

/**
 * SelectContainerResult
 * 
 * @author nveillet
 */
public class SelectContainerResult extends AbstractCommandFilterResult {

    /** serialVersionUID */
    private static final long serialVersionUID = -5307120420029320341L;

    private List<UiFilterContainerType> availableContainers;

    private List<UiFilterContainerType> containers;

    /**
     * Empty constructor for serialization.
     */
    public SelectContainerResult() {
    }

    /**
     * Create SelectContainerResult with values
     * 
     * @param pFilterId
     *            Filter Id.
     * @param pFilterType
     *            Filter type.
     * @param pAvailableContainers
     *            the available containers
     * @param pContainers
     *            the containers
     */
    public SelectContainerResult(String pFilterId, FilterType pFilterType,
            List<UiFilterContainerType> pAvailableContainers,
            List<UiFilterContainerType> pContainers) {
        super(pFilterId, pFilterType);
        availableContainers = pAvailableContainers;
        containers = pContainers;
    }

    /**
     * get available containers
     * 
     * @return the available containers
     */
    public List<UiFilterContainerType> getAvailableContainers() {
        return availableContainers;
    }

    /**
     * get containers
     * 
     * @return the containers
     */
    public List<UiFilterContainerType> getContainers() {
        return containers;
    }

}
