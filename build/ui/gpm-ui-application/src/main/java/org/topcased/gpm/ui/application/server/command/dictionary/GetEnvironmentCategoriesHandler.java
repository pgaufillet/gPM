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
import org.topcased.gpm.ui.application.shared.command.dictionary.GetEnvironmentCategoriesAction;
import org.topcased.gpm.ui.application.shared.command.dictionary.GetEnvironmentCategoriesResult;
import org.topcased.gpm.ui.facade.server.dictionary.DictionaryFacade;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Get environment handler.
 * 
 * @author jlouisy
 */
public class GetEnvironmentCategoriesHandler
        extends
        AbstractCommandActionHandler<GetEnvironmentCategoriesAction, GetEnvironmentCategoriesResult> {

    /**
     * Create GetEnvironmentHandler.
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public GetEnvironmentCategoriesHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetEnvironmentCategoriesResult execute(
            GetEnvironmentCategoriesAction pAction, ExecutionContext pContext)
        throws ActionException {

        DictionaryFacade lDictionaryFacade =
                getFacadeLocator().getDictionaryFacade();

        return new GetEnvironmentCategoriesResult(
                lDictionaryFacade.getEnvironment(getDefaultSession(),
                        pAction.getEnvironmentName()),
                lDictionaryFacade.getDictionaryCategories(getDefaultSession()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<GetEnvironmentCategoriesAction> getActionType() {
        return GetEnvironmentCategoriesAction.class;
    }

}
