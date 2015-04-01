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
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectResultFieldAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectResultFieldResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerHierarchy;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;
import org.topcased.gpm.ui.facade.shared.filter.field.result.UiFilterResultField;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * SelectResultFieldHandler
 * 
 * @author nveillet
 */
public class SelectResultFieldHandler extends
        AbstractCommandActionHandler<SelectResultFieldAction, SelectResultFieldResult> {

    /**
     * Create the SelectResulFieldHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public SelectResultFieldHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public SelectResultFieldResult execute(SelectResultFieldAction pAction,
            ExecutionContext pContext) throws ActionException {

        FilterType lFilterType = pAction.getFilterType();
        UiSession lUiSession = getSession(pAction.getProductName(), lFilterType);
        String lFilterId = pAction.getFilterId();
        List<UiFilterResultField> lResultFields = null;
        UiFilter lFilter = null;

        if (lFilterId == null) {
            lResultFields = new ArrayList<UiFilterResultField>();
        }
        else {
            lFilter = getFacadeLocator().getFilterFacade().getFilter(lUiSession, lFilterId);
            //If no reset is needed, result
            if (!pAction.isResetNeeded()) {

                lResultFields = lFilter.getResultFields();
            }
            else {
                //else result fields are empty
                lResultFields = new ArrayList<UiFilterResultField>();
            }
        }

        Map<String, UiFilterContainerHierarchy> lContainerHierarchy = null;
        int lMaxLevel = 0;
        List<UiFilterContainerType> lContainerTypes = null;
        if (pAction.loadContainerHierarchy()) {
            lContainerTypes = pAction.getFilterContainerTypes();
            if ((lContainerTypes == null || lContainerTypes.isEmpty())
                    && lFilter != null) {
                lContainerTypes = lFilter.getContainerTypes();
            }
            lContainerHierarchy = getFacadeLocator().getFilterFacade().getHierarchyContainers(
                            lUiSession, lContainerTypes, lFilterType);

            lMaxLevel = getFacadeLocator().getFilterFacade().getMaxFieldsDepth();
        }

        return new SelectResultFieldResult(lFilterId, lFilterType,
                lContainerHierarchy, lMaxLevel, lResultFields, lContainerTypes);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<SelectResultFieldAction> getActionType() {
        return SelectResultFieldAction.class;
    }
}
