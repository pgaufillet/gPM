/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.values.field.simple.impl.pointed;

import org.topcased.gpm.business.values.field.simple.BusinessRealField;

/**
 * BusinessRealPointedField
 * 
 * @author nveillet
 */
public class BusinessRealPointedField extends
        AbstractBusinessSimplePointedField<Double> implements BusinessRealField {

    /**
     * Constructor
     * 
     * @param pFieldName
     *            The field name
     * @param pFieldDescription
     *            The field description
     * @param pValue
     *            The value
     */
    public BusinessRealPointedField(String pFieldName,
            String pFieldDescription, Double pValue) {
        super(pFieldName, pFieldDescription, pValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.impl.pointed.AbstractBusinessSimplePointedField#getAsString()
     */
    @Override
    public String getAsString() {
        Double lValue = get();
        if (lValue == null) {
            return new String();
        }
        return lValue.toString();
    }
}
