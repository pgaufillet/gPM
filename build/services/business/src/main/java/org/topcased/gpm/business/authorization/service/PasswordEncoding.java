/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Veillet (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.service;

/**
 * ApplicationAction
 * 
 * @author nveillet
 */
public enum PasswordEncoding {

    CLEAR("clear"), MD5("md5");

    private final String value;

    PasswordEncoding(String pValue) {
        value = pValue;
    }

    /**
     * Get the string value
     * 
     * @return Value
     */
    public String getValue() {
        return value;
    }

    static public PasswordEncoding get(String pValue) {
        PasswordEncoding[] lValues = values();

        for (PasswordEncoding lPasswordEncoding : lValues) {
            if (lPasswordEncoding.getValue().equals(pValue)) {
                return lPasswordEncoding;
            }
        }
        return null;
    }
}
