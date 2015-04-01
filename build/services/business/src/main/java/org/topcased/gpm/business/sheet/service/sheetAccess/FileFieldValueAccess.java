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
 * @author llatil
 */
public class FileFieldValueAccess extends FieldDataAccess implements
        FileFieldValue {

    /**
     * @param pFieldData
     */
    public FileFieldValueAccess(final FieldData pFieldData) {
        super(pFieldData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FileFieldValue#getFilename()
     */
    public String getFilename() {
        return fieldData.getFileValue().getName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FileFieldValue#getMimeType()
     */
    public String getMimeType() {
        return fieldData.getFileValue().getMimeType();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FileFieldValue#getId()
     */
    public String getId() {
        return fieldData.getFileValue().getId();
    }
}
