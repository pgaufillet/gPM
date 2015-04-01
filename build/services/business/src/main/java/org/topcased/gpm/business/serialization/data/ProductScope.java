/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class ProductScope.
 * 
 * @author llatil
 */
@XStreamAlias("productScope")
public class ProductScope extends NamedElement {

    /** Generated UID */
    private static final long serialVersionUID = -4294301615431819976L;

    /** The include sub products. */
    @XStreamAsAttribute
    private boolean includeSubProducts;

    /**
     * Checks if is include sub products.
     * 
     * @return true, if is include sub products
     */
    public boolean isIncludeSubProducts() {
        return includeSubProducts;
    }

    /**
     * set includeSubProducts
     * 
     * @param pIncludeSubProducts
     *            the includeSubProducts to set
     */
    public void setIncludeSubProducts(boolean pIncludeSubProducts) {
        includeSubProducts = pIncludeSubProducts;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {

        if (pOther instanceof ProductScope) {
            ProductScope lOther = (ProductScope) pOther;

            if (!super.equals(lOther)) {
                return false;
            }
            if (includeSubProducts != lOther.includeSubProducts) {
                return false;
            }
            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        // Named hashcode is sufficient
        return super.hashCode();
    }
}
