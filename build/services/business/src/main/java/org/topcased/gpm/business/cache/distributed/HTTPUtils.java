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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

//import org.apache.log4j.Logger;

/**
 * HTTP POST communication tools
 * 
 * @author Olivier Juin
 */
public final class HTTPUtils {

    /** The log4j logger object for this class. */
//    private static Logger staticLOGGER =
//            Logger.getLogger(HTTPUtils.class);
    
    private static final int HTTP_OK = 200; 

    // A utility class cannot be instantiated
    private HTTPUtils() {
    }
    
    // URLs are always passed as Strings, as URL.equals / URL.hashcode()
    // are performance hogs (connection to Internet !!!)
    /**
     * Post a request.
     * 
     * @param pURL URL as String
     * @param pParamMap parameter maps
     * @throws IOException if connection fails
     */
    public static void post(String pURL, Map<String, String[]> pParamMap) throws IOException {
        HttpURLConnection lConnection = (HttpURLConnection) new URL(pURL).openConnection();
        lConnection.setRequestMethod("POST");
        lConnection.setDoOutput(true);
        lConnection.setDoInput(true);
        lConnection.setUseCaches(false);
        lConnection.setAllowUserInteraction(false);
        lConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        lConnection.setRequestProperty("charset", "utf-8");
        
        StringBuilder lBuilder = new StringBuilder();
        boolean lFirst = true;
        for (Map.Entry<String, String[]> lParams : pParamMap.entrySet()) {
            if (lParams.getValue() == null) {
                continue;
            }
            if (!lFirst) {
                lBuilder.append('&');
            }

            lBuilder.append(lParams.getKey() + '=');
            boolean lLastparam = false;
            int lCounterParam = 0;
            for (String lParam : lParams.getValue()) {
                if (lCounterParam == (lParams.getValue().length - 1)) {
                    lLastparam = true;
                }
                if (lParam == null) {
                    continue;
                }
                lBuilder.append(URLEncoder.encode(lParam, "utf-8"));
                if (!lLastparam) {
                    lBuilder.append(',');
                }
                lCounterParam++;

            }
            // After the first cycle, this local is set to false.
            if (lFirst) {
                lFirst = false;
            }
        }
        String lEncodedData = lBuilder.toString();
        lConnection.setRequestProperty("Content-Length", "" + lEncodedData.length());

        OutputStream lStream = lConnection.getOutputStream();
        try {
            lStream.write(lEncodedData.getBytes());
        }
        finally {
            lStream.close();
        }

        if (lConnection.getResponseCode() != HTTP_OK) {
            throw new IOException(lConnection.getResponseMessage());
        }

//        if (staticLOGGER.isDebugEnabled()) {
//            staticLOGGER.debug("POST to " + pURL + " with parameters : "
//                    + lEncodedData);
//        }

        lConnection.disconnect();
    }
}
