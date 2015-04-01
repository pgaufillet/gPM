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
 * ExportParameter
 * 
 * @author nveillet
 */
public enum ExportParameter {

    CSV_ESCAPE_CHARACTER("escapeCharacter"),

    CSV_QUOTE_CHARACTER("quoteCharacter"),

    CSV_SEPARATOR_CHARACTER("separatorCharacter");

    private final String value;

    /**
     * Constructor
     * 
     * @param pValue
     */
    private ExportParameter(String pValue) {
        value = pValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return value;
    }
}
