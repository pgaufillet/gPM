/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.util;

/**
 * List the anchor target.
 * 
 * @author mkargbo
 */
public enum GpmAnchorTarget {
    BLANK("_blank"), SELF("_self");

    private String value;

    /**
     * Create an GpmAnchorTarget.
     * 
     * @param pValue
     *            The value.
     */
    private GpmAnchorTarget(final String pValue) {
        value = pValue;
    }

    /**
     * Get the value.
     * 
     * @return The value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the value.
     * 
     * @param pValue
     *            The value.
     */
    public void setValue(final String pValue) {
        value = pValue;
    }
}