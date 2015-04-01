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

import org.topcased.gpm.business.exception.MethodNotImplementedException;
import org.topcased.gpm.business.values.field.simple.BusinessIntegerField;

/**
 * BusinessIntegerPointedField
 * 
 * @author nveillet
 */
public class BusinessIntegerPointedField extends
        AbstractBusinessSimplePointedField<Integer> implements
        BusinessIntegerField {

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
    public BusinessIntegerPointedField(String pFieldName,
            String pFieldDescription, Integer pValue) {
        super(pFieldName, pFieldDescription, pValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessIntegerField#decrement()
     */
    public void decrement() {
        throw new MethodNotImplementedException("decrement");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.impl.pointed.AbstractBusinessSimplePointedField#getAsString()
     */
    @Override
    public String getAsString() {
        Integer lValue = get();
        if (lValue == null) {
            return new String();
        }
        return lValue.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessIntegerField#increment()
     */
    public void increment() {
        throw new MethodNotImplementedException("increment");
    }
}
