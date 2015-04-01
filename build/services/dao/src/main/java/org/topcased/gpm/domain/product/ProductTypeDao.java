/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
//
// Attention: Generated code! Do not modify by hand!
// Generated by: SpringDao.vsl in andromda-spring-cartridge.
//
package org.topcased.gpm.domain.product;

/**
 * @see org.topcased.gpm.domain.product.ProductType
 * @author Atos
 */
public interface ProductTypeDao
        extends
        org.topcased.gpm.domain.IDao<org.topcased.gpm.domain.product.ProductType, java.lang.String> {
    /**
     * 
     */
    public org.topcased.gpm.domain.product.ProductType getProductType(
            org.topcased.gpm.domain.businessProcess.BusinessProcess pBusinessProcess,
            java.lang.String pName);

    /**
     * 
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getProductTypes(
            org.topcased.gpm.domain.businessProcess.BusinessProcess pBusinessProcess);

    /**
     * Checks if all container are instance of product Type, ids given.
     * 
     * @param pIds
     *            array of container id
     * @return true if the container ids are product type ids and false if one
     *         of the ids is not a product type id
     */
    public boolean checkProductTypeIds(String[] pIds);
}