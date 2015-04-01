/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 *
 ******************************************************************/
package org.topcased.gpm.business.values.field.impl.cacheable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.serialization.data.AttachedField;
import org.topcased.gpm.business.serialization.data.AttachedFieldValueData;
import org.topcased.gpm.business.serialization.data.ChoiceField;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.MultipleField;
import org.topcased.gpm.business.serialization.data.PointerFieldValueData;
import org.topcased.gpm.business.serialization.data.SimpleField;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.multiple.BusinessMultipleField;
import org.topcased.gpm.business.values.field.multiple.impl.cacheable.CacheableMultipleFieldAccess;
import org.topcased.gpm.business.values.field.multivalued.impl.cacheable.CacheableMultivaluedFieldAccess;
import org.topcased.gpm.business.values.field.simple.impl.cacheable.CacheableAttachedFieldAccess;
import org.topcased.gpm.business.values.field.simple.impl.cacheable.CacheableBooleanFieldAccess;
import org.topcased.gpm.business.values.field.simple.impl.cacheable.CacheableChoiceFieldAccess;
import org.topcased.gpm.business.values.field.simple.impl.cacheable.CacheableDateFieldAccess;
import org.topcased.gpm.business.values.field.simple.impl.cacheable.CacheableIntegerFieldAccess;
import org.topcased.gpm.business.values.field.simple.impl.cacheable.CacheablePointerFieldAccess;
import org.topcased.gpm.business.values.field.simple.impl.cacheable.CacheableRealFieldAccess;
import org.topcased.gpm.business.values.field.simple.impl.cacheable.CacheableStringFieldAccess;
import org.topcased.gpm.business.values.field.virtual.BusinessVirtualField;
import org.topcased.gpm.business.values.field.virtual.VirtualFieldType;
import org.topcased.gpm.business.values.field.virtual.impl.cacheable.CacheableContainerTypeAccess;
import org.topcased.gpm.business.values.field.virtual.impl.cacheable.CacheableSheetProductNameAccess;
import org.topcased.gpm.business.values.field.virtual.impl.cacheable.CacheableSheetReferenceAccess;
import org.topcased.gpm.business.values.field.virtual.impl.cacheable.CacheableSheetStateAccess;
import org.topcased.gpm.business.values.impl.cacheable.AbstractCacheableContainerAccess;
import org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess;
import org.topcased.gpm.business.values.sheet.impl.cacheable.CacheableSheetAccess;
import org.topcased.gpm.domain.fields.FieldType;

/**
 * Factory of access on a field.
 * 
 * @author tpanuel
 * @author phtsaan
 */
public class CacheableFieldAccessFactory {
    /** The access constructor for empty fields. */
    @SuppressWarnings("rawtypes")
    private final static Map<Class<? extends AbstractCacheableFieldAccess>,
    Constructor<? extends AbstractCacheableFieldAccess>> EMPTY_ACCESS_CONSTRUCTORS =
        new HashMap<Class<? extends AbstractCacheableFieldAccess>,
        Constructor<? extends AbstractCacheableFieldAccess>>();

    /** The access constructor for already valued fields. */
    @SuppressWarnings({ "rawtypes" })
    private final static Map<Class<? extends AbstractCacheableFieldAccess>,
    Constructor<? extends AbstractCacheableFieldAccess>> ACCESS_CONSTRUCTORS =
        new HashMap<Class<? extends AbstractCacheableFieldAccess>,
        Constructor<? extends AbstractCacheableFieldAccess>>();

    /**
     * map used to storage generated business fields to avoid multiple
     * generations for a same business field
     **/
    private Map<String, BusinessField> storage =
        new HashMap<String, BusinessField>();

