/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.sheet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.ExecutePopupFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectCriteriaFieldAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectResultFieldAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectScopeAction;
import org.topcased.gpm.ui.application.shared.command.sheet.ExecuteSheetInitializationFilterAction;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.filter.FilterFacade;
import org.topcased.gpm.ui.facade.shared.exception.EmptyResultFieldException;
import org.topcased.gpm.ui.facade.shared.exception.NotExistFilterException;
import org.topcased.gpm.ui.facade.shared.exception.NotSpecifiedCriteriaException;
import org.topcased.gpm.ui.facade.shared.exception.NotSpecifiedScopeException;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterScope;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterUsage;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterVisibility;
import org.topcased.gpm.ui.facade.shared.filter.field.criteria.UiFilterCriteriaGroup;
import org.topcased.gpm.ui.facade.shared.filter.field.result.UiFilterResultField;
import org.topcased.gpm.ui.facade.shared.filter.field.sort.UiFilterSortingField;
import org.topcased.gpm.ui.facade.shared.filter.result.table.UiFilterTableResult;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * ExecuteSheetInitializationFilterHandler
 * 
 * @author nveillet
 */
public class ExecuteSheetInitializationFilterHandler
        extends
        AbstractCommandActionHandler<ExecuteSheetInitializationFilterAction, AbstractCommandFilterResult> {

    /**
     * Create the ExecuteSheetInitializationFilterHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public ExecuteSheetInitializationFilterHandler(
            Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public AbstractCommandFilterResult execute(
            ExecuteSheetInitializationFilterAction pAction,
            ExecutionContext pContext) throws ActionException {

        UiSession lSession = getSession(pAction.getProductName());

        UiFilter lFilter = pAction.getFilter();

        FilterFacade lFilterFacade = getFacadeLocator().getFilterFacade();

        // Execute the filter
        UiFilterTableResult lFilterResult;
        try {
            if (lFilter != null) {
                lFilterResult =
                        lFilterFacade.executeFilterSheetInitialization(
                                lSession, lFilter);
            }
            else {
                lFilterResult =
                        lFilterFacade.executeFilterSheetInitialization(
                                lSession, pAction.getSheetId(),
                                pAction.getSheetTypeName());
            }
        }
        catch (NotExistFilterException e) {
            List<UiFilterContainerType> lContainerType =
                    lFilterFacade.getSearcheableContainers(lSession,
                            FilterType.SHEET);
            for (int i = lContainerType.size() - 1; i >= 0; i--) {
                if (!lContainerType.get(i).getName().equals(
                        pAction.getSheetTypeName())) {
                    lContainerType.remove(i);
                }
            }

            lFilter = new UiFilter();
            lFilter.setFilterType(FilterType.SHEET);
            lFilter.setContainerTypes(lContainerType);
            lFilter.setScopes(new ArrayList<UiFilterScope>());
            lFilter.setResultFields(new ArrayList<UiFilterResultField>());
            lFilter.setCriteriaGroups(new ArrayList<UiFilterCriteriaGroup>());
            lFilter.setSortingFields(new ArrayList<UiFilterSortingField>());
            lFilter.setVisibility(UiFilterVisibility.INSTANCE);
            lFilter.setUsage(UiFilterUsage.TABLE_VIEW);
            lFilter.setHidden(false);
            lFilterFacade.addToCache(lSession, FilterType.OTHER, lFilter);

            return pContext.execute(new SelectScopeAction(
                    pAction.getProductName(), lFilter.getId()));

        }
        catch (NotSpecifiedScopeException e) {
            return pContext.execute(new SelectScopeAction(
                    pAction.getProductName(), e.getFilterId()));
        }
        catch (EmptyResultFieldException e) {
            return pContext.execute(new SelectResultFieldAction(
                    pAction.getProductName(), FilterType.SHEET,
                    e.getFilterId(), true));
        }
        catch (NotSpecifiedCriteriaException e) {
            return pContext.execute(new SelectCriteriaFieldAction(
                    pAction.getProductName(), FilterType.SHEET,
                    e.getFilterId(), true));
        }

        return new ExecutePopupFilterResult(lFilterResult.getColumnNames(),
                lFilterResult.getResultValues(), pAction.getSheetTypeName(),
                lFilterResult.getFilterId());
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<ExecuteSheetInitializationFilterAction> getActionType() {
        return ExecuteSheetInitializationFilterAction.class;
    }
}
