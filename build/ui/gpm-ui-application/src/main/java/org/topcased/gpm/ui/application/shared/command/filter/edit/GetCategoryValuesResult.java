/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.filter.edit;

import java.util.List;

import net.customware.gwt.dispatch.shared.Result;

import org.topcased.gpm.ui.application.client.command.popup.filter.edit.AddCriterionAction;

/**
 * GetCategoryValuesResult
 * 
 * @author jlouisy
 */
public class GetCategoryValuesResult implements Result {

    /** serialVersionUID */
    private static final long serialVersionUID = -5186691865508581228L;

    private List<String> categoryValues;

    private AddCriterionAction addCriterionAction;

    /**
     * Empty constructor for serialization.
     */
    public GetCategoryValuesResult() {
    }

    /**
     * Create GetCategoryValuesResult with value
     * 
     * @param pCategoryValues
     *            the category values
     * @param pAddCriterionAction
     *            add criterion action.
     */
    public GetCategoryValuesResult(List<String> pCategoryValues,
            AddCriterionAction pAddCriterionAction) {
        categoryValues = pCategoryValues;
        addCriterionAction = pAddCriterionAction;
    }

    /**
     * Get category values.
     * 
     * @return category values.
     */
    public List<String> getCategoryValues() {
        return categoryValues;
    }

    /**
     * Get add criterion action.
     * 
     * @return add criterion action.
     */
    public AddCriterionAction getAddCriterionAction() {
        return addCriterionAction;
    }

}
