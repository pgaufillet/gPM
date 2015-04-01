/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.util;

/**
 * Various formats for the exports
 * 
 * @author nveillet
 */
public enum ExportFormat {
    /** PDF Format. */
    PDF,
    /** XML Format. */
    XML,
    /** Excel Format. */
    EXCEL,
    /** CSV Format. */
    CSV;

    /**
     * Returns the extension of the file for this type of file
     * 
     * @return The extension of the file for this type of file
     */
    public String getExtension() {
        switch (this) {
            case PDF:
                return "pdf";
            case XML:
                return "xml";
            case EXCEL:
                return "xls";
            case CSV:
                return "csv";
            default:
                return "csv";
        }
    };
}
