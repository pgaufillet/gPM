/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.product;

import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.container.product.UiProduct;

/**
 * GetProductCreationResult
 * 
 * @author nveillet
 */
public class GetProductCreationResult extends GetProductResult {

    /** serialVersionUID */
    private static final long serialVersionUID = 8631244265305406528L;

    private List<String> productNames;

    /**
     * Empty constructor for serialization.
     */
    public GetProductCreationResult() {
    }

    /**
     * Constructor with values
     * 
     * @param pProduct
     *            the product
     * @param pActions
     *            the actions
     * @param pProductNames
     *            the products names
     */
    public GetProductCreationResult(UiProduct pProduct,
            Map<String, UiAction> pActions, List<String> pProductNames) {
        super(pProduct, DisplayMode.CREATION, pActions);
        productNames = pProductNames;
    }

    /**
     * get product names
     * 
     * @return the product names
     */
    public List<String> getProductNames() {
        return productNames;
    }
}
