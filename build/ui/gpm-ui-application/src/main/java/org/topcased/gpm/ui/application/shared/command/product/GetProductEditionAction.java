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

import net.customware.gwt.dispatch.shared.Action;

/**
 * GetProductEditionAction
 * 
 * @author nveillet
 */
public class GetProductEditionAction implements Action<GetProductResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 5070048571171118953L;

    private String productId;

    /**
     * create action
     */
    public GetProductEditionAction() {
        super();
    }

    /**
     * create action with values
     * 
     * @param pProductId
     *            the product identifier
     */
    public GetProductEditionAction(String pProductId) {
        productId = pProductId;
    }

    /**
     * get product identifier
     * 
     * @return the product identifier
     */
    public String getProductId() {
        return productId;
    }

    /**
     * set product identifier
     * 
     * @param pProductId
     *            the product identifier to set
     */
    public void setProductId(String pProductId) {
        productId = pProductId;
    }
}
