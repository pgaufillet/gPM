/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Damien Guerin (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.impl;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.simple.AbstractParameterizedContextMapper;
import org.topcased.gpm.business.authorization.service.EndUserData;

/**
 * EndUserData attributes mapper used by spring LDAP.
 * 
 * @author dguerin
 */
public class EndUserDataAttributesMapper extends
        AbstractParameterizedContextMapper<EndUserData> {

    /** The login. */
    private String login;

    /** The mail addr. */
    private String mailAddr;

    /** The user's name. */
    private String name;

    /** The user's forename. */
    private String forename;

    /** The user. */
    private EndUserData user;

    /**
     * Gets the user.
     * 
     * @return the user
     */
    public EndUserData getUser() {
        return user;
    }

    /**
     * Sets the user.
     * 
     * @param pUser
     *            the new user
     */
    public void setUser(EndUserData pUser) {
        user = pUser;
    }

    /**
     * Method used for the mapping.
     * 
     * @param pAttributes
     *            attributes
     * @return Object
     * @throws NamingException
     *             LDAP exception
     */
    public Object mapFromAttributes(Attributes pAttributes)
        throws NamingException {
        if (user != null) {
            if (pAttributes.get(login) != null) {
                String lUid = (String) pAttributes.get(login).get();
                if (lUid != null) {
                    user.setLogin(lUid);
                }
            }
            if (pAttributes.get(mailAddr) != null) {
                String lMail = (String) pAttributes.get(mailAddr).get();
                if (lMail != null) {
                    user.setMailAddr(lMail);
                }
            }
            if (pAttributes.get(name) != null) {
                String lSn = (String) pAttributes.get(name).get();
                if (lSn != null) {
                    user.setName(lSn);
                }
            }
            if (pAttributes.get(forename) != null) {
                String lCn = (String) pAttributes.get(forename).get();
                if (lCn != null) {
                    user.setForname(lCn);
                }
            }
        }
        return user;
    }

    /**
     * Sets the login.
     * 
     * @param pLogin
     *            the new login
     */
    public void setLogin(String pLogin) {
        login = pLogin;
    }

    /**
     * Sets the mail addr.
     * 
     * @param pMailAddr
     *            the new mail addr
     */
    public void setMailAddr(String pMailAddr) {
        mailAddr = pMailAddr;
    }

    /**
     * Sets the name.
     * 
     * @param pName
     *            the new name
     */
    public void setName(String pName) {
        name = pName;
    }

    /**
     * Set the user's forename.
     * 
     * @param pForname
     *            the new forename
     */
    public void setForename(String pForname) {
        forename = pForname;
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
     * Gets the mail addr.
     * 
     * @return the mail addr
     */
    public String getMailAddr() {
        return mailAddr;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the forename.
     * 
     * @return the forename
     */
    public String getForename() {
        return forename;
    }

    /**
     * Mapping method.
     * 
     * @param pContext
     *            LDAP context
     * @return EndUserData user data
     */
    @Override
    protected EndUserData doMapFromContext(DirContextOperations pContext) {
        if (user == null) {
            user = new EndUserData();
        }
        if (pContext.getStringAttribute(login) != null) {
            String lUid = pContext.getStringAttribute(login);
            if (lUid != null) {
                user.setLogin(lUid);
            }
        }
        if (pContext.getStringAttribute(mailAddr) != null) {
            String lMail = pContext.getStringAttribute(mailAddr);
            if (lMail != null) {
                user.setMailAddr(lMail);
            }
        }
        if (pContext.getStringAttribute(name) != null) {
            String lSn = pContext.getStringAttribute(name);
            if (lSn != null) {
                user.setName(lSn);
            }
        }
        if (pContext.getStringAttribute(forename) != null) {
            String lCn = pContext.getStringAttribute(forename);
            if (lCn != null) {
                user.setForname(lCn);
            }
        }
        return user;
    }
}
