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
// Generated by: HibernateByteBlobType.vsl in andromda-hibernate-cartridge.
//
package org.andromda.persistence.hibernate.usertypes;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.usertype.UserType;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

/**
 * <p>
 * A hibernate user type which converts a Blob into a byte[] and back again.
 * </p>
 * 
 * @author Atos
 */
public class HibernateByteBlobType implements UserType {
    /**
     * @see org.hibernate.usertype.UserType#sqlTypes()
     */
    public int[] sqlTypes() {
        return new int[] { Types.BLOB };
    }

    /**
     * @see org.hibernate.usertype.UserType#returnedClass()
     */
    public Class<byte[]> returnedClass() {
        return byte[].class;
    }

    /**
     * @see org.hibernate.usertype.UserType#equals(java.lang.Object,
     *      java.lang.Object)
     */
    public boolean equals(Object pObjectX, Object pObjectY) {
        return (pObjectX == pObjectY)
                || (pObjectX != null && pObjectY != null && java.util.Arrays.equals(
                        (byte[]) pObjectX, (byte[]) pObjectY));
    }

    /**
     * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet,
     *      java.lang.String[], java.lang.Object)
     */
    public Object nullSafeGet(ResultSet pResultSet, String[] pNames,
            Object pOwner) throws SQLException {
        Connection lConn =
                getUnderlyingConnection(pResultSet.getStatement().getConnection());
        String lDbProductName = lConn.getMetaData().getDatabaseProductName();

        if (lDbProductName.equalsIgnoreCase("postgresql")) {
            return pgGet(lConn, pResultSet, pNames[0]);
        }

        final Blob lBlob = pResultSet.getBlob(pNames[0]);
        if (lBlob == null) {
        	return null;
        }
        final byte[] lResult = new byte[(int) lBlob.length()];
        final InputStream lStream = new BufferedInputStream(lBlob.getBinaryStream());
        final int lSize = lResult.length;
        int c;
        try {
        	for (int i=0; i<lSize; i++) {
        		c = lStream.read();
        		if (c == -1) {
        			throw new SQLException("Could not download attachment: unexpected end of file.");
        		}
        		lResult[i] = (byte) c;
        	}
            lStream.close();
        } catch (IOException ex) {
        	throw new SQLException("Could not download attachment: " + ex.getMessage());
        }
        return lResult;
    }

    /**
     * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement,
     *      java.lang.Object, int)
     */
    public void nullSafeSet(PreparedStatement pStatement, Object pValue,
            int pIndex) throws SQLException {
        if (pValue == null) {
            pStatement.setNull(pIndex, java.sql.Types.BLOB);
        }
        else {
            Connection lConn = pStatement.getConnection();

            String lDbProductName =
                    lConn.getMetaData().getDatabaseProductName();

            if (lDbProductName.equalsIgnoreCase("postgresql")) {
                // Handle the PostgreSQL case
                pgSet(pStatement, (byte[]) pValue, pIndex);
            }
            else {
                final byte[] lBytes = (byte[]) pValue;
                pStatement.setBinaryStream(pIndex, new ByteArrayInputStream(
                        lBytes), lBytes.length);
            }
        }
    }

    private byte[] pgGet(Connection pConn, ResultSet pResultSet, String pColName)
        throws SQLException {
        Connection lUnderlyingConn = getUnderlyingConnection(pConn);
        if (!(lUnderlyingConn instanceof org.postgresql.PGConnection)) {
            throw new RuntimeException("Cannot handle connection type "
                    + lUnderlyingConn.getClass().getName());
        }
        org.postgresql.PGConnection lPGconn =
                (org.postgresql.PGConnection) lUnderlyingConn;
        LargeObjectManager lObjManager = lPGconn.getLargeObjectAPI();

        long lOid = pResultSet.getLong(pColName);
        if (0 == lOid) {
            return null;
        }

        LargeObject lObj = lObjManager.open(lOid, LargeObjectManager.READ);

        byte[] lContent = new byte[lObj.size()];
        lObj.read(lContent, 0, lObj.size());
        lObj.close();
        return lContent;
    }

    private void pgSet(PreparedStatement pStatement, byte[] pValue, int pIndex)
        throws SQLException {
        Connection lConn = getUnderlyingConnection(pStatement.getConnection());
        if (!(lConn instanceof org.postgresql.PGConnection)) {
            throw new RuntimeException("Cannot handle connection type "
                    + lConn.getClass().getName());
        }

        LargeObjectManager lLargeObjectManager =
                ((org.postgresql.PGConnection) lConn).getLargeObjectAPI();

        long lOid = lLargeObjectManager.createLO(LargeObjectManager.READWRITE);
        LargeObject lLargeObject =
                lLargeObjectManager.open(lOid, LargeObjectManager.WRITE);

        lLargeObject.write(pValue);
        lLargeObject.close();

        pStatement.setLong(pIndex, lOid);
    }

    /**
     * Get the underlying database connection
     * 
     * @param pConn
     *            Connection
     * @return Actual database connection
     */
    private Connection getUnderlyingConnection(Connection pConn) {
        Method lGetDelegateMethod = null;
        try {
            lGetDelegateMethod =
                    pConn.getClass().getMethod("getInnermostDelegate",
                            (Class[]) null);
            if (null != lGetDelegateMethod) {
                pConn =
                        (Connection) lGetDelegateMethod.invoke(pConn,
                                (Object[]) null);
            }
        }
        catch (NoSuchMethodException e) {
            // if the method is not provided in the connection object, simply ignore the pbm
            // (we probably received an 'actual' connection)
        }
        catch (InvocationTargetException e) {
        }
        catch (IllegalAccessException e) {
        }
        catch (ClassCastException e) {
        }
        return pConn;
    }

    /**
     * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
     */
    public Object deepCopy(Object pValue) {
        /*if (pValue == null) {
            return null;
        }

        byte[] lBytes = (byte[]) pValue;
        byte[] lResult = new byte[lBytes.length];
        System.arraycopy(lBytes, 0, lResult, 0, lBytes.length);
        */
        return pValue; // Save memory
    }

    /**
     * @see org.hibernate.usertype.UserType#isMutable()
     * @return always true
     */
    public boolean isMutable() {
        return true;
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
     * @see org.hibernate.usertype.UserType#assemble(Object pValue)
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