    static {
        // Search all access constructors
        initAccessConstructor(CacheableStringFieldAccess.class,
                SimpleField.class, FieldValueData.class);
        initAccessConstructor(CacheableIntegerFieldAccess.class,
                SimpleField.class, FieldValueData.class);
        initAccessConstructor(CacheableRealFieldAccess.class,
                SimpleField.class, FieldValueData.class);
        initAccessConstructor(CacheableBooleanFieldAccess.class,
                SimpleField.class, FieldValueData.class);
        initAccessConstructor(CacheableDateFieldAccess.class,
                SimpleField.class, FieldValueData.class);
        initAccessConstructor(CacheableChoiceFieldAccess.class,
                ChoiceField.class, FieldValueData.class);
        initAccessConstructor(CacheablePointerFieldAccess.class, Field.class,
                PointerFieldValueData.class);
        initAccessConstructor(CacheableAttachedFieldAccess.class,
                AttachedField.class, AttachedFieldValueData.class);
        initAccessConstructor(CacheableMultipleFieldAccess.class,
                MultipleField.class, Map.class);
        initAccessConstructor(CacheableMultivaluedFieldAccess.class,
                Field.class, Object.class);
    }

    /**
     * Search the constructors sued to create field access.
     * 
     * @param pAccessInterface
     *            The interface.
     * @param pAccessImplClass
     *            The implementation used.
     * @param pFieldClass
     *            The class of the field.
     * @param pValueClass
     *            The class of value.
     */
    @SuppressWarnings({ "rawtypes" })
    private final static void initAccessConstructor(
            final Class<? extends AbstractCacheableFieldAccess> pAccessImplClass,
            final Class<? extends Field> pFieldClass, final Class<?> pValueClass) {
        try {
            EMPTY_ACCESS_CONSTRUCTORS.put(
                    pAccessImplClass,
                    pAccessImplClass.getConstructor(
                            new Class<?>[] {
                                            pFieldClass,
                                            ICacheableParentAccess.class 
                            }));
            ACCESS_CONSTRUCTORS.put(
                    pAccessImplClass,
                    pAccessImplClass.getConstructor(
                            new Class<?>[] {
                                            pFieldClass,
                                            pValueClass,
                                            ICacheableParentAccess.class
                            }));
        }
        catch (SecurityException e) {
            throw new GDMException("Cannot found constructor for field access "
                    + pAccessImplClass.getName() + ".", e);
        }
        catch (NoSuchMethodException e) {
            throw new GDMException("Cannot found constructor for field access "
                    + pAccessImplClass.getName() + ".", e);
        }
    }

    private final ICacheableParentAccess parentAccess;

    /**
     * Create a factory of access on fields.
     * 
     * @param pParentAccess
     *            The parent access;
     */
    public CacheableFieldAccessFactory(
            final ICacheableParentAccess pParentAccess) {
        parentAccess = pParentAccess;
    }

    /**
     * Get a field access.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The field access.
     */
    public BusinessField getFieldAccess(final String pFieldName) {
        BusinessField lResult = null;

        if (storage.containsKey(pFieldName)) {
            lResult = storage.get(pFieldName);
        }
        else {
            try {
                lResult =
                    getVirtualFieldAccess(VirtualFieldType.valueOf(pFieldName));

            }
            // Not a virtual field
            catch (IllegalArgumentException e) {
                final String[] lFieldParts =
                    StringUtils.split(pFieldName, "|", 2);

                if (lFieldParts.length == 1) {
                    // Top level field
                    if (parentAccess.getFieldType(pFieldName).isMultivalued()) {
                        lResult =
                            getFieldAccess(pFieldName,
                                    CacheableMultivaluedFieldAccess.class);
                    }
                    else {
                        lResult = getMonovaluedFieldAccess(pFieldName);
                    }
                }
                else {
                    // Sub field
                    final BusinessField lParentField;

                    // If multivalued field, take the first one
                    if (parentAccess.getFieldType(lFieldParts[0]).isMultivalued()) {
                        lParentField =
                            getFieldAccess(lFieldParts[0],
                                    CacheableMultivaluedFieldAccess.class).get(
                                            0);
                    }
                    else {
                        lParentField = getMonovaluedFieldAccess(lFieldParts[0]);
                    }

                    // The parent field must be a multiple one
                    if (lParentField instanceof BusinessMultipleField) {
                        lResult =
                            ((BusinessMultipleField) lParentField).getField(lFieldParts[1]);

                    }
                    else {
                        throw new GDMException("Cannot access to field "
                                + pFieldName + " because the field "
                                + lFieldParts[0] + " is not a multiple field.");
                    }
                }
            }
            storage.put(pFieldName, lResult);
        }
        return lResult;
    }

