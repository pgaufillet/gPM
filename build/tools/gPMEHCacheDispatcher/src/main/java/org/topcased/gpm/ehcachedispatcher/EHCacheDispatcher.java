/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: phtsaan (ATOS)
 ******************************************************************/

package org.topcased.gpm.ehcachedispatcher;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.log4j.Logger;

/**
 * EHCacheDispatcher Servlet implementation class.
 * Entry point for evict events.
 * 
 * @author Pierre Hubert TSAAN
 * @author Olivier JUIN
 */
@SuppressWarnings("serial")
public class EHCacheDispatcher extends HttpServlet {

//    private static final Logger LOG = Logger.getLogger(EHCacheDispatcher.class);
 

    // URL (as String) to Forwarder map
    private Map<String, Forwarder> forwarderMap = new HashMap<String, Forwarder>();
    
    /**
     * DoPost: servlet Entry point.
     * 
     * @param pRequest HTTP request
     * @param pResponse HTTP response
     */
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest pRequest, HttpServletResponse pResponse)  {
        // Get all parameters
        Map<String, String[]> lParams = (Map<String, String[]>) pRequest.getParameterMap();

        // Get the sender URL
        String lUrl = pRequest.getParameter("url");

        String lLocalUrl = pRequest.getRequestURL().toString();
        if (lUrl != null && !lUrl.equals(lLocalUrl)) {
            // The same map object is reused in every doPost() call,
            // therefore it needs to be copied to a new Map
            Map<String, String[]> lMapCopy = new HashMap<String, String[]>(lParams);
            lMapCopy.remove("url");
            dispatchCommands(lUrl, lMapCopy);
        }
    }
    
    /**
     * Create/retrieve the forwarder, revive it if needed,
     * and forward commands to all other known forwarders.
     * 
     * @param pUrlAsString subscriber URL as String
     * @param pCommands the commands to be forwarded
     */
    private void dispatchCommands(String pUrlAsString, Map<String, String[]> pCommands) {
        try {
            // Create (and register) the forwarder if needed
            Forwarder lForwarder = forwarderMap.get(pUrlAsString);
            if (lForwarder == null) {
                // the remote instance is not registered yet
                lForwarder = new Forwarder(new URL(pUrlAsString));
                forwarderMap.put(pUrlAsString, lForwarder);
//                LOG.info("EHCache dispatcher registered new client: " + pUrlAsString);
            }
            
            // Revives the forwarder if needed
            lForwarder.enable();
            
            // Send commands to all other forwarders
            if (!pCommands.isEmpty()) {
                for (Forwarder lOtherForwarder : forwarderMap.values()) {
                    if (lForwarder != lOtherForwarder) {
                        lOtherForwarder.queueCommands(pCommands);
                    }
                }
            }
        }
        catch (MalformedURLException ex) {
//            LOG.error("Malformed URL: " + pUrlAsString);
        }
    }
}
