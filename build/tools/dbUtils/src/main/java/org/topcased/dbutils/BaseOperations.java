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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Base Operations
 * 
 * @author llatil
 */
abstract public class BaseOperations implements DbOperations {
    // The log4j logger object for this class.
    private static Logger staticLOGGER =
            org.apache.log4j.Logger.getLogger(BaseOperations.class);

    protected ConnectionParams connectionParams;

    protected Connection connection;

    protected String driverClassname;

    /**
     * Constructor
     * 
     * @param pParams
     *            Connection parameters
     * @param pDriverClassName
     *            Name of the JDBC driver class
     */
    public BaseOperations(ConnectionParams pParams, String pDriverClassName) {
        connectionParams = pParams;
        driverClassname = pDriverClassName;

        // Load the JDBC driver
        try {
            Class.forName(driverClassname);
        }
        catch (ClassNotFoundException e) {
            String lMsg = "Cannot load JDBC driver " + driverClassname;
            staticLOGGER.error(lMsg);
            
            throw new RuntimeException(lMsg);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.dbutils.DbOperations#connect()
     */
    public void connect() {

        Connection lConnection;

        try {
            lConnection =
                    DriverManager.getConnection(connectionParams.getUrl(),
                            connectionParams.getUserName(),
                            connectionParams.getPassword());
        }
        catch (SQLException e) {
            throw new RuntimeException("Cannot open connection to database.", e);
        }

        connection = lConnection;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.dbutils.DbOperations#dropAllTables(java.sql.Connection,
     *      java.lang.String)
     */
    public int dropAllTables() {
        List<String> lTableNames = getTableNames();

        if (null == lTableNames) {
            return 0;
        }
        else {
            return dropTables(lTableNames);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.dbutils.DbOperations#dropAllSequences()
     */
    public int dropAllSequences() {
        List<String> lSeqNames = getSequenceNames();
        if (null == lSeqNames) {
            return 0;
        }
        else {
            dropSequences(lSeqNames);
            return lSeqNames.size();
        }
    }
}
