/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.impl;

import org.topcased.gpm.domain.accesscontrol.EndUser;

/**
 * Class PermanentUserContext
 * 
 * @author tpanuel
 */
public class PermanentUserContext extends UserContext {
    /**
     * Constructs a new user session context.
     * 
     * @param pEndUser
     *            the end user
     * @param pSessionToken
     *            the session token
     */
    public PermanentUserContext(EndUser pEndUser, String pSessionToken) {
        super(pEndUser, pSessionToken, 0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.impl.UserContext#refresh()
     */
    public boolean refresh() {
        // Permanent user is always valid
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.impl.UserContext#isValid()
     */
    public boolean isValid() {
        // Permanent user is always valid
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.impl.UserContext#invalid()
     */
    public void invalid() {
        // Permanent user is always valid
    }
}
