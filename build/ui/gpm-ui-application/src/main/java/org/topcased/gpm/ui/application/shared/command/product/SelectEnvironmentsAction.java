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
 * SelectEnvironmentsAction
 * 
 * @author nveillet
 */
public class SelectEnvironmentsAction implements
        Action<SelectEnvironmentsResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -6024663499540107666L;

    private String productTypeName;

    /**
     * create action
     */
    public SelectEnvironmentsAction() {
        super();
    }

    /**
     * create action with values
     * 
     * @param pProductTypeName
     *            the product type name
     */
    public SelectEnvironmentsAction(String pProductTypeName) {
        productTypeName = pProductTypeName;
    }

    /**
     * get product type name
     * 
     * @return the product type name
     */
    public String getProductTypeName() {
        return productTypeName;
    }

    /**
     * set product type name
     * 
     * @param pProductTypeName
     *            the product type name to set
     */
    public void setProductTypeName(String pProductTypeName) {
        productTypeName = pProductTypeName;
    }
}
