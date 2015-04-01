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
import org.topcased.gpm.business.link.service.LinkData;
import org.topcased.gpm.instantiation.fieldaccess.FieldAccess;

/**
 * @author sie
 */
public class SheetLinkAccess extends FieldCompositeAccess implements
        org.topcased.gpm.instantiation.fieldaccess.SheetLinkAccess {
    private LinkData sheetLinkData;

    /**
     * @param pSheetLinkData
     */
    public SheetLinkAccess(LinkData pSheetLinkData) {
        sheetLinkData = pSheetLinkData;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.SheetLinkAccessI#getDisplayHint()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getDisplayHint()
     */
    public String getDisplayHint() {
        return "SheetLink";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.SheetLinkAccessI#getName()
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
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.SheetLinkAccessI#getType()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getType()
     */
    public String getType() {
        return "SheetLink";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.SheetLinkAccessI#getId()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.SheetLinkAccess#getId()
     */
    public String getId() {
        return sheetLinkData.getId();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.SheetLinkAccessI#size()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.impl.FieldCompositeAccess#size()
     */
    @Override
    public long size() {
        return sheetLinkData.getMultipleLineFieldDatas().length;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.SheetLinkAccessI#iterator()
     */
    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<FieldAccess> iterator() {
        return new SheetLinkDataAccessIterator(
                sheetLinkData.getMultipleLineFieldDatas());
    }

    private class SheetLinkDataAccessIterator implements Iterator<FieldAccess> {
        private MultipleLineFieldData[] multipleLineFieldData;

        private int index;

        public SheetLinkDataAccessIterator(
                MultipleLineFieldData[] pMultipleLineFieldData) {
            multipleLineFieldData = pMultipleLineFieldData;
            index = 0;
        }

        public boolean hasNext() {
            return index < multipleLineFieldData.length;
        }

        public FieldAccess next() {
            FieldAccess lResult =
                    MultipleLineAccess.create(multipleLineFieldData[index]);
            index++;
            return lResult;
        }

        public void remove() {
            throw new RuntimeException("remove is not supported");
        }
    }
}
