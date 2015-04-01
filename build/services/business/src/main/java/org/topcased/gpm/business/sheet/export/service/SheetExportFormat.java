/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Szadel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet.export.service;

/**
 * Various formats for the export of sheets.
 * 
 * @author tszadel
 */
public enum SheetExportFormat {

    /** PDF Format. */
    PDF("PDF"),

    /** XML Format. */
    XML("XML"),

    /** Excel Format. */
    EXCEL("XLS"),

    /** CSV Format. */
    CSV("CSV");

    private final String extension;

    /**
     * Constructor
     */
    private SheetExportFormat(String pExtension) {
        extension = pExtension;
    }

    /**
     * get extension
     * 
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }
}
