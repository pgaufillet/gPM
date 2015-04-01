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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

//import org.apache.log4j.Logger;

/**
 * A Forwarder represents a remote host that needs to receive
 * cache evict notifications from other instances.
 * 
 * @author Pierre Hubert TSAAN
 * @author Olivier JUIN
 */
public class Forwarder {

//    private static final Logger LOG = Logger.getLogger(Forwarder.class);

    private URL url;
    private boolean enabled = true;
    private BlockingQueue<Map<String, String[]>> commandQueue
        = new LinkedBlockingQueue<Map<String, String[]>>();

    /**
     * Forwarder constructor
     * 
     * @param pUrl Remote instance URL
     */
    public Forwarder(URL pUrl) {
        url = pUrl;
        Worker lWorker = new Worker();
        lWorker.setName("Dispatcher to " + pUrl);
        lWorker.start();
    }

    /**
     * Register commands to forward.
     * 
     * @param pCommands commands to forward
     */
    public synchronized void queueCommands(Map<String, String[]> pCommands) {
        commandQueue.add(pCommands);
    }
    
    /**
     * Forward a command.
     * 
     * @param pCmd Command to be forwarded
     */
    private void forwardCommands(Map<String, String[]> pCmd) {
        HttpURLConnection lConnection = null;
        DataOutputStream lStream = null;
        String lParameter = encodeParameters(pCmd);
        try {
            lConnection = (HttpURLConnection) url.openConnection();
            lConnection.setRequestMethod("POST");
            lConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            lConnection.setRequestProperty("Content-Length", "" + lParameter.getBytes().length);
            lConnection.setDoOutput(true);

            lStream = new DataOutputStream(lConnection.getOutputStream());
            lStream.writeBytes(lParameter);
            lStream.flush();

            // Get HTTP request response
            int lCode = lConnection.getResponseCode();

            if (lCode != HttpURLConnection.HTTP_OK) {
                // HTTP request error
                throw new IOException(lConnection.getResponseMessage());
            }
        }
        catch (IOException ex) {
//            LOG.warn("Connection to " + url + " failed. Client disabled.");
            //FIXME cf us61
            enabled = false;
        }
        finally {
            if (null != lConnection) {
                lConnection.disconnect();
            }
            if (null != lStream) {
                try {
                    lStream.close();
                }
                catch (IOException ex) {
//                    LOG.error(ex.getMessage());
                  //FIXME cf us61
                }
            }
        }
    }

    /**
     * Encode parameters from Map format to URL format.
     * 
     * @param pParams parameters as map
     * @return parameter parameters as URL snippet
     */
    private String encodeParameters(Map<String, String[]> pParams) {
        StringBuilder lBuilder = new StringBuilder();
        try {
            for (Entry<String, String[]> lEntry : pParams.entrySet()) {
                if (lBuilder.length() > 0) {
                    lBuilder.append("&");
                }
                lBuilder.append(URLEncoder.encode(lEntry.getKey(), "UTF-8") + "="
                        + URLEncoder.encode(lEntry.getValue()[0], "UTF-8"));
            }
        }
        catch (UnsupportedEncodingException ex) {
//            LOG.error("Unsupported encoding: " + ex.getMessage());
          //FIXME cf us61
        }
        return lBuilder.toString();
    }

    /**
     * Re-enable this forwarder if needed
     */
    public void enable() {
        if (!enabled) {
            enabled = true;
//            LOG.info("Re-enabled client " + url + ".");
          //FIXME cf us61
        }
    }
    
    private class Worker extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    Map<String, String[]> lCommands = commandQueue.take();
                    if (enabled) {
                        forwardCommands(lCommands);
                    }
                }
            }
            catch (InterruptedException ex) {
                // Will not happen
            }
        }
    }
}
