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

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.dialect.Oracle8iDialect;
import org.hibernate.dialect.Oracle9iDialect;
import org.hibernate.dialect.PostgreSQLDialect;

/**
 * Interface that can be used to do SQL request specific at each data base type.
 * 
 * @author tpanuel
 */
public abstract class DriverSQL {
    /**
     * Get the clause that can be used on select to access on a large string
     * field : for infinite string fields.
     * 
     * @param pTableAlias
     *            The alias of the table.
     * @return The clause.
     */
    abstract public String getLargeStringForSelectClause(String pTableAlias);

    /**
     * Get the clause that can be used on where to access on a large string
     * field : for infinite string fields.
     * 
     * @param pTableAlias
     *            The alias of the table.
     * @return The clause.
     */
    abstract public String getLargeStringForWhereClause(String pTableAlias);

    /**
     * Get the clause that can be used on order by to access on a large string
     * field : for infinite string fields.
     * 
     * @param pTableAlias
     *            The alias of the table.
     * @return The clause.
     */
    abstract public String getLargeStringForOrderByClause(String pTableAlias);

    /**
     * Get the correct format date according to the database.
     * 
     * @param pContainerColumnName
     *            the colum container name
     * @return the string to be used in the where clause
     */
    abstract public String getDateForWhereClause(String pContainerColumnName);

    /**
     * Get the Driver SQL corresponding to an Hibernate dialect
     * 
     * @param pHibernateDialect
     *            The Hibernate dialect.
     * @return The driver SQL.
     */
    @SuppressWarnings("deprecation")
    public final static DriverSQL getDriverSQL(final Dialect pHibernateDialect) {
        if (pHibernateDialect instanceof Oracle8iDialect
                || pHibernateDialect instanceof Oracle9iDialect
                || pHibernateDialect instanceof Oracle10gDialect
                || pHibernateDialect instanceof org.hibernate.dialect.OracleDialect) {
            return new OracleDriverSQL();
        }
        else if (pHibernateDialect instanceof PostgreSQLDialect) {
            return new PostgreDriverSQL();
        }
        else {
            throw new RuntimeException("Unsupported data base dialect : "
                    + pHibernateDialect.getClass().getName());
        }
    }
}