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

import net.customware.gwt.dispatch.shared.Result;

/**
 * DisconnectProductResult
 * 
 * @author nveillet
 */
public class DisconnectProductResult implements Result {
    /** serialVersionUID */
    private static final long serialVersionUID = -655896865665851502L;

    private String productName;

    /**
     * Empty constructor for serialization.
     */
    public DisconnectProductResult() {
    }

    /**
     * Create action with product name
     * 
     * @param pProductName
     *            The product name.
     */
    public DisconnectProductResult(final String pProductName) {
        productName = pProductName;
    }

    /**
     * Get product name.
     * 
     * @return The product name.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Set product name.
     * 
     * @param pProductName
     *            The product name to set.
     */
    public void setProductName(final String pProductName) {
        productName = pProductName;
    }
}