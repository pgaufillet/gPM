/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
//
// Attention: Generated code! Do not modify by hand!
// Generated by: ValueObject.vsl in andromda-java-cartridge.
//
package org.topcased.gpm.business.authorization.service;

/**
 * @author Atos
 */
public class RoleData implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public RoleData() {
    }

    /**
     * Constructor taking all properties.
     */
    public RoleData(final java.lang.String pRoleName,
            final java.lang.String pProductName) {
        this.roleName = pRoleName;
        this.productName = pProductName;
    }

    /**
     * Copies constructor from other RoleData
     */
    public RoleData(RoleData pOtherBean) {
        if (pOtherBean != null) {
            this.roleName = pOtherBean.getRoleName();
            this.productName = pOtherBean.getProductName();
        }
    }

    private java.lang.String roleName;

    /**
     * 
     */
    public java.lang.String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(java.lang.String pRoleName) {
        this.roleName = pRoleName;
    }

    private java.lang.String productName;

    /**
     * <p>
     * Name of the product associated with the role (null for admin roles).
     * </p>
     */
    public java.lang.String getProductName() {
        return this.productName;
    }

    public void setProductName(java.lang.String pProductName) {
        this.productName = pProductName;
    }

}