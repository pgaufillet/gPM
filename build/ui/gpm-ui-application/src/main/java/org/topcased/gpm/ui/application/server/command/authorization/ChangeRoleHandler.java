/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.authorization;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.lang.StringEscapeUtils;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.authorization.AbstractConnectionResult;
import org.topcased.gpm.ui.application.shared.command.authorization.ChangeRoleAction;
import org.topcased.gpm.ui.application.shared.command.authorization.DisconnectProductAction;
import org.topcased.gpm.ui.application.shared.command.authorization.GetConnectionInformationAction;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * ChangeRoleHandler
 * 
 * @author nveillet
 */
public class ChangeRoleHandler
        extends
        AbstractCommandActionHandler<ChangeRoleAction, AbstractConnectionResult> {

    /**
     * Create the LoginHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public ChangeRoleHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public AbstractConnectionResult execute(ChangeRoleAction pAction,
            ExecutionContext pContext) throws ActionException {
        // replace HTML and java script code by real character
        String lProductName =
                StringEscapeUtils.unescapeHtml(pAction.getProductName());

        // Disconnect to the product
        pContext.execute(new DisconnectProductAction(lProductName,
                pAction.getOpenedSheetIds()));

        // Connection
        UiSession lSession =
                getFacadeLocator().getAuthorizationFacade().connect(
                        getUserSession(), lProductName, pAction.getRoleName());

        getUserSession().addSession(lProductName, lSession);

        return pContext.execute(new GetConnectionInformationAction(lProductName));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<ChangeRoleAction> getActionType() {
        return ChangeRoleAction.class;
    }

}
