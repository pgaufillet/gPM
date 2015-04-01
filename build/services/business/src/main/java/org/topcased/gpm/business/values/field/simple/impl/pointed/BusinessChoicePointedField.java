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
import org.topcased.gpm.business.values.field.simple.BusinessChoiceField;

/**
 * BusinessChoicePointedField
 * 
 * @author nveillet
 */
public class BusinessChoicePointedField extends AbstractBusinessPointedField
        implements BusinessChoiceField {

    final private String categoryValue;

    /**
     * Constructor
     * 
     * @param pFieldName
     *            The field name
     * @param pFieldDescription
     *            The field description
     * @param pCategoryValue
     *            The category value
     */
    public BusinessChoicePointedField(String pFieldName,
            String pFieldDescription, String pCategoryValue) {
        super(pFieldName, pFieldDescription);
        categoryValue = pCategoryValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.impl.pointed.AbstractBusinessPointedField#getAsString()
     */
    @Override
    public String getAsString() {
        return categoryValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessChoiceField#getCategoryValue()
     */
    public String getCategoryValue() {
        return categoryValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    public boolean hasSameValues(BusinessField pOther) {
        String lValue1 = categoryValue;
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
     * @see org.topcased.gpm.business.values.field.simple.BusinessChoiceField#setCategoryValue(java.lang.String)
     */
    public void setCategoryValue(String pValue) {
        throw new MethodNotImplementedException("setCategoryValue");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.impl.pointed.AbstractBusinessPointedField#toString()
     */
    @Override
    public String toString() {
        return categoryValue;
    }
}
