/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.init;

import java.io.Serializable;
import java.util.Map;

/**
 * Result of the application's initialization action. <h4>Result's elements</h4>
 * <ul>
 * <li>i18n values (text 's values and img's values)</li>
 * </ul>
 * 
 * @author mkargbo
 */
public class InitInfo implements Serializable {
    private static final long serialVersionUID = 6183254269074215584L;

    private boolean adminAccess;

    private String helpUrl;

    private Map<String, String> imgValues;

    private String locale;

    private String language;

    private String login;

    private String contactUrl;

    private String processName;

    private Map<String, String> textValues;

    private boolean externalAuthentication;

    /**
     * Default constructor
     */
    public InitInfo() {
    }

    /**
     * get help url
     * 
     * @return the help url
     */
    public String getHelpUrl() {
        return helpUrl;
    }

    /**
     * get images values
     * 
     * @return the images values
     */
    public Map<String, String> getImgValues() {
        return imgValues;
    }

    /**
     * get locale
     * 
     * @return the locale
     */
    public String getLocale() {
        return locale;
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
     * get texts values
     * 
     * @return the texts values
     */
    public Map<String, String> getTextValues() {
        return textValues;
    }

    /**
     * get administration module access
     * 
     * @return the administration module access
     */
    public boolean isAdminAccess() {
        return adminAccess;
    }

    /**
     * set administration module access
     * 
     * @param pAdminAccess
     *            the administration module access to set
     */
    public void setAdminAccess(boolean pAdminAccess) {
        adminAccess = pAdminAccess;
    }

    /**
     * set help url
     * 
     * @param pHelpUrl
     *            the help url to set
     */
    public void setHelpUrl(String pHelpUrl) {
        helpUrl = pHelpUrl;
    }

    /**
     * set images values
     * 
     * @param pImgValues
     *            the images values to set
     */
    public void setImgValues(Map<String, String> pImgValues) {
        imgValues = pImgValues;
    }

    /**
     * set locale
     * 
     * @param pLocale
     *            the locale to set
     */
    public void setLocale(String pLocale) {
        locale = pLocale;
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
     * set texts values
     * 
     * @param pTextValues
     *            the texts values to set
     */
    public void setTextValues(Map<String, String> pTextValues) {
        textValues = pTextValues;
    }

    /**
     * get contactUrl
     * 
     * @return the contactUrl
     */
    public String getContactUrl() {
        return contactUrl;
    }

    /**
     * set contactUrl
     * 
     * @param pContactUrl
     *            the contactUrl to set
     */
    public void setContactUrl(String pContactUrl) {
        contactUrl = pContactUrl;
    }

    /**
     * get externalAuthentication
     * 
     * @return the externalAuthentication
     */
    public boolean isExternalAuthentication() {
        return externalAuthentication;
    }

    /**
     * set externalAuthentication
     * 
     * @param pExternalAuthentication
     *            the externalAuthentication to set
     */
    public void setExternalAuthentication(boolean pExternalAuthentication) {
        externalAuthentication = pExternalAuthentication;
    }

    /**
     * get processName
     * 
     * @return the processName
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * set processName
     * 
     * @param pProcessName
     *            the processName to set
     */
    public void setProcessName(String pProcessName) {
        processName = pProcessName;
    }

    /**
     * Get language
     * 
     * @return language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Set language
     * 
     * @param pLanguage
     *            language
     */
    public void setLanguage(String pLanguage) {
        this.language = pLanguage;
    }

}