/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
//
// Attention: Generated code! Do not modify by hand!
// Generated by: ValueObject.vsl in andromda-java-cartridge.
//
package org.topcased.gpm.business.authorization.service;

/**
 * @author Atos
 */
public class EndUserData implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public EndUserData() {
    }

    /**
     * Constructor taking all properties.
     */
    public EndUserData(
            final java.lang.String pLogin,
            final java.lang.String pName,
            final java.lang.String pMailAddr,
            final java.lang.String pForname,
            final java.lang.String pId,
            final org.topcased.gpm.business.authorization.service.UserAttributesData[] pUserAttributes) {
        this.login = pLogin;
        this.name = pName;
        this.mailAddr = pMailAddr;
        this.forname = pForname;
        this.id = pId;
        this.userAttributes = pUserAttributes;
    }

    /**
     * Copies constructor from other EndUserData
     */
    public EndUserData(EndUserData pOtherBean) {
        if (pOtherBean != null) {
            this.login = pOtherBean.getLogin();
            this.name = pOtherBean.getName();
            this.mailAddr = pOtherBean.getMailAddr();
            this.forname = pOtherBean.getForname();
            this.id = pOtherBean.getId();
            this.userAttributes = pOtherBean.getUserAttributes();
        }
    }

    private java.lang.String login;

    /**
     * 
     */
    public java.lang.String getLogin() {
        return this.login;
    }

    public void setLogin(java.lang.String pLogin) {
        this.login = pLogin;
    }

    private java.lang.String name;

    /**
     * 
     */
    public java.lang.String getName() {
        return this.name;
    }

    public void setName(java.lang.String pName) {
        this.name = pName;
    }

    private java.lang.String mailAddr;

    /**
     * 
     */
    public java.lang.String getMailAddr() {
        return this.mailAddr;
    }

    public void setMailAddr(java.lang.String pMailAddr) {
        this.mailAddr = pMailAddr;
    }

    private java.lang.String forname;

    /**
     * 
     */
    public java.lang.String getForname() {
        return this.forname;
    }

    public void setForname(java.lang.String pForname) {
        this.forname = pForname;
    }

    private java.lang.String id;

    /**
     * <p>
     * Unique identifier of this user in the database.
     * </p>
     */
    public java.lang.String getId() {
        return this.id;
    }

    public void setId(java.lang.String pId) {
        this.id = pId;
    }

    private org.topcased.gpm.business.authorization.service.UserAttributesData[] userAttributes;

    /**
     * Get the userAttributes
     */
    public org.topcased.gpm.business.authorization.service.UserAttributesData[] getUserAttributes() {
        return this.userAttributes;
    }

    /**
     * Set the userAttributes
     */
    public void setUserAttributes(
            org.topcased.gpm.business.authorization.service.UserAttributesData[] pUserAttributes) {
        this.userAttributes = pUserAttributes;
    }

}