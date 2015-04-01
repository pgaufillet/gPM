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
import org.topcased.gpm.ui.application.shared.command.filter.DeleteFilterAction;
import org.topcased.gpm.ui.application.shared.command.filter.DeleteFilterResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.filter.summary.UiFilterSummaries;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * DeleteFilterHandler
 * 
 * @author nveillet
 */
public class DeleteFilterHandler extends
        AbstractCommandActionHandler<DeleteFilterAction, DeleteFilterResult> {

    /**
     * Create the DeleteFilterHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public DeleteFilterHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public DeleteFilterResult execute(DeleteFilterAction pAction,
            ExecutionContext pContext) throws ActionException {

        UiSession lSession =
                getSession(pAction.getProductName(), pAction.getFilterType());

        // delete filter
        getFacadeLocator().getFilterFacade().deleteFilter(lSession,
                pAction.getFilterId());

        // get filters
        UiFilterSummaries lFilterSummaries =
                getFacadeLocator().getFilterFacade().getFilters(lSession,
                        pAction.getFilterType());

        return new DeleteFilterResult(lFilterSummaries);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<DeleteFilterAction> getActionType() {
        return DeleteFilterAction.class;
    }

}
