/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.util.lang.CollectionUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Store an extended attribute.
 * 
 * @author llatil
 */

@XStreamAlias("attribute")
public class Attribute extends NamedElement {

    /** Generated UID. */
    private static final long serialVersionUID = -6052005731455035001L;

    /** The value. */
    @XStreamAsAttribute
    private String value;

    /** The values. */
    @XStreamImplicit(itemFieldName = "attributeValue")
    private List<AttributeValue> values;

    /**
     * Constructs a new attribute.
     */
    public Attribute() {
        super();
    }

    /**
     * Constructs a new attribute.
     * 
     * @param pName
     *            the name
     * @param pValues
     *            the values
     */
    public Attribute(String pName, List<AttributeValue> pValues) {
        super(pName);
        values = pValues;
    }

    /**
     * Constructs a new attribute.
     * 
     * @param pName
     *            the name
     * @param pValue
     *            the single value to set to this attribute
     */
    public Attribute(String pName, String pValue) {
        super(pName);
        value = pValue;
    }

    /**
     * Get the value of the attribute.
     * 
     * @return Attribute value.
     */
    public String[] getValues() {
        String[] lValues;

        if (null != value) {
            lValues = new String[] { value };
        }
        else {
            if (null == values || 0 == values.size()) {
                return new String[0];
            }
            lValues = new String[values.size()];
            int i = 0;
            for (AttributeValue lValue : values) {
                lValues[i++] = lValue.getValue();
            }
        }
        return lValues;
    }

    /**
     * Set the list of values
     * 
     * @param pValues
     *            list of values to set
     */
    public void setValues(String[] pValues) {
        List<AttributeValue> lValues =
                new ArrayList<AttributeValue>(pValues.length);
        for (String lValue : pValues) {
            lValues.add(new AttributeValue(lValue));
        }
        values = lValues;
    }

    /**
     * Set a single value to the attribute.
     * 
     * @param pValue
     *            Value to set.
     */
    public final void setValue(String pValue) {
        value = pValue;
    }

    /**
     * Get the attribute value.
     * 
     * @return the attribute value
     */
    public String getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {

        if (pOther instanceof Attribute) {
            Attribute lOther = (Attribute) pOther;

            if (!super.equals(lOther)) {
                return false;
            }
            if (!StringUtils.equals(value, lOther.value)) {
                return false;
            }
            if (!CollectionUtils.equals(values, lOther.values)) {
                return false;
            }
            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        // NamedElement hashcode is sufficient
        return super.hashCode();
    }
}
