/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.impl;

import org.topcased.gpm.domain.accesscontrol.EndUser;

/**
 * Implementation of user sessions map.
 * 
 * @author llatil
 */
public class UserSessions extends Sessions<UserContext> {
    public static final long INFINE_SESSION_TIME = -1;

    /**
     * Create a new logged session for a given end user with time out
     * 
     * @param pEndUserId
     *            The user
     * @param pSessionMaxTime
     *            The time max of the session
     * @return The session token
     */
    public String create(EndUser pEndUserId, long pSessionMaxTime) {
        if (pSessionMaxTime == INFINE_SESSION_TIME) {
            return createPermanentUser(pEndUserId);
        }
        // else
        return create(new UserContext(pEndUserId, getRandomToken(),
                pSessionMaxTime));
    }

    /**
     * Create a new logged session for a given end user
     * 
     * @param pEndUserId
     *            The permanent user
     * @return The token of the created user
     */
    public String createPermanentUser(EndUser pEndUserId) {
        return create(new PermanentUserContext(pEndUserId, getRandomToken()));
    }
}
