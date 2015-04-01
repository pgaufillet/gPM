/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.filter;

import java.io.Serializable;

import org.topcased.gpm.business.util.Translation;

/**
 * UiFilterScope
 * 
 * @author nveillet
 */
public class UiFilterScope implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -2105292089562780705L;

    private boolean includeSubProduct;

    private Translation productName;

    /**
     * Constructor
     */
    public UiFilterScope() {
        // Empty constructor
    }

    /**
     * Constructor with values
     * 
     * @param pProductName
     *            the product name
     * @param pIncludeSubProduct
     *            the include sub product condition
     */
    public UiFilterScope(final Translation pProductName,
            final boolean pIncludeSubProduct) {
        productName = pProductName;
        includeSubProduct = pIncludeSubProduct;
    }

    /**
     * get product name
     * 
     * @return the product name
     */
    public Translation getProductName() {
        return productName;
    }

    /**
     * get include sub product condition
     * 
     * @return the include sub product condition
     */
    public boolean isIncludeSubProduct() {
        return includeSubProduct;
    }

    /**
     * set include sub product condition
     * 
     * @param pIncludeSubProduct
     *            the include sub product condition to set
     */
    public void setIncludeSubProduct(final boolean pIncludeSubProduct) {
        includeSubProduct = pIncludeSubProduct;
    }

    /**
     * set product name
     * 
     * @param pProductName
     *            the product name to set
     */
    public void setProductName(final Translation pProductName) {
        productName = pProductName;
    }
}
