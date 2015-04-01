/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.cache.impl;

import java.util.HashMap;
import java.util.Map;

import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;
import org.topcased.gpm.ui.facade.server.cache.UserCache;
import org.topcased.gpm.ui.facade.server.cache.UserCacheManager;

/**
 * User cache manager.
 * 
 * @author tpanuel
 */
public class UserCacheManagerImpl implements UserCacheManager {
    private Map<String, UserCache> cacheMap;

    /**
     * Create a user cache manager.
     */
    public UserCacheManagerImpl() {
        cacheMap = new HashMap<String, UserCache>();
    }

    /**
     * Get the cache associate to a user.
     * 
     * @param pSession
     *            The user session.
     * @return The user cache.
     */
    public UserCache getUserCache(final UiUserSession pSession) {
        UserCache lUserCache = cacheMap.get(pSession.getToken());

        if (lUserCache == null) {
            lUserCache = new UserCache();
            cacheMap.put(pSession.getToken(), lUserCache);
        }

        return lUserCache;
    }

    /**
     * Remove the cache associate to a user.
     * 
     * @param pSession
     *            The user session.
     */
    public void removeUserCache(final UiUserSession pSession) {
        cacheMap.remove(pSession);
    }
}