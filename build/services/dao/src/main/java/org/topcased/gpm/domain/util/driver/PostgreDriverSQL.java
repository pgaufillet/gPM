/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.util.driver;

/**
 * Implementation of SQL driver for Postgre data base.
 * 
 * @author tpanuel
 */
public class PostgreDriverSQL extends DriverSQL {
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.util.driver.DriverSQL#getLargeStringForSelectClause(java.lang.String)
     */
    public String getLargeStringForSelectClause(final String pTableAlias) {
        return "( COALESCE(" + pTableAlias
                + ".string_value, \'\') || COALESCE(" + pTableAlias
                + ".large_string_value, \'\') )";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.util.driver.DriverSQL#getLargeStringForWhereClause(java.lang.String)
     */
    public String getLargeStringForWhereClause(final String pTableAlias) {
        return getLargeStringForSelectClause(pTableAlias);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.util.driver.DriverSQL#getLargeStringForOrderByClause(java.lang.String)
     */
    public String getLargeStringForOrderByClause(final String pTableAlias) {
        return getLargeStringForSelectClause(pTableAlias);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.util.driver.DriverSQL#getDateForWhereClause(String)
     */
    @Override
    public String getDateForWhereClause(String pContainerColumnName) {
        return pContainerColumnName;
    }
}