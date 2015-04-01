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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Oracle Ops
 * 
 * @author llatil
 */
public class OracleOps extends BaseOperations {
    // The log4j logger object for this class.
    private static Logger staticLOGGER =
            org.apache.log4j.Logger.getLogger(OracleOps.class);

    /**
     * Constructor.
     * 
     * @param pParams base operations parameters
     */
    public OracleOps(ConnectionParams pParams) {
        super(pParams, "oracle.jdbc.OracleDriver");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.dbutils.DbOperations#dropTables(java.util.List)
     */
    public int dropTables(List<String> pTableNames) {
        int lCount = 0;
        for (String lTableName : pTableNames) {
            Statement lStat = null;

            try {
                lStat = connection.createStatement();
                String lDropTableQuery =
                        "DROP TABLE " + lTableName + " CASCADE CONSTRAINTS";
                if (staticLOGGER.isDebugEnabled()) {
                    staticLOGGER.debug(lDropTableQuery);
                }
                lStat.execute(lDropTableQuery);
                ++lCount;
                lStat.close();
            }
            catch (SQLException e) {
                staticLOGGER.info("Cannot drop table '" + lTableName + "'\n"
                        + e.getMessage());
            }
            finally {
                if (null != lStat) {
                    try {
                        lStat.close();
                    }
                    catch (SQLException e) {
                        // Can we do something here ?
                    }
                }
            }
        }
        return lCount;
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

                if (StringUtils.isNotBlank(connectionParams.getSchema())) {
                    lDropSequenceQuery += connectionParams.getSchema() + ".";
                }
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
     * @see org.topcased.dbutils.DbOperations#getTableNames()
     */
    public List<String> getTableNames() {
        List<String> lTableNames = new LinkedList<String>();
        ResultSet lResultSet = null;
        try {
            lResultSet =
                    connection.getMetaData().getTables(null,
                            connectionParams.getSchema(), "%",
                            new String[] { "TABLE" });

            while (lResultSet.next()) {
                String lTableName = lResultSet.getString("TABLE_NAME");
                if (lTableName.indexOf('$') != -1) {
                    if (staticLOGGER.isInfoEnabled()) {
                        staticLOGGER.info("Ignoring system table '"
                                + lTableName + "'");
                    }
                }
                else {
                    lTableNames.add(lTableName);
                }
            }
            lResultSet.close();
        }
        catch (SQLException e) {
            throw new RuntimeException("Cannot get the list of tables.", e);
        }
        finally {
            if (null != lResultSet) {
                try {
                    lResultSet.close();
                }
                catch (SQLException e) {
                    // Ignore any exception raised here
                }
            }
        }
        return lTableNames;
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
                    lStmt.executeQuery("SELECT SEQUENCE_NAME FROM USER_SEQUENCES");

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
}
