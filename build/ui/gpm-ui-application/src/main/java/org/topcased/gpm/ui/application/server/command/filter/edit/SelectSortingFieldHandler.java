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
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectSortingFieldAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectSortingFieldResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.filter.field.sort.UiFilterSortingField;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * SelectSortingFieldHandler
 * 
 * @author nveillet
 */
public class SelectSortingFieldHandler
        extends
        AbstractCommandActionHandler<SelectSortingFieldAction, SelectSortingFieldResult> {

    /**
     * Create the SelectSortingFieldHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public SelectSortingFieldHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public SelectSortingFieldResult execute(SelectSortingFieldAction pAction,
            ExecutionContext pContext) throws ActionException {

        FilterType lFilterType = pAction.getFilterType();

        UiSession lUiSession =
                getSession(pAction.getProductName(), lFilterType);

        String lFilterId = pAction.getFilterId();

        List<UiFilterSortingField> lSortingFields =
                new ArrayList<UiFilterSortingField>();

        if (lFilterId != null) {
            lSortingFields =
                    getFacadeLocator().getFilterFacade().getFilter(lUiSession,
                            lFilterId).getSortingFields();
        }

        return new SelectSortingFieldResult(lSortingFields);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<SelectSortingFieldAction> getActionType() {
        return SelectSortingFieldAction.class;
    }

}
