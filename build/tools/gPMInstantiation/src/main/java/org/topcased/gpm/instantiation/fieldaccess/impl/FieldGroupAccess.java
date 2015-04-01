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
import org.topcased.gpm.business.sheet.service.FieldGroupData;
import org.topcased.gpm.instantiation.fieldaccess.FieldAccess;

/**
 * @author sie
 */
public class FieldGroupAccess extends FieldCompositeAccess implements
        org.topcased.gpm.instantiation.fieldaccess.FieldGroupAccess {

    private FieldGroupData fieldGroup;

    /**
     * @param pFieldGroup
     */
    public FieldGroupAccess(FieldGroupData pFieldGroup) {
        fieldGroup = pFieldGroup;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.FieldGroupAccessI#getDisplayHint()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getDisplayHint()
     */
    public String getDisplayHint() {
        return "Group";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.FieldGroupAccessI#getName()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getName()
     */
    public String getName() {
        return fieldGroup.getLabelKey();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.FieldGroupAccessI#getType()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getType()
     */
    public String getType() {
        return "Group";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.FieldGroupAccessI#size()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.impl.FieldCompositeAccess#size()
     */
    @Override
    public long size() {
        return fieldGroup.getMultipleLineFieldDatas().length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.FieldGroupAccessI#iterator()
     */
    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<FieldAccess> iterator() {
        return new FieldGroupDataAccessIterator(
                fieldGroup.getMultipleLineFieldDatas());
    }

    private class FieldGroupDataAccessIterator implements Iterator<FieldAccess> {
        private MultipleLineFieldData[] multipleLineFieldData;

        private int index;

        public FieldGroupDataAccessIterator(
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
