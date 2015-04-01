/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Atos Origin
 ******************************************************************/
package org.topcased.gpm.serialization.options;

/**
 * The Class KindOption.
 * 
 * @author Atos Origin
 */
public class KindOption extends AdditionalOption {

    /** The export flag. */
    private long flag;

    /**
     * Constructs a new kind option.
     * 
     * @param pOptionName
     *            the option name
     * @param pFlag
     *            the flag
     */
    public KindOption(String pOptionName, long pFlag) {
        super(pOptionName);
        flag = pFlag;
    }

    /**
     * Constructs a new kind option.
     * 
     * @param pOptionName
     *            the option name
     * @param pDescription
     *            the description
     * @param pFlag
     *            the flag
     */
    public KindOption(String pOptionName, String pDescription, long pFlag) {
        super(pOptionName, pDescription);
        flag = pFlag;
    }

    /**
     * Gets the flag.
     * 
     * @return the flag
     */
    public final long getFlag() {
        return flag;
    }
}
