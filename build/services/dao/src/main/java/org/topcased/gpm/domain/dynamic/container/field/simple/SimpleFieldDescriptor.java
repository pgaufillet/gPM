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

import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;
import org.topcased.gpm.domain.dynamic.container.field.FieldDescriptor;
import org.topcased.gpm.domain.fields.SimpleField;

/**
 * Describe dynamic mapping used to represent a Simple Field
 * 
 * @author tpanuel
 */
public abstract class SimpleFieldDescriptor extends FieldDescriptor {
    /**
     * Describe the data model of a simple field
     * 
     * @param pField
     *            The simple field
     * @param pClass
     *            The class
     */
    public SimpleFieldDescriptor(SimpleField pField, Class<?> pClass) {
        super(pField, pClass);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dynamic.ColumnDescriptor#generateGetter(net.sf.cglib.core.ClassEmitter)
     */
    @Override
    public CodeEmitter generateGetter(ClassEmitter pClassEmitter) {
        final CodeEmitter lGetterEmitter = super.generateGetter(pClassEmitter);

        // Generate Type annotation
        final AnnotationVisitor lType =
                lGetterEmitter.visitAnnotation(
                        Type.getDescriptor(org.hibernate.annotations.Type.class),
                        true);
        lType.visit("type", getColumnClass().getName());
        lType.visitEnd();

        return lGetterEmitter;
    }
}
