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

import org.topcased.gpm.business.fields.MultipleLineFieldData;
import org.topcased.gpm.instantiation.fieldaccess.FieldAccess;

/**
 * @author sie
 */
public class MultipleFieldAccess extends FieldCompositeAccess implements
        org.topcased.gpm.instantiation.fieldaccess.MultipleFieldAccess {

    private MultipleLineFieldData data;

    private int line;

    private MultipleFieldAccess(MultipleLineFieldData pData, int pLine) {
        data = pData;
        line = pLine;
    }

    /**
     * @param pData
     * @param pLine
     * @return
     */
    public static FieldAccess create(MultipleLineFieldData pData, int pLine) {
        if (pData.isMultiField()) {
            return new MultipleFieldAccess(pData, pLine);
        }
        else {
            return FieldElementAccess.create(pData, pLine, 0);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.MultipleFieldAccessI#getDisplayHint()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getDisplayHint()
     */
    public String getDisplayHint() {
        return "MultipleField";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.MultipleFieldAccessI#getName()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getName()
     */
    public String getName() {
        return data.getLabelKey();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.MultipleFieldAccessI#getType()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getType()
     */
    public String getType() {
        return "MultipleField";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.MultipleFieldAccessI#getRef()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.MultipleFieldAccess#getRef()
     */
    public long getRef() {
        return (data.getLineFieldDatas())[line].getRef();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.MultipleFieldAccessI#isExportable()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.MultipleFieldAccess#isExportable()
     */
    public boolean isExportable() {
        return data.isExportable();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.MultipleFieldAccessI#isConfidential()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.MultipleFieldAccess#isConfidential()
     */
    public boolean isConfidential() {
        return data.isConfidential();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.MultipleFieldAccessI#getI18nName()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.MultipleFieldAccess#getI18nName()
     */
    public String getI18nName() {
        return data.getI18nName();
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
        return new LineFieldDataAccessIterator(data, line);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.MultipleFieldAccessI#size()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.impl.FieldCompositeAccess#size()
     */
    @Override
    public long size() {
        return (data.getLineFieldDatas())[line].getFieldDatas().length;
    }

    private class LineFieldDataAccessIterator implements Iterator<FieldAccess> {
        private MultipleLineFieldData data;

        private int index;

        private int line;

        public LineFieldDataAccessIterator(MultipleLineFieldData pData,
                int pLine) {
            data = pData;
            line = pLine;
            index = 0;
        }

        public boolean hasNext() {
            return index < ((data.getLineFieldDatas())[line].getFieldDatas().length);
        }

        public FieldAccess next() {
            FieldAccess lResult = FieldElementAccess.create(data, line, index);
            index++;
            return lResult;
        }

        public void remove() {
            throw new RuntimeException("remove is not supported");
        }
    }
}
