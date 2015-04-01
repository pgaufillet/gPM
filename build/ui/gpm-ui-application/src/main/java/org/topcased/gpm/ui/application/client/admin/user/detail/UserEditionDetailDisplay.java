/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.user.detail;

import java.util.List;

import org.topcased.gpm.business.util.Translation;

/**
 * Display interface for the UserDetailView.
 * 
 * @author nveillet
 */
public interface UserEditionDetailDisplay extends UserDetailDisplay {

    /**
     * set login
     * 
     * @param pLogin
     *            the login to set
     */
    public void setLogin(String pLogin);

    /**
     * Initialize the affectation field
     * 
     * @param pRoles
     *            the role names
     */
    public void initAffectation(List<Translation> pRoles);

    /**
     * Add a affectation row
     * 
     * @param pProductName
     *            the product name
     * @param pRoleNames
     *            the role names
     */
    public void addProductAffectation(String pProductName, List<String> pRoleNames);

    /**
     * Add a process affectation row
     * 
     * @param pRoleNames
     *            the role names
     */
    public void addProcessAffectation(List<String> pRoleNames);
    
    /**
     * Enable/Disable User edition
     */
    public void enableEdition(boolean pEnable);
}