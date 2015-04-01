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

import java.util.Properties;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.distribution.CacheManagerPeerProvider;
import net.sf.ehcache.distribution.CacheManagerPeerProviderFactory;

/**
 * This Factory provides the Cache Manager Peer Provider instance.
 * 
 * @author Olivier Juin
 */
public class GPMCachePeerProviderFactory extends CacheManagerPeerProviderFactory {

    private static CacheManagerPeerProvider staticInstance;
    
    /** {@inheritDoc} */
    @Override
    public synchronized CacheManagerPeerProvider createCachePeerProvider(
            CacheManager pCacheManager, Properties pProperties) {
        Commons.setCacheManager(pCacheManager);
        if (staticInstance == null) {
            staticInstance = new EHCachePeerProvider(pProperties);
        }
        return staticInstance;
    }
}
