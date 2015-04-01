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
// Generated by: Enumeration.vsl in andromda-java-cartridge.
//
package org.topcased.gpm.domain.search;

/**
 * @author Atos
 */
public class FilterUsage implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public static final FilterUsage TREE_VIEW = new FilterUsage("TREE_VIEW");

    /**
     * 
     */
    public static final FilterUsage TABLE_VIEW = new FilterUsage("TABLE_VIEW");

    /**
     * 
     */
    public static final FilterUsage BOTH_VIEWS = new FilterUsage("BOTH_VIEWS");

    private java.lang.String value;

    private FilterUsage(java.lang.String pValue) {
        this.value = pValue;
    }

    /**
     * The default constructor allowing super classes to access it.
     */
    protected FilterUsage() {
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
     * Creates an instance of FilterUsage from <code>value</code>.
     * 
     * @param pValue
     *            the value to create the FilterUsage from.
     */
    public static FilterUsage fromString(java.lang.String pValue) {
        FilterUsage lTypeValue = (FilterUsage) VALUES.get(pValue);
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
                ((FilterUsage) pObject).getValue());
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
    @SuppressWarnings("rawtypes")
    public static java.util.List names() {
        return staticNames;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object pObject) {
        return (this == pObject)
                || (pObject instanceof FilterUsage && ((FilterUsage) pObject).getValue().equals(
                        this.getValue()));
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return this.getValue().hashCode();
    }

    private static final java.util.Map<String, FilterUsage> VALUES =
            new java.util.HashMap<String, FilterUsage>(3, 1);

    private static java.util.List<String> staticLiterals =
            new java.util.ArrayList<String>(3);

    private static java.util.List<String> staticNames =
            new java.util.ArrayList<String>(3);

    /**
     * Initializes the values.
     */
    static {
        VALUES.put(TREE_VIEW.value, TREE_VIEW);
        staticLiterals.add(TREE_VIEW.value);
        staticNames.add("TREE_VIEW");
        VALUES.put(TABLE_VIEW.value, TABLE_VIEW);
        staticLiterals.add(TABLE_VIEW.value);
        staticNames.add("TABLE_VIEW");
        VALUES.put(BOTH_VIEWS.value, BOTH_VIEWS);
        staticLiterals.add(BOTH_VIEWS.value);
        staticNames.add("BOTH_VIEWS");
        staticLiterals = java.util.Collections.unmodifiableList(staticLiterals);
        staticNames = java.util.Collections.unmodifiableList(staticNames);
    }
}