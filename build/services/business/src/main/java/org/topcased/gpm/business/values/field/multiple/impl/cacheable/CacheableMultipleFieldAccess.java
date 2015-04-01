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
package org.topcased.gpm.business.values.field.multiple.impl.cacheable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.MultipleField;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.impl.cacheable.AbstractCacheableFieldAccess;
import org.topcased.gpm.business.values.field.impl.cacheable.CacheableFieldAccessFactory;
import org.topcased.gpm.business.values.field.multiple.BusinessMultipleField;
import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;
import org.topcased.gpm.business.values.field.multivalued.impl.cacheable.CacheableMultivaluedFieldAccess;
import org.topcased.gpm.business.values.field.simple.BusinessAttachedField;
import org.topcased.gpm.business.values.field.simple.BusinessBooleanField;
import org.topcased.gpm.business.values.field.simple.BusinessChoiceField;
import org.topcased.gpm.business.values.field.simple.BusinessDateField;
import org.topcased.gpm.business.values.field.simple.BusinessIntegerField;
import org.topcased.gpm.business.values.field.simple.BusinessPointerField;
import org.topcased.gpm.business.values.field.simple.BusinessRealField;
import org.topcased.gpm.business.values.field.simple.BusinessStringField;
import org.topcased.gpm.business.values.field.simple.impl.cacheable.CacheableAttachedFieldAccess;
import org.topcased.gpm.business.values.field.simple.impl.cacheable.CacheableBooleanFieldAccess;
import org.topcased.gpm.business.values.field.simple.impl.cacheable.CacheableChoiceFieldAccess;
import org.topcased.gpm.business.values.field.simple.impl.cacheable.CacheableDateFieldAccess;
import org.topcased.gpm.business.values.field.simple.impl.cacheable.CacheableIntegerFieldAccess;
import org.topcased.gpm.business.values.field.simple.impl.cacheable.CacheablePointerFieldAccess;
import org.topcased.gpm.business.values.field.simple.impl.cacheable.CacheableRealFieldAccess;
import org.topcased.gpm.business.values.field.simple.impl.cacheable.CacheableStringFieldAccess;
import org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess;

/**
 * Access on a multiple field.
 * 
 * @author tpanuel
 */
