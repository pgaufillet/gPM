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
package org.topcased.gpm.business.values.impl.cacheable;

import java.text.MessageFormat;
import java.util.Iterator;

import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.values.BusinessContainer;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.impl.cacheable.AbstractCacheableFieldAccess;
import org.topcased.gpm.business.values.field.impl.cacheable.CacheableFieldAccessFactory;
import org.topcased.gpm.business.values.field.multiple.BusinessMultipleField;
import org.topcased.gpm.business.values.field.multiple.impl.cacheable.CacheableMultipleFieldAccess;
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
import org.topcased.gpm.business.values.field.virtual.BusinessVirtualField;
import org.topcased.gpm.business.values.field.virtual.VirtualFieldType;

/**
 * Access on a values container.
 * 
 * @author tpanuel
 */
public abstract class AbstractCacheableContainerAccess<T extends CacheableFieldsContainer, V extends CacheableValuesContainer>
        implements BusinessContainer, ICacheableParentAccess {

    private static final String ACCESS_RIGHTS_EXCEPTION =
            "Access rights are not applied to the type ''{0}''.";

    private final T type;

    private final V values;

    private final CacheableFieldAccessFactory fieldFactory;

    private final ValuesAccessProperties properties;

    private final String roleToken;

    /**
     * Create an access on a values container.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pType
     *            The fields container to access.
     * @param pValues
     *            The values container to access.
     * @param pProperties
     *            The values access properties.
     */
    public AbstractCacheableContainerAccess(final String pRoleToken,
            final T pType, final V pValues,
            final ValuesAccessProperties pProperties) {
        roleToken = pRoleToken;
        type = pType;
        values = pValues;
        fieldFactory = new CacheableFieldAccessFactory(this);
        properties = pProperties;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getId()
     */
    public String getId() {
        return values.getId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getTypeId()
     */
    public String getTypeId() {
        return type.getId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getTypeName()
     */
    public String getTypeName() {
        return type.getName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getTypeDescription()
     */
    public String getTypeDescription() {
        return type.getDescription();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getBusinessProcessName()
     */
    public String getBusinessProcessName() {
        return type.getBusinessProcessName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#isConfidential()
     */
    public boolean isConfidential() {
        if (type.getConfidential() == null) {
            throw new AuthorizationException(MessageFormat.format(
                    ACCESS_RIGHTS_EXCEPTION, type.getName()));
        }
        return type.getConfidential();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#isUpdatable()
     */
    public boolean isUpdatable() {
        if (type.getUpdatable() == null) {
            throw new AuthorizationException(MessageFormat.format(
                    ACCESS_RIGHTS_EXCEPTION, type.getName()));
        }
        return type.getUpdatable();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#isDeletable()
     */
    public boolean isDeletable() {
        if (type.getDeletable() == null) {
            throw new AuthorizationException(MessageFormat.format(
                    ACCESS_RIGHTS_EXCEPTION, type.getName()));
        }
        return type.getDeletable();
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
     * @see org.topcased.gpm.business.values.BusinessContainer#getVirtualField(org.topcased.gpm.business.values.field.virtual.VirtualFieldType)
     */
    public BusinessVirtualField getVirtualField(
            final VirtualFieldType pVirtualFieldType) {
        return fieldFactory.getVirtualFieldAccess(pVirtualFieldType);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<BusinessField> iterator() {
        return new CacheableContainerAccessIterator(this);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess#getFieldType(java.lang.String)
     */
    public Field getFieldType(final String pFieldName) {
        final Field lField = type.getFieldFromLabel(pFieldName);

        if (lField == null) {
            throw new GDMException("Invalid field name " + pFieldName
                    + " for fields container " + getTypeName());
        }
        if (lField.getMultipleField() != null) {
            throw new GDMException("The field " + pFieldName
                    + " is a sub field of the multiple field "
                    + lField.getMultipleField());
        }

        return lField;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess#getValue(java.lang.String)
     */
    public Object getValue(final String pFieldName) {
        return read().getValuesMap().get(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess#addNewValue(org.topcased.gpm.business.values.field.impl.cacheable.AbstractCacheableFieldAccess)
     */
    public void addNewValue(
            final AbstractCacheableFieldAccess<?, ?> pFieldAccess) {
        write().getValuesMap().put(pFieldAccess.getFieldName(),
                pFieldAccess.read());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess#getProperties()
     */
    public ValuesAccessProperties getProperties() {
        return properties;
    }

    /**
     * Get the fields container.
     * 
     * @return The fields container.
     */
    public T getType() {
        return type;
    }

    /**
     * Get the value to read only access.
     * 
     * @return The value to access.
     */
    public V read() {
        if (properties.isCheckAccessRight() && isConfidential()) {
            throw new AuthorizationException(getTypeName(),
                    "The type is confidential.");
        }
        return values;
    }

    /**
     * Get the value to read or write access.
     * 
     * @return The value to access.
     */
    public V write() {
        if (properties.isReadOnly()) {
            throw new GDMException(
                    "The container has been opened on read-only.");
        }
        if (properties.isCheckAccessRight() && isConfidential()) {
            throw new AuthorizationException(getTypeName(),
                    "The type is confidential.");
        }
        if (properties.isCheckAccessRight() && !isUpdatable()) {
            throw new AuthorizationException(getTypeName(),
                    "The type is not updatable.");
        }
        return values;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess#getRoleToken()
     */
    public String getRoleToken() {
        return roleToken;
    }
}