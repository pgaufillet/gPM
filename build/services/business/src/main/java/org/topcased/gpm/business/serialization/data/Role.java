/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.authorization.service.RoleData;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class Role.
 * 
 * @author llatil
 */
@XStreamAlias("role")
public class Role extends NamedElement {

    private static final long serialVersionUID = -7058176311760156202L;

    /** The product name. */
    @XStreamAsAttribute
    private String productName;

    /**
     * Default ctor.
     * <p>
     * No initialization done.
     */
    public Role() {
        super();
    }

    /**
     * Constructs a new Role from a RoleData content
     * 
     * @param pRoleData
     *            Role data retrieved through AuthorizationService
     */
    public Role(RoleData pRoleData) {
        setName(pRoleData.getRoleName());
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
     * Sets the product name.
     * 
     * @param pProductName
     *            the product name
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

        if (pOther instanceof Role) {
            Role lOther = (Role) pOther;

            if (!super.equals(lOther)) {
                return false;
            }
            if (!StringUtils.equals(productName, lOther.productName)) {
                return false;
            }

            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        // NamedElement hashcode is sufficient
        return super.hashCode();
    }
}
