/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.user;

import java.util.List;
import java.util.SortedMap;

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.application.client.popup.PopupDisplay;

import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Display interface for the UserAffectationView.
 * 
 * @author nveillet
 */
public interface UserAffectationDisplay extends PopupDisplay {

    /**
     * Clear the view
     */
    public void clear();

    /**
     * Get process the affectations
     * 
     * @return the process affectations
     */
    List<String> getProcessAffectations();

    /**
     * Get the product affectations
     * 
     * @return the product affectations
     */
    SortedMap<String, List<String>> getProductAffectations();

    /**
     * Initialize the affectation field
     * 
     * @param pProductNames
     *            the product names
     * @param pRoles
     *            the role names
     */
    void initAffectation(List<String> pProductNames,
            List<Translation> pRoles, String[] pDisabledRoles);

    /**
     * Refresh the unused product list (after affection initialization)
     */
    void refreshUnusedProducts();

    /**
     * Set a process affectation row
     * 
     * @param pRoleNames
     *            the role names
     */
    void setProcessAffectation(List<String> pRoleNames, String[] pDisabledRoleNames);

    /**
     * Set a affectation row
     * 
     * @param pProductName
     *            the product name
     * @param pRoleNames
     *            the role names
     */
    void setProductAffectation(String pProductName,
            List<String> pRoleNames, String[] pDisabledRoleNames);

    /**
     * Set the save button handler.
     * 
     * @param pHandler
     *            The handler.
     */
    void setSaveButtonHandler(ClickHandler pHandler);
}
