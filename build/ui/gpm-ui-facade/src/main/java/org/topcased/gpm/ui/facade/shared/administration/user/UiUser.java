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

/**
 * User for management.
 * 
 * @author jlouisy
 */
public class UiUser implements Serializable {
    private static final long serialVersionUID = 4476617501571257242L;

    private String emailAdress;

    private String forename;

    private String language;

    private String login;

    private String name;

    private String passWord;

    private String newPassword;

    /**
     * Empty constructor for serialization.
     */
    public UiUser() {

    }

    /**
     * Create UiUser.
     * 
     * @param pLogin
     *            user login.
     * @param pForename
     *            user forename.
     * @param pName
     *            user name.
     * @param pLanguage
     *            user language.
     */
    public UiUser(String pLogin, String pForename, String pName,
            String pLanguage) {
        super();
        login = pLogin;
        forename = pForename;
        name = pName;
        language = pLanguage;
    }

    /**
     * Get email address.
     * 
     * @return email address.
     */
    public String getEmailAdress() {
        return emailAdress;
    }

    /**
     * Get user forename.
     * 
     * @return user forename.
     */
    public String getForename() {
        return forename;
    }

    /**
     * Get user language.
     * 
     * @return user language.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Get user login.
     * 
     * @return user login.
     */
    public String getLogin() {
        return login;
    }

    /**
     * Get user name.
     * 
     * @return user name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get user password.
     * 
     * @return user password
     */
    public String getPassWord() {
        return passWord;
    }

    /**
     * Set email address.
     * 
     * @param pEmailAdress
     *            email address.
     */
    public void setEmailAdress(String pEmailAdress) {
        emailAdress = pEmailAdress;
    }

    /**
     * Set user forename.
     * 
     * @param pForename
     *            user forename.
     */
    public void setForename(String pForename) {
        forename = pForename;
    }

    /**
     * Set user language.
     * 
     * @param pLanguage
     *            user language.
     */
    public void setLanguage(String pLanguage) {
        language = pLanguage;
    }

    /**
     * Set user login.
     * 
     * @param pLogin
     *            user login.
     */
    public void setLogin(String pLogin) {
        login = pLogin;
    }

    /**
     * Set user name.
     * 
     * @param pName
     *            user name.
     */
    public void setName(String pName) {
        name = pName;
    }

    /**
     * Set user password.
     * 
     * @param pPassWord
     *            user password
     */
    public void setPassWord(String pPassWord) {
        passWord = pPassWord;
    }

    /**
     * get newPassword (used only when editing user profile)
     * 
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * set newPassword (used only when editing user profile)
     * 
     * @param pNewPassword
     *            the newPassword to set
     */
    public void setNewPassword(String pNewPassword) {
        newPassword = pNewPassword;
    }

}
