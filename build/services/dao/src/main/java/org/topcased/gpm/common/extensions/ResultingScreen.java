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
package org.topcased.gpm.common.extensions;

/**
 * @author Atos
 */
public class ResultingScreen implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public static final ResultingScreen SHEET_VISUALIZATION =
            new ResultingScreen("SHEET_VISUALIZATION");

    /**
     * 
     */
    public static final ResultingScreen SHEET_EDITION =
            new ResultingScreen("SHEET_EDITION");

    /**
     * 
     */
    public static final ResultingScreen SHEETS_VISUALIZATION =
            new ResultingScreen("SHEETS_VISUALIZATION");

    /**
     * 
     */
    public static final ResultingScreen SHEETS_EDITION =
            new ResultingScreen("SHEETS_EDITION");

    /**
     * 
     */
    public static final ResultingScreen FILTER_RESULT =
            new ResultingScreen("FILTER_RESULT");

    /**
     * 
     */
    public static final ResultingScreen MESSAGE =
            new ResultingScreen("MESSAGE");

    /**
     * 
     */
    public static final ResultingScreen ERROR = new ResultingScreen("ERROR");

    /**
     * 
     */
    public static final ResultingScreen SHEET_CREATION =
            new ResultingScreen("SHEET_CREATION");

    /**
     * 
     */
    public static final ResultingScreen FILE_OPENING =
            new ResultingScreen("FILE_OPENING");

    private java.lang.String value;

    private ResultingScreen(java.lang.String pValue) {
        this.value = pValue;
    }

    /**
     * The default constructor allowing super classes to access it.
     */
    protected ResultingScreen() {
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
     * Creates an instance of ResultingScreen from <code>value</code>.
     * 
     * @param pValue
     *            the value to create the ResultingScreen from.
     */
    public static ResultingScreen fromString(java.lang.String pValue) {
        ResultingScreen lTypeValue = (ResultingScreen) VALUES.get(pValue);
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
                ((ResultingScreen) pObject).getValue());
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
                || (pObject instanceof ResultingScreen && ((ResultingScreen) pObject).getValue().equals(
                        this.getValue()));
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return this.getValue().hashCode();
    }

    private static final java.util.Map<String, ResultingScreen> VALUES =
            new java.util.HashMap<String, ResultingScreen>(9, 1);

    private static java.util.List<String> staticLiterals =
            new java.util.ArrayList<String>(9);

    private static java.util.List<String> staticNames =
            new java.util.ArrayList<String>(9);

    /**
     * Initializes the values.
     */
    static {
        VALUES.put(SHEET_VISUALIZATION.value, SHEET_VISUALIZATION);
        staticLiterals.add(SHEET_VISUALIZATION.value);
        staticNames.add("SHEET_VISUALIZATION");
        VALUES.put(SHEET_EDITION.value, SHEET_EDITION);
        staticLiterals.add(SHEET_EDITION.value);
        staticNames.add("SHEET_EDITION");
        VALUES.put(SHEETS_VISUALIZATION.value, SHEETS_VISUALIZATION);
        staticLiterals.add(SHEETS_VISUALIZATION.value);
        staticNames.add("SHEETS_VISUALIZATION");
        VALUES.put(SHEETS_EDITION.value, SHEETS_EDITION);
        staticLiterals.add(SHEETS_EDITION.value);
        staticNames.add("SHEETS_EDITION");
        VALUES.put(FILTER_RESULT.value, FILTER_RESULT);
        staticLiterals.add(FILTER_RESULT.value);
        staticNames.add("FILTER_RESULT");
        VALUES.put(MESSAGE.value, MESSAGE);
        staticLiterals.add(MESSAGE.value);
        staticNames.add("MESSAGE");
        VALUES.put(ERROR.value, ERROR);
        staticLiterals.add(ERROR.value);
        staticNames.add("ERROR");
        VALUES.put(SHEET_CREATION.value, SHEET_CREATION);
        staticLiterals.add(SHEET_CREATION.value);
        staticNames.add("SHEET_CREATION");
        VALUES.put(FILE_OPENING.value, FILE_OPENING);
        staticLiterals.add(FILE_OPENING.value);
        staticNames.add("FILE_OPENING");
        staticLiterals = java.util.Collections.unmodifiableList(staticLiterals);
        staticNames = java.util.Collections.unmodifiableList(staticNames);
    }
}