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

import org.topcased.gpm.business.serialization.data.AttachedField;
import org.topcased.gpm.business.serialization.data.ChoiceField;
import org.topcased.gpm.business.serialization.data.MultipleField;
import org.topcased.gpm.business.serialization.data.SimpleField;

/**
 * @author llatil
 */
public enum FieldType {
    SIMPLE_STRING_FIELD("STRING"), SIMPLE_REAL_FIELD("REAL"), SIMPLE_INTEGER_FIELD(
            "INTEGER"), SIMPLE_DATE_FIELD("DATE"), SIMPLE_BOOLEAN_FIELD(
            "BOOLEAN"), CHOICE_FIELD("CHOICE"), ATTACHED_FIELD("ATTACHED"), MULTIPLE_FIELD(
            "MULTIPLE_FIELD");

    /** The type name. */
    private String typeName;

    /**
     * Instantiates a new field type.
     * 
     * @param pName
     *            the name
     */
    FieldType(String pName) {
        typeName = pName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
    public final String toString() {
        return typeName;
    }

    /**
     * Implementation of valueOf.
     * 
     * @see java.lang.Enum#valueOf(Class, String)
     * @param pSerializedField
     *            the serialized field
     * @return the field type of business
     */
    public static org.topcased.gpm.business.fields.FieldType valueOf(
            org.topcased.gpm.business.serialization.data.Field pSerializedField) {
        if (pSerializedField instanceof SimpleField) {
            return Cvt.valueOf((SimpleField) pSerializedField);
        }
        if (pSerializedField instanceof ChoiceField) {
            return CHOICE_FIELD;
        }
        if (pSerializedField instanceof AttachedField) {
            return ATTACHED_FIELD;
        }
        if (pSerializedField instanceof MultipleField) {
            return MULTIPLE_FIELD;
        }
        throw new RuntimeException("Invalid class "
                + pSerializedField.getClass().getName());
    }

    private static final class Cvt {
        static final Map<String, FieldType> TYPE_NAME2FIELD_TYPE =
                new HashMap<String, FieldType>();
        static {
            TYPE_NAME2FIELD_TYPE.put("BOOLEAN", SIMPLE_BOOLEAN_FIELD);
            TYPE_NAME2FIELD_TYPE.put("DATE", SIMPLE_DATE_FIELD);
            TYPE_NAME2FIELD_TYPE.put("INTEGER", SIMPLE_INTEGER_FIELD);
            TYPE_NAME2FIELD_TYPE.put("REAL", SIMPLE_REAL_FIELD);
            TYPE_NAME2FIELD_TYPE.put("STRING", SIMPLE_STRING_FIELD);
        }

        static FieldType valueOf(SimpleField pSimpleField) {
            return TYPE_NAME2FIELD_TYPE.get(pSimpleField.getValueType().toUpperCase());
        }
    }
}
