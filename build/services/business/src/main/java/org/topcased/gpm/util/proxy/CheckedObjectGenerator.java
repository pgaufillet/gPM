/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.util.proxy;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.MultipleField;

/**
 * Construct a proxy 'Checked' on a Field, a CacheableFieldsContainer or
 * CacheableValuesContainer. This proxy is used like a mask allowing the
 * re-definition of the access rights without impact the original object
 * 
 * @author tpanuel
 */
final public class CheckedObjectGenerator {
    private static final Signature CSTRUCT_OBJECT =
            TypeUtils.parseConstructor("Object");

    private static final Class<?>[] OBJECT_CLASSES = { Object.class };

    private static final Class<?>[] NO_CLASSES = {};

    private static final String FIELD_NAME = "notCheckedBean";

    private static final Set<String> ACESS_RIGHTS_FIELD_METHODS =
            new HashSet<String>();

    private static final Set<String> ACESS_RIGHTS_MULTIPLE_FIELD_METHODS =
            new HashSet<String>();

    private static final Set<String> ACESS_RIGHTS_FIELDS_CONTAINER_METHODS =
            new HashSet<String>();

    private static final Set<String> ACESS_RIGHTS_VALUES_CONTAINER_METHODS =
            new HashSet<String>();

    private static final MethodInfo GET_MUTABLE_COPY;

