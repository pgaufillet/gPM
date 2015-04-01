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

import java.util.Map;

import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.ui.application.shared.command.container.GetContainerResult;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.container.product.UiProduct;

/**
 * GetProductResult
 * 
 * @author nveillet
 */
public abstract class GetProductResult extends GetContainerResult<UiProduct> {

    /** serialVersionUID */
    private static final long serialVersionUID = 2792937915086441999L;
    
    /**
     * Empty constructor for serialization.
     */
    protected GetProductResult() {
        super();
    }

    /**
     * Constructor with values
     * 
     * @param pProduct
     *            the product
     * @param pDisplayMode
     *            the display mode
     * @param pActions
     *            the actions
     */
    protected GetProductResult(UiProduct pProduct, DisplayMode pDisplayMode,
            Map<String, UiAction> pActions) {
        super(pProduct, pDisplayMode, pActions);
    }
}
