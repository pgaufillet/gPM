/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet.service.sheetAccess;

import org.topcased.gpm.business.fields.FieldData;

/**
 * The Class ChoiceFieldDataAccess.
 * 
 * @author llatil
 */
public class ChoiceFieldDataAccess extends SimpleFieldDataAccess implements
        ChoiceFieldData {

    /**
     * The Constructor.
     * 
     * @param pFieldData
     *            the p field data
     */
    public ChoiceFieldDataAccess(final FieldData pFieldData) {
        super(pFieldData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.ChoiceFieldData#getAcceptableValues()
     */
    public String[] getAcceptableValues() {
        return fieldData.getFieldAvailableValueData().getValues();
    }

    /**
     * {@inheritDoc}.
     * 
     * @see com.airbus.gdm.business.sheet.service.sheetAccess.ChoiceFieldData#getValues()
     */
    public String[] getValues() {
        return fieldData.getValues().getValues();
    }
}
