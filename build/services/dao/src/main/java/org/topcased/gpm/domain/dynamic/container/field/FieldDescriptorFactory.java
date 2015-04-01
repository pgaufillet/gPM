/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic.container.field;

import org.topcased.gpm.domain.dynamic.container.field.multiple.MultipleFieldDescriptor;
import org.topcased.gpm.domain.dynamic.container.field.multivalued.MultivaluedFieldDescriptor;
import org.topcased.gpm.domain.dynamic.container.field.simple.AttachedFieldDescriptor;
import org.topcased.gpm.domain.dynamic.container.field.simple.BooleanFieldDescriptor;
import org.topcased.gpm.domain.dynamic.container.field.simple.ChoiceFieldDescriptor;
import org.topcased.gpm.domain.dynamic.container.field.simple.DateFieldDescriptor;
import org.topcased.gpm.domain.dynamic.container.field.simple.InfiniteStringFieldDescriptor;
import org.topcased.gpm.domain.dynamic.container.field.simple.IntegerFieldDescriptor;
import org.topcased.gpm.domain.dynamic.container.field.simple.PointerFieldDescriptor;
import org.topcased.gpm.domain.dynamic.container.field.simple.RealFieldDescriptor;
import org.topcased.gpm.domain.dynamic.container.field.simple.StringFieldDescriptor;
import org.topcased.gpm.domain.dynamic.util.ColumnSizeUtils;
import org.topcased.gpm.domain.fields.AttachedField;
import org.topcased.gpm.domain.fields.ChoiceField;
import org.topcased.gpm.domain.fields.Field;
import org.topcased.gpm.domain.fields.FieldType;
import org.topcased.gpm.domain.fields.MultipleField;
import org.topcased.gpm.domain.fields.SimpleField;

/**
 * Factory used to finf the descriptor associated to a specific field
 * 
 * @author tpanuel
 * @author phtsaan
 */
public class FieldDescriptorFactory {
    /**
     * Create a descriptor for a field type
     * 
     * @param pField
     *            The field type to describe
     * @param pIsForRevision
     *            If it's a descriptor for a revision
     * @return The descriptor
     */
    public final static FieldDescriptor getFieldDescriptor(Field pField,
            boolean pIsForRevision) {
        if (pField.isMultiValued()) {
            return new MultivaluedFieldDescriptor(pField, pIsForRevision);
        }
        else {
            return getSubFieldDescriptor(pField, pIsForRevision);
        }
    }

    /**
     * Create an descriptor for a field or a sub field type (in case of multi
     * valued field)
     * 
     * @param pField
     *            The field type to describe
     * @param pIsForRevision
     *            If it's a descriptor for a revision
     * @return The descriptor
     */
    public final static FieldDescriptor getSubFieldDescriptor(Field pField,
            boolean pIsForRevision) {
        if (pField.isPointerField()) {
            return new PointerFieldDescriptor(pField);
        }
        if (pField instanceof SimpleField) {
            final SimpleField lSimpleField = (SimpleField) pField;
            final FieldType lFieldType = lSimpleField.getType();

            if (lFieldType.equals(FieldType.STRING)) {
                if (ColumnSizeUtils.getInstance().isInfineStringField(
                        lSimpleField.getMaxSize())) {
                    return new InfiniteStringFieldDescriptor(lSimpleField);
                }
                else {
                    return new StringFieldDescriptor(lSimpleField);
                }
            }
            else if (lFieldType.equals(FieldType.INTEGER)) {
                return new IntegerFieldDescriptor(lSimpleField);
            }
            else if (lFieldType.equals(FieldType.REAL)) {
                return new RealFieldDescriptor(lSimpleField);
            }
            else if (lFieldType.equals(FieldType.DATE)) {
                return new DateFieldDescriptor(lSimpleField);
            }
            else if (lFieldType.equals(FieldType.BOOLEAN)) {
                return new BooleanFieldDescriptor(lSimpleField);
            }
            else if (lFieldType.equals(FieldType.APPLET)) {
                if (ColumnSizeUtils.getInstance().isInfineStringField(
                        lSimpleField.getMaxSize())) {
                    return new InfiniteStringFieldDescriptor(lSimpleField);
                }
                else {
                    return new StringFieldDescriptor(lSimpleField);
                }
            }
            else {
                throw new RuntimeException("Unknow simple field value's type:"
                        + lFieldType.toString());
            }
        }
        else if (pField instanceof MultipleField) {
            return new MultipleFieldDescriptor((MultipleField) pField,
                    pIsForRevision);
        }
        else if (pField instanceof AttachedField) {
            return new AttachedFieldDescriptor((AttachedField) pField);
        }
        else if (pField instanceof ChoiceField) {
            return new ChoiceFieldDescriptor((ChoiceField) pField);
        }
        else {
            throw new RuntimeException("Unknow field's type:"
                    + pField.getClass());
        }
    }
}