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

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectContainerAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectContainerResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * SelectContainerHandler
 * 
 * @author nveillet
 */
public class SelectContainerHandler
        extends
        AbstractCommandActionHandler<SelectContainerAction, SelectContainerResult> {

    /**
     * Create the SelectContainerHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public SelectContainerHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public SelectContainerResult execute(SelectContainerAction pAction,
            ExecutionContext pContext) throws ActionException {

        FilterType lFilterType = pAction.getFilterType();

        UiSession lUiSession = getSession(pAction.getProductName(), lFilterType);
        String lFilterId = pAction.getFilterId();

        List<UiFilterContainerType> lAvailableContainers =
                getFacadeLocator().getFilterFacade().getSearcheableContainers(
                        lUiSession, lFilterType);

        List<UiFilterContainerType> lContainers =
                new ArrayList<UiFilterContainerType>();
        if (lFilterId != null) {
            List<UiFilterContainerType> lContainersFromFilter =
                    getFacadeLocator().getFilterFacade().getFilter(lUiSession,
                            lFilterId).getContainerTypes();
            for (UiFilterContainerType lContainer : lContainersFromFilter) {
                for (UiFilterContainerType lSearcheableContainer : lAvailableContainers) {
                    if (lSearcheableContainer.getId().equals(lContainer.getId())) {
                        lContainers.add(lSearcheableContainer);
                    }
                }
            }
        }

        return new SelectContainerResult(lFilterId, lFilterType,
                lAvailableContainers, lContainers);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<SelectContainerAction> getActionType() {
        return SelectContainerAction.class;
    }

}
