/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic.container.field.simple;

import javax.persistence.Column;

import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;
import org.topcased.gpm.domain.fields.SimpleField;

/**
 * Describe dynamic mapping used to represent a String Simple Field
 * 
 * @author tpanuel
 */
public class StringFieldDescriptor extends SimpleFieldDescriptor {
    private final int maxSize;

    /**
     * Describe the data model of an string simple field
     * 
     * @param pField
     *            The simple field
     */
    public StringFieldDescriptor(SimpleField pField) {
        super(pField, String.class);
        maxSize = pField.getMaxSize();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dynamic.ColumnDescriptor#generateGetter(net.sf.cglib.core.ClassEmitter)
     */
    @Override
    public CodeEmitter generateGetter(ClassEmitter pClassEmitter) {
        final CodeEmitter lGetterEmitter = super.generateGetter(pClassEmitter);

        // Generate Column annotation
        final AnnotationVisitor lColumn =
                lGetterEmitter.visitAnnotation(
                        Type.getDescriptor(Column.class), true);
        lColumn.visit("name", getColumnName());
        lColumn.visit("length", maxSize);
        lColumn.visitEnd();

        return lGetterEmitter;
    }
}
