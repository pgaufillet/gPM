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

import java.util.List;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.filter.edit.GetUsableFieldsAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.GetUsableFieldsResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * PreSaveFilterHandler
 * 
 * @author nveillet
 */
public class GetUsableFieldsHandler
        extends
        AbstractCommandActionHandler<GetUsableFieldsAction, GetUsableFieldsResult> {

    /**
     * Create the PreSaveFilterHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public GetUsableFieldsHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetUsableFieldsResult execute(GetUsableFieldsAction pAction,
            ExecutionContext pContext) throws ActionException {

        UiSession lUiSession =
                getSession(pAction.getProductName(), pAction.getFilterType());
        List<String> lContainerId = pAction.getContainerIds();

        return new GetUsableFieldsResult(
                getFacadeLocator().getFilterFacade().getUsableFields(
                        lUiSession, lContainerId));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<GetUsableFieldsAction> getActionType() {
        return GetUsableFieldsAction.class;
    }

}
