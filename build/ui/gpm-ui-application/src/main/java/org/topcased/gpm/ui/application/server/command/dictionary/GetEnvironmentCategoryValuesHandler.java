/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.dictionary;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.dictionary.GetEnvironmentCategoryValuesAction;
import org.topcased.gpm.ui.application.shared.command.dictionary.GetEnvironmentCategoryValuesResult;
import org.topcased.gpm.ui.facade.server.dictionary.DictionaryFacade;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Get selectable and selected values for a category handler.
 * 
 * @author jlouisy
 */
public class GetEnvironmentCategoryValuesHandler
        extends
        AbstractCommandActionHandler<GetEnvironmentCategoryValuesAction, GetEnvironmentCategoryValuesResult> {

    /**
     * Create GetEnvironmentCategoryValuesHandler.
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public GetEnvironmentCategoryValuesHandler(
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
    public GetEnvironmentCategoryValuesResult execute(
            GetEnvironmentCategoryValuesAction pAction,
            ExecutionContext pContext) throws ActionException {

        DictionaryFacade lDictionaryFacade =
                getFacadeLocator().getDictionaryFacade();

        return new GetEnvironmentCategoryValuesResult(
                lDictionaryFacade.getDictionaryCategoryValues(
                        getDefaultSession(), pAction.getCategoryName()),
                lDictionaryFacade.getEnvironmentCategoryValues(
                        getDefaultSession(), pAction.getEnvironmentName(),
                        pAction.getCategoryName()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<GetEnvironmentCategoryValuesAction> getActionType() {
        return GetEnvironmentCategoryValuesAction.class;
    }

}
