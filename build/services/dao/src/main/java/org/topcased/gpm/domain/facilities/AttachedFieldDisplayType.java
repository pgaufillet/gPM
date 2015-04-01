/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/

package org.topcased.gpm.domain.facilities;

/**
 * @author Atos
 */
public class AttachedFieldDisplayType implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public static final AttachedFieldDisplayType IMAGE =
            new AttachedFieldDisplayType("IMAGE");

    private java.lang.String value;

    private AttachedFieldDisplayType(java.lang.String pValue) {
        this.value = pValue;
    }

    /**
     * The default constructor allowing super classes to access it.
     */
    protected AttachedFieldDisplayType() {
    }

    /**
     * Return the value
     * 
     * @return the value
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return value;
    }

    /**
     * Creates an instance of AttachedFieldDisplayType from <code>value</code>.
     * 
     * @param pValue
     *            the value to create the AttachedFieldDisplayType from.
     */
    public static AttachedFieldDisplayType fromString(java.lang.String pValue) {
        AttachedFieldDisplayType lTypeValue =
                (AttachedFieldDisplayType) VALUES.get(pValue);
        if (lTypeValue == null) {
            throw new IllegalArgumentException("invalid value '" + pValue
                    + "', possible values are: " + staticLiterals);
        }
        return lTypeValue;
    }

    /**
     * Gets the underlying value of this type safe enumeration.
     * 
     * @return the underlying value.
     */
    public java.lang.String getValue() {
        return this.value;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object pObject) {
        return (this == pObject) ? 0 : this.getValue().compareTo(
                ((AttachedFieldDisplayType) pObject).getValue());
    }

    /**
     * Returns an unmodifiable list containing the literals that are known by
     * this enumeration.
     * 
     * @return A List containing the actual literals defined by this
     *         enumeration, this list can not be modified.
     */
    @SuppressWarnings("rawtypes")
    public static java.util.List literals() {
        return staticLiterals;
    }

    /**
     * Returns an unmodifiable list containing the names of the literals that
     * are known by this enumeration.
     * 
     * @return A List containing the actual names of the literals defined by
     *         this enumeration, this list can not be modified.
     */
    public static java.util.List<String> names() {
        return staticNames;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object pObject) {
        return (this == pObject)
                || (pObject instanceof AttachedFieldDisplayType && ((AttachedFieldDisplayType) pObject).getValue().equals(
                        this.getValue()));
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return this.getValue().hashCode();
    }

    private static final java.util.Map<String, AttachedFieldDisplayType> VALUES =
            new java.util.HashMap<String, AttachedFieldDisplayType>(1, 1);

    private static java.util.List<String> staticLiterals =
            new java.util.ArrayList<String>(1);

    private static java.util.List<String> staticNames =
            new java.util.ArrayList<String>(1);

    /**
     * Initializes the values.
     */
    static {
        VALUES.put(IMAGE.value, IMAGE);
        staticLiterals.add(IMAGE.value);
        staticNames.add("IMAGE");
        staticLiterals = java.util.Collections.unmodifiableList(staticLiterals);
        staticNames = java.util.Collections.unmodifiableList(staticNames);
    }
}