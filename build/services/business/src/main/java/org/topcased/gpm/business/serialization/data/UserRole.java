/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class UserRole.
 * 
 * @author llatil
 */
@XStreamAlias("userRole")
public class UserRole implements Serializable {
    private static final long serialVersionUID = -3140521785884523978L;

    /** The login. */
    @XStreamAsAttribute
    private String login;

    /** The role name. */
    @XStreamAsAttribute
    private String roleName;

    /** The product name. */
    @XStreamAsAttribute
    private String productName;

    /**
     * Gets the login.
     * 
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Gets the product name.
     * 
     * @return the product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Gets the role name.
     * 
     * @return the role name
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * set login
     * 
     * @param pLogin
     *            the login to set
     */
    public void setLogin(String pLogin) {
        login = pLogin;
    }

    /**
     * set roleName
     * 
     * @param pRoleName
     *            the roleName to set
     */
    public void setRoleName(String pRoleName) {
        roleName = pRoleName;
    }

    /**
     * set productName
     * 
     * @param pProductName
     *            the productName to set
     */
    public void setProductName(String pProductName) {
        productName = pProductName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {

        if (pOther instanceof UserRole) {
            UserRole lUserRole = (UserRole) pOther;

            if (!StringUtils.equals(login, lUserRole.getLogin())) {
                return false;
            }
            if (!StringUtils.equals(productName, lUserRole.getProductName())) {
                return false;
            }
            if (!StringUtils.equals(roleName, lUserRole.getRoleName())) {
                return false;
            }

            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(login + productName + roleName).toHashCode();
    }
}
