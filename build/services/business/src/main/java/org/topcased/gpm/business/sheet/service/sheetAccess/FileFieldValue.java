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

/**
 * Access to an attached file field.
 * 
 * @author llatil
 */
public interface FileFieldValue extends FieldData {

    /**
     * Get the name of the file
     * 
     * @return Filename
     */
    String getFilename();

    /**
     * Get the MIME type of the file
     * 
     * @return MIME type
     */
    String getMimeType();

    /**
     * Get the field identifier (used to retrieve file content)
     * 
     * @return Identifier.
     */
    String getId();
}
