/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.authorization;

import java.util.List;

import org.topcased.gpm.ui.facade.shared.container.product.UiProduct;
import org.topcased.gpm.ui.facade.shared.container.product.UiProductHierarchy;

/**
 * SelectProductResult
 * 
 * @author nveillet
 */
public class SelectProductResult extends AbstractConnectionResult {

    /** serialVersionUID */
    private static final long serialVersionUID = -3264464721366067293L;

    private List<UiProductHierarchy> productHierarchy;

    private List<UiProduct> productList;

    /**
     * Empty constructor for serialization.
     */
    public SelectProductResult() {
    }

    /**
     * Create SelectProductResult with products
     * 
     * @param pProductList
     *            the product list
     * @param pProductHierarchy
     *            the product hierarchy
     */
    public SelectProductResult(List<UiProduct> pProductList,
            List<UiProductHierarchy> pProductHierarchy) {
        productList = pProductList;
        productHierarchy = pProductHierarchy;
    }

    /**
     * get product hierarchy
     * 
     * @return the product hierarchy
     */
    public List<UiProductHierarchy> getProductHierarchy() {
        return productHierarchy;
    }

    /**
     * get productList
     * 
     * @return the productList
     */
    public List<UiProduct> getProductList() {
        return productList;
    }
}
