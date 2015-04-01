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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.lang.StringEscapeUtils;
import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.business.util.search.FilterResult;
import org.topcased.gpm.ui.application.server.command.AbstractCommandWithMenuActionHandler;
import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.ExecuteTableFilterAction;
import org.topcased.gpm.ui.application.shared.command.filter.ExecuteTableFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectContainerAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectCriteriaFieldAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectResultFieldAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectScopeAction;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.filter.FilterFacade;
import org.topcased.gpm.ui.facade.server.i18n.I18nTranslationManager;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.exception.EmptyResultFieldException;
import org.topcased.gpm.ui.facade.shared.exception.NotExistFilterException;
import org.topcased.gpm.ui.facade.shared.exception.NotSpecifiedCriteriaException;
import org.topcased.gpm.ui.facade.shared.exception.NotSpecifiedScopeException;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;
import org.topcased.gpm.ui.facade.shared.filter.result.table.UiFilterTableResult;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * ExecuteTableFilterHandler
 * 
 * @author nveillet
 */
public class ExecuteTableFilterHandler
        extends
        AbstractCommandWithMenuActionHandler<ExecuteTableFilterAction, AbstractCommandFilterResult> {

    /**
     * Create the ExecuteTableFilterHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public ExecuteTableFilterHandler(Provider<HttpSession> pHttpSession) {
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
            ExecuteTableFilterAction pAction, ExecutionContext pContext)
        throws ActionException {

        UiFilter lFilter = pAction.getFilter();
        FilterType lFilterType = pAction.getFilterType();
        String lFilterId = pAction.getFilterId();

        if (lFilter != null) {
            lFilterType = lFilter.getFilterType();
            lFilterId = lFilter.getId();
        }

        UiSession lSession = getSession(pAction.getProductName(), lFilterType);

        FilterFacade lFilterFacade = getFacadeLocator().getFilterFacade();

        UiFilterTableResult lFilterResult;
        try {
            if (lFilter != null) {
                lFilterResult =
                        lFilterFacade.executeFilterTable(lSession, lFilter);
                if (lFilterId == null) {
                    lFilterId = lFilterResult.getFilterId();
                }
                // Get merged filter
                lFilter = lFilterFacade.getFilter(lSession, lFilterId);
            }
            else {
                lFilterResult =
                        lFilterFacade.executeFilterTable(lSession, lFilterId,
                                pAction.isUseCache());
                lFilter = lFilterFacade.getFilter(lSession, lFilterId);
            }
        }
        catch (NotExistFilterException e) {
            return pContext.execute(new SelectContainerAction(
                    pAction.getProductName(), lFilterType));
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

        // Get translation manager
        I18nTranslationManager lTranslationManager =
                getFacadeLocator().getI18nFacade().getTranslationManager(
                        lSession.getParent().getLanguage());

        // get filter informations
        String lFilterName =
                lTranslationManager.getTextTranslation(lFilter.getName());
        String lFilterDescription =
                lTranslationManager.getTextTranslation(lFilter.getDescription());

        // Actions
        Map<String, UiAction> lActions = getFilterActions(lSession, lFilter);

        // Extended actions
        List<UiAction> lExtendedActions =
                getFacadeLocator().getExtendedActionFacade().getAvailableExtendedActions(
                        lSession, lFilter.getContainerTypes());

        // Merge actions and apply access controls
        mergeActions(lSession, lActions, lExtendedActions,
                lFilter.getContainerTypes(), null);

        // encode special character for display 
        String lFName = StringEscapeUtils.escapeHtml(lFilterName);

        // encode special character on filter result
        return new ExecuteTableFilterResult(
                escapeSpecialCharacter(lFilterResult), lFilterId, lFName,
                lFilterDescription, lActions, lExtendedActions);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<ExecuteTableFilterAction> getActionType() {
        return ExecuteTableFilterAction.class;
    }

    /**
     * Encode special character on filter result values
     * 
     * @return UiFilterTableResult
     */
    private UiFilterTableResult escapeSpecialCharacter(
            UiFilterTableResult pFilterResult) {
        List<FilterResult> lFilterResultList = new ArrayList<FilterResult>();
        int lIcount = 0;
        for (lIcount = 0; lIcount < pFilterResult.getResultValues().size(); lIcount++) {
            FilterResult lFilterRs =
                    pFilterResult.getResultValues().get(lIcount);
            Iterator<String> lFilterRsIter = lFilterRs.getValues().iterator();
            List<String> lNewValues = new ArrayList<String>();
            while (lFilterRsIter.hasNext()) {
                lNewValues.add(StringEscapeUtils.escapeHtml(lFilterRsIter.next()));
            }
            lFilterResultList.add(new FilterResult(
                    lFilterRs.getFilterResultId(), lNewValues));
        }
        return new UiFilterTableResult(pFilterResult.getColumnNames(),
                lFilterResultList, pFilterResult.getFilterExecutionReport(),
                pFilterResult.getFilterId());
    }
}