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
import org.topcased.gpm.ui.application.shared.command.dictionary.CreateEnvironmentAction;
import org.topcased.gpm.ui.application.shared.command.dictionary.CreateEnvironmentResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.dictionary.DictionaryFacade;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Create environment handler.
 * 
 * @author jlouisy
 */
public class CreateEnvironmentHandler
        extends
        AbstractCommandActionHandler<CreateEnvironmentAction, CreateEnvironmentResult> {

    /**
     * Create GetEnvironmentHandler.
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public CreateEnvironmentHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public CreateEnvironmentResult execute(CreateEnvironmentAction pAction,
            ExecutionContext pContext) throws ActionException {

        UiSession lSession = getDefaultSession();

        DictionaryFacade lDictionaryFacade =
                getFacadeLocator().getDictionaryFacade();

        // Create Environment
        lDictionaryFacade.createEnvironment(lSession,
                pAction.getEnvironmentName(), pAction.isPublic());

        return new CreateEnvironmentResult(lDictionaryFacade.getEnvironment(
                lSession, pAction.getEnvironmentName()),
                lDictionaryFacade.getDictionaryCategories(lSession),
                lDictionaryFacade.getEnvironmentNames(lSession));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<CreateEnvironmentAction> getActionType() {
        return CreateEnvironmentAction.class;
    }

}
