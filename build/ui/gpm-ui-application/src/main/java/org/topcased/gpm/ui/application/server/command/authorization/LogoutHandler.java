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

import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.util.log.GPMActionLogConstants;
import org.topcased.gpm.business.util.log.GPMLogger;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.authorization.LogoutAction;
import org.topcased.gpm.ui.application.shared.command.authorization.LogoutResult;
import org.topcased.gpm.ui.facade.shared.exception.UiSessionException;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * LogoutHandler
 * 
 * @author nveillet
 */
public class LogoutHandler extends
        AbstractCommandActionHandler<LogoutAction, LogoutResult> {

	/** GPM Logger */
    private GPMLogger gpmLogger = GPMLogger.getLogger(LogoutHandler.class);
	
    /**
     * Create the LogoutHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public LogoutHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public LogoutResult execute(LogoutAction pAction, ExecutionContext pContext)
        throws ActionException {

        try {
            // logout to all session
            getFacadeLocator().getAuthorizationFacade().logout(getUserSession());
        }
        catch (UiSessionException e) {
            // Do nothing because we want logout
        }
        catch (InvalidTokenException e) {
            // Do nothing because we want logout
        }

        gpmLogger.highInfo(getUserSession().getLogin(), GPMActionLogConstants.USER_DISCONNECTION);
        
        // Destroy the user session
        clearSession();
        
        
        return new LogoutResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<LogoutAction> getActionType() {
        return LogoutAction.class;
    }

}
