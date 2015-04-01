/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.authorization;

import java.util.HashMap;

import net.customware.gwt.dispatch.shared.Action;

/**
 * LoginAction
 * 
 * @author nveillet
 */
public class LoginAction implements Action<AbstractConnectionResult> {
    private static final long serialVersionUID = 5542791916337772625L;

    private String applicationUrl;

    private String[] availableLocale;

    private String[] availableLanguages;

    private String localeName;

    private String login;

    private HashMap<String, String> parameter;

    private String password;

    /**
     * Create Action Constructor for serialization
     */
    public LoginAction() {
    }

    /**
     * create action
     */
    public LoginAction(String pApplicationUrl) {
        applicationUrl = pApplicationUrl;
    }

    /**
     * create action with login and password
     * 
     * @param pLogin
     *            the login
     * @param pPassword
     *            the password
     * @param pApplicationUrl
     *            the application url
     */
    public LoginAction(String pLogin, String pPassword, String pApplicationUrl) {
        login = pLogin;
        password = pPassword;
        applicationUrl = pApplicationUrl;
    }

    /**
     * get applicationUrl
     * 
     * @return the applicationUrl
     */
    public String getApplicationUrl() {
        return applicationUrl;
    }

    public String[] getAvailableLocale() {
        return availableLocale;
    }

    /**
     * get locale name
     * 
     * @return the locale name
     */
    public String getLocaleName() {
        return localeName;
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
     * get a parameter from its name
     * 
     * @param pParameterName
     *            the parameter name
     * @return the parameter name
     */
    public String getParameter(String pParameterName) {
        if (parameter == null) {
            return null;
        }
        else {
            // Parameter to upper case because all keys of the Map also are in upper case
            return parameter.get(pParameterName.toUpperCase());
        }
    }

    /**
     * get password
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * set applicationUrl
     * 
     * @param pApplicationUrl
     *            the applicationUrl to set
     */
    public void setApplicationUrl(String pApplicationUrl) {
        applicationUrl = pApplicationUrl;
    }

    public void setAvailableLocale(String[] pAvailableLocale) {
        availableLocale = pAvailableLocale;
    }

    /**
     * set locale name
     * 
     * @param pLocaleName
     *            the locale name to set
     */
    public void setLocaleName(String pLocaleName) {
        localeName = pLocaleName;
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
     * Set the parameters.<br />
     * <br />
     * <b>Note: </b>All keys of the Map must be in upper case
     * 
     * @param pParameter
     *            the parameters (map keys in upper case)
     */
    public void setParameters(HashMap<String, String> pParameter) {
        parameter = pParameter;
    }

    /**
     * set password
     * 
     * @param pPassword
     *            the password to set
     */
    public void setPassword(String pPassword) {
        password = pPassword;
    }

    /**
     * Get available languages defined in instance.
     * 
     * @return available languages
     */
    public String[] getAvailableLanguages() {
        return availableLanguages;
    }

    /**
     * Set available languages
     * 
     * @param pAvailableLanguages
     *            Available languages
     */
    public void setAvailableLanguages(String[] pAvailableLanguages) {
        this.availableLanguages = pAvailableLanguages;
    }

}