/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.container.sheet.state;

import java.io.Serializable;
import java.util.Date;

/**
 * UiTransition
 * 
 * @author nveillet
 */
public class UiTransition implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -5916004166569217665L;

    private Date date;

    private String destinationState;

    private String login;

    private String originState;

    /**
     * Empty constructor
     */
    public UiTransition() {
    }

    /**
     * Constructor with values
     * 
     * @param pOriginState
     *            the origin state
     * @param pDestinationState
     *            the destination state
     * @param pLogin
     *            the login
     * @param pDate
     *            the date
     */
    public UiTransition(String pOriginState, String pDestinationState,
            String pLogin, Date pDate) {
        originState = pOriginState;
        destinationState = pDestinationState;
        login = pLogin;
        date = pDate;
    }

    /**
     * get date
     * 
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * get destination state
     * 
     * @return the destination state
     */
    public String getDestinationState() {
        return destinationState;
    }

    /**
     * get login
     * 
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * get origin state
     * 
     * @return the origin state
     */
    public String getOriginState() {
        return originState;
    }

    /**
     * set date
     * 
     * @param pDate
     *            the date to set
     */
    public void setDate(Date pDate) {
        date = pDate;
    }

    /**
     * set destination state
     * 
     * @param pDestinationState
     *            the destination state to set
     */
    public void setDestinationState(String pDestinationState) {
        destinationState = pDestinationState;
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
     * set origin state
     * 
     * @param pOriginState
     *            the origin state to set
     */
    public void setOriginState(String pOriginState) {
        originState = pOriginState;
    }
}
