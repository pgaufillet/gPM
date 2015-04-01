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

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.distribution.CachePeer;
import net.sf.ehcache.distribution.EventMessage;

//import org.apache.log4j.Logger;

/**
 * GPM CachePeer implementation
 * 
 * @author Olivier Juin
 */
class GPMCachePeer implements CachePeer {

//    private static final Logger LOG = Logger.getLogger(GPMCachePeer.class);

    private final Ehcache cache;

    /**
     * Construct a new cache peer.
     *
     * @param pCache the local cache
     */
    public GPMCachePeer(Ehcache pCache) {
        this.cache = pCache;
    }

    @Override
    public final String getUrl() throws RemoteException {
        throw new RemoteException("GPMCachePeer.getUrl() should not be used");
    }

    @Override
    public final String getUrlBase() throws RemoteException {
        throw new RemoteException("GPMCachePeer.getUrlBase() should not be used");
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getKeys() throws RemoteException {
        return cache.getKeys();
    }

    /** {@inheritDoc} */
    @Override
    public Element getQuiet(Serializable pKey) throws RemoteException {
        return cache.getQuiet(pKey);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public List<Element> getElements(List pKeys) throws RemoteException {
        if (pKeys == null) {
            return new ArrayList<Element>();
        }
        List<Element> lElements = new ArrayList<Element>();
        for (Object lKey : pKeys) {
            Element lElement = cache.getQuiet((Serializable) lKey);
            lElements.add(lElement);
        }
        return lElements;
    }

    /** {@inheritDoc} */
    @Override
    public void put(Element pElement)
    throws RemoteException, IllegalArgumentException, IllegalStateException {
        // This event will not be received
    }

    /** {@inheritDoc} */
    @Override
    public final boolean remove(Serializable pKey) throws RemoteException, IllegalStateException {
        return cache.remove(pKey, true);
    }

    /** {@inheritDoc} */
    @Override
    public final void removeAll() throws RemoteException, IllegalStateException {
        // This event will not be received
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public final void send(List pEventMessages) throws RemoteException {
        for (int i = 0; i < pEventMessages.size(); i++) {
            EventMessage lEventMessage = (EventMessage) pEventMessages.get(i);
            if (lEventMessage.getEvent() == EventMessage.PUT) {
                put(lEventMessage.getElement());
            }
            else if (lEventMessage.getEvent() == EventMessage.REMOVE) {
                remove(lEventMessage.getSerializableKey());
            }
            else if (lEventMessage.getEvent() == EventMessage.REMOVE_ALL) {
                removeAll();
            }

            
        }
    }

    /** {@inheritDoc} */
    @Override
    public final String getName() throws RemoteException {
        return cache.getName();
    }

    /** {@inheritDoc} */
    @Override
    public final String getGuid() throws RemoteException {
        return cache.getGuid();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "CachePeer for " + cache.getName();
    }
}
