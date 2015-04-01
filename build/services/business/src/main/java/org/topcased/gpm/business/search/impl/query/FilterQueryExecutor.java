/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

//import org.apache.log4j.Logger;
import org.topcased.gpm.util.session.GpmSessionFactory;

/**
 * FilterQueryExecutor executes a query (representing a filter). Results have
 * been transformed into a List of Object[]:
 * <ul>
 * <li>List for each row</li>
 * <li>Object[] for each columns (number of columns depends of the filter data)</li>
 * </ul>
 * <p>
 * The query is written is native SQL and this use Java SQL API (Connection,
 * Statement, ResultSet) to execute it.
 * </p>
 * 
 * @author mkargbo
 */
public class FilterQueryExecutor {

//    private static final Logger LOGGER =
//            Logger.getLogger(FilterQueryExecutor.class);

    /**
     * Executes the low level query
     * 
     * @param pQuery
     *            Query to execute
     * @param pFilterQueryConfigurator
     *            Configurator of the filter
     * @return Results
     * @throws SQLException
     *             Query execution
     */
    public static ResultSet execute(String pQuery,
            FilterQueryConfigurator pFilterQueryConfigurator)
        throws SQLException {

//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("Executed query: " + pQuery);
//        }
        final Connection lConnection =
                GpmSessionFactory.getInstance().getConnection();
        PreparedStatement lPreparedStatement = null;
        ResultSet lResultSet = null;

        Map<Integer, QueryParameter> lNativeParameterMap =
                pFilterQueryConfigurator.getParameters();
        Map<Integer, QueryParameter> lNewParameterMap =
                new HashMap<Integer, QueryParameter>();
        if (!lNativeParameterMap.isEmpty()) {
            lNewParameterMap =
                    FilterQueryGenerator.computeStringQueryParameterPosition(
                            pQuery, lNativeParameterMap);
            lPreparedStatement =
                    lConnection.prepareStatement(FilterQueryGenerator.getStringQuery());

        }
        else {
            lPreparedStatement = lConnection.prepareStatement(pQuery);
        }
        lPreparedStatement.setMaxRows(pFilterQueryConfigurator.getMaxResult());

        for (Map.Entry<Integer, QueryParameter> lEntry : lNewParameterMap.entrySet()) {
            switch (lEntry.getValue().getType()) {
                case Types.BOOLEAN:
                    lPreparedStatement.setBoolean(lEntry.getKey().intValue(),
                            (Boolean) lEntry.getValue().getParameter());
                    break;
                default:
                    lPreparedStatement.setObject(lEntry.getKey(),
                            lEntry.getValue().getParameter(),
                            lEntry.getValue().getType());
            }
        }
        lResultSet = lPreparedStatement.executeQuery();
        if (pFilterQueryConfigurator.getBlockSize() > 0) {
            lResultSet.setFetchSize(pFilterQueryConfigurator.getBlockSize());
        }

        return lResultSet;
    }
}
