/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
//
// Attention: Generated code! Do not modify by hand!
// Generated by: HibernateEnumeration.vsl in andromda-hibernate-cartridge.
//
package org.topcased.gpm.domain.search;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;

/**
 * @author Atos
 */
@SuppressWarnings("rawtypes")
public final class FilterTypeEnum extends FilterType implements
        java.io.Serializable, java.lang.Comparable,
        org.hibernate.usertype.UserType {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final int[] SQL_TYPES = { Types.VARCHAR };

    /**
     * Default constructor. Hibernate needs the default constructor to retrieve
     * an instance of the enum from a JDBC resultset. The instance will be
     * converted to the correct enum instance in
     * {@link #nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)}
     * .
     */
    public FilterTypeEnum() {
        super();
    }

    /**
     * @see org.hibernate.usertype.UserType#sqlTypes()
     */
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    /**
     * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
     */
    public Object deepCopy(Object pValue) throws HibernateException {
        // Enums are immutable - nothing to be done to deeply clone it
        return pValue;
    }

    /**
     * @see org.hibernate.usertype.UserType#isMutable()
     */
    public boolean isMutable() {
        // Enums are immutable
        return false;
    }

    /**
     * @see org.hibernate.usertype.UserType#equals(java.lang.Object,
     *      java.lang.Object)
     */
    public boolean equals(Object pObjectX, Object pObjectY)
        throws HibernateException {
        return (pObjectX == pObjectY)
                || (pObjectX != null && pObjectY != null && pObjectY.equals(pObjectX));
    }

    /**
     * @see org.hibernate.usertype.UserType#returnedClass()
     */
    public Class returnedClass() {
        return FilterType.class;
    }

    /**
     * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet,
     *      java.lang.String[], java.lang.Object)
     */
    public Object nullSafeGet(ResultSet pResultSet, String[] pValues,
            Object pOwner) throws HibernateException, SQLException {
        final java.lang.String lValue =
                (java.lang.String) pResultSet.getObject(pValues[0]);
        return pResultSet.wasNull() ? null : fromString(lValue);
    }

    /**
     * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement,
     *      java.lang.Object, int)
     */
    public void nullSafeSet(PreparedStatement pStatement, Object pValue,
            int pIndex) throws HibernateException, SQLException {
        if (pValue == null) {
            pStatement.setNull(pIndex, Types.VARCHAR);
        }
        else {
            pStatement.setObject(pIndex,
                    java.lang.String.valueOf(java.lang.String.valueOf(pValue)));
        }
    }

    /**
     * @see org.hibernate.usertype.UserType#replace(Object original, Object
     *      target, Object owner)
     */
    public Object replace(Object pOriginal, Object pTarget, Object pOwner) {
        return pOriginal;
    }

    /**
     * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable
     *      cached, Object owner)
     */
    public Object assemble(java.io.Serializable pCached, Object pOwner) {
        return pCached;
    }

    /**
     * @see org.hibernate.usertype.UserType#assemble(Object value)
     */
    public java.io.Serializable disassemble(Object pValue) {
        return (java.io.Serializable) pValue;
    }

    /**
     * @see org.hibernate.usertype.UserType#assemble(Object value)
     */
    public int hashCode(Object pObject) {
        return pObject.hashCode();
    }
}