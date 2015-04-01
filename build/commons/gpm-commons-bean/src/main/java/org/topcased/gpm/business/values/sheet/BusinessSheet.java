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
package org.topcased.gpm.business.values.sheet;

import java.util.List;

import org.topcased.gpm.business.values.BusinessContainer;
import org.topcased.gpm.business.values.field.BusinessFieldGroup;

/**
 * Interface used to access on a sheet.
 * 
 * @author tpanuel
 */
public interface BusinessSheet extends BusinessContainer {
    /**
     * Get the functional reference of the sheet.
     * 
     * @return The functional reference of the sheet.
     */
    public String getFunctionalReference();

    /**
     * Get the name of the product.
     * 
     * @return The name of the product.
     */
    public String getProductName();

    /**
     * Get the state of the sheet.
     * 
     * @return The state of the sheet.
     */
    public String getState();

    /**
     * Get the list of fields groups names
     * 
     * @return List<String> The list of FieldGroup names
     */
    public List<String> getFieldGroupNames();

    /**
     * Get the BusinessFieldGroup
     * 
     * @param pFieldGroupName
     *            The name of the group
     * @return The corresponding BusinessFieldGroup
     */
    public BusinessFieldGroup getFieldGroup(String pFieldGroupName);
}