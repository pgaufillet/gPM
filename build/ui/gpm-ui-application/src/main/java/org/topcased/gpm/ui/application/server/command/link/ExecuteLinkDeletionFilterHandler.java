/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.link;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.application.client.util.CollectionUtil;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.ExecutePopupFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectCriteriaFieldAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectResultFieldAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectScopeAction;
import org.topcased.gpm.ui.application.shared.command.link.ExecuteLinkDeletionFilterAction;
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
 * ExecuteLinkDeletionFilterHandler
 * 
 * @author nveillet
 */
public class ExecuteLinkDeletionFilterHandler
        extends
        AbstractCommandActionHandler<ExecuteLinkDeletionFilterAction, AbstractCommandFilterResult> {

    /**
     * Create the ExecuteLinkDeletionFilterHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public ExecuteLinkDeletionFilterHandler(Provider<HttpSession> pHttpSession) {
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
            ExecuteLinkDeletionFilterAction pAction, ExecutionContext pContext)
        throws ActionException {

        UiSession lSession =
                getSession(pAction.getProductName(),
                        pAction.getValuesContainerId(),
                        pAction.getFieldsContainerType());

        FilterType lFilterType =
                FilterType.valueOf(pAction.getFieldsContainerType().name());

        UiFilter lFilter = pAction.getFilter();

        FilterFacade lFilterFacade = getFacadeLocator().getFilterFacade();

        // Execute the filter
        UiFilterTableResult lFilterResult;
        try {
            if (lFilter != null) {
                lFilterResult =
                        lFilterFacade.executeFilterLinkDeletion(lSession,
                                pAction.getValuesContainerId(),
                                pAction.getLinkTypeName(), lFilter);
            }
            else {
                lFilterResult =
                        lFilterFacade.executeFilterLinkDeletion(lSession,
                                pAction.getValuesContainerId(),
                                pAction.getLinkTypeName());
            }
        }
        catch (NotExistFilterException e) {
            List<UiFilterContainerType> lContainerType =
                    CollectionUtil.singleton(getFacadeLocator().getLinkFacade().getDestinationContainerType(
                            lSession, pAction.getLinkTypeName(),
                            pAction.getValuesContainerId()));

            lFilter = new UiFilter();
            lFilter.setFilterType(lFilterType);
            lFilter.setContainerTypes(lContainerType);
            lFilter.setScopes(new ArrayList<UiFilterScope>());
            lFilter.setResultFields(new ArrayList<UiFilterResultField>());
            lFilter.setCriteriaGroups(new ArrayList<UiFilterCriteriaGroup>());
            lFilter.setSortingFields(new ArrayList<UiFilterSortingField>());
            lFilter.setVisibility(UiFilterVisibility.INSTANCE);
            lFilter.setUsage(UiFilterUsage.TABLE_VIEW);
            lFilter.setHidden(false);
            lFilterFacade.addToCache(lSession, FilterType.OTHER, lFilter);

            switch (lFilterType) {
                case PRODUCT:
                    return pContext.execute(new SelectResultFieldAction(
                            pAction.getProductName(), lFilterType,
                            lFilter.getId(), null, true));
                default:

                    return pContext.execute(new SelectScopeAction(
                            pAction.getProductName(), lFilter.getId()));
            }
        }
        catch (NotSpecifiedScopeException e) {
            return pContext.execute(new SelectScopeAction(
                    pAction.getProductName(), e.getFilterId()));
        }
        catch (EmptyResultFieldException e) {
            return pContext.execute(new SelectResultFieldAction(
                    pAction.getProductName(), lFilterType, e.getFilterId(),
                    true));
        }
        catch (NotSpecifiedCriteriaException e) {
            return pContext.execute(new SelectCriteriaFieldAction(
                    pAction.getProductName(), lFilterType, e.getFilterId(),
                    true));
        }

        return new ExecutePopupFilterResult(lFilterResult.getColumnNames(),
                lFilterResult.getResultValues(), pAction.getLinkTypeName(),
                lFilterResult.getFilterId());
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<ExecuteLinkDeletionFilterAction> getActionType() {
        return ExecuteLinkDeletionFilterAction.class;
    }

}
