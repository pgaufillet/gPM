/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.instantiation.fieldaccess.impl;

import java.util.Iterator;

import org.topcased.gpm.business.revision.RevisionData;
import org.topcased.gpm.business.sheet.service.FieldGroupData;
import org.topcased.gpm.instantiation.fieldaccess.FieldAccess;

/**
 * RevisionAccess
 * 
 * @author mfranche
 */
public class RevisionAccess extends FieldCompositeAccess implements
        org.topcased.gpm.instantiation.fieldaccess.SheetAccess {

    private RevisionData revisionData;

    /**
     * Constructor
     * 
     * @param pRevisionData
     *            The revision data
     */
    public RevisionAccess(RevisionData pRevisionData) {
        revisionData = pRevisionData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.impl.FieldCompositeAccess#size()
     */
    @Override
    public long size() {
        return revisionData.getFieldGroupDatas().length;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.SheetAccess#getId()
     */
    public String getId() {
        return revisionData.getId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.SheetAccess#getProcessName()
     */
    public String getProcessName() {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.SheetAccess#getProductName()
     */
    public String getProductName() {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.SheetAccess#getReference()
     */
    public MultipleFieldAccess getReference() {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.SheetAccess#getSheetTypeName()
     */
    public String getSheetTypeName() {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.SheetAccess#getStringReference()
     */
    public String getStringReference() {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.SheetAccess#getVersion()
     */
    public int getVersion() {
        return 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getDisplayHint()
     */
    public String getDisplayHint() {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getName()
     */
    public String getName() {
        return revisionData.getLabel();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getType()
     */
    public String getType() {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<FieldAccess> iterator() {
        return new RevisionAccessIterator(revisionData.getFieldGroupDatas());
    }

    private class RevisionAccessIterator implements Iterator<FieldAccess> {
        private FieldGroupData[] fieldGroupData;

        private int index;

        public RevisionAccessIterator(FieldGroupData[] pFieldGroupData) {
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
