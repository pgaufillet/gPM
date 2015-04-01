/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.filter.edit;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectScopeAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectScopeResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterScope;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * SelectScopeHandler
 * 
 * @author nveillet
 */
public class SelectScopeHandler extends
        AbstractCommandActionHandler<SelectScopeAction, SelectScopeResult> {

    /**
     * Create the SelectScopeHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public SelectScopeHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public SelectScopeResult execute(SelectScopeAction pAction,
            ExecutionContext pContext) throws ActionException {

        UiSession lSession = getSession(pAction.getProductName());

        List<UiFilterScope> lAvailableScopes =
                getFacadeLocator().getFilterFacade().getAvailableProductScope(
                        lSession);

        List<UiFilterScope> lScopes = new ArrayList<UiFilterScope>();
        if (pAction.getFilterId() != null) {
            List<UiFilterScope> lSelectedScopes =
                    getFacadeLocator().getFilterFacade().getFilter(lSession,
                            pAction.getFilterId()).getScopes();
            for (UiFilterScope lSelectedScope : lSelectedScopes) {
                for (UiFilterScope lAvailableScope : lAvailableScopes) {
                    if (lAvailableScope.getProductName().equals(
                            lSelectedScope.getProductName())) {
                        lAvailableScope.setIncludeSubProduct(lSelectedScope.isIncludeSubProduct());
                        lScopes.add(lAvailableScope);
                    }
                }
            }
        }
        return new SelectScopeResult(pAction.getFilterId(),
                pAction.getFilterType(), lAvailableScopes, lScopes);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<SelectScopeAction> getActionType() {
        return SelectScopeAction.class;
    }

}
