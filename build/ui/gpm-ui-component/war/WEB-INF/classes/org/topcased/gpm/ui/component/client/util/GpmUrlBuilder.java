/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.util;

import java.util.HashMap;
import java.util.Map;

import org.topcased.gpm.business.values.link.IUnsupportedProtocol;
import org.topcased.gpm.ui.component.shared.util.DownloadParameter;

import com.google.gwt.http.client.URL;
import com.google.gwt.http.client.UrlBuilder;

/**
 * GpmUrlBuilder.
 * <p>
 * Build an url; only handle parameters. (Takes a leaf out of {@link UrlBuilder}
 * </p>
 * 
 * @author mkargbo
 */
public class GpmUrlBuilder {
    private String url;

    private static IUnsupportedProtocol staticIUnsupportedProtocol;

    private Map<DownloadParameter, String> listParamMap =
            new HashMap<DownloadParameter, String>();

    /**
     * Constructor from an existing url
     * 
     * @param pUrl
     *            Url to build
     */
    public GpmUrlBuilder(final String pUrl) {
        url = pUrl;
    }

    /**
     * Get parameter.
     * 
     * @param pKey
     *            The key.
     */
    public String getParameter(final DownloadParameter pKey) {
        return listParamMap.get(pKey);
    }

    /**
     * Set a parameter.
     * 
     * @param pKey
     *            The key.
     * @param pValue
     *            The value.
     */
    public void setParameter(final DownloadParameter pKey, final String pValue) {
        listParamMap.put(pKey, pValue);
    }

    /**
     * Clone the URL builder.
     * 
     * @return The clone.
     */
    public GpmUrlBuilder getClone() {
        final GpmUrlBuilder lClone = new GpmUrlBuilder(url);

        for (Map.Entry<DownloadParameter, String> lEntry : listParamMap.entrySet()) {
            lClone.setParameter(lEntry.getKey(), lEntry.getValue());
        }

        return lClone;
    }

    /**
     * Build the URL and return it as an encoded string.
     * 
     * @return The encoded URL string.
     */
    public String buildString() {
        final StringBuilder lUrl = new StringBuilder(url);
        final int lWwwPrefixLength = 4;
        final int lFilePrefixLength = 4;
        // Generate the query string.
        // http://www.google.com:80/path/to/file.html?k0=v0&k1=v1
        char lPrefix = '?';
        // to manage the case of an URL without protocol specified
        // http:// will be added as default if the URL begins with
        if (lUrl.substring(0, lWwwPrefixLength).equalsIgnoreCase("www.")) {
            lUrl.insert(0, "http://");
        }
        if (lUrl.substring(0, lFilePrefixLength).equalsIgnoreCase("file")) {
            if (isFirefoxBrowser() && (staticIUnsupportedProtocol != null)) {
                lUrl.insert(lFilePrefixLength + 1, "///");
                staticIUnsupportedProtocol.displayPopupMessage(lUrl.toString());
                return null;
            }
        }

        for (Map.Entry<DownloadParameter, String> lEntry : listParamMap.entrySet()) {
            final String lKey = lEntry.getKey().name().toLowerCase();
            final String lValue = lEntry.getValue();

            lUrl.append(lPrefix).append(lKey).append('=');
            if (lValue != null) {
                lUrl.append(lValue.replaceAll("%", "%25").replaceAll("#", "%23").replaceAll(
                        ":", "%3A").replaceAll("\\?", "%3F").replaceAll(
                        Character.toString('\u20AC'), "%e2%82%ac").replaceAll(
                        ";", "%3b"));
            }
            lPrefix = '&';
        }
        
        String lResult = lUrl.toString();
        if (!isFirefoxBrowser()) {
            lResult = URL.encode(lResult);
        }
        return lResult;
    }

    /**
     * Set UnsupportedProtocol value
     * 
     * @param pIUnsupportedProtocol 
     */
    public static void setIUnsupportedProtocol(
            IUnsupportedProtocol pIUnsupportedProtocol) {
        staticIUnsupportedProtocol = pIUnsupportedProtocol;
    }

    /**
     * Gets the name of the used browser.
     * 
     * @return browser name
     */
    public static native String getBrowserName() /*-{
                                                 return navigator.userAgent.toLowerCase();
                                                 }-*/;

    /**
     * Returns true if the current browser is Firefox.
     * 
     * @return boolean
     */
    public boolean isFirefoxBrowser() {
        return getBrowserName().toLowerCase().contains("firefox");
    }
}