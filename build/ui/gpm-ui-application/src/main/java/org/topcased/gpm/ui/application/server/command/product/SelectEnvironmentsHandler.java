/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.product;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.product.SelectEnvironmentsAction;
import org.topcased.gpm.ui.application.shared.command.product.SelectEnvironmentsResult;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * SelectEnvironmentsHandler
 * 
 * @author nveillet
 */
public class SelectEnvironmentsHandler
        extends
        AbstractCommandActionHandler<SelectEnvironmentsAction, SelectEnvironmentsResult> {

    /**
     * Create the SelectEnvironmentsHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public SelectEnvironmentsHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public SelectEnvironmentsResult execute(SelectEnvironmentsAction pAction,
            ExecutionContext pContext) throws ActionException {
        return new SelectEnvironmentsResult(pAction.getProductTypeName(),
                getFacadeLocator().getDictionaryFacade().getEnvironmentNames(
                        getDefaultSession()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<SelectEnvironmentsAction> getActionType() {
        return SelectEnvironmentsAction.class;
    }
}
