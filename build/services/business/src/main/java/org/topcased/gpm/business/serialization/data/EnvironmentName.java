/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * The Class EnvironmentName.
 * 
 * @author tpanuel
 */
@XStreamAlias("environmentName")
public class EnvironmentName extends NamedElement {
    /** serialVersionUID. */
    private static final long serialVersionUID = -2532384336380647123L;

    /**
     * Constructs a new Environment Name.
     * 
     * @param pName
     *            Name of the environement
     */
    public EnvironmentName(String pName) {
        super(pName);
    }
}
