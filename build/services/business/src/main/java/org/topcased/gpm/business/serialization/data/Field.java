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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * This interface implement an abstract serializable field.
 * 
 * @author llatil
 */
public abstract class Field extends AttributesContainer {

    /** serialVersionUID */
    private static final long serialVersionUID = 1316216542799623241L;

    /** Technical identifier of the field. */
    @XStreamAsAttribute
    private String id;

    /** Specify if this field can have multiple values. */
    @XStreamAsAttribute
    private Boolean multivalued;

    /** Mandatory option. */
    @XStreamAsAttribute
    private Boolean mandatory;

    /** Updatable option. */
    @XStreamAsAttribute
    private Boolean updatable;

    /** Confidential option. */
    @XStreamAsAttribute
    private Boolean confidential;

    /** Exportable of the simpleField. */
    @XStreamAsAttribute
    private Boolean exportable;

    /** Label key of the field. */
    @XStreamAsAttribute
    private String labelKey;

    /** Technical identifier of the multiple field containing this field. */
    @XStreamAsAttribute
    private String multipleField;

    /** Indicates if the current field points on another field or not */
    @XStreamAsAttribute
    private boolean pointerField;

    /** Name of the link type for automatic fill of pointer field values */
    @XStreamAsAttribute
    private String referencedLinkType;

    /**
     * Label key of the linked sheet field for automatic fill of pointer field
     * values
     */
    @XStreamAsAttribute
    private String referencedFieldLabel;

    /**
     * Get the label key of the field.
     * 
     * @return Label key
     */
    public String getLabelKey() {
        return labelKey;
    }

    /**
     * Set the label key of the field.
     * 
     * @param pLabelKey
     *            New label key
     */
    public void setLabelKey(String pLabelKey) {
        labelKey = pLabelKey;
    }

    /**
     * Checks if is confidential.
     * 
     * @return the boolean
     */
    public Boolean isConfidential() {
        if (null == confidential) {
            return false;
        }
        return confidential;
    }

    /**
     * Sets the confidential.
     * 
     * @param pIsConfidential
     *            the is confidential
     */
    public void setConfidential(Boolean pIsConfidential) {
        confidential = pIsConfidential;
    }

    /**
     * Checks if is exportable.
     * 
     * @return the boolean
     */
    public Boolean isExportable() {
        if (null == exportable) {
            return true;
        }
        return exportable;
    }

    /**
     * Sets the exportable.
     * 
     * @param pIsExportable
     *            the is exportable
     */
    public void setExportable(Boolean pIsExportable) {
        exportable = pIsExportable;
    }

    /**
     * Checks if is multivalued.
     * 
     * @return the boolean
     */
    public Boolean isMultivalued() {
        if (null == multivalued) {
            return false;
        }
        return multivalued;
    }

    /**
     * Sets the multivalued.
     * 
     * @param pIsList
     *            the is list
     */
    public void setMultivalued(Boolean pIsList) {
        multivalued = pIsList;
    }

    /**
     * Checks if is mandatory.
     * 
     * @return the boolean
     */
    public Boolean isMandatory() {
        if (null == mandatory) {
            return false;
        }
        return mandatory;
    }

    /**
     * Sets the mandatory.
     * 
     * @param pIsMandatory
     *            the is mandatory
     */
    public void setMandatory(Boolean pIsMandatory) {
        mandatory = pIsMandatory;
    }

    /**
     * Checks if is updatable.
     * 
     * @return the boolean
     */
    public Boolean isUpdatable() {
        if (null == updatable) {
            return true;
        }
        return updatable;
    }

    /**
     * Sets the updatable.
     * 
     * @param pIsUpdatable
     *            the is updatable
     */
    public void setUpdatable(Boolean pIsUpdatable) {
        updatable = pIsUpdatable;
    }

    /**
     * Get the technical identifier of the field.
     * 
     * @return Identifier Technical identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Set the technical identifier of the field.
     * 
     * @param pId
     *            Identifier Technical identifier
     */
    public void setId(String pId) {
        id = pId;
    }

    /**
     * get multivalued.
     * 
     * @return the multivalued
     */
    public Boolean getMultivalued() {
        return multivalued;
    }

    /**
     * get mandatory.
     * 
     * @return the mandatory
     */
    public Boolean getMandatory() {
        return mandatory;
    }

    /**
     * get updatable.
     * 
     * @return the updatable
     */
    public Boolean getUpdatable() {
        return updatable;
    }

    /**
     * get confidential.
     * 
     * @return the confidential
     */
    public Boolean getConfidential() {
        return confidential;
    }

    /**
     * get exportable.
     * 
     * @return the exportable
     */
    public Boolean getExportable() {
        return exportable;
    }

    /**
     * get multiple.
     * 
     * @return the multiple
     */
    public Boolean getMultiple() {
        return false;
    }

    /**
     * Gets the technical identifier of the multiple field containing this
     * field.
     * 
     * @return The technical identifier of the multiple field containing this
     *         field.
     */
    public String getMultipleField() {
        return multipleField;
    }

    /**
     * Sets the technical identifier of the multiple field containing this
     * field.
     * 
     * @param pMultipleField multiple field
     */
    public void setMultipleField(String pMultipleField) {
        multipleField = pMultipleField;
    }

    /**
     * Indicates if current field points on another field or not
     * 
     * @return value of pointerField attribute
     */
    public boolean isPointerField() {
        return pointerField;
    }

    /**
     * Set the pointer field attribute.
     * 
     * @param pPointerField
     *            true for pointers , false otherwise
     */
    public void setPointerField(boolean pPointerField) {
        this.pointerField = pPointerField;
    }

    /**
     * Get the link type (NULL, or link type name if automatic fill of pointer
     * fields)
     * 
     * @return link type name
     */
    public String getReferencedLinkType() {
        return referencedLinkType;
    }

    /**
     * Set the link type name for automatic fill of pointer fields
     * 
     * @param pReferencedLinkType
     *            the link type name
     */
    public void setReferencedLinkType(String pReferencedLinkType) {
        this.referencedLinkType = pReferencedLinkType;
    }

    /**
     * Get the referenced field label (NULL, or linked sheet field label if
     * automatic fill of pointer fields)
     * 
     * @return referenced field label key
     */
    public String getReferencedFieldLabel() {
        return referencedFieldLabel;
    }

    /**
     * Set the referenced field label for automatic fill of pointer fields
     * 
     * @param pReferencedFieldLabel
     *            the Referenced field label key
     */
    public void setReferencedFieldLabel(String pReferencedFieldLabel) {
        this.referencedFieldLabel = pReferencedFieldLabel;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {
        if (pOther instanceof Field) {
            return StringUtils.equals(getLabelKey(),
                    ((Field) pOther).getLabelKey());
        }
        // else
        return super.equals(pOther);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(labelKey).toHashCode();
    }
}
