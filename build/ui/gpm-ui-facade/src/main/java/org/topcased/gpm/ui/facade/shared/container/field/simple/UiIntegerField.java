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
import org.topcased.gpm.business.values.field.simple.BusinessIntegerField;
import org.topcased.gpm.ui.facade.shared.util.GpmStringUtils;

/**
 * UiIntegerField
 * 
 * @author nveillet
 */
public class UiIntegerField extends UiSimpleField<Integer> implements
        BusinessIntegerField {

    /** serialVersionUID */
    private static final long serialVersionUID = -7169554133637918596L;

    /**
     * Create new UiIntegerField
     */
    public UiIntegerField() {
        super(FieldType.INTEGER);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessIntegerField#decrement()
     */
    @Override
    public void decrement() {
        value--;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.shared.container.field.simple.UiSimpleField#get()
     */
    @Override
    public Integer get() {
        return value;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.shared.container.field.simple.UiSimpleField#getAsString()
     */
    @Override
    public String getAsString() {
        if (value != null) {
            return value.toString();
        }
        return new String();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#getEmptyClone()
     */
    @Override
    public UiIntegerField getEmptyClone() {
        UiIntegerField lField = new UiIntegerField();
        lField.setFieldName(getFieldName());
        lField.setFieldDescription(getFieldDescription());
        lField.setMandatory(isMandatory());
        lField.setUpdatable(isUpdatable());

        return lField;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessIntegerField#increment()
     */
    @Override
    public void increment() {
        value++;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.shared.container.field.simple.UiSimpleField#set(java.lang.Object)
     */
    @Override
    public void set(Integer pValue) {
        value = pValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.shared.container.field.simple.UiSimpleField#setAsString(java.lang.String)
     */
    @Override
    public void setAsString(String pValue) {
        if (GpmStringUtils.isEmptyOrNull(pValue)) {
            set(null);
        }
        else {
            try {
                set(Integer.valueOf(pValue));
            }
            catch (NumberFormatException e) {
                set(null);
            }
        }
    }
}
