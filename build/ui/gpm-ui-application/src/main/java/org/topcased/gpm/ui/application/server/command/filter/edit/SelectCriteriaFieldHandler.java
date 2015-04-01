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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectCriteriaFieldAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectCriteriaFieldResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerHierarchy;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;
import org.topcased.gpm.ui.facade.shared.filter.field.criteria.UiFilterCriteriaGroup;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * SelectCriteriaFieldHandler
 * 
 * @author nveillet
 */
public class SelectCriteriaFieldHandler
        extends
        AbstractCommandActionHandler<SelectCriteriaFieldAction, SelectCriteriaFieldResult> {

    /**
     * Create the SelectCriteriaFieldHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public SelectCriteriaFieldHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public SelectCriteriaFieldResult execute(SelectCriteriaFieldAction pAction,
            ExecutionContext pContext) throws ActionException {

        UiSession lUiSession = getSession(pAction.getProductName(), pAction.getFilterType());
        String lFilterId = pAction.getFilterId();

        List<UiFilterCriteriaGroup> lCriteriaGroups = null;
        Map<String, List<String>> lCategoryValues = new HashMap<String, List<String>>();
        UiFilter lFilter = null;

        if (lFilterId != null) {
            lFilter = getFacadeLocator().getFilterFacade().getFilter(lUiSession, lFilterId);
            lCriteriaGroups = lFilter.getCriteriaGroups();
            lCategoryValues = lFilter.getCategoryValues();
        }

        Map<String, UiFilterContainerHierarchy> lContainerHierarchy = null;
        int lMaxLevel = 0;
        List<UiFilterContainerType> lContainerTypes = null;
        if (pAction.loadContainerHierarchy() && lFilter != null) {

            lContainerTypes = lFilter.getContainerTypes();
            lContainerHierarchy = getFacadeLocator().getFilterFacade().getHierarchyContainers(
                            lUiSession, lContainerTypes, pAction.getFilterType());

            lMaxLevel = getFacadeLocator().getFilterFacade().getMaxFieldsDepth();
        }

        return new SelectCriteriaFieldResult(lFilterId,
                pAction.getFilterType(), lContainerHierarchy, lMaxLevel,
                lCriteriaGroups, lCategoryValues, lContainerTypes);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<SelectCriteriaFieldAction> getActionType() {
        return SelectCriteriaFieldAction.class;
    }

}
