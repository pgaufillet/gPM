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

import net.customware.gwt.dispatch.shared.Result;

/**
 * SelectEnvironmentsResult
 * 
 * @author nveillet
 */
public class SelectEnvironmentsResult implements Result {

    /** serialVersionUID */
    private static final long serialVersionUID = 2479321548280709442L;

    private List<String> environmentNames;

    private String productTypeName;

    /**
     * Empty constructor for serialization.
     */
    public SelectEnvironmentsResult() {
    }

    /**
     * create action with values
     * 
     * @param pProductTypeName
     *            the product type name
     * @param pEnvironmentNames
     *            the environments names
     */
    public SelectEnvironmentsResult(String pProductTypeName,
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
}
