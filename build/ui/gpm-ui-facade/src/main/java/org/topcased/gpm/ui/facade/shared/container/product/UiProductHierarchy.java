/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.container.product;

import java.io.Serializable;
import java.util.List;

/**
 * UiProductHierarchy
 * 
 * @author nveillet
 */
public class UiProductHierarchy implements Serializable,
        Comparable<UiProductHierarchy> {

    /** serialVersionUID */
    private static final long serialVersionUID = -6902013135693606632L;

    private List<UiProductHierarchy> children;

    private String productName;

    private String description;

    private boolean selectable;

    /**
     * Create UiProductHierarchy
     */
    public UiProductHierarchy() {
    }

    /**
     * Create UiProductHierarchy (leaf) with product name
     * 
     * @param pProductName
     *            the product name
     */
    public UiProductHierarchy(String pProductName) {
        productName = pProductName;
        setSelectable(true);
    }

    /**
     * Create UiProductHierarchy (node) with all values
     * 
     * @param pProductName
     *            the product name
     * @param pProductDescription
     *            the product description
     * @param pChildren
     *            the children
     * @param pSelectable
     *            the selectable condition
     */
    public UiProductHierarchy(String pProductName, String pProductDescription,
            List<UiProductHierarchy> pChildren, boolean pSelectable) {
        productName = pProductName;
        description = pProductDescription;
        children = pChildren;
        setSelectable(pSelectable);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(UiProductHierarchy pUiProductHierarchy) {
        return this.getProductName().compareTo(
                pUiProductHierarchy.getProductName());
    }

    /**
     * get children
     * 
     * @return the children
     */
    public List<UiProductHierarchy> getChildren() {
        return children;
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
     * get selectable condition
     * 
     * @return the selectable condition
     */
    public boolean isSelectable() {
        return selectable;
    }

    /**
     * set children
     * 
     * @param pChildren
     *            the children to set
     */
    public void setChildrens(List<UiProductHierarchy> pChildren) {
        children = pChildren;
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

    /**
     * set selectable condition
     * 
     * @param pSelectable
     *            the selectable condition to set
     */
    public void setSelectable(boolean pSelectable) {
        selectable = pSelectable;
    }

    /**
     * get description
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * set description
     * 
     * @param pDescription
     *            the description to set
     */
    public void setDescription(String pDescription) {
        description = pDescription;
    }

}
