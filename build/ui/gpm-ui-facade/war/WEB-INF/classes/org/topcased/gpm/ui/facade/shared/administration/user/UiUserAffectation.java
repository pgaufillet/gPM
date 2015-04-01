/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.administration.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import org.topcased.gpm.business.util.Translation;

/**
 * User affectations for management.
 * 
 * @author jlouisy
 */
public class UiUserAffectation implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -3275731300542433136L;

    private List<String> processAffectations;

    //product roles : key is product name, value is role Collection
    private SortedMap<String, List<String>> productAffectations;

    private List<Translation> roleTranslations = new ArrayList<Translation>();
    private String[] disabledRoleNames = new String[0];

    /**
     * UiUserAffectation must be serializable
     */
    public UiUserAffectation() {
    }

    /**
     * Create an UiUserAffectation
     * 
     * @param pProcessAffectations
     *            process roles.
     * @param pProductAffectations
     *            product roles.
     */
    public UiUserAffectation(List<String> pProcessAffectations,
            SortedMap<String, List<String>> pProductAffectations) {
        super();
        processAffectations = pProcessAffectations;
        productAffectations = pProductAffectations;
    }

    /**
     * Create an UiUserAffectation
     * 
     * @param pProcessAffectations
     *            process roles.
     * @param pProductAffectations
     *            product roles.
     * @param pRoleNames
     *            role names.
     */
    public UiUserAffectation(List<String> pProcessAffectations,
            SortedMap<String, List<String>> pProductAffectations,
            List<Translation> pRoleNames) {
        super();
        processAffectations = pProcessAffectations;
        productAffectations = pProductAffectations;
        roleTranslations = pRoleNames;
    }

    /**
     * Get process affectations.
     * 
     * @return process affectations.
     */
    public List<String> getProcessAffectations() {
        return processAffectations;
    }

    /**
     * Get product affectations.
     * 
     * @return product affectations.
     */
    public SortedMap<String, List<String>> getProductAffectations() {
        return productAffectations;
    }

    /**
     * Get role names.
     * 
     * @return role names.
     */
    public List<Translation> getRoleTranslations() {
        return roleTranslations;
    }

    /**
     * Set process affectations.
     * 
     * @param pProcessAffectations
     *            process affectations.
     */
    public void setProcessAffectations(List<String> pProcessAffectations) {
        processAffectations = pProcessAffectations;
    }

    /**
     * Set product affectations.
     * 
     * @param pProductAffectations
     *            product affectations.
     */
    public void setProductAffectations(
            SortedMap<String, List<String>> pProductAffectations) {
        productAffectations = pProductAffectations;
    }

    /**
     * Set role names.
     * 
     * @param pRoleNames
     *            role names.
     */
    public void setRoleTranslations(List<Translation> pRoleNames) {
        roleTranslations = pRoleNames;
    }
    
    /**
     * Get disabled role names.
     * 
     * @return disabled role names.
     */
    public String[] getDisabledRoleNames() {
    	return disabledRoleNames;
    }
    
    /**
     * Set disabled role names.
     * 
     * @param pDisabledRoleNames role names.
     */
    public void setDisabledRoleNames(String[] pDisabledRoleNames) {
    	disabledRoleNames = pDisabledRoleNames;
    }
}
