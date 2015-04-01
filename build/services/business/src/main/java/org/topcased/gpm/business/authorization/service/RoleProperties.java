/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: XXX (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.service;

import java.io.Serializable;

/**
 * RoleProperties
 * 
 * @author ahaugomm
 */
public class RoleProperties implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -474379758839541124L;

    /** Defines if the associated role is an instance role or not */
    private boolean instanceRole = false;

    /** Defines if the associated role is a product role or not */
    private boolean productRole = false;

    /** Role on a product (not on instance) */
    public static final RoleProperties PRODUCT_ROLE_ONLY =
            new RoleProperties(false, true);

    /** Role on instance (not on a product) */
    public static final RoleProperties INSTANCE_ROLE_ONLY =
            new RoleProperties(true, false);

    /** Role either on instance or on a product */
    public static final RoleProperties PRODUCT_OR_INSTANCE_ROLE =
            new RoleProperties(true, true);

    /**
     * Empty constructor
     */
    public RoleProperties() {
        super();
    }

    /**
     * Constructor with parameters instance role and product role
     * 
     * @param pInstanceRole
     *            indicates if Instance roles are allowed or not
     * @param pProductRole
     *            indicates if Product roles are allowed or not
     */
    public RoleProperties(boolean pInstanceRole, boolean pProductRole) {
        instanceRole = pInstanceRole;
        productRole = pProductRole;
    }

    /**
     * set the boolean characteristic instanceRole
     * 
     * @param pInstanceRole
     *            the instanceRole to set
     */
    public void setInstanceRole(boolean pInstanceRole) {
        instanceRole = pInstanceRole;
    }

    /**
     * set the boolean characteristic productRole
     * 
     * @param pProductRole
     *            the productRole to set
     */
    public void setProductRole(boolean pProductRole) {
        productRole = pProductRole;
    }

    /**
     * Is an instance Role?
     * 
     * @return value of instanceRole characteristic
     */
    public boolean getInstanceRole() {
        return instanceRole;
    }

    /**
     * Is an instance Role?
     * 
     * @return value of productRole characteristic
     */
    public boolean getProductRole() {
        return productRole;
    }

}
