/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.impl.filter;

import java.io.Serializable;

import org.topcased.gpm.business.search.criterias.CriteriaData;

/**
 * Constraints for filter access.
 * 
 * @author mkargbo
 */
public class FilterAccessContraint implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 8544261116126438603L;

    private String name;

    private String description;

    private CriteriaData constraints;

    /**
     * Default constructor
     */
    public FilterAccessContraint() {
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String pDescription) {
        description = pDescription;
    }

    public CriteriaData getConstraints() {
        return constraints;
    }

    public void setConstraints(CriteriaData pConstraints) {
        constraints = pConstraints;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object pOther) {
        if (!(pOther instanceof FilterAccessContraint)) {
            return false;
        }
        else {
            // Name is the constraint identifier
            return getName().equals(((FilterAccessContraint) pOther).getName());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        if (getName() == null) {
            return 0;
        }
        else {
            return getName().hashCode();
        }
    }
}