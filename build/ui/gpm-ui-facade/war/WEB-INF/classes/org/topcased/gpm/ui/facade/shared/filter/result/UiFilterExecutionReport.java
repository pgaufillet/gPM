/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.filter.result;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

/**
 * UiFilterExecutionReport
 * 
 * @author nveillet
 */
public class UiFilterExecutionReport implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -4198477013920165340L;

    /** Map<TYPE, Map<ROLE_NAME, CONSTRAINTS> */
    private HashMap<String, HashMap<String, HashSet<String>>> additionalConstraints;

    /** Map<ROLE_NAME, PRODUCT_NAMES> */
    private HashMap<String, HashSet<String>> executableProducts;

    /** PRODUCT_NAMES */
    private HashSet<String> nonExecutableProducts;

    /**
     * Constructor
     */
    public UiFilterExecutionReport() {
    }

    /**
     * get additional constraints
     * 
     * @return the additional constraints
     */
    public HashMap<String, HashMap<String, HashSet<String>>> getAdditionalConstraints() {
        return additionalConstraints;
    }

    /**
     * get executable products
     * 
     * @return the executable products
     */
    public HashMap<String, HashSet<String>> getExecutableProducts() {
        return executableProducts;
    }

    /**
     * get non executable products
     * 
     * @return the non executable products
     */
    public HashSet<String> getNonExecutableProducts() {
        return nonExecutableProducts;
    }

    /**
     * set additional constraints
     * 
     * @param pAdditionalConstraints
     *            the additional constraints to set
     */
    public void setAdditionalConstraints(
            HashMap<String, HashMap<String, HashSet<String>>> pAdditionalConstraints) {
        additionalConstraints = pAdditionalConstraints;
    }

    /**
     * set executable products
     * 
     * @param pExecutableProducts
     *            the executable products to set
     */
    public void setExecutableProducts(
            HashMap<String, HashSet<String>> pExecutableProducts) {
        executableProducts = pExecutableProducts;
    }

    /**
     * set non executable products
     * 
     * @param pNonExecutableProducts
     *            the non executable products to set
     */
    public void setNonExecutableProducts(HashSet<String> pNonExecutableProducts) {
        nonExecutableProducts = pNonExecutableProducts;
    }
}
