/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 *
 ******************************************************************/
package org.topcased.gpm.business.values.product;

import java.util.List;

import org.topcased.gpm.business.values.BusinessContainer;
import org.topcased.gpm.business.values.field.BusinessFieldGroup;

/**
 * Interface used to access on a product.
 * 
 * @author tpanuel
 */
public interface BusinessProduct extends BusinessContainer {

    /**
     * Get the list of product children names
     * 
     * @return The product children names
     */
    public List<String> getChildren();

    /**
     * Get the name of the product.
     * 
     * @return The product name.
     */
    public String getName();

    /**
     * Get the description of the product
     * 
     * @return The description of the product
     */
    public String getDescription();

    /**
     * Get the list of product parents names
     * 
     * @return The product parents names
     */
    public List<String> getParents();

    /**
     * Get the list of fields groups names
     * 
     * @return List<String> The list of FieldGroup names
     */
    public List<String> getFieldGroupNames();

    /**
     * Get the BusinessFieldGroup by its name
     * 
     * @param pFieldGroupName
     *            The name of the group
     * @return The corresponding BusinessFieldGroup
     */
    public BusinessFieldGroup getFieldGroup(String pFieldGroupName);
}