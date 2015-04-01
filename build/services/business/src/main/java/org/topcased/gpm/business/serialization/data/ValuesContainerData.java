/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.util.lang.CollectionUtils;
import org.topcased.gpm.util.lang.IntegerUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class ValuesContainerData.
 * 
 * @author llatil
 */
public abstract class ValuesContainerData extends AttributesContainer {

    private static final long serialVersionUID = -4287153355175066104L;

    /** Technical identifier of this container. */
    @XStreamAsAttribute
    private String id;

    /**
     * Type name of this element. This type determines available fields for this
     * container.
     */
    @XStreamAsAttribute
    private String type;

    /** Name of product containing this sheet. */
    @XStreamAsAttribute
    private String productName;

    /** Functional reference of the element (as string value). */
    //@XStreamOmitField
    @XStreamAsAttribute
    private String reference;

    /** Current version of the element. */
    @XStreamAsAttribute
    private Integer version;

    /** Values set on the fields of this element. */
    @XStreamAlias(value = "fieldValues", impl = LinkedList.class)
    private List<FieldValueData> fieldValues;

    /** Lock */
    @XStreamAlias("lock")
    private Lock lock;

    /** Extension points to exclude from execution */
    @XStreamAlias(value = "extensionPointsToExclude")
    private ExtensionPointsToExcludeData extensionPointsToExclude;

    /**
     * Gets the field values.
     * 
     * @return the field values
     */
    public List<FieldValueData> getFieldValues() {
        return fieldValues;
    }

    /**
     * Set the list of field values.
     * 
     * @param pFieldValues
     *            Field values to add.
     */
    public void setFieldValues(final List<FieldValueData> pFieldValues) {
        fieldValues = pFieldValues;
    }

    /**
     * Test if this element has defined values.
     * 
     * @return true if values exist for this element.
     */
    public boolean hasFieldValues() {
        return (null != fieldValues && fieldValues.size() > 0);
    }

    /**
     * Add a list of new field values.
     * 
     * @param pFieldValues
     *            Field values to add.
     */
    public void addFieldValues(
            final Collection<? extends FieldValueData> pFieldValues) {
        if (null == fieldValues) {
            fieldValues = new ArrayList<FieldValueData>(pFieldValues);
        }
        else {
            fieldValues.addAll(pFieldValues);
        }
    }

    /**
     * Add a new field value.
     * 
     * @param pFieldValue
     *            Field value to add.
     */
    public void addFieldValue(final FieldValueData pFieldValue) {
        if (null == fieldValues) {
            fieldValues = new ArrayList<FieldValueData>();
        }
        fieldValues.add(pFieldValue);
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     * 
     * @param pId
     *            the new id
     */
    public void setId(final String pId) {
        id = pId;
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     * 
     * @param pType
     *            the new type
     */
    public void setType(String pType) {
        type = pType;
    }

    /**
     * Gets the reference.
     * 
     * @return the reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets the reference.
     * 
     * @param pRef
     *            the ref
     */
    public void setReference(String pRef) {
        reference = pRef;
    }

    /**
     * Gets the product name.
     * 
     * @return the product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the product name.
     * 
     * @param pProduct
     *            the product
     */
    public void setProductName(String pProduct) {
        productName = pProduct;
    }

    /**
     * Get the version of this element.
     * 
     * @return The current version, or null if not specified
     */
    public final Integer getVersion() {
        return version;
    }

    /**
     * Set the version of this element.
     * 
     * @param pVersion
     *            The current version
     */
    public final void setVersion(Integer pVersion) {
        version = pVersion;
    }

    /**
     * get lock
     * 
     * @return the lock
     */
    public Lock getLock() {
        return lock;
    }

    /**
     * set lock
     * 
     * @param pLock
     *            the lock to set
     */
    public void setLock(Lock pLock) {
        lock = pLock;
    }

    /**
     * Get the {@link ExtensionPointsToExcludeData} for this object
     * 
     * @return the {@link ExtensionPointsToExcludeData} for this object
     */
    public ExtensionPointsToExcludeData getExtensionPointsToExclude() {
        return extensionPointsToExclude;
    }

    /**
     * Set the {@link ExtensionPointsToExcludeData} for this object
     * 
     * @param pExtensionPointsToExclude
     *            the {@link ExtensionPointsToExcludeData} for this object
     */
    public void setExtensionPointsToExclude(
            final ExtensionPointsToExcludeData pExtensionPointsToExclude) {
        this.extensionPointsToExclude = pExtensionPointsToExclude;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {

        if (pOther instanceof ValuesContainerData) {
            ValuesContainerData lOther = (ValuesContainerData) pOther;

            if (!super.equals(lOther)) {
                return false;
            }
            if (!StringUtils.equals(id, lOther.id)) {
                return false;
            }
            if (!StringUtils.equals(productName, lOther.productName)) {
                return false;
            }
            if (!StringUtils.equals(type, lOther.type)) {
                return false;
            }
            if (!StringUtils.equals(reference, lOther.reference)) {
                return false;
            }
            if (!IntegerUtils.equals(version, lOther.version)) {
                return false;
            }
            if (!CollectionUtils.equals(fieldValues, lOther.fieldValues)) {
                return false;
            }

            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        // AttributesContainer hashcode is sufficient
        return super.hashCode();
    }
}
