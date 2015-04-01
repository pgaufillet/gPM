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

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.TypeUtils;

import org.apache.commons.lang.StringUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;
import org.topcased.gpm.domain.PersistentObject;
import org.topcased.gpm.domain.dynamic.container.field.FieldDescriptor;
import org.topcased.gpm.domain.dynamic.container.field.multiple.MultipleFieldDescriptor;
import org.topcased.gpm.domain.dynamic.container.field.multivalued.DynamicMultivaluedFieldGeneratorFactory;
import org.topcased.gpm.domain.dynamic.container.field.multivalued.DynamicMultivaluedFieldRevisionGeneratorFactory;
import org.topcased.gpm.domain.dynamic.container.field.multivalued.MultivaluedFieldDescriptor;
import org.topcased.gpm.domain.dynamic.util.DynamicObjectNamesUtils;
import org.topcased.gpm.domain.dynamic.util.GpmClassEmitter;

/**
 * Generator of dynamic class, the class is an Hibernate Entity associated to a
 * table and each field is described by a ColumnDescriptor object
 * 
 * @author tpanuel
 * @param <DYNAMIC_OBJECT>
 *            The super class of the generated objects
 */
public abstract class DynamicObjectGenerator<DYNAMIC_OBJECT extends PersistentObject>
        extends AbstractClassGenerator {
    private List<ColumnDescriptor> columns;

    private Class<DYNAMIC_OBJECT> superClass;

    private Class<?> generatedClass;

    private final String dynamicObjectId;

    private final boolean isForRevision;

    private final DynamicObjectNamesUtils dynamicObjectNamesUtils;

    /**
     * Construct a dynamic class generator
     * 
     * @param pSource
     *            The source
     * @param pSuperClass
     *            The super class of the generated objects
     * @param pDynamicObjectId
     *            The id of the dynamic object
     * @param pForRevision
     *            If it's a table for a revision
     */
    public DynamicObjectGenerator(Source pSource,
            Class<DYNAMIC_OBJECT> pSuperClass, String pDynamicObjectId,
            boolean pForRevision) {
        super(pSource);
        superClass = pSuperClass;
        columns = new LinkedList<ColumnDescriptor>();
        generatedClass = null;
        dynamicObjectId = pDynamicObjectId;
        isForRevision = pForRevision;
        dynamicObjectNamesUtils = DynamicObjectNamesUtils.getInstance();
    }

    /**
     * Create an instance of the dynamic class
     * 
     * @return An instance of the dynamic class
     */
    @SuppressWarnings("unchecked")
    public DYNAMIC_OBJECT create() {
        return (DYNAMIC_OBJECT) super.create(getTableName());
    }

    /**
     * Get the table name associated to the dynamic entity
     * 
     * @return The table name
     */
    protected abstract String getTableName();

    /**
     * {@inheritDoc}
     * 
     * @see net.sf.cglib.core.ClassGenerator#generateClass(org.objectweb.asm.ClassVisitor)
     */
    public void generateClass(ClassVisitor pVisitor) {
        final ClassEmitter lMappedClassEmitter = new GpmClassEmitter(pVisitor);

        // Generate class declaration
        lMappedClassEmitter.begin_class(Constants.V1_5, Constants.ACC_PUBLIC,
                getClassName(), Type.getType(superClass), null,
                Constants.SOURCE_FILE);

        // Generate Entity annotation
        lMappedClassEmitter.visitAnnotation(
                Type.getDescriptor(javax.persistence.Entity.class), true).visitEnd();

        // Generate Table annotation
        final AnnotationVisitor lTable =
                lMappedClassEmitter.visitAnnotation(
                        Type.getDescriptor(javax.persistence.Table.class), true);
        lTable.visit("name", getTableName());
        lTable.visitEnd();

        // Generate constructor
        final CodeEmitter lConstructorEmitter =
                lMappedClassEmitter.begin_method(Constants.ACC_PUBLIC,
                        TypeUtils.parseConstructor(StringUtils.EMPTY), null);
        lConstructorEmitter.load_this();
        lConstructorEmitter.super_invoke_constructor();
        lConstructorEmitter.return_value();
        lConstructorEmitter.end_method();

        // Generate serialVersionUID
        lMappedClassEmitter.declare_field(Constants.PRIVATE_FINAL_STATIC,
                Constants.SUID_FIELD_NAME, Type.LONG_TYPE,
                UUID.randomUUID().getMostSignificantBits());

        // Generate fields
        for (ColumnDescriptor lColumn : columns) {
            lColumn.generateColumn(lMappedClassEmitter);
        }

        lMappedClassEmitter.end_class();
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.sf.cglib.core.AbstractClassGenerator#firstInstance(java.lang.Class)
     */
    @SuppressWarnings("rawtypes")
    protected Object firstInstance(Class pType) {
        return ReflectUtils.newInstance(pType, null, null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.sf.cglib.core.AbstractClassGenerator#getDefaultClassLoader()
     */
    protected ClassLoader getDefaultClassLoader() {
        return superClass.getClassLoader();
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.sf.cglib.core.AbstractClassGenerator#nextInstance(java.lang.Object)
     */
    protected Object nextInstance(Object pInstance) {
        return firstInstance(pInstance.getClass());
    }

    /**
     * Add a column at the entity definition
     * 
     * @param pColumn
     *            The column to add
     */
    protected void addColumn(ColumnDescriptor pColumn) {
        columns.add(pColumn);
    }

    /**
     * Get all column descriptors
     * 
     * @return All the column descriptors
     */
    protected List<ColumnDescriptor> getColumns() {
        return columns;
    }

    /**
     * Getter on the generated class
     * 
     * @return The generated class
     */
    public Class<?> getGeneratedClass() {
        // The first time create an object for get its class
        if (generatedClass == null) {
            generatedClass = create().getClass();
        }

        return generatedClass;
    }

    /**
     * Getter on the id of the dynamic object
     * 
     * @return The id of the dynamic object
     */
    public String getDynamicObjectId() {
        return dynamicObjectId;
    }

    /**
     * Clean the generator
     */
    public void cleanGenerator() {
        // Clean name of the table and the columns
        if (isForRevision) {
            dynamicObjectNamesUtils.cleanDynamicRevisionNames(getDynamicObjectId());
        }
        else {
            dynamicObjectNamesUtils.cleanDynamicNames(getDynamicObjectId());
        }
        // Clean generator associated to sub-fields
        for (ColumnDescriptor lColumnDescriptor : getColumns()) {
            cleanSubGenerator(lColumnDescriptor);
        }
        // Remove generated class of the Class register
        DynamicClassRegister.removeDynamicClass(getGeneratedClass());
    }

    private void cleanSubGenerator(ColumnDescriptor pColumnDescriptor) {
        // Clean tables for multi valued fields
        if (pColumnDescriptor instanceof MultivaluedFieldDescriptor) {
            final String lFieldId =
                    ((FieldDescriptor) pColumnDescriptor).getFieldId();

            if (isForRevision) {
                DynamicMultivaluedFieldRevisionGeneratorFactory.getInstance().cleanGenerator(
                        lFieldId);
            }
            else {
                DynamicMultivaluedFieldGeneratorFactory.getInstance().cleanGenerator(
                        lFieldId);
            }
        }
        // Clean table for multi valued subfields
        if (pColumnDescriptor instanceof MultipleFieldDescriptor) {
            for (ColumnDescriptor lSubColumnDescriptor : ((MultipleFieldDescriptor) pColumnDescriptor).getColumnDescriptors()) {
                if (lSubColumnDescriptor instanceof MultivaluedFieldDescriptor) {
                    cleanSubGenerator(lSubColumnDescriptor);
                }
            }
        }
        // For other field, no table has been generated
    }
}