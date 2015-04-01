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

import net.customware.gwt.dispatch.shared.Action;

/**
 * ChangeProductAction
 * 
 * @author nveillet
 */
public class ConnectProductAction implements Action<AbstractConnectionResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -4213430777045439477L;

    private String productName;

    /**
     * create action
     */
    public ConnectProductAction() {
    }

    /**
     * create action with product name
     * 
     * @param pProductName
     *            the product name
     */
    public ConnectProductAction(String pProductName) {
        productName = pProductName;
    }

    /**
     * get product name
     * 
     * @return the product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * set product name
     * 
     * @param pProductName
     *            the product name to set
     */
    public void setProductName(String pProductName) {
        productName = pProductName;
    }
}
