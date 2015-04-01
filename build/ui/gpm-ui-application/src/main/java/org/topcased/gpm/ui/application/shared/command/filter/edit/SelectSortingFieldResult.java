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

import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterResult;
import org.topcased.gpm.ui.facade.shared.filter.field.sort.UiFilterSortingField;

/**
 * SelectSortingFieldResult
 * 
 * @author nveillet
 */
public class SelectSortingFieldResult extends AbstractCommandFilterResult {

    /** serialVersionUID */
    private static final long serialVersionUID = -6125146667297441547L;

    private List<UiFilterSortingField> sortingFields;

    /**
     * Empty constructor for serialization.
     */
    public SelectSortingFieldResult() {
    }

    /**
     * Create SelectSortingFieldResult with values
     * 
     * @param pSortingFields
     *            the sorting fields
     */
    public SelectSortingFieldResult(List<UiFilterSortingField> pSortingFields) {
        sortingFields = pSortingFields;
    }

    /**
     * get sorting fields
     * 
     * @return the sorting fields
     */
    public List<UiFilterSortingField> getSortingFields() {
        return sortingFields;
    }

}
