/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.cache;

import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;

/**
 * User cache manager.
 * 
 * @author tpanuel
 */
public interface UserCacheManager {

    /**
     * Get the cache associate to a user.
     * 
     * @param pSession
     *            The user session.
     * @return The user cache.
     */
    public UserCache getUserCache(final UiUserSession pSession);

    /**
     * Remove the cache associate to a user.
     * 
     * @param pSession
     *            The user session.
     */
    public void removeUserCache(final UiUserSession pSession);
}