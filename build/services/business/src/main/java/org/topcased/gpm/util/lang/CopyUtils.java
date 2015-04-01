/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil,  Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.util.lang;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import org.apache.log4j.Logger;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.serialization.data.AppletParameter;
import org.topcased.gpm.util.proxy.ImmutableGpmList;
import org.topcased.gpm.util.proxy.ImmutableGpmMap;
import org.topcased.gpm.util.proxy.ImmutableGpmObject;
import org.topcased.gpm.util.proxy.ImmutableGpmSet;
import org.topcased.gpm.util.proxy.ImmutableObjectGenerator;

/**
 * Utility methods used to clone / copy objects.
 * 
 * @author llatil
 * @author tpanuel
 */
public class CopyUtils {
    // The log4j logger object for this class.
//    private static Logger staticLogger =
//            org.apache.log4j.Logger.getLogger(CopyUtils.class);

    /**
     * Returns a deep copy of an object.
     * <p>
     * It should be noted that this method uses the Java serialization feature
     * to implement the deep clone copying, providing the cloning of any
     * serializable classes without any further constraints.
     * <p>
     * However this method is rather slow, so any other clone / copy method
     * should be preferred.
     * 
     * @param pSource
     *            Source object to deep copy (can be null).
     * @param <OBJ>
     *            Class of the object to be deep-cloned (automatically bound).
     * @return A deep clone of the source object (or null if the source object
     *         is null).
     */
    @SuppressWarnings("unchecked")
    public static <OBJ extends Serializable> OBJ deepClone(OBJ pSource) {
        // Test the 'null' source case.
        if (null == pSource) {
            return null;
        }

        if (pSource instanceof ImmutableGpmObject) {
            return getMutableCopy(pSource);
        }

        Object lObj;
        try {
            ObjectOutputStream lOut = null;
            ObjectInputStream lIn = null;
            try {
                // Write the object out to a byte array
                ByteArrayOutputStream lBos = new ByteArrayOutputStream();
                lOut = new ObjectOutputStream(lBos);
                lOut.writeObject(pSource);

                // Make an input stream from the byte array and read
                // a copy of the object back in.
                lIn =
                        new ObjectInputStream(new ByteArrayInputStream(
                                lBos.toByteArray()));
                lObj = lIn.readObject();
            }
            catch (IOException e) {
//                staticLogger.debug("Error in deep copy.", e);
                throw new GDMException("Error in deep copy.", e);
            }
            catch (ClassNotFoundException e) {
//                staticLogger.debug("Error in deep copy.", e);
                throw new GDMException("Error in deep copy.", e);
            }
            finally {
                if (lOut != null) {
                    lOut.flush();
                    lOut.close();
                }
                if (lIn != null) {
                    lIn.close();
                }
            }
        }
        catch (IOException e) {
//            staticLogger.debug("Error in deep copy.", e);
            throw new GDMException("Error in deep copy.", e);
        }
        return (OBJ) lObj;
    }

    /**
     * Standard Java object that are not mutable
     */
    public final static Set<Class<? extends Object>> IMMUTABLE_STANDARD_CLASS =
            new HashSet<Class<? extends Object>>();
    static {
        IMMUTABLE_STANDARD_CLASS.add(String.class);
        IMMUTABLE_STANDARD_CLASS.add(Integer.class);
        IMMUTABLE_STANDARD_CLASS.add(Float.class);
        IMMUTABLE_STANDARD_CLASS.add(Double.class);
        IMMUTABLE_STANDARD_CLASS.add(Boolean.class);
        IMMUTABLE_STANDARD_CLASS.add(Date.class);
        IMMUTABLE_STANDARD_CLASS.add(Timestamp.class);
        IMMUTABLE_STANDARD_CLASS.add(AppletParameter.class);
    }

    /**
     * Compute an immutable version of an object, this object defined getter and
     * setter for all this fields and define an empty constructor
     * 
     * @param <OBJ>
     *            The object type
     * @param pMutableObject
     *            The mutable object to convert
     * @return The immutable object
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <OBJ> OBJ getImmutableCopy(OBJ pMutableObject) {
        if (pMutableObject == null || pMutableObject.getClass().isPrimitive()
                || pMutableObject instanceof Enum
                || IMMUTABLE_STANDARD_CLASS.contains(pMutableObject.getClass())
                || pMutableObject instanceof ImmutableGpmObject) {
            // Null object, primitive, enum or immutable object -> always immutable
            return pMutableObject;
        }
        else if (pMutableObject instanceof List) {
            return (OBJ) new ImmutableGpmList((List) pMutableObject);
        }
        else if (pMutableObject instanceof Set) {
            return (OBJ) new ImmutableGpmSet((Set) pMutableObject);
        }
        else if (pMutableObject instanceof Map) {
            return (OBJ) new ImmutableGpmMap((Map) pMutableObject);
        }
        else if (pMutableObject.getClass().isArray()) {
            return (OBJ) getImmutableArray((OBJ[]) pMutableObject);
        }
        else {
            return ImmutableObjectGenerator.create(pMutableObject);
        }
    }

    /**
     * Compute an immutable version of an array, all the elements are immutable
     * too
     * 
     * @param <E>
     *            The type of the elements of the array
     * @param pMutableArray
     *            The mutable array
     * @return The immutable array
     */
    private static <E> E[] getImmutableArray(E[] pMutableArray) {
        final int lNbElement = pMutableArray.length;
        final E[] lImmutableArray = pMutableArray.clone();

        for (int i = 0; i < lNbElement; i++) {
            // All the the element of the array must be immutable
            lImmutableArray[i] = getImmutableCopy(pMutableArray[i]);
        }
        // FIXME : An array cannot be immutable
        return lImmutableArray;
    }

    /**
     * Compute a mutable version for an immutable object, this object must
     * computed with the getImmutableCopy method (ImmutableGpmObject)
     * 
     * @param <OBJ>
     *            The object type
     * @param pImmutableObject
     *            The immutable object to convert
     * @return The new mutable object
     */
    @SuppressWarnings("unchecked")
    public static <OBJ> OBJ getMutableCopy(OBJ pImmutableObject) {
        if (pImmutableObject == null
                || pImmutableObject.getClass().isPrimitive()
                || pImmutableObject instanceof Enum
                || IMMUTABLE_STANDARD_CLASS.contains(pImmutableObject.getClass())) {
            // Null object, primitive, enum or immutable object -> always immutable
            return pImmutableObject;
        }
        else if (pImmutableObject.getClass().isArray()) {
            return (OBJ) getMutableArray((OBJ[]) pImmutableObject);
        }
        // If the immutable object is not an ImmutableGpmObject, an exception will be thrown
        else if (pImmutableObject instanceof ImmutableGpmObject) {
            return (OBJ) ((ImmutableGpmObject) pImmutableObject).getMutableCopy();
        }
        else {
            // Not supported type
            throw new RuntimeException(pImmutableObject.getClass()
                    + " cannot be mutable");
        }
    }

    /**
     * Compute an mutable version of an array, all the elements are mutable too
     * 
     * @param <E>
     *            The type of the elements of the array
     * @param pImmutableArray
     *            The immutable array
     * @return The mutable array
     */
    private static <E> E[] getMutableArray(E[] pImmutableArray) {
        final int lNbElement = pImmutableArray.length;
        final E[] lMutableArray = pImmutableArray.clone();

        for (int i = 0; i < lNbElement; i++) {
            // All the the element of the array must be mutable
            lMutableArray[i] = getMutableCopy(pImmutableArray[i]);
        }
        return lMutableArray;
    }
}
