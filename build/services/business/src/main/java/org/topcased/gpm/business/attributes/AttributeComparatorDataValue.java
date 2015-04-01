/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
//
// Attention: Generated code! Do not modify by hand!
// Generated by: ValueObject.vsl in andromda-java-cartridge.
//
package org.topcased.gpm.business.attributes;

/**
 * AttributeComparatorDataValue class
 * 
 * @author Atos
 */
public class AttributeComparatorDataValue implements java.io.Serializable {
    
    /**
     * Default serial UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public AttributeComparatorDataValue() {
    }

    /**
     * Constructor taking all properties.
     * 
     * @param pValues the value array
     */
    public AttributeComparatorDataValue(final java.lang.String[] pValues) {
        this.values = pValues;
    }

    /**
     * Copies constructor from other AttributeComparatorDataValue
     * 
     * @param pOtherBean the bean to be copied
     */
    public AttributeComparatorDataValue(AttributeComparatorDataValue pOtherBean) {
        if (pOtherBean != null) {
            this.values = pOtherBean.getValues();
        }
    }

    private java.lang.String[] values;

    /**
     * Return the value array
     * 
     * @return a value array
     */
    public java.lang.String[] getValues() {
        return this.values;
    }

    public void setValues(java.lang.String[] pValues) {
        this.values = pValues;
    }

}