    static {
        try {
            GET_MUTABLE_COPY =

                    ReflectUtils.getMethodInfo(ImmutableGpmObject.class.getMethod(
                            "getMutableCopy", NO_CLASSES));
        }
        catch (SecurityException e) {
            throw new RuntimeException(e);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        // Class Field
        // Field.attributes
        ACESS_RIGHTS_FIELD_METHODS.add("getAttributes");
        ACESS_RIGHTS_FIELD_METHODS.add("setAttributes");
        // Field.mandatory
        ACESS_RIGHTS_FIELD_METHODS.add("getMandatory");
        ACESS_RIGHTS_FIELD_METHODS.add("isMandatory");
        ACESS_RIGHTS_FIELD_METHODS.add("setMandatory");
        // Field.updatable
        ACESS_RIGHTS_FIELD_METHODS.add("getUpdatable");
        ACESS_RIGHTS_FIELD_METHODS.add("isUpdatable");
        ACESS_RIGHTS_FIELD_METHODS.add("setUpdatable");
        // Field.confidential
        ACESS_RIGHTS_FIELD_METHODS.add("getConfidential");
        ACESS_RIGHTS_FIELD_METHODS.add("isConfidential");
        ACESS_RIGHTS_FIELD_METHODS.add("setConfidential");
        // Field.exportable
        ACESS_RIGHTS_FIELD_METHODS.add("getExportable");
        ACESS_RIGHTS_FIELD_METHODS.add("isExportable");
        ACESS_RIGHTS_FIELD_METHODS.add("setExportable");
        // Class Multiple Field extend Field
        // MultipleField.fields
        ACESS_RIGHTS_MULTIPLE_FIELD_METHODS.add("getFields");
        ACESS_RIGHTS_MULTIPLE_FIELD_METHODS.add("setFields");
        // Class CacheableFieldsContainer
        // CacheableFieldsContainer.fieldsIdMap
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("getFieldFromId");
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("getFieldsIdMap");
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("setFieldsIdMap");
        // CacheableFieldsContainer.fieldsKeyMap
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("addField");
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("getAllFields");
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("getFieldFromLabel");
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("getFieldsKeyMap");
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("setFieldsKeyMap");
        // CacheableFieldsContainer.topLevelFields
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("addTopLevelField");
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("getFields");
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("getTopLevelFields");
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("setTopLevelFields");
        // CacheableFieldsContainer.attributesMap
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("addAttributes");
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("getAllAttributeNames");
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("getAllAttributes");
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("getAttributesMap");
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("getAttributeValues");
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("setAttributesMap");
        // CacheableFieldsContainer.creatable
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("getCreatable");
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("setCreatable");
        // CacheableFieldsContainer.updatable
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("getUpdatable");
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("setUpdatable");
        // CacheableFieldsContainer.confidential
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("getConfidential");
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("setConfidential");
        // CacheableFieldsContainer.deletable
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("getDeletable");
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("setDeletable");
        // CacheableFieldsContainer.marshal
        ACESS_RIGHTS_FIELDS_CONTAINER_METHODS.add("marshal");
        // Class CacheableValuesContainer
        // CacheableValuesContainer.valuesMap
        ACESS_RIGHTS_VALUES_CONTAINER_METHODS.add("addFieldValueData");
        ACESS_RIGHTS_VALUES_CONTAINER_METHODS.add("addSubFieldData");
        ACESS_RIGHTS_VALUES_CONTAINER_METHODS.add("addValue");
        ACESS_RIGHTS_VALUES_CONTAINER_METHODS.add("addValues");

        ACESS_RIGHTS_VALUES_CONTAINER_METHODS.add("getAllAttachedFileValues");
        ACESS_RIGHTS_VALUES_CONTAINER_METHODS.add("getAllTopLevelValues");
        ACESS_RIGHTS_VALUES_CONTAINER_METHODS.add("getFieldLabels");
        ACESS_RIGHTS_VALUES_CONTAINER_METHODS.add("getValue");
        ACESS_RIGHTS_VALUES_CONTAINER_METHODS.add("getValueCount");
        ACESS_RIGHTS_VALUES_CONTAINER_METHODS.add("getValuedFieldNames");
        ACESS_RIGHTS_VALUES_CONTAINER_METHODS.add("getValuesMap");
        ACESS_RIGHTS_VALUES_CONTAINER_METHODS.add("removeFieldValue");
        ACESS_RIGHTS_VALUES_CONTAINER_METHODS.add("removeSubFieldValue");
        ACESS_RIGHTS_VALUES_CONTAINER_METHODS.add("setValue");
        ACESS_RIGHTS_VALUES_CONTAINER_METHODS.add("setValuesMap");
        // CacheableValuesContainer.marshal
        ACESS_RIGHTS_VALUES_CONTAINER_METHODS.add("marshal");
    }

    private CheckedObjectGenerator() {
    }

    /**
     * Create a proxy 'Checked'
     * 
     * @param <OBJ>
     *            The type of the bean
     * @param pNotCheckedBean
     *            The not checked bean
     * @return The new proxy 'Checked'
     */
    @SuppressWarnings("unchecked")
    public static <OBJ> OBJ create(OBJ pNotCheckedBean) {
        final Generator lGenerator = new Generator();
        lGenerator.setNotCheckedBean(pNotCheckedBean);
        return (OBJ) lGenerator.create();
    }

    /**
     * Generator of proxy 'Checked'
     * 
     * @author tpanuel
     */
    public static class Generator extends AbstractClassGenerator {
        private static final Source SOURCE =
                new Source(CheckedObjectGenerator.class.getName());

        private Object notCheckedBean;

        private Class<?> notCheckedClass;

        private String notCheckedClassName;

        /**
         * Default constructor
         */
        public Generator() {
            super(SOURCE);
        }

        /**
         * Setter on the not checked bean
         * 
         * @param pNotCheckedBean
         *            The not checked bean to convert
         */
        public void setNotCheckedBean(Object pNotCheckedBean) {
            notCheckedBean = pNotCheckedBean;
            notCheckedClass = ProxyUtils.getNotProxySuperClass(pNotCheckedBean);
            notCheckedClassName = pNotCheckedBean.getClass().getName();
        }

        /**
         * Create a proxy 'Checked'
         * 
         * @return The new proxy 'Checked'
         */
        public Object create() {
            setNamePrefix(notCheckedClassName);
            return super.create(notCheckedClassName);
        }

        /**
         * {@inheritDoc}
         * 
         * @see net.sf.cglib.core.ClassGenerator#generateClass(org.objectweb.asm.ClassVisitor)
         */
        public void generateClass(ClassVisitor pVisitor) {
            final Type[] lIntefaces;
            final Set<String> lNotOverridenMethods;

            // The proxy Checked must override a mutable class
            // else the access rights could not be edited
            if (notCheckedBean instanceof ImmutableGpmObject) {
                // The ImmutableGpmObject interface is implemented
                // because the not checked bean is immutable
                lIntefaces = new Type[] { Type.getType(CheckedGpmObject.class),

                Type.getType(ImmutableGpmObject.class) };
            }
            else {
                lIntefaces =
                        new Type[] { Type.getType(CheckedGpmObject.class) };
            }

            // The not overridden methods are linked to the type of the not checked bean
            if (notCheckedBean instanceof Field) {
                lNotOverridenMethods =
                        new HashSet<String>(ACESS_RIGHTS_FIELD_METHODS);
                // Add specific methods for multiple fields
                if (notCheckedBean instanceof MultipleField) {

                    lNotOverridenMethods.addAll(ACESS_RIGHTS_MULTIPLE_FIELD_METHODS);
                }
            }
            else if (notCheckedBean instanceof CacheableFieldsContainer) {
                lNotOverridenMethods = ACESS_RIGHTS_FIELDS_CONTAINER_METHODS;
            }
            else if (notCheckedBean instanceof CacheableValuesContainer) {
                lNotOverridenMethods = ACESS_RIGHTS_VALUES_CONTAINER_METHODS;
            }
            else {
                throw new RuntimeException(
                        "A proxy Checked cannot be created for the "
                                + notCheckedClass);
            }

            final Type lTargetType = Type.getType(notCheckedClass);
            final Type lNotCheckedType = Type.getType(notCheckedClass);
            final Method[] lMethods = notCheckedClass.getMethods();
            final ClassEmitter lCheckedClassEmitter =
                    new ClassEmitter(pVisitor);

            lCheckedClassEmitter.begin_class(Constants.V1_2,
                    Constants.ACC_PUBLIC, getClassName(), lTargetType,
                    lIntefaces, Constants.SOURCE_FILE);

            lCheckedClassEmitter.declare_field(Constants.ACC_FINAL
                    | Constants.ACC_PRIVATE, FIELD_NAME, lNotCheckedType, null);

            final CodeEmitter lConstructorEmitter =
                    lCheckedClassEmitter.begin_method(Constants.ACC_PUBLIC,
                            CSTRUCT_OBJECT, null);

            lConstructorEmitter.load_this();
            lConstructorEmitter.super_invoke_constructor();
            lConstructorEmitter.load_this();
            lConstructorEmitter.load_arg(0);
            lConstructorEmitter.checkcast(lNotCheckedType);
            lConstructorEmitter.putfield(FIELD_NAME);
            lConstructorEmitter.return_value();
            lConstructorEmitter.end_method();

            // The methods, not linked to access rights, use the field notCheckedBean
            for (int i = 0; i < lMethods.length; i++) {
                final Method lMethod = lMethods[i];
                final MethodInfo lMethodInfo =
                        ReflectUtils.getMethodInfo(lMethod);

                // Method, using fields linked to access rights, are not overridden
                // Final methods cannot be overridden
                if (!lNotOverridenMethods.contains(lMethod.getName())
                        && (lMethodInfo.getModifiers() & Constants.ACC_FINAL) == 0) {
                    // (public/protected) (Object/void) methodX (arg0) {
                    final CodeEmitter lMethodEmitter =
                            EmitUtils.begin_method(lCheckedClassEmitter,
                                    lMethodInfo, Constants.ACC_PUBLIC);

                    // (return) notCheckedBean.methodX(arg0);
                    lMethodEmitter.load_this();
                    lMethodEmitter.getfield(FIELD_NAME);
                    lMethodEmitter.dup();
                    lMethodEmitter.load_args();
                    lMethodEmitter.invoke(lMethodInfo);
                    // Return value only if necessary
                    if (lMethod.getReturnType() != null) {
                        lMethodEmitter.return_value();
                    }

                    // }
                    lMethodEmitter.end_method();
                }
            }

            // If immutable, override the getMutableCopy method
            if (notCheckedBean instanceof ImmutableGpmObject) {
                final CodeEmitter lGetMutableCopyEmmiter =
                        EmitUtils.begin_method(lCheckedClassEmitter,
                                GET_MUTABLE_COPY, Constants.ACC_PUBLIC);

                lGetMutableCopyEmmiter.load_this();
                lGetMutableCopyEmmiter.getfield(FIELD_NAME);
                lGetMutableCopyEmmiter.dup();
                lGetMutableCopyEmmiter.invoke(GET_MUTABLE_COPY);
                lGetMutableCopyEmmiter.return_value();
                lGetMutableCopyEmmiter.end_method();
            }

            // }
            lCheckedClassEmitter.end_class();
        }

        /**
         * {@inheritDoc}
         * 
         * @see net.sf.cglib.core.AbstractClassGenerator#firstInstance(java.lang.Class)
         */
        @SuppressWarnings("rawtypes")
		protected Object firstInstance(Class pType) {
            return ReflectUtils.newInstance(pType, OBJECT_CLASSES,
                    new Object[] { notCheckedBean });
        }

        /**
         * {@inheritDoc}
         * 
         * @see net.sf.cglib.core.AbstractClassGenerator#getDefaultClassLoader()
         */
        protected ClassLoader getDefaultClassLoader() {
            return notCheckedClass.getClassLoader();
        }

        /**
         * {@inheritDoc}
         * 
         * @see net.sf.cglib.core.AbstractClassGenerator#nextInstance(java.lang.Object)
         */
        protected Object nextInstance(Object pInstance) {
            return firstInstance(pInstance.getClass());
        }
    }
}
