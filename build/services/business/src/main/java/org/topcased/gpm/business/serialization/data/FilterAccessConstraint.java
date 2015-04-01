/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Constraint for filter access
 * 
 * @author mkargbo
 */
@XStreamAlias("filterAccessConstraint")
public class FilterAccessConstraint {

    @XStreamAsAttribute
    private String constraintName;

    /** The description. */
    @XStreamAsAttribute
    private String description;

    private List<CriteriaGroup> criteria;

    /**
     * Default constructor
     */
    public FilterAccessConstraint() {
        // TODO Auto-generated constructor stub
    }

    public String getConstraintName() {
        return constraintName;
    }

    public void setConstraintName(String pConstraintName) {
        constraintName = pConstraintName;
    }

    /**
     * Get the description of the element.
     * 
     * @return Description
     */
    public String getDescription() {
        return org.topcased.gpm.util.lang.StringUtils.normalizeString(description);
    }

    /**
     * Set the description of the element.
     * 
     * @param pDescription
     *            Description text to set
     */
    public void setDescription(String pDescription) {
        description = pDescription;
    }

    public List<CriteriaGroup> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<CriteriaGroup> pCriteria) {
        criteria = pCriteria;
    }
}
