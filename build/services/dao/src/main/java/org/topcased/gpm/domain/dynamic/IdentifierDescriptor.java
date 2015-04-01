/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;

import org.hibernate.annotations.GenericGenerator;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;

/**
 * Describe dynamic mapping used to represent an identifier
 * 
 * @author tpanuel
 */
public class IdentifierDescriptor extends ColumnDescriptor {
    /**
     * Describe the data model of an identifier
     * 
     * @param pColumnName
     *            The name of the column on the data base
     * @param pColumnClass
     *            The class of the object representing
     * @param pGetterName
     *            The name of the getter on the column
     * @param pSetterName
     *            The name of the setter on the column
     */
    public IdentifierDescriptor(String pColumnName, Class<?> pColumnClass,
            String pGetterName, String pSetterName) {
        super(pColumnName, pColumnClass, pGetterName, pSetterName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dynamic.ColumnDescriptor#generateGetter(net.sf.cglib.core.ClassEmitter)
     */
    @Override
    public CodeEmitter generateGetter(ClassEmitter pClassEmitter) {
        final CodeEmitter lGetterEmitter = super.generateGetter(pClassEmitter);

        // Generate Id annotation
        lGetterEmitter.visitAnnotation(Type.getDescriptor(Id.class), true);

        // Generate column annotation
        // Generate Type annotation
        final AnnotationVisitor lType =
                lGetterEmitter.visitAnnotation(
                        Type.getDescriptor(org.hibernate.annotations.Type.class),
                        true);
        lType.visit("type", getColumnClass().getName());
        lType.visit("nullable", Boolean.FALSE);
        lType.visitEnd();

        // Generate GeneratedValue annotation
        final AnnotationVisitor lGeneratedValue =
                lGetterEmitter.visitAnnotation(
                        Type.getDescriptor(GeneratedValue.class), true);
        lGeneratedValue.visit("generator", "UUID_GEN");
        lGeneratedValue.visitEnd();

        // Generate GenericGenerator annotation
        final AnnotationVisitor lGenericGenerator =
                lGetterEmitter.visitAnnotation(
                        Type.getDescriptor(GenericGenerator.class), true);
        lGenericGenerator.visit("name", "UUID_GEN");
        lGenericGenerator.visit("strategy",
                "org.topcased.gpm.domain.util.UuidGenerator");
        lGenericGenerator.visitEnd();

        return lGetterEmitter;
    }
}