    /**
     * Get a mono valued field access even if the field is multi valued.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The field access.
     */
    @SuppressWarnings({ "rawtypes" })
    public AbstractCacheableFieldAccess getMonovaluedFieldAccess(
            final String pFieldName) {
        AbstractCacheableFieldAccess lResult = null;
        if (storage.containsKey(pFieldName)) {
            lResult = (AbstractCacheableFieldAccess) storage.get(pFieldName);
        }
        else {
            final Field lField = parentAccess.getFieldType(pFieldName);
            final Object lValue = parentAccess.getValue(pFieldName);

            try {
                final Class<? extends BusinessField> lImplClass;

                if (lField.isPointerField()) {
                    lImplClass = CacheablePointerFieldAccess.class;
                }
                else if (lField instanceof SimpleField) {
                    final SimpleField lSimpleField = (SimpleField) lField;
                    final String lValueType = lSimpleField.getValueType();

                    if (lValueType.equalsIgnoreCase(FieldType.STRING.toString())) {
                        lImplClass = CacheableStringFieldAccess.class;
                    }
                    else if (lValueType.equalsIgnoreCase(FieldType.INTEGER.toString())) {
                        lImplClass = CacheableIntegerFieldAccess.class;
                    }
                    else if (lValueType.equalsIgnoreCase(FieldType.REAL.toString())) {
                        lImplClass = CacheableRealFieldAccess.class;
                    }
                    else if (lValueType.equalsIgnoreCase(FieldType.DATE.toString())) {
                        lImplClass = CacheableDateFieldAccess.class;
                    }
                    else if (lValueType.equalsIgnoreCase(FieldType.BOOLEAN.toString())) {
                        lImplClass = CacheableBooleanFieldAccess.class;
                    }
                    else if (lValueType.equalsIgnoreCase(FieldType.APPLET.toString())) {
                        lImplClass = CacheableStringFieldAccess.class;
                    }
                    else {
                        throw new RuntimeException("Unknow value's type:"
                                + lValueType.toString() + ".");
                    }
                }
                else if (lField instanceof MultipleField) {
                    lImplClass = CacheableMultipleFieldAccess.class;
                }
                else if (lField instanceof AttachedField) {
                    lImplClass = CacheableAttachedFieldAccess.class;
                }
                else if (lField instanceof ChoiceField) {
                    lImplClass = CacheableChoiceFieldAccess.class;
                }
                else {
                    throw new GDMException("Unknow field's type:"
                            + lField.getClass() + ".");
                }

                if (lValue == null) {
                    lResult =
                        EMPTY_ACCESS_CONSTRUCTORS.get(lImplClass).newInstance(
                                new Object[] { lField, parentAccess });
                }
                else {
                    lResult =
                        ACCESS_CONSTRUCTORS.get(lImplClass).newInstance(
                                new Object[] { lField, lValue, parentAccess });
                }
            }
            catch (IllegalArgumentException e) {
                throw new GDMException("Cannot create access on field "
                        + lField.getLabelKey() + " of type "
                        + lField.getClass().getName() + ".", e);
            }
            catch (InstantiationException e) {
                throw new GDMException("Cannot create access on field "
                        + lField.getLabelKey() + " of type "
                        + lField.getClass().getName() + ".", e);
            }
            catch (IllegalAccessException e) {
                throw new GDMException("Cannot create access on field "
                        + lField.getLabelKey() + " of type "
                        + lField.getClass().getName() + ".", e);
            }
            catch (InvocationTargetException e) {
                throw new GDMException("Cannot create access on field "
                        + lField.getLabelKey() + " of type "
                        + lField.getClass().getName() + ".", e);
            }
            storage.put(pFieldName, lResult);
        }
        return lResult;

    }

