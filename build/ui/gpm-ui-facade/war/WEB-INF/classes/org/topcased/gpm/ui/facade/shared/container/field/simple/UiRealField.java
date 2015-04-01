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
import org.topcased.gpm.business.values.field.simple.BusinessRealField;
import org.topcased.gpm.ui.facade.shared.util.GpmStringUtils;

/**
 * UiRealField
 * 
 * @author nveillet
 */
public class UiRealField extends UiSimpleField<Double> implements
        BusinessRealField {

    /** serialVersionUID */
    private static final long serialVersionUID = -7122636394756895678L;

    /**
     * Create new UiRealField
     */
    public UiRealField() {
        super(FieldType.REAL);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.shared.container.field.simple.UiSimpleField#get()
     */
    @Override
    public Double get() {
        return value;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.shared.container.field.simple.UiSimpleField#getAsString()
     */
    @Override
    public String getAsString() {
        if (value == null) {
            return "";
        }

        return value.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#getEmptyClone()
     */
    @Override
    public UiRealField getEmptyClone() {
        UiRealField lField = new UiRealField();
        lField.setFieldName(getFieldName());
        lField.setFieldDescription(getFieldDescription());
        lField.setMandatory(isMandatory());
        lField.setUpdatable(isUpdatable());

        return lField;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.shared.container.field.simple.UiSimpleField#set(java.lang.Object)
     */
    @Override
    public void set(Double pValue) {
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
                set(Double.valueOf(pValue));
            }
            catch (NumberFormatException e) {
                set(null);
            }
        }
    }
}
