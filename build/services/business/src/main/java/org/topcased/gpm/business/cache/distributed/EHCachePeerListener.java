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
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Status;
import net.sf.ehcache.distribution.CacheManagerPeerListener;
import net.sf.ehcache.distribution.CachePeer;

import org.jfree.util.Log;

/**
 * EHCache peer listener, pretty useless in our implementation,
 * as the cache to be distributed is already known.
 * 
 * @author Olivier Juin
 */
public class EHCachePeerListener implements CacheManagerPeerListener {

    private Status status;
    private GPMCachePeer cachePeer;

    /**
     * Constructor.
     * 
     * @throws UnknownHostException if provided hostname is invalid
     */
    public EHCachePeerListener()
    throws UnknownHostException {
        status = Status.STATUS_UNINITIALISED;
        Commons.setPeerListener(this);
    }

    /** {@inheritDoc} */
    @Override
    public void init() throws CacheException {
        // Initialize cachePeers
        
        Ehcache lCache = Commons.getCacheManager().getEhcache(Commons.CACHE_NAME);
        cachePeer = new GPMCachePeer(lCache);
        status = Status.STATUS_ALIVE;
    }

    /** {@inheritDoc} */
    public synchronized void dispose() throws CacheException {
        // Do not release resources
    }

    /**
     * All of the caches which are listening for remote changes.
     * Contains one element (unused method).
     *
     * @return a list of <code>CachePeer</code> objects. The list if not live
     */
    public List<CachePeer> getBoundCachePeers() {
        List<CachePeer> lPeers = new ArrayList<CachePeer>();
        lPeers.add(cachePeer);
        return lPeers;
    }

    @Override
    public Status getStatus() {
        // Unused
        return status;
    }

    @Override
    public String getUniqueResourceIdentifier() {
        return "EHCACHE listener";
    }

    /** {@inheritDoc} */
    @Override
    public void attemptResolutionOfUniqueResourceConflict()
    throws IllegalStateException, CacheException {
        // Do not care
    }

    /** {@inheritDoc} */
    public synchronized void notifyCacheAdded(String pCacheName) {
        // Nothing to do
    }

    /** {@inheritDoc} */
    public synchronized void notifyCacheRemoved(String pCacheName) {
        // Nothing to do
    }

    // Evict from the local cache
    void evict(String pCacheName, String pKey) {
        try {
            if (cachePeer == null) {
//                Log.warn("Invalid Remote evict cache");
                return;
            }
            cachePeer.remove(pKey);
        }
        catch (RemoteException ex) {
            ex.printStackTrace();
        }
        catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }
}
