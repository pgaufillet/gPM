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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
import org.topcased.gpm.util.lang.CopyUtils;

/**
 * Construct an immutable object from a mutable one
 * 
 * @author tpanuel
 */
final public class ImmutableObjectGenerator {
    private static final Type ILLEGAL_STATE_EXCEPTION =
            TypeUtils.parseType("IllegalStateException");

    private static final Signature CSTRUCT_OBJECT =
            TypeUtils.parseConstructor("Object");

    private static final Class<?>[] OBJECT_CLASSES = { Object.class };

    private static final Class<?>[] NO_CLASSES = {};

    private static final MethodInfo GET_MUTABLE_OBJECT;

    private static final MethodInfo GET_IMMUTABLE_OBJECT;

    private static final MethodInfo GET_MUTABLE_COPY;

    static {
        try {
            GET_MUTABLE_OBJECT =
                    ReflectUtils.getMethodInfo(CopyUtils.class.getMethod(
                            "getMutableCopy", OBJECT_CLASSES));
            GET_IMMUTABLE_OBJECT =
                    ReflectUtils.getMethodInfo(CopyUtils.class.getMethod(
                            "getImmutableCopy", OBJECT_CLASSES));
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

    /**
     * Private ctor to prevent instantiation of this class
     */
    private ImmutableObjectGenerator() {
    }

    /**
     * Create
     * 
     * @param <OBJ>
     *            The type of the mutable object
     * @param pMutableBean
     *            The mutable bean
     * @return The immutable bean
     */
    @SuppressWarnings("unchecked")
    public static <OBJ> OBJ create(OBJ pMutableBean) {
        final Generator lGenerator = new Generator();
        lGenerator.setMutableBean(pMutableBean);
        return (OBJ) lGenerator.create();
    }

    /**
     * Generate an immutable object from a mutable one
     * 
     * @author tpanuel
     */
    public static class Generator extends AbstractClassGenerator {
        private static final Source SOURCE =
                new Source(ImmutableObjectGenerator.class.getName());

        private Object mutableBean;

        private Class<?> mutableClass;

        /**
         * Default constructor
         */
        public Generator() {
            super(SOURCE);
        }

        /**
         * Set the mutable bean to generate
         * 
         * @param pMutableBean
         *            The mutable bean
         */
        public void setMutableBean(Object pMutableBean) {
            mutableBean = pMutableBean;

            // Get the actual class of the bean
            mutableClass = ProxyUtils.getNotProxySuperClass(pMutableBean);
        }

        /**
         * Create an immutable object
         * 
         * @return The immutable object
         */
        public Object create() {
            final String lName = mutableClass.getName();
            setNamePrefix(lName);
            return super.create(lName);
        }

        /**
         * {@inheritDoc}
         * 
         * @see net.sf.cglib.core.ClassGenerator#generateClass(org.objectweb.asm.ClassVisitor)
         */
        public void generateClass(ClassVisitor pVisitor) {
            final Type lMutableType = Type.getType(mutableClass);
            final ClassEmitter lImmutableClassEmitter =
                    new ClassEmitter(pVisitor);
            final Method[] lMethods = mutableClass.getMethods();
            final List<Method[]> lGetterSetters = getBeanGetterSetterInfo();

            lImmutableClassEmitter.begin_class(Constants.V1_5,
                    Constants.ACC_PUBLIC, getClassName(), lMutableType,
                    new Type[] { Type.getType(ImmutableGpmObject.class) },
                    Constants.SOURCE_FILE);

            final CodeEmitter lConstructorEmitter =
                    lImmutableClassEmitter.begin_method(Constants.ACC_PUBLIC,
                            CSTRUCT_OBJECT, null);
            lConstructorEmitter.load_this();
            lConstructorEmitter.super_invoke_constructor();
            for (Method[] lGetterSetter : lGetterSetters) {
                final MethodInfo lGetter =
                        ReflectUtils.getMethodInfo(lGetterSetter[0]);
                final MethodInfo lSetter =
                        ReflectUtils.getMethodInfo(lGetterSetter[1]);

                lConstructorEmitter.load_this();
                lConstructorEmitter.dup();
                lConstructorEmitter.load_arg(0);
                lConstructorEmitter.checkcast(lMutableType);
                lConstructorEmitter.invoke(lGetter);
                if (!lGetterSetter[0].getReturnType().isPrimitive()) {
                    lConstructorEmitter.invoke_static(
                            Type.getType(CopyUtils.class),
                            GET_IMMUTABLE_OBJECT.getSignature());
                    lConstructorEmitter.checkcast(lGetter.getSignature().getReturnType());
                }
                lConstructorEmitter.super_invoke(lSetter.getSignature());
            }
            lConstructorEmitter.return_value();
            lConstructorEmitter.end_method();

            // Override all the setters
            for (int i = 0; i < lMethods.length; i++) {
                final Method lMethod = lMethods[i];
                final MethodInfo lMethodInfo =
                        ReflectUtils.getMethodInfo(lMethod);

                // Final methods cannot be overridden
                if (!isGetter(lMethod.getName())
                        && (lMethodInfo.getModifiers() & Constants.ACC_FINAL) == 0) {
                    // Setter are not accessible: throw Exception
                    final CodeEmitter lSetterEmitter =
                            EmitUtils.begin_method(lImmutableClassEmitter,
                                    lMethodInfo, Constants.ACC_PUBLIC);
                    lSetterEmitter.throw_exception(ILLEGAL_STATE_EXCEPTION,
                            "Bean of the " + mutableClass + " is immutable");
                    lSetterEmitter.end_method();

                }
            }

            // Implements method of the interface that return a mutable copy of the object
            final CodeEmitter lGetMutableCopyEmmiter =
                    EmitUtils.begin_method(lImmutableClassEmitter,
                            GET_MUTABLE_COPY, Constants.ACC_PUBLIC);

            lGetMutableCopyEmmiter.new_instance(lMutableType);
            lGetMutableCopyEmmiter.dup();
            lGetMutableCopyEmmiter.invoke_constructor(lMutableType);
            lGetMutableCopyEmmiter.checkcast(lMutableType);
            // Initialize all fields of the new object with mutable version of the old one
            for (Method[] lGetterSetter : lGetterSetters) {
                final MethodInfo lGetter =
                        ReflectUtils.getMethodInfo(lGetterSetter[0]);
                final MethodInfo lSetter =
                        ReflectUtils.getMethodInfo(lGetterSetter[1]);

                lGetMutableCopyEmmiter.dup();
                lGetMutableCopyEmmiter.dup();
                lGetMutableCopyEmmiter.load_this();
                lGetMutableCopyEmmiter.invoke(lGetter);
                if (!lGetterSetter[0].getReturnType().isPrimitive()) {
                    lGetMutableCopyEmmiter.invoke_static(
                            Type.getType(CopyUtils.class),
                            GET_MUTABLE_OBJECT.getSignature());
                    lGetMutableCopyEmmiter.checkcast(lGetter.getSignature().getReturnType());
                }
                lGetMutableCopyEmmiter.invoke(lSetter);
            }
            lGetMutableCopyEmmiter.return_value();
            lGetMutableCopyEmmiter.end_method();

            lImmutableClassEmitter.end_class();
        }

        /**
         * Return a list of getter / setter methods tuples for each property of
         * the bean.
         * 
         * @return List of getter / setter methods tuples
         */
        private List<Method[]> getBeanGetterSetterInfo() {
            final PropertyDescriptor[] lProperties =
                    ReflectUtils.getBeanProperties(mutableClass);
            final List<Method[]> lGetterSetterInfo =
                    new ArrayList<Method[]>(lProperties.length);

            for (PropertyDescriptor lProperty : lProperties) {
                if (lProperty.getReadMethod() != null
                        && lProperty.getWriteMethod() != null) {
                    lGetterSetterInfo.add(new Method[] {
                                                        lProperty.getReadMethod(),
                                                        lProperty.getWriteMethod() });
                }
            }
            return lGetterSetterInfo;
        }

        /**
         * {@inheritDoc}
         * 
         * @see net.sf.cglib.core.AbstractClassGenerator#firstInstance(java.lang.Class)
         */
        @SuppressWarnings("rawtypes")
		protected Object firstInstance(Class pType) {
            return ReflectUtils.newInstance(pType, OBJECT_CLASSES,
                    new Object[] { mutableBean });
        }

        /**
         * {@inheritDoc}
         * 
         * @see net.sf.cglib.core.AbstractClassGenerator#getDefaultClassLoader()
         */
        protected ClassLoader getDefaultClassLoader() {
            return mutableClass.getClassLoader();
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
         * Getter's name
         */
        private static final String[] GETTERS_NAME =
                new String[] { "^get.*", "^is.*", "^can.*", "^has.*",
                              "^notify.*", "^same.*", "^wait.*", "^equal.*",
                              "^hash.*", "^iterator.*", "^to.*", "^marsh.*" };

        /**
         * Test if the method is a getter
         * 
         * @param pMethodName
         *            The name of the method
         * @return If the method is a getter
         */
        private boolean isGetter(String pMethodName) {
            boolean lResult = false;

            for (String lGetterName : GETTERS_NAME) {
                if (pMethodName.matches(lGetterName)) {
                    lResult = true;
                }
            }
            return lResult;
        }
    }
}
