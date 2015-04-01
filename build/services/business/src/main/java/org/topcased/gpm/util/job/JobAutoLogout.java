/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.util.job;

import org.topcased.gpm.business.authorization.impl.AuthorizationServiceImpl;

/**
 * Job for auto delete sessions
 * 
 * @author tpanuel
 */
public class JobAutoLogout implements GpmJob {
    private AuthorizationServiceImpl authorizationServiceImpl;

    /**
     * Get the authorization service implementation
     * 
     * @return The authorization service implementation
     */
    public AuthorizationServiceImpl getAuthorizationServiceImpl() {
        return authorizationServiceImpl;
    }

    /**
     * Set the authorization service implementation
     * 
     * @param pAuthorizationServiceImpl
     *            The new authorization service implementation
     */
    public void setAuthorizationServiceImpl(
            AuthorizationServiceImpl pAuthorizationServiceImpl) {
        authorizationServiceImpl = pAuthorizationServiceImpl;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.util.job.GpmJob#execute()
     */
    public void execute() {
        authorizationServiceImpl.cleanExpiredSessions();
    }
}
