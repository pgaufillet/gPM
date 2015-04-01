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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * FilterAccessConstraint name
 * 
 * @author mkargbo
 */
@XStreamAlias("filterAccessConstraintName")
public class FilterAccessConstraintName {

    @XStreamAsAttribute
    private String constraintName;

    /**
     * Constructor
     */
    public FilterAccessConstraintName() {
    }

    public String getConstraintName() {
        return constraintName;
    }

    public void setConstraintName(String pConstraintName) {
        constraintName = pConstraintName;
    }
}
