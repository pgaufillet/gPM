/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws.v2.business;

import java.io.Serializable;
import java.util.List;

/**
 * ProductNode
 * 
 * @author jeballar
 */
public class ProductNode implements Serializable, Comparable<ProductNode> {
    /** serialVersionUID */
    private static final long serialVersionUID = -2871612323875544528L;

    private List<ProductNode> children;

    private String productName;

    private boolean enabled;

    public ProductNode(String pProductName, List<ProductNode> pChildren,
            boolean pEnabled) {
        productName = pProductName;
        children = pChildren;
        enabled = pEnabled;
    }

    public ProductNode() {
        super();
    }

    /**
     * get children
     * 
     * @return the children
     */
    public List<ProductNode> getChildren() {
        return children;
    }

    /**
     * get productName
     * 
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * get enabled
     * 
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * {@inheritDoc} <br/>
     * Compare product nodes by product names.
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(ProductNode pO) {
        return productName.compareTo(pO.getProductName());
    }

    /**
     * set children
     * 
     * @param pChildren
     *            the children to set
     */
    public void setChildren(List<ProductNode> pChildren) {
        children = pChildren;
    }

    /**
     * set productName
     * 
     * @param pProductName
     *            the productName to set
     */
    public void setProductName(String pProductName) {
        productName = pProductName;
    }

    /**
     * set enabled
     * 
     * @param pEnabled
     *            the enabled to set
     */
    public void setEnabled(boolean pEnabled) {
        enabled = pEnabled;
    }

}
