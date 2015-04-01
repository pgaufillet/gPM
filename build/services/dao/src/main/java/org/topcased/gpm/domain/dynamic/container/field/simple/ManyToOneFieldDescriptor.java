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

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;

import org.hibernate.annotations.ForeignKey;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;
import org.topcased.gpm.domain.dynamic.container.field.FieldDescriptor;
import org.topcased.gpm.domain.fields.Field;

/**
 * Describe dynamic mapping used to represent Many To One relation
 * 
 * @author tpanuel
 */
public abstract class ManyToOneFieldDescriptor extends FieldDescriptor {
    private final Class<?> implementedClass;

    private final boolean cascadeAll;

    /**
     * Describe the data model of a field using a Many To One relation
     * 
     * @param pField
     *            The field using a Many To One
     * @param pInterfaceClass
     *            The class of the interface
     * @param pImplementedClass
     *            The class of the implementation
     * @param pCascadeAll
     *            If the mode cascade ALL is used
     */
    public ManyToOneFieldDescriptor(Field pField, Class<?> pInterfaceClass,
            Class<?> pImplementedClass, boolean pCascadeAll) {
        super(pField, pInterfaceClass);
        implementedClass = pImplementedClass;
        cascadeAll = pCascadeAll;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dynamic.ColumnDescriptor#generateGetter(net.sf.cglib.core.ClassEmitter)
     */
    @Override
    public CodeEmitter generateGetter(ClassEmitter pClassEmitter) {
        final CodeEmitter lGetterEmitter = super.generateGetter(pClassEmitter);

        // Generate ManyToOne annotation
        final AnnotationVisitor lManyToOne =
                lGetterEmitter.visitAnnotation(
                        Type.getDescriptor(ManyToOne.class), true);
        lManyToOne.visit("targetEntity", Type.getType(implementedClass));
        // Cascade all can be disabled
        if (cascadeAll) {
            final AnnotationVisitor lManyToOneCascade =
                    lManyToOne.visitArray("cascade");
            lManyToOneCascade.visitEnum("cascade",
                    Type.getDescriptor(CascadeType.class),
                    CascadeType.ALL.name());
            lManyToOneCascade.visitEnd();
        }
        lManyToOne.visitEnum("fetch", Type.getDescriptor(FetchType.class),
                FetchType.LAZY.name());
        lManyToOne.visitEnd();

        // Generate JoinColumn annotation
        final AnnotationVisitor lJoinColumn =
                lGetterEmitter.visitAnnotation(
                        Type.getDescriptor(JoinColumn.class), true);
        lJoinColumn.visit("name", getColumnName());
        lJoinColumn.visitEnd();

        // Generate ForeignKey annotation
        final AnnotationVisitor lForeignKey =
                lGetterEmitter.visitAnnotation(
                        Type.getDescriptor(ForeignKey.class), true);
        lForeignKey.visit("name", getColumnName());
        lForeignKey.visitEnd();

        return lGetterEmitter;
    }
}