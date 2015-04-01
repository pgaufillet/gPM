/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.container.sheet;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.values.field.BusinessFieldGroup;
import org.topcased.gpm.business.values.sheet.BusinessSheet;
import org.topcased.gpm.ui.facade.shared.container.UiContainer;
import org.topcased.gpm.ui.facade.shared.container.field.UiFieldGroup;
import org.topcased.gpm.ui.facade.shared.container.sheet.state.UiTransition;

/**
 * UiSheet
 * 
 * @author nveillet
 */
public class UiSheet extends UiContainer implements BusinessSheet {

    /** serialVersionUID */
    private static final long serialVersionUID = -6712471680059502735L;

    private ArrayList<UiFieldGroup> fieldGroups;

    private String functionalReference;

    private String lockUserLogin;

    private String productName;

    private String state;

    private ArrayList<UiTransition> transitionHistoric;

    private int version;

    /**
     * Create new UiSheet
     */
    public UiSheet() {
        super();
        fieldGroups = new ArrayList<UiFieldGroup>();
    }

    /**
     * Add a field group
     * 
     * @param pFieldGroup
     *            The field group to add
     */
    public void addFieldGroup(UiFieldGroup pFieldGroup) {
        fieldGroups.add(pFieldGroup);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.sheet.BusinessSheet#getFieldGroup(java.lang.String)
     */
    @Override
    public BusinessFieldGroup getFieldGroup(String pFieldGroupName) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.sheet.BusinessSheet#getFieldGroupNames()
     */
    @Override
    public List<String> getFieldGroupNames() {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * Get the field groups
     * 
     * @return The field groups
     */
    public List<UiFieldGroup> getFieldGroups() {
        return fieldGroups;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.sheet.BusinessSheet#getFunctionalReference()
     */
    @Override
    public String getFunctionalReference() {
        return functionalReference;
    }

    /**
     * get the user login who's lock sheet
     * 
     * @return the lockUserLogin
     */
    public String getLockUserLogin() {
        return lockUserLogin;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.sheet.BusinessSheet#getProductName()
     */
    @Override
    public String getProductName() {
        return productName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.sheet.BusinessSheet#getState()
     */
    @Override
    public String getState() {
        return state;
    }

    /**
     * get transition historic
     * 
     * @return the transition historic
     */
    public ArrayList<UiTransition> getTransitionHistoric() {
        return transitionHistoric;
    }

    /**
     * get version
     * 
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * Set the sheet functional reference
     * 
     * @param pFunctionalReference
     *            The functional reference to set
     */
    public void setFunctionalReference(String pFunctionalReference) {
        functionalReference = pFunctionalReference;
    }

    /**
     * set the user login who's lock sheet
     * 
     * @param pLockUserLogin
     *            the lockUserLogin to set
     */
    public void setLockUserLogin(String pLockUserLogin) {
        lockUserLogin = pLockUserLogin;
    }

    /**
     * Set the sheet product name
     * 
     * @param pProductName
     *            The product name to set
     */
    public void setProductName(String pProductName) {
        productName = pProductName;
    }

    /**
     * Set the sheet state (translated)
     * 
     * @param pState
     *            The state to set
     */
    public void setState(String pState) {
        state = pState;
    }

    /**
     * set transition historic
     * 
     * @param pTransitionHistoric
     *            the transition historic to set
     */
    public void setTransitionHistoric(
            ArrayList<UiTransition> pTransitionHistoric) {
        transitionHistoric = pTransitionHistoric;
    }

    /**
     * set version
     * 
     * @param pVersion
     *            the version to set
     */
    public void setVersion(int pVersion) {
        version = pVersion;
    }
}
