/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.instantiation.options;

/**
 * The Class AdditionalOption.
 * 
 * @author llatil
 */
public class AdditionalOption {

    /** The option name. */
    private String optionName;

    /** The description. */
    private String description;

    /**
     * Constructs a new additional option.
     * 
     * @param pOptionName
     *            the option name
     */
    public AdditionalOption(String pOptionName) {
        optionName = pOptionName;
    }

    /**
     * Constructs a new additional option.
     * 
     * @param pOptionName
     *            the option name
     * @param pDescription
     *            the description
     */
    public AdditionalOption(String pOptionName, String pDescription) {
        optionName = pOptionName;
        description = pDescription;
    }

    /**
     * Gets the option name.
     * 
     * @return the option name
     */
    public final String getOptionName() {
        return optionName;
    }

    /**
     * Gets the description.
     * 
     * @return the description
     */
    public final String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {
        if (pOther == this) {
            return true;
        }
        if (pOther != null && pOther.equals(optionName)) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return optionName.hashCode();
    }
}
