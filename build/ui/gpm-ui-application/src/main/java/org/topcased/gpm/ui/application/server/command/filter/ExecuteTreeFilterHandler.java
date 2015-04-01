/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.filter;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.ExecuteTreeFilterAction;
import org.topcased.gpm.ui.application.shared.command.filter.ExecuteTreeFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectContainerAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectCriteriaFieldAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectResultFieldAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectScopeAction;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.exception.EmptyResultFieldException;
import org.topcased.gpm.ui.facade.shared.exception.NotExistFilterException;
import org.topcased.gpm.ui.facade.shared.exception.NotSpecifiedCriteriaException;
import org.topcased.gpm.ui.facade.shared.exception.NotSpecifiedScopeException;
import org.topcased.gpm.ui.facade.shared.filter.result.tree.UiFilterTreeResult;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * ExecuteTreeFilterHandler
 * 
 * @author nveillet
 */
public class ExecuteTreeFilterHandler
        extends
        AbstractCommandActionHandler<ExecuteTreeFilterAction, AbstractCommandFilterResult> {

    /**
     * Create the ExecuteTreeFilterHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public ExecuteTreeFilterHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public AbstractCommandFilterResult execute(ExecuteTreeFilterAction pAction,
            ExecutionContext pContext) throws ActionException {

        UiSession lSession =
                getSession(pAction.getProductName(), pAction.getFilterType());

        // Execute the filter
        UiFilterTreeResult lFilterResult;
        try {
            lFilterResult =
                    getFacadeLocator().getFilterFacade().executeFilterTree(
                            lSession, pAction.getFilterId());
        }
        catch (NotExistFilterException e) {
            return pContext.execute(new SelectContainerAction(
                    pAction.getProductName(), pAction.getFilterType()));
        }
        catch (NotSpecifiedScopeException e) {
            return pContext.execute(new SelectScopeAction(
                    pAction.getProductName(), e.getFilterId()));
        }
        catch (EmptyResultFieldException e) {
            return pContext.execute(new SelectResultFieldAction(
                    pAction.getProductName(), pAction.getFilterType(),
                    e.getFilterId(), true));
        }
        catch (NotSpecifiedCriteriaException e) {
            return pContext.execute(new SelectCriteriaFieldAction(
                    pAction.getProductName(), pAction.getFilterType(),
                    e.getFilterId(), true));
        }

        // If the result filter is null (problem in the filter), open filter in edition mode
        if (lFilterResult == null) {
            return pContext.execute(new SelectResultFieldAction(
                    pAction.getProductName(), pAction.getFilterType(),
                    pAction.getFilterId(), true));
        }

        return new ExecuteTreeFilterResult(lFilterResult);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<ExecuteTreeFilterAction> getActionType() {
        return ExecuteTreeFilterAction.class;
    }
}
