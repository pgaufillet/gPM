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

import org.topcased.gpm.business.util.FieldType;
import org.topcased.gpm.business.values.field.simple.BusinessBooleanField;

/**
 * UiBooleanField
 * 
 * @author nveillet
 */
public class UiBooleanField extends UiSimpleField<Boolean> implements
        BusinessBooleanField {

    /** serialVersionUID */
    private static final long serialVersionUID = 3576937343598084745L;

    /**
     * Create new UiBooleanField
     */
    public UiBooleanField() {
        super(FieldType.BOOLEAN);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField#get()
     */
    @Override
    public Boolean get() {
        return value;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField#getAsString()
     */
    @Override
    public String getAsString() {
        return value.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#getEmptyClone()
     */
    @Override
    public UiBooleanField getEmptyClone() {
        UiBooleanField lField = new UiBooleanField();
        lField.setFieldName(getFieldName());
        lField.setFieldDescription(getFieldDescription());
        lField.setMandatory(isMandatory());
        lField.setUpdatable(isUpdatable());

        return lField;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField#set(java.lang.Object)
     */
    @Override
    public void set(Boolean pValue) {
        value = pValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField#setAsString(java.lang.String)
     */
    @Override
    public void setAsString(String pValue) {
        value = Boolean.valueOf(pValue);
    }
}
