/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet.service.sheetAccess;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.fields.AttachedFieldModificationData;
import org.topcased.gpm.business.fields.FieldValueData;
import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.business.fields.MultipleLineFieldData;

/**
 * @author llatil
 */
public class MultiLineFieldDataAccess implements MultiLineFieldData {

    public MultiLineFieldDataAccess(final MultipleLineFieldData pFieldData) {
        multipleLineFieldData = pFieldData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.MultiLineFieldData#size()
     */
    public int size() {
        return multipleLineFieldData.getLineFieldDatas().length;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.MultiLineFieldData#get(int)
     */
    public FieldData get(final int pIndex) {
        FieldData lResult = null;

        if (multipleLineFieldData.isMultiField()) {
            lResult =
                    new MultipleFieldDataAccess(multipleLineFieldData, pIndex);
        }
        else {
            lResult =
                    FieldDataFactory.create(multipleLineFieldData.getLineFieldDatas()[pIndex].getFieldDatas()[0]);
        }
        return lResult;
    }

    /**
     * Get the list of fields.
     * 
     * @return List of fields
     */
    public List<FieldData> getFields() {
        int lSize = size();
        List<FieldData> lResult = new ArrayList<FieldData>(lSize);

        for (int i = 0; i < lSize; ++i) {
            lResult.add(get(i));
        }
        return lResult;
    }

    /**
     * Add a new value in the field. {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.MultiLineFieldData#addLine()
     */
    public int addLine() {
        //multipleLineFieldData
        LineFieldData lFdTemplate =
                multipleLineFieldData.getLineFieldDatas()[0];

        // Create a new LineFieldData.
        LineFieldData lFd = new LineFieldData();

        // Create all FieldData for this line.
        org.topcased.gpm.business.fields.FieldData[] lFieldDatas;
        lFieldDatas =
                new org.topcased.gpm.business.fields.FieldData[lFdTemplate.getFieldDatas().length];

        int i = 0;
        for (org.topcased.gpm.business.fields.FieldData lFieldData : lFdTemplate.getFieldDatas()) {
            org.topcased.gpm.business.fields.FieldData lFdCopy;
            lFdCopy =
                    new org.topcased.gpm.business.fields.FieldData(lFieldData);

            if (lFdCopy.getValues() != null) {
                lFdCopy.setValues(new FieldValueData(new String[] { "" }));
            }

            if (lFdCopy.getFileValue() != null) {
                AttachedFieldModificationData lAfmd;
                lAfmd = new AttachedFieldModificationData();

                lAfmd.setName(null);
                lAfmd.setMimeType(null);
                lAfmd.setContent(null);
                lAfmd.setId(null);
            }

            lFieldDatas[i++] = lFdCopy;
        }
        lFd.setFieldDatas(lFieldDatas);

        // Add the new LineFieldData to MultipleLineFieldData
        LineFieldData[] lFdArraySrc = multipleLineFieldData.getLineFieldDatas();
        LineFieldData[] lFdArrayDest =
                new LineFieldData[lFdArraySrc.length + 1];

        int j;

        for (j = 0; j < lFdArraySrc.length; j++) {
            lFdArrayDest[j] = lFdArraySrc[j];
        }

        if (j == 0) {
            lFd.setRef(j);
        }
        else {
            // In some case, the ref starts to 1 instead of 0
            // After a cacheable conversion for example
            // So increment the ref of the previous last object
            lFd.setRef(lFdArrayDest[j - 1].getRef() + 1);
        }

        // Add the new LineFieldData in the last array element
        lFdArrayDest[j] = lFd;
        multipleLineFieldData.setLineFieldDatas(lFdArrayDest);

        return j;
    }

    /**
     * Remove a value from the field.
     * 
     * @param pLine
     *            line of the value to delete
     */
    public void removeLine(final int pLine) {
        // remove a LineFieldData to MultipleLineFieldData
        LineFieldData[] lFdArraySrc = multipleLineFieldData.getLineFieldDatas();
        LineFieldData[] lFdArrayDest =
                new LineFieldData[lFdArraySrc.length - 1];

        // Init the new array without the value to delete
        int i = 0;
        for (int j = 0; j < lFdArraySrc.length; j++) {
            if (j != pLine) {
                lFdArrayDest[i] = lFdArraySrc[j];
                i++;
            }
        }

        multipleLineFieldData.setLineFieldDatas(lFdArrayDest);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FieldData#getName()
     */
    public String getName() {
        return multipleLineFieldData.getI18nName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FieldData#getLabelKey()
     */
    public String getLabelKey() {
        return multipleLineFieldData.getLabelKey();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FieldData#getDescription()
     */
    public String getDescription() {
        return "";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FieldData#isMandatory()
     */
    public boolean isMandatory() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FieldData#isConfidential()
     */
    public boolean isConfidential() {
        if (isMultiField()) {
            return false;
        }
        // else
        org.topcased.gpm.business.fields.FieldData lFieldData;
        lFieldData = getFirstFieldData();
        if (null != lFieldData) {
            return lFieldData.isConfidential();
        }
        // else
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FieldData#isUpdatable()
     */
    public boolean isUpdatable() {
        if (isMultiField()) {
            return true;
        }
        else {
            org.topcased.gpm.business.fields.FieldData lFieldData;
            lFieldData = getFirstFieldData();
            if (null != lFieldData) {
                return lFieldData.isUpdatable();
            }
            else {
                return false;
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FieldData#isMultiLine()
     */
    public boolean isMultiLine() {
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FieldData#isMultiField()
     */
    public boolean isMultiField() {
        return multipleLineFieldData.isMultiField();
    }

    private org.topcased.gpm.business.fields.FieldData getFirstFieldData() {
        if (multipleLineFieldData.getLineFieldDatas().length != 0
                && multipleLineFieldData.getLineFieldDatas()[0].getFieldDatas().length != 0) {
            return multipleLineFieldData.getLineFieldDatas()[0].getFieldDatas()[0];
        }
        else {
            return null;
        }
    }

    private MultipleLineFieldData multipleLineFieldData;
}
