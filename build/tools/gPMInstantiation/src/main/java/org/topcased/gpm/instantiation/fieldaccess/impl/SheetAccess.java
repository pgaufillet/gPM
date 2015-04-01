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

import static org.apache.commons.lang.StringUtils.EMPTY;

import java.util.Iterator;

import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.business.fields.MultipleLineFieldData;
import org.topcased.gpm.business.sheet.service.FieldGroupData;
import org.topcased.gpm.business.sheet.service.SheetData;
import org.topcased.gpm.instantiation.fieldaccess.FieldAccess;

/**
 * @author sie
 */
public class SheetAccess extends FieldCompositeAccess implements
        org.topcased.gpm.instantiation.fieldaccess.SheetAccess {
    private final SheetData sheetData;

    /**
     * @param pSheetData
     */
    public SheetAccess(SheetData pSheetData) {
        sheetData = pSheetData;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.SheetAccessI#getDisplayHint()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getDisplayHint()
     */
    public String getDisplayHint() {
        return "Sheet";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.SheetAccessI#getName()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getName()
     */
    public String getName() {
        // A sheet has no name.
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.SheetAccessI#getType()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getType()
     */
    public String getType() {
        return "Sheet";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.SheetAccessI#getId()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.SheetAccess#getId()
     */
    public String getId() {
        return sheetData.getId();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.SheetAccessI#getProcessName()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.SheetAccess#getProcessName()
     */
    public String getProcessName() {
        return sheetData.getProcessName();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.SheetAccessI#getProductName()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.SheetAccess#getProductName()
     */
    public String getProductName() {
        return sheetData.getProductName();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.SheetAccessI#getReference()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.SheetAccess#getReference()
     */
    public org.topcased.gpm.instantiation.fieldaccess.MultipleFieldAccess getReference() {

        // To encapsulate the reference into a MultipleFieldAccess, we need to
        // create a dummy
        // MultipleLineFieldData with only purpose to contain that
        // LineFieldData.
        LineFieldData[] lLineList = new LineFieldData[1];
        lLineList[0] = sheetData.getReference();
        MultipleLineFieldData lMultipleLineFieldData =
                new MultipleLineFieldData(EMPTY, 0, false, true, true, EMPTY,
                        false, EMPTY, null, false, false, lLineList);

        return (org.topcased.gpm.instantiation.fieldaccess.MultipleFieldAccess) MultipleFieldAccess.create(
                lMultipleLineFieldData, 0);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.SheetAccessI#getStringReference()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.SheetAccess#getStringReference()
     */
    public String getStringReference() {
        return sheetData.getSheetReference();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.SheetAccessI#getVersion()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.SheetAccess#getVersion()
     */
    public int getVersion() {
        return sheetData.getVersion();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.SheetAccessI#getSheetTypeName()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.SheetAccess#getSheetTypeName()
     */
    public String getSheetTypeName() {
        return sheetData.getSheetTypeName();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.SheetAccessI#size()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.impl.FieldCompositeAccess#size()
     */
    @Override
    public long size() {
        return sheetData.getFieldGroupDatas().length;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.SheetAccessI#iterator()
     */
    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<FieldAccess> iterator() {
        return new SheetAccessIterator(sheetData.getFieldGroupDatas());
    }

    private class SheetAccessIterator implements Iterator<FieldAccess> {
        private final FieldGroupData[] fieldGroupData;

        private int index;

        public SheetAccessIterator(FieldGroupData[] pFieldGroupData) {
            fieldGroupData = pFieldGroupData;
            index = 0;
        }

        public boolean hasNext() {
            return index < fieldGroupData.length;
        }

        public FieldAccess next() {
            FieldGroupAccess lResult =
                    new FieldGroupAccess(fieldGroupData[index]);
            index++;
            return lResult;
        }

        public void remove() {
            throw new RuntimeException("remove is not supported");
        }
    }
}