    /**
     * Get a virtual field access.
     * 
     * @param pVirtualFieldType
     *            The type of virtual field.
     * @return The virtual field access.
     */
    public BusinessVirtualField getVirtualFieldAccess(
            final VirtualFieldType pVirtualFieldType) {
        BusinessVirtualField lResult = null;

        if (storage.containsKey(pVirtualFieldType.getValue())) {
            lResult = (BusinessVirtualField) storage.get(pVirtualFieldType.getValue());
        }
        else {
            // Check the type of the parent access
            switch (pVirtualFieldType) {
                case $SHEET_STATE:
                case $SHEET_REFERENCE:
                case $PRODUCT_NAME:
                    if (!(parentAccess instanceof CacheableSheetAccess)) {
                        throw new GDMException("The virtual field "
                                + pVirtualFieldType.getValue()
                                + " is only available for sheets.");
                    }
                    break;
                default:
                    if (!(parentAccess instanceof AbstractCacheableContainerAccess)) {
                        throw new GDMException(
                        "Virtual fields are only available for containers.");
                    }
            }

            // Search the good virtual field access
            switch (pVirtualFieldType) {
                case $SHEET_STATE:
                    lResult =
                        new CacheableSheetStateAccess(
                                (CacheableSheetAccess) parentAccess);
                    break;
                case $SHEET_REFERENCE:
                    lResult =
                        new CacheableSheetReferenceAccess(
                                (CacheableSheetAccess) parentAccess);
                    break;
                case $PRODUCT_NAME:
                    lResult =
                        new CacheableSheetProductNameAccess(
                                (CacheableSheetAccess) parentAccess);
                    break;
                case $SHEET_TYPE:
                    lResult =
                        new CacheableContainerTypeAccess(
                                (CacheableSheetAccess) parentAccess);
                    break;
                default:
                    throw new GDMException("The virtual field "
                            + pVirtualFieldType.getValue()
                            + " is not supported.");

            }
            storage.put(pVirtualFieldType.getValue(), lResult);
        }

        return lResult;

    }

    /**
     * Get a field access.
     * 
     * @param <A>
     *            The type of field access.
     * @param pFieldName
     *            The name of the field.
     * @param pAccessClass
     *            The class of the access.
     * @return The field access.
     */
    @SuppressWarnings("unchecked")
    public <A extends BusinessField> A getFieldAccess(final String pFieldName,
            final Class<A> pAccessClass) {

        A lResult = null;

        if (storage.containsKey(pFieldName)) {
            lResult = (A) storage.get(pFieldName);
        }
        else {
            final Field lField = parentAccess.getFieldType(pFieldName);
            final Object lValue = parentAccess.getValue(pFieldName);

            try {
                if (lValue == null) {
                    lResult =
                        (A) EMPTY_ACCESS_CONSTRUCTORS.get(pAccessClass).newInstance(
                                new Object[] { lField, parentAccess });
                }
                else {
                    lResult =
                        (A) ACCESS_CONSTRUCTORS.get(pAccessClass).newInstance(
                                new Object[] { lField, lValue, parentAccess });
                }
            }
            catch (IllegalArgumentException e) {
                throw new GDMException("Cannot create "
                        + pAccessClass.getName() + " on field "
                        + lField.getLabelKey() + " of type "
                        + lField.getClass().getName() + ".", e);
            }
            catch (InstantiationException e) {
                throw new GDMException("Cannot create "
                        + pAccessClass.getName() + " on field "
                        + lField.getLabelKey() + " of type "
                        + lField.getClass().getName() + ".", e);
            }
            catch (IllegalAccessException e) {
                throw new GDMException("Cannot create "
                        + pAccessClass.getName() + " on field "
                        + lField.getLabelKey() + " of type "
                        + lField.getClass().getName() + ".", e);
            }
            catch (InvocationTargetException e) {
                throw new GDMException("Cannot create "
                        + pAccessClass.getName() + " on field "
                        + lField.getLabelKey() + " of type "
                        + lField.getClass().getName() + ".", e);
            }
            storage.put(lResult.getFieldName(), lResult);
        }
        return lResult;
    }
}