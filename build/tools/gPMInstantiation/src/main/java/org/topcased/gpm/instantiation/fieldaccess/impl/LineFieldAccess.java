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

import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.instantiation.fieldaccess.FieldAccess;

/**
 * @author llatil
 */
public class LineFieldAccess extends FieldCompositeAccess implements
        org.topcased.gpm.instantiation.fieldaccess.MultipleLineAccess {
    private LineFieldData data;

    /**
     * @param pLineFieldData
     */
    public LineFieldAccess(LineFieldData pLineFieldData) {
        data = pLineFieldData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.MultipleLineAccess#addLine()
     */
    public FieldAccess addLine() {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.MultipleLineAccess#removeLine(int)
     */
    public void removeLine(int pIndex) {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.impl.FieldCompositeAccess#size()
     */
    @Override
    public long size() {
        return 1;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getDisplayHint()
     */
    public String getDisplayHint() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getName()
     */
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getType()
     */
    public String getType() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.MultipleFieldAccessI#iterator()
     */
    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<FieldAccess> iterator() {
        return new LineFieldDataAccessIterator(data);
    }

    private class LineFieldDataAccessIterator implements Iterator<FieldAccess> {
        private LineFieldData data;

        private int index;

        public LineFieldDataAccessIterator(LineFieldData pData) {
            data = pData;
            index = 0;
        }

        public boolean hasNext() {
            return index < data.getFieldDatas().length;
        }

        public FieldAccess next() {
            FieldAccess lResult = new FieldElementAccess(data, index);
            index++;
            return lResult;
        }

        public void remove() {
            throw new RuntimeException("remove is not supported");
        }
    }
}