public class CacheableMultipleFieldAccess extends
        AbstractCacheableFieldAccess<MultipleField, Map<String, Object>>
        implements BusinessMultipleField, ICacheableParentAccess {
    private final static String DEFAULT_SEPARATOR = " ";

    private final CacheableFieldAccessFactory fieldFactory;

    /**
     * Create an empty multiple field access.
     * 
     * @param pMultipleField
     *            The multiple field to access.
     * @param pParentAccess
     *            The parent access.
     */
    public CacheableMultipleFieldAccess(final MultipleField pMultipleField,
            final ICacheableParentAccess pParentAccess) {
        super(pMultipleField, new HashMap<String, Object>(), pParentAccess,
                false);
        fieldFactory = new CacheableFieldAccessFactory(this);
    }

    /**
     * Create a multiple field access.
     * 
     * @param pMultipleField
     *            The multiple field to access.
     * @param pValues
     *            The values of the multiple field to access.
     * @param pParentAccess
     *            The parent access.
     */
    public CacheableMultipleFieldAccess(final MultipleField pMultipleField,
            final Map<String, Object> pValues,
            final ICacheableParentAccess pParentAccess) {
        super(pMultipleField, pValues, pParentAccess, true);
        fieldFactory = new CacheableFieldAccessFactory(this);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getField(java.lang.String)
     */
    public BusinessField getField(final String pFieldName) {
        return fieldFactory.getFieldAccess(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getStringField(java.lang.String)
     */
    public BusinessStringField getStringField(final String pFieldName) {
        return fieldFactory.getFieldAccess(pFieldName,
                CacheableStringFieldAccess.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getMultivaluedStringField(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public BusinessMultivaluedField<BusinessStringField> getMultivaluedStringField(
            final String pFieldName) {
        return fieldFactory.getFieldAccess(pFieldName,
                CacheableMultivaluedFieldAccess.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getIntegerField(java.lang.String)
     */
    public BusinessIntegerField getIntegerField(final String pFieldName) {
        return fieldFactory.getFieldAccess(pFieldName,
                CacheableIntegerFieldAccess.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getMultivaluedIntegerField(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public BusinessMultivaluedField<BusinessIntegerField> getMultivaluedIntegerField(
            final String pFieldName) {
        return fieldFactory.getFieldAccess(pFieldName,
                CacheableMultivaluedFieldAccess.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getRealField(java.lang.String)
     */
    public BusinessRealField getRealField(final String pFieldName) {
        return fieldFactory.getFieldAccess(pFieldName,
                CacheableRealFieldAccess.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getMultivaluedRealField(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public BusinessMultivaluedField<BusinessRealField> getMultivaluedRealField(
            final String pFieldName) {
        return fieldFactory.getFieldAccess(pFieldName,
                CacheableMultivaluedFieldAccess.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getBooleanField(java.lang.String)
     */
    public BusinessBooleanField getBooleanField(final String pFieldName) {
        return fieldFactory.getFieldAccess(pFieldName,
                CacheableBooleanFieldAccess.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getMultivaluedBooleanField(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public BusinessMultivaluedField<BusinessBooleanField> getMultivaluedBooleanField(
            final String pFieldName) {
        return fieldFactory.getFieldAccess(pFieldName,
                CacheableMultivaluedFieldAccess.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getDateField(java.lang.String)
     */
    public BusinessDateField getDateField(final String pFieldName) {
        return fieldFactory.getFieldAccess(pFieldName,
                CacheableDateFieldAccess.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getMultivaluedDateField(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public BusinessMultivaluedField<BusinessDateField> getMultivaluedDateField(
            final String pFieldName) {
        return fieldFactory.getFieldAccess(pFieldName,
                CacheableMultivaluedFieldAccess.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getChoiceField(java.lang.String)
     */
    public BusinessChoiceField getChoiceField(final String pFieldName) {
        return fieldFactory.getFieldAccess(pFieldName,
                CacheableChoiceFieldAccess.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getMultivaluedChoiceField(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public BusinessMultivaluedField<BusinessChoiceField> getMultivaluedChoiceField(
            final String pFieldName) {
        return fieldFactory.getFieldAccess(pFieldName,
                CacheableMultivaluedFieldAccess.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getPointerField(java.lang.String)
     */
    public BusinessPointerField getPointerField(final String pFieldName) {
        return fieldFactory.getFieldAccess(pFieldName,
                CacheablePointerFieldAccess.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getMultivaluedPointerField(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public BusinessMultivaluedField<BusinessPointerField> getMultivaluedPointerField(
            final String pFieldName) {
        return fieldFactory.getFieldAccess(pFieldName,
                CacheableMultivaluedFieldAccess.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getAttachedField(java.lang.String)
     */
    public BusinessAttachedField getAttachedField(final String pFieldName) {
        return fieldFactory.getFieldAccess(pFieldName,
                CacheableAttachedFieldAccess.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getMultivaluedAttachedField(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public BusinessMultivaluedField<BusinessAttachedField> getMultivaluedAttachedField(
            final String pFieldName) {
        return fieldFactory.getFieldAccess(pFieldName,
                CacheableMultivaluedFieldAccess.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getMultipleField(java.lang.String)
     */
    public BusinessMultipleField getMultipleField(final String pFieldName) {
        return fieldFactory.getFieldAccess(pFieldName,
                CacheableMultipleFieldAccess.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getMultivaluedMultipleField(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public BusinessMultivaluedField<BusinessMultipleField> getMultivaluedMultipleField(
            final String pFieldName) {
        return fieldFactory.getFieldAccess(pFieldName,
                CacheableMultivaluedFieldAccess.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<BusinessField> iterator() {
        return new CacheableMultipleFieldAccessIterator(this);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess#getFieldType(java.lang.String)
     */
    public Field getFieldType(final String pFieldName) {
        for (final Field lSubField : getFieldType().getFields()) {
            if (lSubField.getLabelKey().equals(pFieldName)) {
                return lSubField;
            }
        }
        throw new GDMException("Invalid field name " + pFieldName
                + " for multiple field " + getFieldName());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess#getValue(java.lang.String)
     */
    public Object getValue(final String pFieldName) {
        return read().get(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess#addNewValue(org.topcased.gpm.business.values.field.impl.cacheable.AbstractCacheableFieldAccess)
     */
    public void addNewValue(
            final AbstractCacheableFieldAccess<?, ?> pFieldAccess) {
        write().put(pFieldAccess.getFieldName(), pFieldAccess.read());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    public boolean hasSameValues(final BusinessField pOther) {
        if (!(pOther instanceof BusinessMultipleField)) {
            return false;
        }
        else if (pOther == null || pOther.isEmpty()) {
            return isEmpty();
        }
        else {
            final BusinessMultipleField lOther = (BusinessMultipleField) pOther;

            // Test if each sub access has the same values
            for (BusinessField lFieldAccess : this) {
                final BusinessField lOtherFieldAccess =
                        lOther.getField(lFieldAccess.getFieldName());

                if (!lFieldAccess.hasSameValues(lOtherFieldAccess)) {
                    return false;
                }
            }

            return true;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isEmpty()
     */
    public boolean isEmpty() {
        // Test if each sub access is empty
        for (BusinessField lFieldAccess : this) {
            if (!lFieldAccess.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#clear()
     */
    public void clear() {
        // Clear all sub access
        for (BusinessField lFieldAccess : this) {
            lFieldAccess.clear();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#reset()
     */
    public void reset() {
        // Reset all sub access
        for (BusinessField lFieldAccess : this) {
            lFieldAccess.reset();
        }
    }

    /**
     * For a business attached field, verify if this field must be deleted or not.
     * An attached field has to be deleted if its name and its content are null
     * 
     * @param pOther The business attached field to verify
     * 
     * @return <tt>true</tt> if the field must be erased, <tt>false</tt> otherwise
     */
    public boolean verifyIfErase(final BusinessField pOther) {
        boolean lReturn = false;
        if (pOther != null) {
            if (pOther instanceof BusinessMultipleField) {
                final BusinessMultipleField lOther =
                        (BusinessMultipleField) pOther;
                Iterator<BusinessField> lIterator = iterator();
                while (lIterator.hasNext()) {
                    BusinessField lFieldAccess = lIterator.next();
                    BusinessField lBusinessField = lOther.getField(lFieldAccess.getFieldName());
                    if (lBusinessField != null && (lBusinessField instanceof 
                            BusinessAttachedField)) {
                        BusinessAttachedField lAttachedField = 
                            (BusinessAttachedField) lBusinessField;
                        if (StringUtils.isBlank(lAttachedField.getFileName()) && 
                                lAttachedField.getContent() == null) {
                            lReturn = true;
                        }
                    }
                }
            }
        }
        return lReturn;
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    public void copy(final BusinessField pOther) {
        if (pOther != null) {
            if (pOther instanceof BusinessMultipleField) {
                final BusinessMultipleField lOther =
                        (BusinessMultipleField) pOther;

                // Copy all sub values
                Iterator<BusinessField> lIterator = iterator();
                while (lIterator.hasNext()) {
                    BusinessField lFieldAccess = lIterator.next();
                    lFieldAccess.copy(lOther.getField(lFieldAccess.getFieldName()));
                }
            }
            else {
                throw new GDMException("Cannot copy "
                        + pOther.getClass().getName() + " on "
                        + getClass().getName() + ".");
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final Iterator<BusinessField> lFields = iterator();
        final StringBuffer lStringValue = new StringBuffer();
        String lFieldSeparator = getFieldType().getFieldSeparator();

        if (lFieldSeparator == null) {
            lFieldSeparator = DEFAULT_SEPARATOR;
        }

        while (lFields.hasNext()) {
            lStringValue.append(lFields.next().toString());
            if (lFields.hasNext()) {
                lStringValue.append(lFieldSeparator);
            }
        }

        return lStringValue.toString();
    }
}