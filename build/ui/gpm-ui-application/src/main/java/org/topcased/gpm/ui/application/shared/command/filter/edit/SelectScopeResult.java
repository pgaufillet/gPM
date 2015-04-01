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
import org.topcased.gpm.ui.facade.shared.filter.UiFilterScope;

/**
 * SelectScopeResult
 * 
 * @author nveillet
 */
public class SelectScopeResult extends AbstractCommandFilterResult {

    /** serialVersionUID */
    private static final long serialVersionUID = -5098074052929134288L;

    private List<UiFilterScope> availableScopes;

    private List<UiFilterScope> scopes;

    /**
     * Empty constructor for serialization.
     */
    public SelectScopeResult() {
    }

    /**
     * Create SelectScopeResult with values
     * 
     * @param pFilterId
     *            the filter Id
     * @param pFilterType
     *            Filter type.
     * @param pAvailableScopes
     *            the available scopes
     * @param pScopes
     *            the scopes
     */
    public SelectScopeResult(String pFilterId, FilterType pFilterType,
            List<UiFilterScope> pAvailableScopes, List<UiFilterScope> pScopes) {
        super(pFilterId, pFilterType);
        availableScopes = pAvailableScopes;
        scopes = pScopes;
    }

    /**
     * get available scopes
     * 
     * @return the available scopes
     */
    public List<UiFilterScope> getAvailableScopes() {
        return availableScopes;
    }

    /**
     * get scopes
     * 
     * @return the scopes
     */
    public List<UiFilterScope> getScopes() {
        return scopes;
    }

}
