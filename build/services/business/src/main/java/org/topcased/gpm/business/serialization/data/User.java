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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.util.lang.CollectionUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class User.
 * 
 * @author llatil
 */
@XStreamAlias("user")
public class User extends AttributesContainer {

    private static final long serialVersionUID = -2630966576510020058L;

    /** The login. */
    @XStreamAsAttribute
    private String login;

    /** The mail. */
    @XStreamAsAttribute
    private String mail;

    /** The forname. */
    @XStreamAsAttribute
    private String forname;

    /** The password. */
    @XStreamAsAttribute
    private String password;

    /** Define the password encoding. */
    @XStreamAsAttribute
    private String passwordEncoding;

    /** The roles. */
    @XStreamAlias(value = "roles")
    private List<Role> roles;

    /**
     * Gets the forname.
     * 
     * @return the forname
     */
    public String getForname() {
        return forname;
    }

    /**
     * Gets the login.
     * 
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets the login.
     * 
     * @param pLogin
     *            the login
     */
    public void setLogin(String pLogin) {
        login = pLogin;
    }

    /**
     * Gets the mail.
     * 
     * @return the mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * Gets the password.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the roles.
     * 
     * @return the roles
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * Sets the forname.
     * 
     * @param pForname
     *            the forname
     */
    public void setForname(String pForname) {
        forname = pForname;
    }

    /**
     * Sets the mail.
     * 
     * @param pMail
     *            the mail adress.
     */
    public void setMail(String pMail) {
        mail = pMail;
    }

    /**
     * Sets the password.
     * 
     * @param pPassword
     *            the password
     */
    public void setPassword(String pPassword) {
        password = pPassword;
    }

    /**
     * Sets the roles.
     * 
     * @param pRoles
     *            the roles
     */
    public void setRoles(List<Role> pRoles) {
        roles = pRoles;
    }

    /**
     * get passwordEncoding
     * 
     * @return the passwordEncoding
     */
    public String getPasswordEncoding() {
        return passwordEncoding;
    }

    /**
     * set passwordEncoding
     * 
     * @param pPasswordEncoding
     *            the passwordEncoding to set
     */
    public void setPasswordEncoding(String pPasswordEncoding) {
        passwordEncoding = pPasswordEncoding;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {

        if (pOther instanceof User) {
            User lOther = (User) pOther;

            if (!super.equals(lOther)) {
                return false;
            }
            if (!StringUtils.equals(login, lOther.login)) {
                return false;
            }
            if (!StringUtils.equals(forname, lOther.forname)) {
                return false;
            }
            if (!StringUtils.equals(mail, lOther.mail)) {
                return false;
            }
            if (!StringUtils.equals(password, lOther.password)) {
                return false;
            }
            if (!StringUtils.equals(passwordEncoding, lOther.passwordEncoding)) {
                return false;
            }
            if (!CollectionUtils.equals(roles, lOther.roles)) {
                return false;
            }

            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        // AttributesContainer hashcode is sufficient
        return super.hashCode();
    }
}
