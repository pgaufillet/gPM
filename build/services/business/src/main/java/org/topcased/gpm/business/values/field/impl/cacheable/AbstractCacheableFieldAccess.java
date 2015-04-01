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

import java.text.MessageFormat;

import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess;

/**
 * Abstract access on a field.
 * 
 * @author tpanuel
 * @param <F>
 *            The type of field.
 * @param <V>
 *            The type of value to access.
 */
public abstract class AbstractCacheableFieldAccess<F extends Field, V>
        implements BusinessField {

    private static final String ACCESS_RIGHTS_EXCEPTION =
            "Access rights are not applied to the field ''{0}''.";

    private final F fieldType;

    private final V value;

    private final ICacheableParentAccess parentAccess;

    private boolean existOnContainer;

    private final ValuesAccessProperties properties;

    /**
     * Create an access on a field.
     * 
     * @param pField
     *            The field to access.
     * @param pValue
     *            The value to access.
     * @param pParentAccess
     *            The parent access.
     * @param pExistOnContainer
     *            If the value is present on the parent container, else the
     *            value must be add at the first write action.
     */
    public AbstractCacheableFieldAccess(final F pField, final V pValue,
            final ICacheableParentAccess pParentAccess,
            final boolean pExistOnContainer) {
        fieldType = pField;
        value = pValue;
        parentAccess = pParentAccess;
        existOnContainer = pExistOnContainer;
        properties = parentAccess.getProperties();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getFieldName()
     */
    public String getFieldName() {
        return fieldType.getLabelKey();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getFieldDescription()
     */
    public String getFieldDescription() {
        return fieldType.getDescription();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isConfidential()
     */
    public boolean isConfidential() {
        if (fieldType.getUpdatable() == null) {
            throw new AuthorizationException(MessageFormat.format(
                    ACCESS_RIGHTS_EXCEPTION, fieldType.getName()));
        }
        return fieldType.isConfidential();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isUpdatable()
     */
    public boolean isUpdatable() {
        if (fieldType.getUpdatable() == null) {
            throw new AuthorizationException(MessageFormat.format(
                    ACCESS_RIGHTS_EXCEPTION, fieldType.getName()));
        }
        return fieldType.isUpdatable();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isMandatory()
     */
    public Boolean isMandatory() {
        if (fieldType.getUpdatable() == null) {
            throw new AuthorizationException(MessageFormat.format(
                    ACCESS_RIGHTS_EXCEPTION, fieldType.getName()));
        }
        return fieldType.isMandatory();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isExportable()
     */
    public boolean isExportable() {
        if (fieldType.getUpdatable() == null) {
            throw new AuthorizationException(MessageFormat.format(
                    ACCESS_RIGHTS_EXCEPTION, fieldType.getName()));
        }
        return fieldType.isExportable();
    }

    /**
     * Get the value to read only access.
     * 
     * @return The value to access.
     */
    public V read() {
        if (properties.isCheckAccessRight() && isConfidential()) {
            throw new AuthorizationException(getFieldName(),
                    "The field is confidential");
        }

        return value;
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
            throw new AuthorizationException(getFieldName(),
                    "The field is confidential.");
        }
        if (properties.isCheckAccessRight() && !isUpdatable()) {
            throw new AuthorizationException(getFieldName(),
                    "The field is not updatable.");
        }
        // If need, add the value on the container
        if (!existOnContainer) {
            parentAccess.addNewValue(this);
            existOnContainer = true;
        }

        return value;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#getAsString()
     */
    public String getAsString() {
        return toString();
    }

    /**
     * Get the field type.
     * 
     * @return The field type.
     */
    public F getFieldType() {
        return fieldType;
    }

    /**
     * Get the values access properties.
     * 
     * @return The values access properties.
     */
    public ValuesAccessProperties getProperties() {
        return properties;
    }

    /**
     * Get the role token used to create the access.
     * 
     * @return The role token.
     */
    public String getRoleToken() {
        return parentAccess.getRoleToken();
    }
}