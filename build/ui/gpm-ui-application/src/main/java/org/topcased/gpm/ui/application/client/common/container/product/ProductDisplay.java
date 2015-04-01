/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.container.product;

import java.util.List;

import org.topcased.gpm.ui.application.client.common.container.ContainerDisplay;

/**
 * Display interface for the ProductView.
 * 
 * @author tpanuel
 */
public interface ProductDisplay extends ContainerDisplay {
    /**
     * Initialize the properties group.
     * 
     * @param pProductNameEditable
     *            If the product name is editable.
     * @param pProductEditable
     *            If the product is editable.
     * @param pProductName
     *            the list of product used to child and parent. If null, mode
     *            visualization.
     * @param pParents
     *            The parent products.
     * @param pChild
     *            The child products.
     * @param pDescription
     *            The description of the product.
     */
    public void initPropertiesGroup(final boolean pProductNameEditable,
            boolean pProductEditable, final List<String> pProductName,
            final List<String> pParents, final List<String> pChild,
            String pDescription);

    /**
     * Get the product name.
     * 
     * @return The product name.
     */
    public String getProductName();

    /**
     * Get the product description.
     * 
     * @return The product description.
     */
    public String getProductDescription();

    /**
     * Get the parent products.
     * 
     * @return The parent products.
     */
    public List<String> getParentProducts();

    /**
     * Get the child products.
     * 
     * @return The child products.
     */
    public List<String> getChildProducts();

    /**
     * Validate view
     * 
     * @return the validation message
     */
    public String validate();
}