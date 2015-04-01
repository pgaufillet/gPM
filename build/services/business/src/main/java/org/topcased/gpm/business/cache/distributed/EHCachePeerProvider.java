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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.distribution.CacheManagerPeerProvider;

//import org.apache.log4j.Logger;

/**
 * This class manages connections to remote hosts.
 * This class supports multiple close/reopen
 * 
 * @author Olivier Juin
 */
public class EHCachePeerProvider implements CacheManagerPeerProvider {

    // Never send more than one message every second
    private static final long MESSAGE_DELAY = 1000;
    
    // In the ehcache.xml file, properties may look like:
    // properties=
    // "urls=http://172.16.35.201:4002/FOO/gpm/ehcache|http://172.16.35.202:4002/BAR/gpm/ehcache"
    private static final String URL_PROPERTY = "urls"; 

//    private static final Logger LOG = Logger.getLogger(EHCachePeerProvider.class);

    // Remote URLs
    private List<String> urls = new ArrayList<String>();

    //// MAPS
    // Peer -> message queue table
    private Map<String, BlockingQueue<String>> queueByURLMap =
        new HashMap<String, BlockingQueue<String>>();
    // Peer -> replication thread table
    private Map<String, Replicator> replicatorByURLMap = new HashMap<String, Replicator>();
    
    /**
     * EHCache manager peer provider.
     * The ehcache.xml configuration file shall declare a peer provider,
     * through a factory.
     * 
     * This peer provider is in charge of initializing and maintaining
     * the connection to all remote peers.
     * @param pCacheManager the cache manager 
     * @param pProperties ip:port/cache properties
     */
    EHCachePeerProvider(Properties pProperties) {
        String lURLProperty = pProperties.getProperty(URL_PROPERTY);
        if (lURLProperty == null) {
//            LOG.error("No property defined for cacheManagerPeerProviderFactory in ehcache.xml");
            return;
        }
        for (String lURL : lURLProperty.split("\\|")) {
            registerPeer(lURL);
        }
    }

    // URLs are always passed as Strings, as URL.equals / URL.hashcode()
    // are performance hogs (connection to Internet !!!)
    private synchronized void initConnection(final String pURL) {
        if (replicatorByURLMap.containsKey(pURL)) {
            // Already Started.
            return;
        }
        // Use a separate thread for each remote peer
        Replicator lThread = new Replicator(pURL);
        lThread.setDaemon(true);
        replicatorByURLMap.put(pURL, lThread);
        lThread.start();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void dispose() throws CacheException {
        // Do not dispose anything, as this object may be re-used later.
        // This happens as the gPM global session is created several times
        // at application startup.
    }

    @Override
    public long getTimeForClusterToForm() {
        // Out of scope
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public void init() {
        // Nothing to init. Keep in mind that this method will be called
        // several times, several gPM global sessions are instantiated. 
    }

    /** {@inheritDoc} */
    @Override
    public synchronized List<String> listRemoteCachePeers(Ehcache pCache) {
        // Return a copy of the list
        return new ArrayList<String>(urls);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void registerPeer(String pURL) {
        // Register new peer
        urls.add(pURL);
        queueByURLMap.put(pURL, new LinkedBlockingQueue<String>());
        initConnection(pURL);
    }

    /** {@inheritDoc} */
    @Override
    public void unregisterPeer(String pUrl) {
        // unused
        System.out.println("Unregistering peer:" + pUrl);
    }

    synchronized void sendMessage(Ehcache pCache, String pKey) {
        for (BlockingQueue<String> lQueue : queueByURLMap.values()) {
            lQueue.add(pKey);
        }
    }
    
    private class Replicator extends Thread {
        
        private String url;
        
        public Replicator(String pUrl) {
            super("Replicator to " + pUrl);
            url = pUrl;
        }
        
        public void run() {
            try {
                // Start with a POST command to register to the remote peer if needed
                Map<String, String[]> lInitMap = new HashMap<String, String[]>();
                lInitMap.put("url", new String[] { Commons.getLocalURL() });
                HTTPUtils.post(url, lInitMap);

                // Replication infinite loop
                // This loop will be exited when the thread is interrupted
                BlockingQueue<String> lQueue = queueByURLMap.get(url);
                while (true) {
                    Map<String, String[]> lParamMap = new HashMap<String, String[]>();
                    lParamMap.put("url", new String[] { Commons.getLocalURL() });
                    
                    // When an element is present, wait for 1 second
                    // and send all elements, removing duplicates
                    Collection<String> lKeys = new HashSet<String>();
                    lKeys.add(lQueue.take());
                    Thread.sleep(MESSAGE_DELAY);
                    lQueue.drainTo(lKeys);
                    lParamMap.put("keys", lKeys.toArray(new String[lKeys.size()]));
                    
                    HTTPUtils.post(url, lParamMap);
                }
            }
            catch (InterruptedException ex) {
                // Shutdown 
            }
            catch (IOException ex) {
                // Connection could not be established.
                // Fail silently, as execution speed is the top priority.
            }
        }
    }
}
