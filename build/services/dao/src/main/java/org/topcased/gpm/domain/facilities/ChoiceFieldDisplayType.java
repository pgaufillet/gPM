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
package org.topcased.gpm.domain.facilities;

/**
 * @author Atos
 */
public class ChoiceFieldDisplayType implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public static final ChoiceFieldDisplayType RADIO =
            new ChoiceFieldDisplayType("RADIO");

    /**
     * 
     */
    public static final ChoiceFieldDisplayType COMBO =
            new ChoiceFieldDisplayType("COMBO");

    /**
     * 
     */
    public static final ChoiceFieldDisplayType LIST =
            new ChoiceFieldDisplayType("LIST");

    /**
     * 
     */
    public static final ChoiceFieldDisplayType CHECKBOX =
            new ChoiceFieldDisplayType("CHECKBOX");

    /**
     * 
     */
    public static final ChoiceFieldDisplayType IMAGE =
            new ChoiceFieldDisplayType("IMAGE");

    /**
     * 
     */
    public static final ChoiceFieldDisplayType IMAGE_TEXT =
            new ChoiceFieldDisplayType("IMAGE_TEXT");

    /**
     * 
     */
    public static final ChoiceFieldDisplayType TREE =
            new ChoiceFieldDisplayType("TREE");

    private java.lang.String value;

    private ChoiceFieldDisplayType(java.lang.String pValue) {
        this.value = pValue;
    }

    /**
     * The default constructor allowing super classes to access it.
     */
    protected ChoiceFieldDisplayType() {
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
     * Creates an instance of ChoiceFieldDisplayType from <code>value</code>.
     * 
     * @param pValue
     *            the value to create the ChoiceFieldDisplayType from.
     */
    public static ChoiceFieldDisplayType fromString(java.lang.String pValue) {
        ChoiceFieldDisplayType lTypeValue =
                (ChoiceFieldDisplayType) VALUES.get(pValue);
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
                ((ChoiceFieldDisplayType) pObject).getValue());
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
                || (pObject instanceof ChoiceFieldDisplayType && ((ChoiceFieldDisplayType) pObject).getValue().equals(
                        this.getValue()));
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return this.getValue().hashCode();
    }

    private static final java.util.Map<String, ChoiceFieldDisplayType> VALUES =
            new java.util.HashMap<String, ChoiceFieldDisplayType>(7, 1);

    private static java.util.List<String> staticLiterals =
            new java.util.ArrayList<String>(7);

    private static java.util.List<String> staticNames =
            new java.util.ArrayList<String>(7);

    /**
     * Initializes the values.
     */
    static {
        VALUES.put(RADIO.value, RADIO);
        staticLiterals.add(RADIO.value);
        staticNames.add("RADIO");
        VALUES.put(COMBO.value, COMBO);
        staticLiterals.add(COMBO.value);
        staticNames.add("COMBO");
        VALUES.put(LIST.value, LIST);
        staticLiterals.add(LIST.value);
        staticNames.add("LIST");
        VALUES.put(CHECKBOX.value, CHECKBOX);
        staticLiterals.add(CHECKBOX.value);
        staticNames.add("CHECKBOX");
        VALUES.put(IMAGE.value, IMAGE);
        staticLiterals.add(IMAGE.value);
        staticNames.add("IMAGE");
        VALUES.put(IMAGE_TEXT.value, IMAGE_TEXT);
        staticLiterals.add(IMAGE_TEXT.value);
        staticNames.add("IMAGE_TEXT");
        VALUES.put(TREE.value, TREE);
        staticLiterals.add(TREE.value);
        staticNames.add("TREE");
        staticLiterals = java.util.Collections.unmodifiableList(staticLiterals);
        staticNames = java.util.Collections.unmodifiableList(staticNames);
    }
}