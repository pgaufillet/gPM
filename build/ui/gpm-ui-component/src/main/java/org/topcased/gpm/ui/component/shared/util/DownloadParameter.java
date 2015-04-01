/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.shared.util;

/**
 * Download enumeration.
 * <p>
 * Expected values for the download servlet (GET parameters)
 * </p>
 * 
 * @author mkargbo
 */
public enum DownloadParameter {
    /** Type of the export, expected values 'type_export', 'type_attached_file' */
    TYPE(),
    /** Identifier of the file to download */
    FILE_ID(),
    /** Name of product from which the user is logged */
    PRODUCT_NAME(),
    /** Format of the export */
    FORMAT(),
    /** For export */
    TYPE_EXPORT(),
    /** For attached file */
    TYPE_ATTACHED_FILE(), FILE_NAME(), FILE_MIME_TYPE();
}