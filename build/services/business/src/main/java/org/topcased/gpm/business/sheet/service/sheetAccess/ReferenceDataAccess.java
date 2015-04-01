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

import org.topcased.gpm.business.fields.LineFieldData;

/**
 * @author llatil
 */
public class ReferenceDataAccess extends MultipleFieldDataAccess implements
        ReferenceData {

    /**
     * @param pLineFieldData
     */
    public ReferenceDataAccess(final LineFieldData pLineFieldData) {
        super(pLineFieldData);
    }
}
