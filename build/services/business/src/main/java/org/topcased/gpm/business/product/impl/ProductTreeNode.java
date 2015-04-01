/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.product.impl;

import java.util.Collection;

import javax.swing.tree.DefaultMutableTreeNode;

import org.topcased.gpm.business.product.service.ProductData;

/**
 * Useful class to construct the products tree.
 * 
 * @author mfranche
 */
public class ProductTreeNode extends DefaultMutableTreeNode {

    /** serialVersionUID */
    private static final long serialVersionUID = 6114141621282008399L;

    /** Is this node enabled by the user */
    private boolean enabled;

    /**
     * Constructor
     * 
     * @param pProductData
     *            The node userObject
     * @param pEnabled
     *            Is this node accessible
     */
    public ProductTreeNode(ProductData pProductData, boolean pEnabled) {
        super(pProductData);
        enabled = pEnabled;
    }

    /**
     * Constructor used to initialize a node with a list of product node.
     * <p>
     * Note: The product data of the created node is set to null.
     * 
     * @param pProductList
     *            List of product nodes. These nodes are used to initialize the
     *            children list of this node.
     */
    public ProductTreeNode(Collection<ProductTreeNode> pProductList) {
        super(null);
        for (ProductTreeNode lProd : pProductList) {
            add(lProd);
        }
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
     * set enabled
     * 
     * @param pEnabled
     *            the enabled to set
     */
    public void setEnabled(boolean pEnabled) {
        enabled = pEnabled;
    }

    /**
     * get product data
     * 
     * @return The productData
     */
    public ProductData getProductData() {
        return (ProductData) getUserObject();
    }
}
