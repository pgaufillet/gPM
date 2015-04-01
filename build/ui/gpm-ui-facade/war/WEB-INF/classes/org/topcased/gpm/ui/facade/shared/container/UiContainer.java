/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.container;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import org.topcased.gpm.business.values.BusinessContainer;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.multiple.BusinessMultipleField;
import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;
import org.topcased.gpm.business.values.field.simple.BusinessAttachedField;
import org.topcased.gpm.business.values.field.simple.BusinessBooleanField;
import org.topcased.gpm.business.values.field.simple.BusinessChoiceField;
import org.topcased.gpm.business.values.field.simple.BusinessDateField;
import org.topcased.gpm.business.values.field.simple.BusinessIntegerField;
import org.topcased.gpm.business.values.field.simple.BusinessPointerField;
import org.topcased.gpm.business.values.field.simple.BusinessRealField;
import org.topcased.gpm.business.values.field.simple.BusinessStringField;
import org.topcased.gpm.business.values.field.virtual.BusinessVirtualField;
import org.topcased.gpm.business.values.field.virtual.VirtualFieldType;

/**
 * UiContainer
 * 
 * @author nveillet
 */
public class UiContainer implements BusinessContainer, Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -5859741972503505468L;

    private String businessProcessName;

    private boolean confidential;

    private boolean deletable;

    private boolean duplicable;

    private HashMap<String, BusinessField> fields;

    private String id;

    private String typeDescription;

    private String typeId;

    private String typeName;

    private boolean updatable;

    /**
     * Create new UiContainer
     */
    public UiContainer() {
        fields = new HashMap<String, BusinessField>();
    }

    /**
     * Add a field
     * 
     * @param pField
     *            The field to add
     */
    public void addField(BusinessField pField) {
        fields.put(pField.getFieldName(), pField);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getAttachedField(java.lang.String)
     */
    @Override
    public BusinessAttachedField getAttachedField(String pFieldName) {
        return (BusinessAttachedField) fields.get(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getBooleanField(java.lang.String)
     */
    @Override
    public BusinessBooleanField getBooleanField(String pFieldName) {
        return (BusinessBooleanField) fields.get(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getBusinessProcessName()
     */
    @Override
    public String getBusinessProcessName() {
        return businessProcessName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getChoiceField(java.lang.String)
     */
    @Override
    public BusinessChoiceField getChoiceField(String pFieldName) {
        return (BusinessChoiceField) fields.get(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getDateField(java.lang.String)
     */
    @Override
    public BusinessDateField getDateField(String pFieldName) {
        return (BusinessDateField) fields.get(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getField(java.lang.String)
     */
    @Override
    public BusinessField getField(String pFieldName) {
        return fields.get(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getIntegerField(java.lang.String)
     */
    @Override
    public BusinessIntegerField getIntegerField(String pFieldName) {
        return (BusinessIntegerField) fields.get(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getMultipleField(java.lang.String)
     */
    @Override
    public BusinessMultipleField getMultipleField(String pFieldName) {
        return (BusinessMultipleField) fields.get(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getMultivaluedAttachedField(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public BusinessMultivaluedField<BusinessAttachedField> getMultivaluedAttachedField(
            String pFieldName) {
        return (BusinessMultivaluedField<BusinessAttachedField>) fields.get(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getMultivaluedBooleanField(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public BusinessMultivaluedField<BusinessBooleanField> getMultivaluedBooleanField(
            String pFieldName) {
        return (BusinessMultivaluedField<BusinessBooleanField>) fields.get(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getMultivaluedChoiceField(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public BusinessMultivaluedField<BusinessChoiceField> getMultivaluedChoiceField(
            String pFieldName) {
        return (BusinessMultivaluedField<BusinessChoiceField>) fields.get(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getMultivaluedDateField(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public BusinessMultivaluedField<BusinessDateField> getMultivaluedDateField(
            String pFieldName) {
        return (BusinessMultivaluedField<BusinessDateField>) fields.get(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getMultivaluedIntegerField(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public BusinessMultivaluedField<BusinessIntegerField> getMultivaluedIntegerField(
            String pFieldName) {
        return (BusinessMultivaluedField<BusinessIntegerField>) fields.get(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getMultivaluedMultipleField(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public BusinessMultivaluedField<BusinessMultipleField> getMultivaluedMultipleField(
            String pFieldName) {
        return (BusinessMultivaluedField<BusinessMultipleField>) fields.get(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getMultivaluedPointerField(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public BusinessMultivaluedField<BusinessPointerField> getMultivaluedPointerField(
            String pFieldName) {
        return (BusinessMultivaluedField<BusinessPointerField>) fields.get(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getMultivaluedRealField(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public BusinessMultivaluedField<BusinessRealField> getMultivaluedRealField(
            String pFieldName) {
        return (BusinessMultivaluedField<BusinessRealField>) fields.get(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getMultivaluedStringField(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public BusinessMultivaluedField<BusinessStringField> getMultivaluedStringField(
            String pFieldName) {
        return (BusinessMultivaluedField<BusinessStringField>) fields.get(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getPointerField(java.lang.String)
     */
    @Override
    public BusinessPointerField getPointerField(String pFieldName) {
        return (BusinessPointerField) fields.get(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getRealField(java.lang.String)
     */
    @Override
    public BusinessRealField getRealField(String pFieldName) {
        return (BusinessRealField) fields.get(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getStringField(java.lang.String)
     */
    @Override
    public BusinessStringField getStringField(String pFieldName) {
        return (BusinessStringField) fields.get(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getTypeDescription()
     */
    @Override
    public String getTypeDescription() {
        return typeDescription;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getTypeId()
     */
    @Override
    public String getTypeId() {
        return typeId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getTypeName()
     */
    @Override
    public String getTypeName() {
        return typeName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#getVirtualField(org.topcased.gpm.business.values.field.virtual.VirtualFieldType)
     */
    @Override
    public BusinessVirtualField getVirtualField(
            VirtualFieldType pVirtualFieldType) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#isConfidential()
     */
    @Override
    public boolean isConfidential() {
        return confidential;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#isDeletable()
     */
    @Override
    public boolean isDeletable() {
        return deletable;
    }

    /**
     * get duplicable access
     * 
     * @return the duplicable access
     */
    public boolean isDuplicable() {
        return duplicable;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.BusinessContainer#isUpdatable()
     */
    @Override
    public boolean isUpdatable() {
        return updatable;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<BusinessField> iterator() {
        return fields.values().iterator();
    }

    /**
     * Set the business process name
     * 
     * @param pBusinessProcessName
     *            The business process name to set
     */
    public void setBusinessProcessName(String pBusinessProcessName) {
        businessProcessName = pBusinessProcessName;
    }

    /**
     * Set deletable access
     * 
     * @param pDeletable
     *            The deletable access to set
     */
    public void setDeletable(boolean pDeletable) {
        deletable = pDeletable;
    }

    /**
     * set duplicable access
     * 
     * @param pDuplicable
     *            the duplicable access to set
     */
    public void setDuplicable(boolean pDuplicable) {
        duplicable = pDuplicable;
    }

    /**
     * Set the container id
     * 
     * @param pId
     *            The id to set
     */
    public void setId(String pId) {
        id = pId;
    }

    /**
     * Set the container type description
     * 
     * @param pTypeDescription
     *            The type description to set
     */
    public void setTypeDescription(String pTypeDescription) {
        typeDescription = pTypeDescription;
    }

    /**
     * Set the container type id
     * 
     * @param pTypeId
     *            The type id to set
     */
    public void setTypeId(String pTypeId) {
        typeId = pTypeId;
    }

    /**
     * Set the container type name
     * 
     * @param pTypeName
     *            The type name to set
     */
    public void setTypeName(String pTypeName) {
        typeName = pTypeName;
    }

    /**
     * Set updatable access
     * 
     * @param pUpdatable
     *            The updatable access to set
     */
    public void setUpdatable(boolean pUpdatable) {
        updatable = pUpdatable;
    }
}
