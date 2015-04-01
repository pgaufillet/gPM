/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Atos Origin
 ******************************************************************/
package org.topcased.gpm.business.values.field.virtual;

/**
 * The Enum VirtualFieldType.
 * 
 * @author ahaugommard
 */
public enum VirtualFieldType {

    /** The product name. */
    $PRODUCT_NAME("$PRODUCT_NAME"),

    /** The product hierarchy. */
    $PRODUCT_HIERARCHY("$PRODUCT_HIERARCHY"),

    /** The product description. */
    $PRODUCT_DESCRIPTION("$PRODUCT_DESCRIPTION"),

    /** The product state. */
    $SHEET_STATE("$SHEET_STATE"),

    /** The sheet type. */
    $SHEET_TYPE("$SHEET_TYPE"),

    /** The sheet reference. */
    $SHEET_REFERENCE("$SHEET_REFERENCE"),

    /** The sheet field. */
    $PRODUCT_FIELD("$PRODUCT_FIELD"),

    /** The link field. */
    $LINK_FIELD("$LINK_FIELD"), $LINKED_SHEET_FIELD("$LINKED_SHEET_FIELD"), $ORIGIN_SHEET_REF(
            "$ORIGIN_SHEET_REF"), $ORIGIN_PRODUCT("$ORIGIN_PRODUCT"), $DEST_SHEET_REF(
            "$DEST_SHEET_REF"), $DEST_PRODUCT("$DEST_PRODUCT");

    /** The value. */
    private final String value;

    /**
     * Constructs a new virtual field type.
     * 
     * @param pValue
     *            the value
     */
    private VirtualFieldType(String pValue) {
        value = pValue;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * If enum exist
     * 
     * @param pName
     *            the name
     * @return if value exist
     */
    public static boolean isExist(String pName) {
        try {
            valueOf(pName);
        }
        catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}