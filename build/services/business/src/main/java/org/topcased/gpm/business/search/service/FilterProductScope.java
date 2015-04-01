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
// Generated by: ValueObject.vsl in andromda-java-cartridge.
//
package org.topcased.gpm.business.search.service;

/**
 * @author Atos
 */
public class FilterProductScope implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public FilterProductScope() {
    }

    /**
     * Constructor taking all properties.
     */
    public FilterProductScope(final java.lang.String pProductName,
            final boolean pIncludeSubProducts) {
        this.productName = pProductName;
        this.includeSubProducts = pIncludeSubProducts;
    }

    /**
     * Copies constructor from other FilterProductScope
     */
    public FilterProductScope(FilterProductScope pOtherBean) {
        if (pOtherBean != null) {
            this.productName = pOtherBean.getProductName();
            this.includeSubProducts = pOtherBean.isIncludeSubProducts();
        }
    }

    private java.lang.String productName;

    /**
     * 
     */
    public java.lang.String getProductName() {
        return this.productName;
    }

    public void setProductName(java.lang.String pProductName) {
        this.productName = pProductName;
    }

    private boolean includeSubProducts;

    /**
     * 
     */
    public boolean isIncludeSubProducts() {
        return this.includeSubProducts;
    }

    public void setIncludeSubProducts(boolean pIncludeSubProducts) {
        this.includeSubProducts = pIncludeSubProducts;
    }

}