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

import org.topcased.gpm.business.values.field.simple.BusinessStringField;

/**
 * BusinessStringPointedField
 * 
 * @author nveillet
 */
public class BusinessStringPointedField extends
        AbstractBusinessSimplePointedField<String> implements
        BusinessStringField {

    private String internalUrlSheetReference;

    /**
     * Constructor
     * 
     * @param pFieldName
     *            The field name
     * @param pFieldDescription
     *            The field description
     * @param pValue
     *            The value
     * @param pInternalUrlSheetReference
     */
    public BusinessStringPointedField(String pFieldName,
            String pFieldDescription, String pValue,
            String pInternalUrlSheetReference) {
        super(pFieldName, pFieldDescription, pValue);
        internalUrlSheetReference = pInternalUrlSheetReference;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.impl.pointed.AbstractBusinessSimplePointedField#getAsString()
     */
    @Override
    public String getAsString() {
        return get();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessStringField#getSize()
     */
    @Override
    public int getSize() {
        return -1;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessStringField#getInternalUrlSheetReference()
     */
    @Override
    public String getInternalUrlSheetReference() {
        return internalUrlSheetReference;
    }
}
