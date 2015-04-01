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

import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.TypeUtils;

import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;

/**
 * Interface used to describe a column of a dynamic table
 * 
 * @author tpanuel
 */
public class ColumnDescriptor {
    private final String columnName;

    private final Class<?> columnClass;

    private final String getterName;

    private final String setterName;

    /**
     * Describe the data model of a column
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
    public ColumnDescriptor(String pColumnName, Class<?> pColumnClass,
            String pGetterName, String pSetterName) {
        columnName = pColumnName;
        columnClass = pColumnClass;
        getterName = pGetterName;
        setterName = pSetterName;
    }

    /**
     * Generate the the column : field, getter and setter
     * 
     * @param pClassEmitter
     *            The class emitter
     * @return Visitor on the field
     */
    public FieldVisitor generateColumn(ClassEmitter pClassEmitter) {
        final FieldVisitor lFieldVisitor =
                pClassEmitter.visitField(Constants.ACC_PRIVATE,
                        getColumnName(columnName),
                        Type.getDescriptor(columnClass), null, null);

        if (getterName != null) {
            generateGetter(pClassEmitter);
        }
        if (setterName != null) {
            generateSetter(pClassEmitter);
        }

        return lFieldVisitor;
    }

    /**
     * Generate the getter on the column
     * 
     * @param pClassEmitter
     *            The class emitter
     * @return Visitor on the getter
     */
    public CodeEmitter generateGetter(ClassEmitter pClassEmitter) {
        final CodeEmitter lGetterEmitter =
                pClassEmitter.begin_method(Constants.ACC_PUBLIC,
                        TypeUtils.parseSignature(columnClass.getName() + ' '
                                + getterName + "()"), null);

        lGetterEmitter.load_this();
        lGetterEmitter.visitFieldInsn(Constants.GETFIELD,
                pClassEmitter.getClassType().getInternalName(),
                getColumnName(columnName), Type.getDescriptor(columnClass));
        lGetterEmitter.dup();
        lGetterEmitter.checkcast(Type.getType(columnClass));
        lGetterEmitter.return_value();
        lGetterEmitter.end_method();

        return lGetterEmitter;
    }

    /**
     * Generate the setter on the column
     * 
     * @param pClassEmitter
     *            The class emitter
     * @return Visitor on the setter
     */
    public CodeEmitter generateSetter(ClassEmitter pClassEmitter) {
        final CodeEmitter lSetterEmitter =
                pClassEmitter.begin_method(Constants.ACC_PUBLIC,
                        TypeUtils.parseSignature("void " + setterName + '('
                                + columnClass.getName() + ')'), null);

        lSetterEmitter.load_this();
        lSetterEmitter.load_arg(0);
        lSetterEmitter.visitFieldInsn(Constants.PUTFIELD,
                pClassEmitter.getClassType().getInternalName(),
                getColumnName(columnName), Type.getDescriptor(columnClass));
        // For having an empty stack
        lSetterEmitter.return_value();
        lSetterEmitter.end_method();

        return lSetterEmitter;
    }

    private String getColumnName(String pColumnName) {
        String lFirstChar = pColumnName.substring(0, 1).toLowerCase();
        return (lFirstChar + pColumnName.substring(1));
    }

    /**
     * Getter on the column name
     * 
     * @return The column name
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Getter on the column class
     * 
     * @return The column class
     */
    public Class<?> getColumnClass() {
        return columnClass;
    }

    /**
     * Getter on the getter name
     * 
     * @return The getter name
     */
    public String getGetterName() {
        return getterName;
    }

    /**
     * Getter on the setter name
     * 
     * @return The setter name
     */
    public String getSetterName() {
        return setterName;
    }
}