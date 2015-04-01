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

import java.util.Date;

import org.topcased.gpm.business.values.field.simple.BusinessDateField;
import org.topcased.gpm.domain.util.FieldsUtil;

/**
 * BusinessDatePointedField
 * 
 * @author nveillet
 */
public class BusinessDatePointedField extends
        AbstractBusinessSimplePointedField<Date> implements BusinessDateField {

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
    public BusinessDatePointedField(String pFieldName,
            String pFieldDescription, Date pValue) {
        super(pFieldName, pFieldDescription, pValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.impl.pointed.AbstractBusinessSimplePointedField#getAsString()
     */
    @Override
    public String getAsString() {
        Date lValue = get();
        if (lValue == null) {
            return null;
        }
        return FieldsUtil.formatDate(lValue);
    }
}
