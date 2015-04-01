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
 * Implementation of SQL driver for Oracle data base.
 * 
 * @author tpanuel
 */
public class OracleDriverSQL extends DriverSQL {
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.util.driver.DriverSQL#getLargeStringForSelectClause(java.lang.String)
     */
    public String getLargeStringForSelectClause(final String pTableAlias) {
        return '(' + pTableAlias + ".string_value || dbms_lob.substr("
                + pTableAlias + ".large_string_value,4000,1))";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.util.driver.DriverSQL#getLargeStringForWhereClause(java.lang.String)
     */
    public String getLargeStringForWhereClause(final String pTableAlias) {
        return '(' + pTableAlias + ".string_value || dbms_lob.substr("
                + pTableAlias + ".large_string_value, 4000, 1))";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.util.driver.DriverSQL#getLargeStringForOrderByClause(java.lang.String)
     */
    public String getLargeStringForOrderByClause(final String pTableAlias) {
        return getLargeStringForWhereClause(pTableAlias);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.util.driver.DriverSQL#getDateForWhereClause(String)
     */
    @Override
    public String getDateForWhereClause(String pContainerColumnName) {
        return "TO_DATE(" + pContainerColumnName + ") ";
    }
}