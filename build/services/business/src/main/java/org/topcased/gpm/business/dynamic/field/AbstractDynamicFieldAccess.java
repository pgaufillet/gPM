/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.dynamic.field;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.domain.dynamic.util.DynamicObjectNamesUtils;

/**
 * Abstract access on fields
 * 
 * @author tpanuel
 * @param <T>
 *            The type of the field
 */
public abstract class AbstractDynamicFieldAccess<T> {
    private final static Object[] EMPTY_OBJECT_TAB = new Object[] {};

    private final String fieldId;

    private final String fieldName;

    private final Class<?> columnClass;

    private Method columnGetter;

    private Method columnSetter;

    /**
     * Map<String Key , Object Value> This cache can be used to store data to be
     * kept from a field to an other For example in a multi-valued field this
     * cache can store data that will be used for all fields of the list Clear
     * this cache to avoid memory loss !
     */
    protected Map<String, Object> localCache;

    /**
     * Create an abstract access on fields
     * 
     * @param pField
     *            The field
     * @param pColumnClass
     *            The type of the column mapping the field
     */
    public AbstractDynamicFieldAccess(Field pField, Class<?> pColumnClass) {
        fieldId = pField.getId();
        fieldName = pField.getLabelKey();
        columnClass = pColumnClass;
        // Getter or setter cannot be find now,
        // because dynamic classes are unknown
        columnGetter = null;
        columnSetter = null;
        localCache = new HashMap<String, Object>();
    }

    /**
     * Getter on the name of the field
     * 
     * @return The name of the field
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Getter on the field value
     * 
     * @param pDomainContainer
     *            The container of the domain that own the field value
     * @param pContext
     *            The context
     * @return The field value
     */
    abstract public Object getFieldValue(Object pDomainContainer,
            Context pContext);

    /**
     * Convert a field value on a String
     * 
     * @param pDomainContainer
     *            The container of the domain that own the field value
     * @param pContext
     *            The context
     * @return The field value as a String
     */
    abstract public String getFieldValueAsString(Object pDomainContainer,
            Context pContext);

    /**
     * Setter on the field value
     * 
     * @param pDomainContainer
     *            The container of the domain that own the field value
     * @param pFieldValue
     *            The new field value
     * @param pContext
     *            The context
     */
    abstract public void setFieldValue(Object pDomainContainer,
            Object pFieldValue, Context pContext);

    /**
     * Getter on a value
     * 
     * @param pObject
     *            The container of values
     * @return The value
     */
    @SuppressWarnings("unchecked")
    public T getValue(Object pObject) {
        if (columnGetter == null) {
            initGetterAndSetter(pObject);
        }
        try {
            return (T) columnGetter.invoke(pObject, EMPTY_OBJECT_TAB);
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException("Field not found : " + fieldName, e);
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Field not found : " + fieldName, e);
        }
        catch (SecurityException e) {
            throw new RuntimeException("Field not found : " + fieldName, e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException("Field not found : " + fieldName, e);
        }
    }

    /**
     * Setter on a value
     * 
     * @param pObject
     *            The container of values
     * @param pValue
     *            The new value
     */
    public void setValue(Object pObject, T pValue) {
        if (columnSetter == null) {
            initGetterAndSetter(pObject);
        }
        try {
            columnSetter.invoke(pObject, new Object[] { pValue });
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException("Field not found : " + fieldName, e);
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Field not found : " + fieldName, e);
        }
        catch (SecurityException e) {
            throw new RuntimeException("Field not found : " + fieldName, e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException("Field not found : " + fieldName, e);
        }
    }

    private void initGetterAndSetter(Object pObject) {
        try {
            columnGetter =
                    pObject.getClass().getMethod(
                            DynamicObjectNamesUtils.getInstance().getDynamicColumnGetterName(
                                    fieldId), new Class[] {});
            columnSetter =
                    pObject.getClass().getMethod(
                            DynamicObjectNamesUtils.getInstance().getDynamicColumnSetterName(
                                    fieldId), new Class[] { columnClass });
        }
        catch (SecurityException e) {
            throw new RuntimeException("Field not found : " + fieldName, e);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException("Field not found : " + fieldName, e);
        }
    }

    public void clearLocalCache() {
        if (localCache != null) {
            localCache.clear();
        }
    }
}
