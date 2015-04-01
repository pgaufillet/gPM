/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.container.field.simple;

import java.util.Date;

import org.topcased.gpm.business.util.DateDisplayHintType;
import org.topcased.gpm.business.util.FieldType;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessDateField;
import org.topcased.gpm.business.values.field.simple.BusinessPointerField;
import org.topcased.gpm.business.values.field.simple.BusinessSimpleField;
import org.topcased.gpm.ui.facade.shared.exception.NotImplementedException;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * UiDateField
 * 
 * @author nveillet
 */
public class UiDateField extends UiSimpleField<Date> implements
        BusinessDateField {

    /** serialVersionUID */
    private static final long serialVersionUID = -1158624891979796619L;

    private DateDisplayHintType dateDisplayHintType;

    /**
     * Create new UiDateField
     */
    public UiDateField() {
        super(FieldType.DATE);
        dateDisplayHintType = DateDisplayHintType.DATE_SHORT;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void copy(BusinessField pOther) {
    	// if the field is the pointer field, get the pointed value
    	if (pOther instanceof BusinessPointerField) {
    		BusinessPointerField lBusinessPointerField =
    				(BusinessPointerField) pOther;
    		if (lBusinessPointerField.getPointedContainerId() != null) {
    			BusinessField lPointedField =
    					lBusinessPointerField.getPointedField();
    			if (lPointedField instanceof BusinessDateField) {
    				value = ((BusinessDateField) lPointedField).get();
    			}
    		}
    	} else {
    		value = ((BusinessSimpleField<Date>) pOther).get();
    	}
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField#get()
     */
    @Override
    public Date get() {
        return value;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField#getAsString()
     */
    @Override
    public String getAsString() {
        final String lValue;
        final Date lDateValue = get();
        if (lDateValue == null) {
            lValue = null;
        }
        else {
            lValue = DateTimeFormat.getFullDateTimeFormat().format(get());
        }
        return lValue;
    }

    /**
     * Get the date display hint type
     * 
     * @return The date display hint type
     */
    public DateDisplayHintType getDateDisplayHintType() {
        return dateDisplayHintType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#getEmptyClone()
     */
    @Override
    public UiDateField getEmptyClone() {
        UiDateField lField = new UiDateField();
        lField.setFieldName(getFieldName());
        lField.setFieldDescription(getFieldDescription());
        lField.setMandatory(isMandatory());
        lField.setUpdatable(isUpdatable());

        lField.setDateDisplayHintType(dateDisplayHintType);

        return lField;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean hasSameValues(BusinessField pOther) {
        if (value == null) {
            return ((BusinessSimpleField<Date>) pOther).get() == null;
        }
        else {
            return value.equals(((BusinessSimpleField<Date>) pOther).get());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField#set(java.lang.Object)
     */
    @Override
    public void set(Date pValue) {
        value = pValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField#setAsString(java.lang.String)
     */
    @Override
    public void setAsString(String pValue) {
        throw new NotImplementedException(
                "setAsString is not implemented, use set method.");
    }

    /**
     * Set the date display hint type
     * 
     * @param pDateDisplayHintType
     *            The date display hint type to set
     */
    public void setDateDisplayHintType(DateDisplayHintType pDateDisplayHintType) {
        dateDisplayHintType = pDateDisplayHintType;
    }
}
