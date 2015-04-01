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
import org.topcased.gpm.ui.application.shared.command.dictionary.DeleteEnvironmentAction;
import org.topcased.gpm.ui.application.shared.command.dictionary.DeleteEnvironmentResult;
import org.topcased.gpm.ui.facade.server.dictionary.DictionaryFacade;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Delete environment handler.
 * 
 * @author jlouisy
 */
public class DeleteEnvironmentHandler
        extends
        AbstractCommandActionHandler<DeleteEnvironmentAction, DeleteEnvironmentResult> {

    /**
     * Create GetEnvironmentHandler.
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public DeleteEnvironmentHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public DeleteEnvironmentResult execute(DeleteEnvironmentAction pAction,
            ExecutionContext pContext) throws ActionException {

        DictionaryFacade lDictionaryFacade =
                getFacadeLocator().getDictionaryFacade();

        lDictionaryFacade.deleteEnvironment(getDefaultSession(),
                pAction.getEnvironmentName());

        return new DeleteEnvironmentResult(
                lDictionaryFacade.getEnvironmentNames(getDefaultSession()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<DeleteEnvironmentAction> getActionType() {
        return DeleteEnvironmentAction.class;
    }

}
