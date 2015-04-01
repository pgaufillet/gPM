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

import java.net.UnknownHostException;
import java.util.Properties;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.distribution.CacheManagerPeerListener;
import net.sf.ehcache.distribution.CacheManagerPeerListenerFactory;

//import org.apache.log4j.Logger;

/**
 * Factory that returns an EHCachePeerListener object.
 * 
 * @author Olivier Juin
 */
public class GPMCachePeerListenerFactory extends CacheManagerPeerListenerFactory {

//    private static final Logger LOG = Logger.getLogger(EHCachePeerProvider.class);
    private static CacheManagerPeerListener staticInstance;
    
    /** {@inheritDoc} */
    @Override
    public synchronized CacheManagerPeerListener createCachePeerListener(
            CacheManager pCacheManager, Properties pProperties) {
        Commons.setCacheManager(pCacheManager);
        if (staticInstance == null) {
            try {
                Commons.setLocalURL(pProperties.getProperty("url"));
                staticInstance = new EHCachePeerListener();
            }
            catch (UnknownHostException ex) {
//                LOG.error(
//                "Unknown host in cacheManagerPeerListenerFactory property (ehcache.xml)");
               //FIXME cf us61 
            }
        }
        return staticInstance;
    }
}
