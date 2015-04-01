/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command;

import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;

/**
 * <p>
 * Abstract super-class for {@link Action} implementations.
 * </p>
 * 
 * @param <R>
 *            The {@link Result} implementation.
 * @author nveillet
 */
public abstract class AbstractCommandAction<R extends Result> implements
        Action<R> {

    /** serialVersionUID */
    private static final long serialVersionUID = -3164517229950810375L;

    private String productName;

    /**
     * create action
     */
    protected AbstractCommandAction() {
    }

    /**
     * create action with product name
     * 
     * @param pProductName
     *            the product name
     */
    protected AbstractCommandAction(String pProductName) {
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
