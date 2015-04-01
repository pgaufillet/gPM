/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.instantiation.fieldaccess.impl;

import org.topcased.gpm.business.fields.AttachedFieldModificationData;
import org.topcased.gpm.business.fields.FieldAvailableValueData;
import org.topcased.gpm.business.fields.FieldData;
import org.topcased.gpm.business.fields.FieldValueData;
import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.business.fields.MultipleLineFieldData;
import org.topcased.gpm.business.fields.TextAreaSize;
import org.topcased.gpm.instantiation.fieldaccess.FieldAccess;

/**
 * @author sie
 */
public class FieldElementAccess implements FieldAccess,
        org.topcased.gpm.instantiation.fieldaccess.FieldElementAccess {
    private int lineNumber;

    private int fieldNumber;

    private FieldData lFieldData;

    private FieldElementAccess(MultipleLineFieldData pData, int pLineNumber,
            int pFieldNumber) {
        setLineNumber(pLineNumber);
        setFieldNumber(pFieldNumber);
        lFieldData =
                ((pData.getLineFieldDatas()[pLineNumber]).getFieldDatas()[pFieldNumber]);
    }

    /**
     * @param pData
     * @param pFieldNumber
     */
    public FieldElementAccess(LineFieldData pData, int pFieldNumber) {
        setLineNumber(0);
        setFieldNumber(pFieldNumber);
        lFieldData = pData.getFieldDatas()[pFieldNumber];
    }

    // Use a factory to be ready in case we must implements different class
    // depending of FieldType.
    /**
     * @param pData
     * @param pLineNumber
     * @param pFieldNumber
     * @return
     */
    public static FieldElementAccess create(MultipleLineFieldData pData,
            int pLineNumber, int pFieldNumber) {
        return new FieldElementAccess(pData, pLineNumber, pFieldNumber);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getDisplayHint()
     */
    public String getDisplayHint() {
        return lFieldData.getDisplayType();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getName()
     */
    public String getName() {
        return lFieldData.getLabelKey();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getType()
     */
    public String getType() {
        // TODO Auto-generated method stub
        return lFieldData.getFieldType();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldElementAccess#getValues()
     */
    public String[] getValues() {
        return lFieldData.getValues().getValues();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldElementAccess#setValues(java.lang.String[])
     */
    public void setValues(String[] pValues) {
        lFieldData.setValues(new FieldValueData(pValues));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldElementAccess#isConfidential()
     */
    public boolean isConfidential() {
        return lFieldData.isConfidential();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldElementAccess#getDescription()
     */
    public String getDescription() {
        return lFieldData.getDescription();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldElementAccess#isMandatory()
     */
    public boolean isMandatory() {
        return lFieldData.isMandatory();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldElementAccess#isUpdateable()
     */
    public boolean isUpdateable() {
        return lFieldData.isUpdatable();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldElementAccess#isExportable()
     */
    public boolean isExportable() {
        return lFieldData.isExportable();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldElementAccess#getI18nName()
     */
    public String getI18nName() {
        return lFieldData.getI18nName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldElementAccess#getAvailableValues()
     */
    public String[] getAvailableValues() {
        FieldAvailableValueData lAvailable =
                lFieldData.getFieldAvailableValueData();
        return lAvailable.getValues();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldElementAccess#getFileValue()
     */
    public AttachedFieldModificationData getFileValue() {
        return lFieldData.getFileValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldElementAccess#setFileValue(org.topcased.gpm.business.fields.AttachedFieldModificationData)
     */
    public void setFileValue(AttachedFieldModificationData pData) {
        lFieldData.setFileValue(pData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldElementAccess#getTextAreaSize()
     */
    public TextAreaSize getTextAreaSize() {
        return lFieldData.getTextAreaSize();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getField(java.lang.String)
     */
    public FieldAccess getField(String pName) {
        if (pName.equals(getName())) {
            return this;
        }
        else {
            return null;
        }
    }

    /**
     * Set the lineNumber
     * 
     * @param pLineNumber
     *            the new lineNumber
     */
    public void setLineNumber(int pLineNumber) {
        this.lineNumber = pLineNumber;
    }

    /**
     * Get the lineNumber
     * 
     * @return the lineNumber
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Set the fieldNumber
     * 
     * @param pFieldNumber
     *            the new fieldNumber
     */
    public void setFieldNumber(int pFieldNumber) {
        this.fieldNumber = pFieldNumber;
    }

    /**
     * Get the fieldNumber
     * 
     * @return the fieldNumber
     */
    public int getFieldNumber() {
        return fieldNumber;
    }
}
