/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.product.impl;

import java.io.Serializable;
import java.util.Collection;

/**
 * Element store on the cache of the ProductHierarchyManager.
 * 
 * @author tpanuel
 */
public class ProductHierarchyElement implements Serializable {
    private static final long serialVersionUID = 550151267196014840L;

    private String processName;

    private String productName;

    private Collection<String> directSubProductNames;

    private Collection<String> allSubProductNames;

    /**
     * Create an element that can be store on the cache of the
     * ProductHierarchyManager.
     */
    public ProductHierarchyElement() {
    }

    /**
     * Get the process name.
     * 
     * @return The process name.
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * Set the process name.
     * 
     * @param pProcessName
     *            The new process name.
     */
    public void setProcessName(final String pProcessName) {
        processName = pProcessName;
    }

    /**
     * Get the product name.
     * 
     * @return The product name.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Set the product name.
     * 
     * @param pProductName
     *            The new product name.
     */
    public void setProductName(String pProductName) {
        productName = pProductName;
    }

    /**
     * Get the name of the direct sub products.
     * 
     * @return The direct sub products name.
     */
    public Collection<String> getDirectSubProductNames() {
        return directSubProductNames;
    }

    /**
     * Set the name of the direct sub products.
     * 
     * @param pDirectSubProductNames
     *            The new name of the direct sub products.
     */
    public void setDirectSubProductNames(
            final Collection<String> pDirectSubProductNames) {
        directSubProductNames = pDirectSubProductNames;
    }

    /**
     * Get the name of all the sub products.
     * 
     * @return All the sub products name.
     */
    public Collection<String> getAllSubProductNames() {
        return allSubProductNames;
    }

    /**
     * Set the name of all the sub products.
     * 
     * @param pAllSubProductNames
     *            All the new sub products name.
     */
    public void setAllSubProductNames(
            final Collection<String> pAllSubProductNames) {
        allSubProductNames = pAllSubProductNames;
    }
}