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

import java.util.Iterator;

import org.topcased.gpm.business.fields.AttachedFieldModificationData;
import org.topcased.gpm.business.fields.FieldValueData;
import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.business.fields.MultipleLineFieldData;
import org.topcased.gpm.instantiation.fieldaccess.FieldAccess;

/**
 * @author sie
 */
public class MultipleLineAccess extends FieldCompositeAccess implements
        org.topcased.gpm.instantiation.fieldaccess.MultipleLineAccess {
    private MultipleLineFieldData data;

    private MultipleLineAccess(MultipleLineFieldData pData) {
        data = pData;
    }

    /**
     * We use a factory to create the right instance. We create a real
     * MultipleLineFieldDataAccess if the field is multiLined else we call the
     * LineFieldDataAccess factory
     * 
     * @param pData
     * @return
     */
    public static FieldAccess create(MultipleLineFieldData pData) {
        if (pData.isMultiLined()) {
            return new MultipleLineAccess(pData);
        }
        else {
            return MultipleFieldAccess.create(pData, 0);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.MultipleLineAccessI#getDisplayHint()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getDisplayHint()
     */
    public String getDisplayHint() {
        return "MultipleLine";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.MultipleLineAccessI#getName()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getName()
     */
    public String getName() {
        if (data.isMultiLined()) {
            return data.getLabelKey();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.MultipleLineAccessI#getType()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getType()
     */
    public String getType() {
        return "MultipleLine";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.MultipleLineAccessI#addLine()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.MultipleLineAccess#addLine()
     */
    public FieldAccess addLine() {

        // multipleLineFieldData
        LineFieldData lFdTemplate = data.getLineFieldDatas()[0];

        // Create a new LineFieldData.
        LineFieldData lFd = new LineFieldData();
        lFd.setRef(data.getLineFieldDatas().length);

        // Create all FieldData for this line.
        org.topcased.gpm.business.fields.FieldData[] lFieldDataTab;
        lFieldDataTab =
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

            lFieldDataTab[i++] = lFdCopy;
        }
        lFd.setFieldDatas(lFieldDataTab);

        // Add the new LineFieldData to MultipleLineFieldData
        LineFieldData[] lFdArraySrc = data.getLineFieldDatas();
        LineFieldData[] lFdArrayDest =
                new LineFieldData[lFdArraySrc.length + 1];

        int j;
        for (j = 0; j < lFdArraySrc.length; j++) {
            lFdArrayDest[j] = lFdArraySrc[j];
        }

        // Add the new LineFieldData in the last array element
        lFdArrayDest[j] = lFd;
        data.setLineFieldDatas(lFdArrayDest);
        return MultipleFieldAccess.create(data, j);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.MultipleLineAccessI#removeLine(int)
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.MultipleLineAccess#removeLine(int)
     */
    public void removeLine(int pIndex) {
        LineFieldData[] lOld = data.getLineFieldDatas();
        if (pIndex >= lOld.length) {
            throw new RuntimeException(
                    "Invalid index for removeLine in MultipleLineFieldData");
        }
        LineFieldData[] lNew =
                new LineFieldData[data.getLineFieldDatas().length - 1];

        int i = 0;
        int j = 0;
        while (i < lOld.length) {
            if (i != pIndex) {
                lNew[j] = lOld[i];
                j++;
            }
            i++;
        }
        data.setLineFieldDatas(lNew);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.MultipleLineAccessI#size()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.impl.FieldCompositeAccess#size()
     */
    @Override
    public long size() {
        return data.getLineFieldDatas().length;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.MultipleLineAccessI#iterator()
     */
    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<FieldAccess> iterator() {

        // TODO Auto-generated method stub
        return new MultipleLineFieldDataAccessIterator(data);
    }

    private class MultipleLineFieldDataAccessIterator implements
            Iterator<FieldAccess> {
        private MultipleLineFieldData data;

        private int index;

        public MultipleLineFieldDataAccessIterator(MultipleLineFieldData pData) {
            data = pData;
            index = 0;
        }

        public boolean hasNext() {
            return index < data.getLineFieldDatas().length;
        }

        public FieldAccess next() {
            FieldAccess lResult = MultipleFieldAccess.create(data, index);
            index++;
            return lResult;
        }

        public void remove() {
            throw new RuntimeException("remove is not supported");
        }
    }
}
