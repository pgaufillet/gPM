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

import net.customware.gwt.dispatch.shared.Action;

/**
 * GetProductCreationAction
 * 
 * @author nveillet
 */
public class GetProductCreationAction implements Action<GetProductResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 4496988820524322332L;

    private List<String> environmentNames;

    private String productTypeName;

    /**
     * create action
     */
    public GetProductCreationAction() {
        super();
    }

    /**
     * create action with product type name
     * 
     * @param pProductTypeName
     *            the product type name
     */
    public GetProductCreationAction(String pProductTypeName) {
        productTypeName = pProductTypeName;
    }

    /**
     * create action with product type name and environments
     * 
     * @param pProductTypeName
     *            the product type name
     * @param pEnvironmentNames
     *            the environments names
     */
    public GetProductCreationAction(String pProductTypeName,
            List<String> pEnvironmentNames) {
        productTypeName = pProductTypeName;
        environmentNames = pEnvironmentNames;
    }

    /**
     * get environment names
     * 
     * @return the environment names
     */
    public List<String> getEnvironmentNames() {
        return environmentNames;
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
     * set environment names
     * 
     * @param pEnvironmentNames
     *            the environment names to set
     */
    public void setEnvironmentNames(List<String> pEnvironmentNames) {
        environmentNames = pEnvironmentNames;
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
