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

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.business.util.action.AdministrationAction;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.filter.GetFilterSummariesAction;
import org.topcased.gpm.ui.application.shared.command.filter.GetFilterSummariesResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.filter.summary.UiFilterSummaries;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * GetFilterSummariesHandler
 * 
 * @author nveillet
 */
public class GetFilterSummariesHandler
        extends
        AbstractCommandActionHandler<GetFilterSummariesAction, GetFilterSummariesResult> {

    /**
     * Create the GetTableFiltersHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public GetFilterSummariesHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetFilterSummariesResult execute(GetFilterSummariesAction pAction,
            ExecutionContext pContext) throws ActionException {

        FilterType lFilterType = pAction.getFilterType();

        UiSession lSession = getSession(pAction.getProductName(), lFilterType);

        // Get filters summaries
        UiFilterSummaries lFiltersSummaries =
                getFacadeLocator().getFilterFacade().getFilters(lSession,
                        lFilterType);

        boolean lFilterCreatable = true;
        if (FilterType.PRODUCT.equals(lFilterType)) {
            lFilterCreatable =
                    getFacadeLocator().getAuthorizationFacade().hasAnyAdminAccess(
                            lSession,
                            AdministrationAction.PRODUCT_SEARCH_NEW_EDIT);
        }

        return new GetFilterSummariesResult(lFiltersSummaries, lFilterCreatable);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<GetFilterSummariesAction> getActionType() {
        return GetFilterSummariesAction.class;
    }
}
