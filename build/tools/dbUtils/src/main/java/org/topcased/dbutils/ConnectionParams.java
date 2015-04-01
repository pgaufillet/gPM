/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Thomas Szadel
 * (Atos Origin)
 ******************************************************************/
package org.topcased.dbutils;

import java.util.Properties;

/**
 * Connection parameters
 * 
 * @author llatil
 */
public class ConnectionParams {
    private String userName;

    private String password;

    private String url;

    private String schema;

    ConnectionParams(Properties pProps) {
        readProperties(pProps);
    }

    /**
     * Read properties
     * 
     * @param pProps the properties
     */
    public void readProperties(Properties pProps) {
        url = pProps.getProperty("jdbc.url");
        userName = pProps.getProperty("jdbc.username");
        password = pProps.getProperty("jdbc.password");
        schema = pProps.getProperty("jdbc.schema");
    }

    public final String getPassword() {
        return password;
    }

    public final String getSchema() {
        return schema;
    }

    public final String getUrl() {
        return url;
    }

    public final String getUserName() {
        return userName;
    }
}
