/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * This class reads the properties to run a WS test case. The properties are in
 * a xml file which respect the properties dtd.
 * 
 * @see Properties You have to specify the path to the properties file by
 *      setting the system propertie named "ws.properties".
 * @author ogehin
 */
public class WsProperties {
    private static final String SYSTEM_PROPERTIES_FILE = "ws.properties";

    private static final String URL_PROPERTIES = "ws.url";

    private static final String SERVLET_PROPERTIES = "ws.servlet";

    private String url;

    private String servlet;

    /**
     * Construct the properties from the file given by the ws.properties
     * property. If the properties file is omitted, get the properties from the
     * system. If the system properties are omitted, set default properties and
     * throw a exception for the mandatory properties.
     * 
     * @throws Exception
     *             configuration exception.
     */
    public WsProperties() throws Exception {
        Properties lProperties = System.getProperties();
        String lPropertiesFile =
                lProperties.getProperty(SYSTEM_PROPERTIES_FILE);
        if (lPropertiesFile != null) {
            InputStream lIs = null;
            try {
                lIs = new FileInputStream(lPropertiesFile);
            }
            catch (java.io.FileNotFoundException e) {
                lIs =
                        this.getClass().getClassLoader().getResourceAsStream(
                                lPropertiesFile);
            }
            finally {
            	if (lIs != null) {
            		lIs.close();
            	}
            }
            if (lIs == null) {
                throw new Exception("Cannot read configuration file '"
                        + lPropertiesFile + "'");
            }
            try {
                lProperties.load(lIs);
            }
            catch (InvalidPropertiesFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        else {
            InputStream lIs = null;
            try {
                lIs = new FileInputStream("ws.properties");
            }
            catch (java.io.FileNotFoundException e) {
                lIs =
                        this.getClass().getClassLoader().getResourceAsStream(
                                "ws.properties");
            }
            finally {
            	if (lIs != null) {
            		lIs.close();
            	}
            }
            if (lIs != null) {
                try {
                    lProperties.load(lIs);
                }
                catch (InvalidPropertiesFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        url = lProperties.getProperty(URL_PROPERTIES);
        servlet = lProperties.getProperty(SERVLET_PROPERTIES);
    }

    public String getUrl() {
        return url;
    }

    public String getServletName() {
        return servlet;
    }
}
