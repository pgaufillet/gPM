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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class FieldValueData.
 * 
 * @author llatil
 */
@XStreamAlias("fieldValue")
public class FieldValueData extends NamedElement {

    /** Generated UID */
    private static final long serialVersionUID = -8113476845823689575L;

    /** Value set on the field. */
    @XStreamAsAttribute
    protected String value;

    /** Label of the field */
    @XStreamAsAttribute
    private String label;

    /** List of values set on the field (if multi-valued). */
    @XStreamAlias(value = "fieldValues")
    private List<FieldValueData> fieldValues;

    /**
     * Constructs a new FieldValueData (default ctor).
     */
    public FieldValueData() {
        super();
    }

    /**
     * Constructs a new FieldValueData.
     * 
     * @param pName
     *            Name of the field
     */
    public FieldValueData(String pName) {
        super(pName);
    }

    /**
     * Constructs a new FieldValueData.
     * 
     * @param pName
     *            Name of the field
     * @param pValue
     *            Value of the field
     */
    public FieldValueData(String pName, String pValue) {
        super(pName);
        setValue(pValue);
    }

    /**
     * Constructs a new FieldValueData.
     * 
     * @param pName
     *            Name of the field
     * @param pSubfieldValues
     *            List of subfield values
     */
    public FieldValueData(String pName, List<FieldValueData> pSubfieldValues) {
        super(pName);
        fieldValues = pSubfieldValues;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value.
     * 
     * @param pValue
     *            the value
     */
    public void setValue(String pValue) {
        value = pValue;
    }

    /**
     * Gets the label.
     * 
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label.
     * 
     * @param pLabel
     *            the label
     */
    public void setLabel(String pLabel) {
        this.label = pLabel;
    }

    /**
     * Add a new child field value.
     * 
     * @param pFieldValue
     *            Child value to add
     */
    public void addFieldValue(FieldValueData pFieldValue) {
        if (null == fieldValues) {
            fieldValues = new ArrayList<FieldValueData>();
        }
        fieldValues.add(pFieldValue);
    }

    /**
     * Add a list of children field values.
     * 
     * @param pFieldValues
     *            List of children values to add
     */
    public void addFieldValues(List<FieldValueData> pFieldValues) {
        if (null == fieldValues) {
            fieldValues = new ArrayList<FieldValueData>(pFieldValues);
        }
        else {
            fieldValues.addAll(pFieldValues);
        }
    }

    /**
     * Gets the field values.
     * 
     * @return the field values
     */
    public List<FieldValueData> getFieldValues() {
        return fieldValues;
    }

    /**
     * Sets the field values.
     * 
     * @param pFieldValues
     *            the new field values
     */
    public void setFieldValues(List<FieldValueData> pFieldValues) {
        this.fieldValues = pFieldValues;
    }

    /**
     * Test if this value contains sub-values.
     * 
     * @return true if sub-values exist for this element.
     */
    public boolean hasFieldValues() {
        return (null != fieldValues && fieldValues.size() > 0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {
        if (this == pOther) {
            return true;
        }
        if (!(pOther instanceof FieldValueData)) {
            return false;
        }
        FieldValueData lOtherFVData = (FieldValueData) pOther;

        if (fieldValues == null) {
            return StringUtils.equals(value, lOtherFVData.value)
                    && (lOtherFVData.fieldValues == null)
                    && super.equals(pOther);
        }
        else if (lOtherFVData.fieldValues == null) {
            return StringUtils.equals(value, lOtherFVData.value)
                    && super.equals(pOther);
        }
        else {
            return StringUtils.equals(value, lOtherFVData.value)
                    && ((fieldValues == lOtherFVData.fieldValues)
                            || CollectionUtils.isEqualCollection(
                                    fieldValues, lOtherFVData.fieldValues))
                    && super.equals(pOther);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getName()).append(value).append(
                fieldValues).toHashCode();
    }

    /**
     * Compare the values of two FieldValueData
     * 
     * @param pOther
     *            The other FieldValueData to compare
     * @return The result of the comparison
     */
    public boolean sameValues(FieldValueData pOther) {
        String lValueA = value;
        String lValueB = pOther.getValue();

        // For Oracle, empty string is the same as null string
        if (lValueA == null) {
            lValueA = StringUtils.EMPTY;
        }
        if (lValueB == null) {
            lValueB = StringUtils.EMPTY;
        }

        try {
            // If number format, test the number value and not the string value
            return Float.parseFloat(lValueA) == Float.parseFloat(lValueB);
        }
        catch (NumberFormatException e) {
            // Not a number, test string value
            return lValueA.equals(lValueB);
        }
    }
}
