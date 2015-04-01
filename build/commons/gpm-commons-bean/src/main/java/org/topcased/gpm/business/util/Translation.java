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

import java.io.Serializable;

/**
 * Translation
 * 
 * @author nveillet
 */
public class Translation implements Serializable, Comparable<String> {

    /** serialVersionUID */
    private static final long serialVersionUID = -6041293296202634879L;

    private String translatedValue;

    private String value;

    /**
     * Empty Constructor
     */
    protected Translation() {
    }

    /**
     * Constructor
     * 
     * @param pValue
     *            the original value
     * @param pTranslatedValue
     *            the translated value
     */
    public Translation(String pValue, String pTranslatedValue) {
        value = pValue;
        translatedValue = pTranslatedValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(String pOtherString) {
        return value.compareTo(pOtherString);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pObj) {
        if (pObj instanceof Translation) {
            Translation lTranslation = (Translation) pObj;
            return value.equals(lTranslation.value)
                    && translatedValue.equals(lTranslation.translatedValue);
        }
        // XXX Dodgy code: a translation cannot be equal to a String
        else if (pObj instanceof String) {
            return value.equals((String) pObj);
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return value.hashCode();
    }
    
    /**
     * get translated value
     * 
     * @return the translated value
     */
    public String getTranslatedValue() {
        return translatedValue;
    }

    /**
     * get original value
     * 
     * @return the original value
     */
    public String getValue() {
        return value;
    }

    /**
     * set translated value
     * 
     * @param pTranslatedValue
     *            the translated value to set
     */
    public void setTranslatedValue(String pTranslatedValue) {
        translatedValue = pTranslatedValue;
    }

    /**
     * set original value
     * 
     * @param pValue
     *            the original value to set
     */
    public void setValue(String pValue) {
        value = pValue;
    }

}
