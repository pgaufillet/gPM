/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.fields;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class FieldTypes.
 * 
 * @author ahaugommard
 */
public class FieldTypes {

    /** The Constant SIMPLE_STRING_FIELD. */
    public static final String SIMPLE_STRING_FIELD = "SIMPLE_STRING_FIELD";

    /** The Constant SIMPLE_REAL_FIELD. */
    public static final String SIMPLE_REAL_FIELD = "SIMPLE_REAL_FIELD";

    /** The Constant SIMPLE_INTEGER_FIELD. */
    public static final String SIMPLE_INTEGER_FIELD = "SIMPLE_INTEGER_FIELD";

    /** The Constant SIMPLE_DATE_FIELD. */
    public static final String SIMPLE_DATE_FIELD = "SIMPLE_DATE_FIELD";

    /** The Constant SIMPLE_BOOLEAN_FIELD. */
    public static final String SIMPLE_BOOLEAN_FIELD = "SIMPLE_BOOLEAN_FIELD";

    /** The Constant CHOICE_FIELD. */
    public static final String CHOICE_FIELD = "CHOICE_FIELD";

    /** The Constant ATTACHED_FIELD. */
    public static final String ATTACHED_FIELD = "ATTACHED_FIELD";

    /** The Constant MULTIPLE_FIELD. */
    public static final String MULTIPLE_FIELD = "MULTIPLE_FIELD";

    /** The Constant FIELDTYPE_MAP. */
    private static final Map<FieldType, String> FIELDTYPE_MAP =
            new HashMap<FieldType, String>();
    static {
        FIELDTYPE_MAP.put(FieldType.SIMPLE_STRING_FIELD, SIMPLE_STRING_FIELD);
        FIELDTYPE_MAP.put(FieldType.SIMPLE_REAL_FIELD, SIMPLE_REAL_FIELD);
        FIELDTYPE_MAP.put(FieldType.SIMPLE_INTEGER_FIELD, SIMPLE_INTEGER_FIELD);
        FIELDTYPE_MAP.put(FieldType.SIMPLE_BOOLEAN_FIELD, SIMPLE_BOOLEAN_FIELD);
        FIELDTYPE_MAP.put(FieldType.SIMPLE_DATE_FIELD, SIMPLE_DATE_FIELD);
        FIELDTYPE_MAP.put(FieldType.CHOICE_FIELD, CHOICE_FIELD);
        FIELDTYPE_MAP.put(FieldType.ATTACHED_FIELD, ATTACHED_FIELD);
        FIELDTYPE_MAP.put(FieldType.MULTIPLE_FIELD, MULTIPLE_FIELD);
    }

    /**
     * Value of.
     * 
     * @param pType
     *            the type
     * @return the string value of the type
     */
    public static final String valueOf(FieldType pType) {
        return FIELDTYPE_MAP.get(pType);
    }
}
