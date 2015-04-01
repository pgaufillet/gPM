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

import org.topcased.gpm.ui.application.shared.command.CloseContainerResult;

/**
 * CloseProductAction
 * 
 * @author nveillet
 */
public class CloseProductAction implements Action<CloseContainerResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 3070290760335686563L;

    private String productId;

    /**
     * create action
     */
    public CloseProductAction() {
    }

    /**
     * create action with values
     * 
     * @param pProductId
     *            the product identifier
     */
    public CloseProductAction(String pProductId) {
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
