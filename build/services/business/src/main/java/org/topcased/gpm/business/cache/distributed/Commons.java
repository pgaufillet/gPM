/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Juin (Atos)
 ******************************************************************/
package org.topcased.gpm.business.cache.distributed;

import net.sf.ehcache.CacheManager;

/**
 * Common data for EHCache module
 * 
 * @author Olivier Juin
 */
final class Commons {

    public static final String CACHE_NAME = "org.topcased.gpm.business.ELEMENT_CACHE";
    
    private static CacheManager staticCacheManager;
    private static String staticLocalURL;
    private static EHCachePeerListener staticPeerListener;

    private Commons() {
        // Hidden constructor
    }
    
    public static synchronized CacheManager getCacheManager() {
        return staticCacheManager;
    }

    public static synchronized void setCacheManager(CacheManager pCacheManager) {
        Commons.staticCacheManager = pCacheManager;
    }

    public static String getLocalURL() {
        return staticLocalURL;
    }

    public static void setLocalURL(String pStaticLocalURL) {
        Commons.staticLocalURL = pStaticLocalURL;
    }
    
    public static EHCachePeerListener getPeerListener() {
        return staticPeerListener;
    }
    
    public static void setPeerListener(EHCachePeerListener pListener) {
        Commons.staticPeerListener = pListener;
    }
}
