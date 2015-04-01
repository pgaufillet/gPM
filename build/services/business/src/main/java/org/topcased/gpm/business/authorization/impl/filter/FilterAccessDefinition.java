/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.impl.filter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Filter access controls definition for a specific fields container.
 * 
 * @author tpanuel
 */
public class FilterAccessDefinition implements Serializable {
    /** serialVersionUID */
    private static final long serialVersionUID = 3865481884053008691L;

    private FilterAccessControl typeAccessControl;

    private Map<String, FilterAccessControl> fieldsAccessControl;

    /**
     * Create an access control on a specific filter.
     */
    public FilterAccessDefinition() {
        fieldsAccessControl = new HashMap<String, FilterAccessControl>();
    }

    /**
     * Get the access control definition for the type.
     * 
     * @return The access control definition for the type.
     */
    public FilterAccessControl getTypeAccessControl() {
        return typeAccessControl;
    }

    /**
     * Set the access control definition for the type.
     * 
     * @param pTypeAccessControl
     *            The new access control definition for the type.
     */
    public void setTypeAccessControl(
            final FilterAccessControl pTypeAccessControl) {
        typeAccessControl = pTypeAccessControl;
    }

    /**
     * Get the access control definition for the fields.
     * 
     * @return The access control definition for the fields.
     */
    public Map<String, FilterAccessControl> getFieldsAccessControl() {
        return fieldsAccessControl;
    }

    /**
     * Set the access control definition for the fields.
     * 
     * @param pFieldsAccessControl
     *            The new access control definition for the fields.
     */
    public void setFieldsAccessControl(
            final Map<String, FilterAccessControl> pFieldsAccessControl) {
        fieldsAccessControl = pFieldsAccessControl;
    }
}