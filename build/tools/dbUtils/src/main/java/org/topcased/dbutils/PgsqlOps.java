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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Basics operations for postgres sql datas base.
 * 
 * @author llatil
 */
public class PgsqlOps extends BaseOperations {
    // The log4j logger object for this class.
    private static Logger staticLOGGER =
            org.apache.log4j.Logger.getLogger(PgsqlOps.class);

    /**
     * constructor
     * 
     * @param pParams
     *            the parameters of the connection
     */
    public PgsqlOps(ConnectionParams pParams) {
        super(pParams, "org.postgresql.Driver");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.dbutils.DbOperations#dropSequences(java.util.List)
     */
    public void dropSequences(List<String> pSequenceNames) {

        for (String lSeqName : pSequenceNames) {
            try {
                Statement lStat = connection.createStatement();
                String lDropSequenceQuery = "DROP SEQUENCE ";

                lDropSequenceQuery += lSeqName;

                if (staticLOGGER.isDebugEnabled()) {
                    staticLOGGER.debug(lDropSequenceQuery);
                }
                lStat.execute(lDropSequenceQuery);
                lStat.close();
            }
            catch (SQLException e) {
                staticLOGGER.debug("Cannot drop sequence '" + lSeqName + "'\n"
                        + e.getMessage());
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.dbutils.DbOperations#dropTables(java.util.List)
     */
    public int dropTables(List<String> pTableNames) {
        int lCount = 0;
        for (String lTableName : pTableNames) {
            try {
                Statement lStat = connection.createStatement();
                String lDropTableQuery =
                        "DROP TABLE " + lTableName + " CASCADE";
                if (staticLOGGER.isDebugEnabled()) {
                    staticLOGGER.debug(lDropTableQuery);
                }
                lStat.execute(lDropTableQuery);
                lCount++;
                lStat.close();
            }
            catch (SQLException e) {
                throw new RuntimeException("Cannot drop table '" + lTableName
                        + "'\n" + e.getMessage());
            }
        }
        return lCount;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.dbutils.DbOperations#getSequenceNames()
     */
    public List<String> getSequenceNames() {
        List<String> lSeqNames = new LinkedList<String>();
        ResultSet lResultSet = null;
        Statement lStmt = null;

        try {
            lStmt = connection.createStatement();
            lResultSet =
                    lStmt.executeQuery("SELECT sequence_name FROM information_schema.sequences"
                            + " WHERE sequence_schema='public'");

            while (lResultSet.next()) {
                lSeqNames.add(lResultSet.getString(1));
            }
            lStmt.close();
        }
        catch (SQLException e) {
            throw new RuntimeException("Cannot get the list of seqeunces.", e);
        }
        finally {
            try {
                if (null != lResultSet) {
                    lResultSet.close();
                }
                if (null != lStmt) {
                    lStmt.close();
                }
            }
            catch (SQLException e) {
                // Ignore any exception raised here
            }
        }
        return lSeqNames;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.dbutils.DbOperations#getTableNames()
     */
    public List<String> getTableNames() {
        List<String> lTableNames = new LinkedList<String>();

        try {
            ResultSet lResultSet =
                    connection.getMetaData().getTables(null,
                            connectionParams.getSchema(), "%",
                            new String[] { "TABLE" });

            while (lResultSet.next()) {
                String lTableName = lResultSet.getString("TABLE_NAME");
                lTableNames.add(lTableName);
            }
            lResultSet.close();
        }
        catch (SQLException e) {
            throw new RuntimeException("Cannot get the list of tables.", e);
        }
        return lTableNames;
    }
}
