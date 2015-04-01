/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.container.field.multiple;

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.topcased.gpm.business.util.FieldType;
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
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.exception.NotImplementedException;

/**
 * UiMultipleField
 * 
 * @author nveillet
 */
public class UiMultipleField extends UiField implements BusinessMultipleField {

    /** serialVersionUID */
    private static final long serialVersionUID = -3466348676575039853L;

    private LinkedHashMap<String, BusinessField> fields;

    /**
     * Constructor
     */
    public UiMultipleField() {
        super(FieldType.MULTIPLE);
        fields = new LinkedHashMap<String, BusinessField>();
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
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public void copy(BusinessField pOther) {
        final BusinessMultipleField lOther = ((BusinessMultipleField) pOther);

        for (BusinessField lOtherSubField : lOther) {
            final BusinessField lSubField =
                    fields.get(lOtherSubField.getFieldName());

            if (lSubField != null) {
                lSubField.copy(lOtherSubField);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#getAsString()
     */
    @Override
    public String getAsString() {
        throw new NotImplementedException(
                "getAsString is not implemented, use get method.");
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
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#getEmptyClone()
     */
    @Override
    public UiMultipleField getEmptyClone() {
        UiMultipleField lField = new UiMultipleField();
        lField.setFieldName(getFieldName());
        lField.setFieldDescription(getFieldDescription());
        lField.setMandatory(isMandatory());
        lField.setUpdatable(isUpdatable());

        for (BusinessField lSubField : fields.values()) {
            lField.addField(((UiField) lSubField).getEmptyClone());
        }

        return lField;
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
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(BusinessField pOther) {
        for (BusinessField lOtherField : (BusinessMultipleField) pOther) {
            if (!fields.get(lOtherField.getFieldName()).hasSameValues(
                    lOtherField)) {
                return false;
            }
        }

        return true;
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

}
