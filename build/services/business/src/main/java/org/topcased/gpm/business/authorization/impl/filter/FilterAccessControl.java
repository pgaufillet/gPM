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
import java.util.Collection;
import java.util.HashSet;

/**
 * Access control on a specific filter.
 * 
 * @author tpanuel
 */
public class FilterAccessControl implements Serializable {
    private static final long serialVersionUID = 7019606716051047106L;

    private Boolean executable;

    private Boolean editable;

    private Collection<FilterAccessContraint> additionalConstraints;

    /**
     * Create an access control on a specific filter.
     */
    public FilterAccessControl() {
        executable = true;
        editable = true;
        additionalConstraints = new HashSet<FilterAccessContraint>();
    }

    /**
     * Test if the filter is executable.
     * 
     * @return If the filter is executable.
     */
    public Boolean getExecutable() {
        return executable;
    }

    /**
     * Set if the filter is executable.
     * 
     * @param pExecutable
     *            If the filter is executable.
     */
    public void setExecutable(final Boolean pExecutable) {
        executable = pExecutable;
    }

    /**
     * Test if the filter is editable.
     * 
     * @return If the filter is editable.
     */
    public Boolean getEditable() {
        return editable;
    }

    /**
     * Set if the filter is editable.
     * 
     * @param pEditable
     *            If the filter is editable.
     */
    public void setEditable(final Boolean pEditable) {
        editable = pEditable;
    }

    /**
     * Get the id of the additional constraint to add at the filter execution.
     * 
     * @return The id of the additional constraint to add at the filter
     *         execution.
     */
    public Collection<FilterAccessContraint> getAdditionalConstraints() {
        return additionalConstraints;
    }

    /**
     * Set the id of the additional constraint to add at the filter execution.
     * 
     * @param pAdditionalConstraints
     *            The new id of the additional constraint to add at the filter
     *            execution.
     */
    public void setAdditionalConstraints(
            final Collection<FilterAccessContraint> pAdditionalConstraints) {
        additionalConstraints = pAdditionalConstraints;
    }
}