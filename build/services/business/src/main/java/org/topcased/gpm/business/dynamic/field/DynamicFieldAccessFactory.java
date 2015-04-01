/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel, pierre Hubert TSAAN (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.dynamic.field;

import org.topcased.gpm.business.dynamic.field.multiple.DynamicMultipleFieldAccess;
import org.topcased.gpm.business.dynamic.field.multivalued.DynamicMultivaluedFieldAccess;
import org.topcased.gpm.business.dynamic.field.simple.DynamicAttachedFieldAccess;
import org.topcased.gpm.business.dynamic.field.simple.DynamicBooleanFieldAccess;
import org.topcased.gpm.business.dynamic.field.simple.DynamicChoiceFieldAccess;
import org.topcased.gpm.business.dynamic.field.simple.DynamicDateFieldAccess;
import org.topcased.gpm.business.dynamic.field.simple.DynamicInfiniteStringFieldAccess;
import org.topcased.gpm.business.dynamic.field.simple.DynamicIntegerFieldAccess;
import org.topcased.gpm.business.dynamic.field.simple.DynamicPointerFieldAccess;
import org.topcased.gpm.business.dynamic.field.simple.DynamicRealFieldAccess;
import org.topcased.gpm.business.dynamic.field.simple.DynamicStringFieldAccess;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.serialization.data.AttachedField;
import org.topcased.gpm.business.serialization.data.ChoiceField;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.MultipleField;
import org.topcased.gpm.business.serialization.data.SimpleField;
import org.topcased.gpm.domain.dynamic.util.ColumnSizeUtils;
import org.topcased.gpm.domain.fields.FieldType;

/**
 * Factory for field's access
 * 
 * @author tpanuel
 */
public class DynamicFieldAccessFactory {
    /**
     * Create an access for a field type
     * 
     * @param pField
     *            The field type to access
     * @param pIsForRevision
     *            If it's an access on a revision
     * @return The field access
     */
    public final static AbstractDynamicFieldAccess<? extends Object> getFieldAccessor(
            Field pField, boolean pIsForRevision) {
        if (pField.isMultivalued()) {
            return new DynamicMultivaluedFieldAccess(pField, pIsForRevision);
        }
        else {
            return getSubFieldAccessor(pField, pIsForRevision);
        }
    }

    /**
     * Create an access for a field or a sub field type (in case of multi valued
     * field)
     * 
     * @param pField
     *            The field type to access
     * @param pIsForRevision
     *            If it's an access on a revision
     * @return The field access
     */
    public final static AbstractDynamicFieldAccess<? extends Object> getSubFieldAccessor(
            Field pField, boolean pIsForRevision) {
        if (pField.isPointerField()) {
            return new DynamicPointerFieldAccess(pField);
        }
        else if (pField instanceof SimpleField) {
            final SimpleField lSimpleField = (SimpleField) pField;
            final String lValueType = lSimpleField.getValueType();

            if (lValueType.equalsIgnoreCase(FieldType.STRING.toString())) {
                if (ColumnSizeUtils.getInstance().isInfineStringField(
                        lSimpleField.getSizeAsInt())) {
                    return new DynamicInfiniteStringFieldAccess(lSimpleField);
                }
                else {
                    return new DynamicStringFieldAccess(lSimpleField);
                }
            }
            else if (lValueType.equalsIgnoreCase(FieldType.INTEGER.toString())) {
                return new DynamicIntegerFieldAccess(lSimpleField);
            }
            else if (lValueType.equalsIgnoreCase(FieldType.REAL.toString())) {
                return new DynamicRealFieldAccess(lSimpleField);
            }
            else if (lValueType.equalsIgnoreCase(FieldType.DATE.toString())) {
                return new DynamicDateFieldAccess(lSimpleField);
            }
            else if (lValueType.equalsIgnoreCase(FieldType.BOOLEAN.toString())) {
                return new DynamicBooleanFieldAccess(lSimpleField);
            }
            else if (lValueType.equalsIgnoreCase(FieldType.APPLET.toString())) {
                return new DynamicStringFieldAccess(lSimpleField);
            }
            else {
                throw new RuntimeException("Unknow value's type:"
                        + lValueType.toString());
            }
        }
        else if (pField instanceof MultipleField) {
            return new DynamicMultipleFieldAccess((MultipleField) pField,
                    pIsForRevision);
        }
        else if (pField instanceof AttachedField) {
            return new DynamicAttachedFieldAccess((AttachedField) pField);
        }
        else if (pField instanceof ChoiceField) {
            return new DynamicChoiceFieldAccess((ChoiceField) pField);
        }
        else {
            throw new GDMException("Unknow field's type:" + pField.getClass());
        }
    }
}