/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: XXX (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.authorization.impl.filter.FilterExecutionReport;
import org.topcased.gpm.business.exception.MethodNotImplementedException;

/**
 * FilterResultIterator
 * 
 * @author mkargbo
 */
public class FilterResultIdIterator implements Iterator<String> {
    private Connection connection;

    private Statement statement;

    private ResultSet resultSet;

    private FilterQueryConfigurator filterQueryConfigurator;

    private final FilterExecutionReport executionReport;

    /**
     * Constructor
     * 
     * @param pFilterQueryConfigurator the filter configurator 
     * @param pResultSet the result set
     * @param pExecutionReport the execution report
     * @throws SQLException if a problem occurs
     */
    public FilterResultIdIterator(
            final FilterQueryConfigurator pFilterQueryConfigurator,
            final ResultSet pResultSet,
            final FilterExecutionReport pExecutionReport) throws SQLException {
        resultSet = pResultSet;
        statement = pResultSet.getStatement();
        connection = pResultSet.getStatement().getConnection();
        filterQueryConfigurator = pFilterQueryConfigurator;
        executionReport = pExecutionReport;

        //Move to start
        if (filterQueryConfigurator.getStartFrom() > 0) {
            for (int i = 0; i < filterQueryConfigurator.getStartFrom(); i++) {
                resultSet.next();
            }
        }
    }

    /**
     * Constructor
     * 
     * @param pFilterQueryConfigurator the filter configurator
     * @param pExecutionReport the execution report
     */
    public FilterResultIdIterator(
            final FilterQueryConfigurator pFilterQueryConfigurator,
            final FilterExecutionReport pExecutionReport) {
        filterQueryConfigurator = pFilterQueryConfigurator;
        executionReport = pExecutionReport;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        return has(true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#next()
     */
    public String next() {
        return currentElement();
    }

    /**
     * Return true if has previous
     * 
     * @return true if has previous
     */
    public boolean hasPrevious() {
        return has(false);
    }

    /**
     * Get previous
     * 
     * @return previous
     */
    public String previous() {
        return currentElement();
    }

    private String currentElement() {
        String lIdentifier = StringUtils.EMPTY;
        if (resultSet != null) {
            try {
                lIdentifier = resultSet.getString(1);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lIdentifier;
    }

    private boolean has(boolean pNextOrPrevious) {
        boolean lEnd = true;
        if (resultSet != null) {
            try {
                if (pNextOrPrevious) {
                    lEnd = !resultSet.next();
                }
                else {
                    lEnd = !resultSet.previous();
                }
                if (lEnd) {
                    resultSet.close();
                    statement.close();
                    connection.close();
                }
            }
            catch (SQLException e) {
                try {
                    resultSet.close();
                    statement.close();
                    connection.close();
                }
                catch (SQLException e1) {
                    e1.printStackTrace();
                }

                e.printStackTrace();
                return false;
            }
        }
        return !lEnd;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        throw new MethodNotImplementedException(
                "Cannot remove an element of a " + getClass().getName());
    }

    /**
     * Get the execution report.
     * 
     * @return The execution report.
     */
    public FilterExecutionReport getExecutionReport() {
        return executionReport;
    }
}