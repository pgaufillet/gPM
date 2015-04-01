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

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.AddCriterionAction;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterScope;

/**
 * GetCategoryValuesAction
 * 
 * @author jlouisy
 */
public class GetCategoryValuesAction extends
        AbstractCommandEditFilterAction<GetCategoryValuesResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -7117117820554967195L;

    private List<UiFilterScope> scopes;

    private AddCriterionAction addCriterionAction;

    /**
     * create action
     */
    public GetCategoryValuesAction() {
    }

    /**
     * create action with product name
     * 
     * @param pProductName
     *            the product name.
     * @param pFilterType
     *            the filter type.
     * @param pScopes
     *            filter product scopes.
     * @param pAddCriterionAction
     *            add criterion action.
     */
    public GetCategoryValuesAction(String pProductName, FilterType pFilterType,
            List<UiFilterScope> pScopes, AddCriterionAction pAddCriterionAction) {
        super(pProductName, pFilterType);
        scopes = pScopes;
        addCriterionAction = pAddCriterionAction;
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
     * @param pScopes
     *            filter product scopes.
     * @param pAddCriterionAction
     *            add criterion action.
     */
    public GetCategoryValuesAction(String pProductName, FilterType pFilterType,
            String pFilterId, List<UiFilterScope> pScopes,
            AddCriterionAction pAddCriterionAction) {
        super(pProductName, pFilterType, pFilterId);
        scopes = pScopes;
        addCriterionAction = pAddCriterionAction;
    }

    /**
     * Get scopes.
     * 
     * @return product scopes.
     */
    public List<UiFilterScope> getScopes() {
        return scopes;
    }

    /**
     * Set scopes.
     * 
     * @param pScopes
     *            product scopes.
     */
    public void setScopes(List<UiFilterScope> pScopes) {
        scopes = pScopes;
    }

    /**
     * Get add criterion action.
     * 
     * @return add criterion action.
     */
    public AddCriterionAction getAddCriterionAction() {
        return addCriterionAction;
    }

    /**
     * Set add criterion action.
     * 
     * @param pAddCriterionAction
     *            add criterion action.
     */
    public void setAddCriterionAction(AddCriterionAction pAddCriterionAction) {
        addCriterionAction = pAddCriterionAction;
    }

}
