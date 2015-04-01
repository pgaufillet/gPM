/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.values.field.virtual.impl.pointed;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.impl.pointed.AbstractBusinessPointedField;
import org.topcased.gpm.business.values.field.virtual.BusinessVirtualField;

/**
 * BusinessVirtualPointedField
 * 
 * @author nveillet
 */
public class BusinessVirtualPointedField extends AbstractBusinessPointedField
        implements BusinessVirtualField {

    private String value;

    /**
     * Constructor
     * 
     * @param pFieldName
     *            The field name
     * @param pValue
     *            The value
     */
    public BusinessVirtualPointedField(String pFieldName, String pValue) {
        super(pFieldName, null);
        value = pValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.impl.pointed.AbstractBusinessPointedField#getAsString()
     */
    @Override
    public String getAsString() {
        return value;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.virtual.BusinessVirtualField#getValue()
     */
    public String getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    public boolean hasSameValues(BusinessField pOther) {
        if (!(pOther instanceof BusinessVirtualField)) {
            return false;
        }

        String lValue1 = getValue();
        String lValue2 = ((BusinessVirtualField) pOther).getValue();

        if (lValue1 == null) {
            lValue1 = StringUtils.EMPTY;
        }

        if (lValue2 == null) {
            lValue2 = StringUtils.EMPTY;
        }

        return lValue1.equals(lValue2);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.impl.pointed.AbstractBusinessPointedField#isMandatory()
     */
    @Override
    public Boolean isMandatory() {
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.impl.pointed.AbstractBusinessPointedField#toString()
     */
    @Override
    public String toString() {
        return value;
    }

}
