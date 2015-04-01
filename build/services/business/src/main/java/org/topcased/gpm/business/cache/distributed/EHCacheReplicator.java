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

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.distribution.CacheReplicator;

/**
 * This cache event listener aims at sending replication commands
 * to other servers as fast as possible
 * 
 * @author Olivier Juin
 */
public class EHCacheReplicator implements CacheReplicator {

    /** {@inheritDoc} */
    @Override
    public void dispose() {
        // Nothing to do
    }

    /** {@inheritDoc} */
    @Override
    public void notifyElementEvicted(Ehcache pCache, Element pElement) {
        // Do not propagate evictions! Propagate removals/updates instead
    }

    /** {@inheritDoc} */
    @Override
    public void notifyElementExpired(Ehcache pCache, Element pElement) {
        // Do not propagate expired elements
    }

    /** {@inheritDoc} */
    @Override
    public void notifyElementPut(Ehcache pCache, Element pElement)
        throws CacheException {
        // Do not propagate element creations
    }

    /** {@inheritDoc} */
    @Override
    public void notifyElementRemoved(Ehcache pCache, Element pElement)
        throws CacheException {
        propagate(pCache, pElement);
    }

    /** {@inheritDoc} */
    @Override
    public void notifyElementUpdated(Ehcache pCache, Element pElement)
        throws CacheException {
        propagate(pCache, pElement);
    }

    /** {@inheritDoc} */
    @Override
    public void notifyRemoveAll(Ehcache pCache) {
        // Do not propagate
    }

    private void propagate(Ehcache pCache, Element pElement) {
        if (pElement.getKey() != null) {
            ((EHCachePeerProvider) pCache.getCacheManager().getCacheManagerPeerProvider())
                .sendMessage(pCache, pElement.getKey().toString());
        }
    }

    /** {@inheritDoc} */
    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        return new EHCacheReplicator();
    }

    /** {@inheritDoc} */
    @Override
    public boolean alive() {
        return true;
    }

    @Override
    public boolean isReplicateUpdatesViaCopy() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean notAlive() {
        return false;
    }
}
