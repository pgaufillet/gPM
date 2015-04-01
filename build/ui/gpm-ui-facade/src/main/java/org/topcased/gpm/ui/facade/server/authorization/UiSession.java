/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.authorization;

/**
 * UISession represents a gPM user's session.
 * <p>
 * gPM session is a roleToken. This token is the combination of user, product
 * and role.
 * </p>
 * 
 * @author mkargbo
 */
public class UiSession {

    private UiUserSession parent;

    private String productName;

    private String roleName;

    private String roleToken;

    /**
     * Default constructor
     */
    public UiSession() {

    }

    public UiUserSession getParent() {
        return parent;
    }

    public String getProductName() {
        return productName;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getRoleToken() {
        return roleToken;
    }

    public void setParent(UiUserSession pParent) {
        parent = pParent;
    }

    public void setProductName(String pProductName) {
        productName = pProductName;
    }

    public void setRoleName(String pRoleName) {
        roleName = pRoleName;
    }

    public void setRoleToken(String pRoleToken) {
        roleToken = pRoleToken;
    }
}
