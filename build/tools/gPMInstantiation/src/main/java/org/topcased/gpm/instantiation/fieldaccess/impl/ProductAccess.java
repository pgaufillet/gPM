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
import org.topcased.gpm.business.product.service.ProductData;
import org.topcased.gpm.instantiation.fieldaccess.FieldAccess;

/**
 * @author sie
 */
public class ProductAccess extends FieldCompositeAccess implements
        org.topcased.gpm.instantiation.fieldaccess.ProductAccess {

    private ProductData productData;

    /**
     * @param pProductData
     */
    public ProductAccess(ProductData pProductData) {
        productData = pProductData;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.ProductAccessI#getDisplayHint()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getDisplayHint()
     */
    public String getDisplayHint() {
        return "Product";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.ProductAccessI#getName()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getName()
     */
    public String getName() {
        return productData.getName();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.ProductAccessI#getType()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldAccess#getType()
     */
    public String getType() {
        return "Product";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.ProductAccessI#getEnvironmentName()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.ProductAccess#getEnvironmentName()
     */
    public String[] getEnvironmentNames() {
        return productData.getEnvironmentNames();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.ProductAccessI#getId()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.ProductAccess#getId()
     */
    public String getId() {
        return productData.getId();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.ProductAccessI#getProcessName()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.ProductAccess#getProcessName()
     */
    public String getProcessName() {
        return productData.getProcessName();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.ProductAccessI#size()
     */
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.impl.FieldCompositeAccess#size()
     */
    @Override
    public long size() {
        return productData.getMultipleLineFieldDatas().length;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.topcased.gpm.sheetimport.fieldaccess.impl.ProductAccessI#iterator()
     */
    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<FieldAccess> iterator() {
        return new ProductDataAccessIterator(
                productData.getMultipleLineFieldDatas());
    }

    private class ProductDataAccessIterator implements Iterator<FieldAccess> {
        private MultipleLineFieldData[] multipleLineFieldData;

        private int index;

        public ProductDataAccessIterator(
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
