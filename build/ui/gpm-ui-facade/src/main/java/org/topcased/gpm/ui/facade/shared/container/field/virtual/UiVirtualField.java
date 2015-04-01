/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.container.field.virtual;

import org.topcased.gpm.business.util.FieldType;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.virtual.BusinessVirtualField;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;

/**
 * UiVirtualField
 * 
 * @author nveillet
 */
public class UiVirtualField extends UiField implements BusinessVirtualField {

    /** serialVersionUID */
    private static final long serialVersionUID = 8285988535621589560L;

    private String value;

    /**
     * Constructor
     */
    public UiVirtualField() {
        super(FieldType.VIRTUAL);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public void copy(BusinessField pOther) {
        value = pOther.getAsString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#getAsString()
     */
    @Override
    public String getAsString() {
        return value;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#getEmptyClone()
     */
    @Override
    public UiField getEmptyClone() {
        UiVirtualField lField = new UiVirtualField();

        lField.setFieldName(getFieldName());
        lField.setFieldDescription(getFieldDescription());
        lField.setMandatory(isMandatory());
        lField.setUpdatable(isUpdatable());

        return lField;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.virtual.BusinessVirtualField#getValue()
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(BusinessField pOther) {
        BusinessVirtualField lOther = (BusinessVirtualField) pOther;
        if (value == null) {
            return lOther.getValue() == null;
        }
        else {
            return value.equals(lOther.getValue());
        }
    }

    /**
     * set value
     * 
     * @param pValue
     *            the value to set
     */
    public void setValue(String pValue) {
        value = pValue;
    }

}
