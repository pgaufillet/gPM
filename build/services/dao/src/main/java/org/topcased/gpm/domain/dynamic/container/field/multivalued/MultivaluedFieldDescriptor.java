/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic.container.field.multivalued;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;

import org.hibernate.annotations.IndexColumn;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;
import org.topcased.gpm.domain.dynamic.container.field.FieldDescriptor;
import org.topcased.gpm.domain.fields.Field;

/**
 * Describe dynamic mapping used to represent a multi valued field
 * 
 * @author tpanuel
 */
public class MultivaluedFieldDescriptor extends FieldDescriptor {
    private final Class<?> lMultivaluedClass;

    /**
     * Describe the data model of a multi valued field
     * 
     * @param pMultiValuedField
     *            The multi valued field
     * @param pIsForRevision
     *            If it's a descriptor for a revision
     */
    public MultivaluedFieldDescriptor(Field pMultiValuedField,
            boolean pIsForRevision) {
        super(pMultiValuedField, List.class);
        if (pIsForRevision) {
            lMultivaluedClass =
                    DynamicMultivaluedFieldRevisionGeneratorFactory.getInstance().initDynamicObjectGenerator(
                            pMultiValuedField.getId(), pMultiValuedField).getGeneratedClass();
        }
        else {
            lMultivaluedClass =
                    DynamicMultivaluedFieldGeneratorFactory.getInstance().initDynamicObjectGenerator(
                            pMultiValuedField.getId(), pMultiValuedField).getGeneratedClass();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dynamic.ColumnDescriptor#generateGetter(net.sf.cglib.core.ClassEmitter)
     */
    @Override
    public CodeEmitter generateGetter(ClassEmitter pClassEmitter) {
        final CodeEmitter lGetterEmitter = super.generateGetter(pClassEmitter);

        // Generate OneToMany annotation
        final AnnotationVisitor lOneToMany =
                lGetterEmitter.visitAnnotation(
                        Type.getDescriptor(OneToMany.class), true);
        lOneToMany.visit("targetEntity", Type.getType(lMultivaluedClass));
        final AnnotationVisitor lOneToManyCascade =
                lOneToMany.visitArray("cascade");
        lOneToManyCascade.visitEnum("cascade",
                Type.getDescriptor(CascadeType.class), CascadeType.ALL.name());
        lOneToManyCascade.visitEnd();
        lOneToMany.visitEnd();

        // Generate JoinColumn annotation
        final AnnotationVisitor lJoinColumn =
                lGetterEmitter.visitAnnotation(
                        Type.getDescriptor(JoinColumn.class), true);
        lJoinColumn.visit("name",
                DynamicMultivaluedFieldGenerator.COLUMN_PARENT_ID_INFO);
        lJoinColumn.visitEnd();

        // Generate IndexColumn annotation
        final AnnotationVisitor lIndexColumn =
                lGetterEmitter.visitAnnotation(
                        Type.getDescriptor(IndexColumn.class), true);
        lIndexColumn.visit("name",
                DynamicMultivaluedFieldGenerator.COLUMN_NUM_LINE_INFO);
        lIndexColumn.visitEnd();

        return lGetterEmitter;
    }
}