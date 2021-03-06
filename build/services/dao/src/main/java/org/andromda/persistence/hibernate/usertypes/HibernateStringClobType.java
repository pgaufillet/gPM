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
// Generated by: HibernateStringClobType.vsl in andromda-hibernate-cartridge.
//
package org.andromda.persistence.hibernate.usertypes;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.lang.ObjectUtils;
//import org.apache.log4j.Logger;
import org.hibernate.type.ImmutableType;

/**
 * <p>
 * A hibernate user type which converts a Clob into a String and back again.
 * </p>
 * 
 * @author Atos
 */
public class HibernateStringClobType extends ImmutableType {
    /**
     * The log4j logger object for this class.
     */
//    private static Logger staticLogger =
//            org.apache.log4j.Logger.getLogger(HibernateStringClobType.class);

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 10000L;

    /**
     * @see org.hibernate.type.NullableType#get(java.sql.ResultSet,
     *      java.lang.String)
     */
    public Object get(ResultSet pRs, String pName) throws SQLException {

        Reader lReader = pRs.getCharacterStream(pName);
        if (lReader == null) {
            return null;
        }
        StringBuffer lSb = new StringBuffer();
        try {
            char[] lCharbuf = new char[4096];
            for (int i = lReader.read(lCharbuf); i > 0; i =
                    lReader.read(lCharbuf)) {
                lSb.append(lCharbuf, 0, i);
            }
        }
        catch (IOException e) {
            throw new SQLException(e.getMessage());
        }
        return lSb.toString();
    }

    /**
     * @see org.hibernate.type.Type#getReturnedClass()
     */
    @SuppressWarnings("rawtypes")
    public Class getReturnedClass() {
        return String.class;
    }

    /**
     * @see org.hibernate.type.NullableType#set(java.sql.PreparedStatement,
     *      java.lang.Object, int)
     */
    public void set(PreparedStatement pSt, Object pValue, int pIndex)
        throws SQLException {
        boolean lSuccess = false;

        try {
            pSt.setString(pIndex, (String) pValue);
            lSuccess = true;
        }
        catch (SQLException e) {
//            if (staticLogger.isDebugEnabled()) {
//                staticLogger.debug("setString() method failed. We try again using the (slower) setCharacterStream() method");
//            }
            //FIXME us61
        }
        if (!lSuccess) {
            pSt.setCharacterStream(pIndex, new StringReader((String) pValue),
                    ((String) pValue).length());
        }
    }

    /**
     * @see org.hibernate.type.NullableType#sqlType()
     */
    public int sqlType() {
        return Types.CLOB;
    }

    /**
     * @see org.hibernate.type.Type#getName()
     */
    public String getName() {
        return "string";
    }

    /**
     * @see org.hibernate.type.Type#equals(java.lang.Object, java.lang.Object)
     */
    public boolean equals(Object pObjectX, Object pObjectY) {
        return ObjectUtils.equals(pObjectX, pObjectY);
    }

    /**
     * @see org.hibernate.type.NullableType#fromString(java.lang.String)
     */
    public Object fromStringValue(String pXml) {
        return pXml;
    }

    /**
     * @see org.hibernate.type.NullableType#toString(java.lang.Object)
     */
    public String toString(Object pVal) {
        return pVal.toString();
    }
}
