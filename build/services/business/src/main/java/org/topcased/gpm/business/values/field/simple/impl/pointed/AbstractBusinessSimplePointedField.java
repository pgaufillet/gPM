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

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exception.MethodNotImplementedException;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.impl.pointed.AbstractBusinessPointedField;
import org.topcased.gpm.business.values.field.simple.BusinessSimpleField;

/**
 * AbstractBusinessSimplePointedField
 * 
 * @author nveillet
 * @param <T>
 *            The type of the simple field.
 */
public abstract class AbstractBusinessSimplePointedField<T> extends
        AbstractBusinessPointedField implements BusinessSimpleField<T> {

    final private T value;

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
    public AbstractBusinessSimplePointedField(String pFieldName,
            String pFieldDescription, T pValue) {
        super(pFieldName, pFieldDescription);
        value = pValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#get()
     */
    public T get() {
        return value;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.impl.pointed.AbstractBusinessPointedField#getAsString()
     */
    @Override
    public abstract String getAsString();

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    public boolean hasSameValues(BusinessField pOther) {
        String lValue1 = getAsString();
        String lValue2 = pOther.getAsString();

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
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#set(java.lang.Object)
     */
    public void set(T pValue) {
        throw new MethodNotImplementedException("set");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#setAsString(java.lang.String)
     */
    public void setAsString(String pValue) {
        throw new MethodNotImplementedException("setAsString");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.impl.pointed.AbstractBusinessPointedField#toString()
     */
    @Override
    public String toString() {
        return getAsString();
    }
}
