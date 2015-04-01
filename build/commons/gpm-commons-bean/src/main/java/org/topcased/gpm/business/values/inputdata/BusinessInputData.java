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
package org.topcased.gpm.business.values.inputdata;

import java.util.List;

import org.topcased.gpm.business.values.BusinessContainer;
import org.topcased.gpm.business.values.field.BusinessFieldGroup;

/**
 * Interface used to access on an input data.
 * 
 * @author tpanuel
 */
public interface BusinessInputData extends BusinessContainer {

    /**
     * Get the BusinessFieldGroup
     * 
     * @param pFieldGroupName
     *            The name of the group
     * @return The corresponding BusinessFieldGroup
     */
    public BusinessFieldGroup getFieldGroup(String pFieldGroupName);

    /**
     * Get the list of fields groups names
     * 
     * @return List<String> The list of FieldGroup names
     */
    public List<String> getFieldGroupNames();

    /**
     * Get the list of BusinessFieldGroup
     * 
     * @return List<BusinessFieldGroup> The list of BusinessFieldGroup
     */
    public List<BusinessFieldGroup> getFieldGroups();
}