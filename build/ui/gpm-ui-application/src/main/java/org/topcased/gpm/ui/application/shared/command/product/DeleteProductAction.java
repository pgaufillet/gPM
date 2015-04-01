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
 * DeleteProductAction
 * 
 * @author nveillet
 */
public class DeleteProductAction implements Action<DeleteProductResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -8096675212589710726L;

    private String productId;

    /**
     * create action
     */
    public DeleteProductAction() {
    }

    /**
     * create action with values
     * 
     * @param pProductId
     *            the product identifier
     */
    public DeleteProductAction(String pProductId) {
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
