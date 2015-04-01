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

import java.util.List;

import javax.naming.directory.SearchControls;

import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.simple.SimpleLdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.EqualsFilter;
import org.topcased.gpm.business.authorization.service.EndUserData;
import org.topcased.gpm.business.authorization.service.UserCredentials;
import org.topcased.gpm.domain.accesscontrol.EndUser;
import org.topcased.gpm.domain.accesscontrol.EndUserDao;

/**
 * UserCredentials implementation for LDAP.
 * 
 * @author dguerin
 */
public class UserCredentialsLDAP implements UserCredentials {

    /** The LDAP template. */
    private SimpleLdapTemplate ldapTemplate;

    /** The end user data attributes mapper. */
    private EndUserDataAttributesMapper endUserDataAttributesMapper;

    /** The LDAP context source. */
    private LdapContextSource ldapContextSource;

    /**
     * Gets the LDAP context source.
     * 
     * @return the LDAP context source
     */
    public LdapContextSource getLdapContextSource() {
        return ldapContextSource;
    }

    /**
     * Sets the LDAP context source.
     * 
     * @param pLdapContextSource
     *            the new LDAP context source
     */
    public void setLdapContextSource(LdapContextSource pLdapContextSource) {
        ldapContextSource = pLdapContextSource;
    }

    /**
     * Gets the LDAP template.
     * 
     * @return the LDAP template
     */
    public SimpleLdapTemplate getLdapTemplate() {
        return ldapTemplate;
    }

    /**
     * Sets the LDAP template.
     * 
     * @param pLdapTemplate
     *            the new LDAP template
     */
    public void setLdapTemplate(SimpleLdapTemplate pLdapTemplate) {
        ldapTemplate = pLdapTemplate;
    }

    /**
     * Gets the end user data attributes mapper.
     * 
     * @return the end user data attributes mapper
     */
    public EndUserDataAttributesMapper getEndUserDataAttributesMapper() {
        return endUserDataAttributesMapper;
    }

    /**
     * Sets the end user data attributes mapper.
     * 
     * @param pEndUserDataAttributesMapper
     *            the new end user data attributes mapper
     */
    public void setEndUserDataAttributesMapper(
            EndUserDataAttributesMapper pEndUserDataAttributesMapper) {
        endUserDataAttributesMapper = pEndUserDataAttributesMapper;
    }

    /**
     * UserCredentialsBD constructor.
     */
    public UserCredentialsLDAP() {
    }

    /**
     * Get information on a user.
     * 
     * @param pLoginName
     *            Login name of the user
     * @param pCaseSensitive
     *            Login case sensitive
     *            
     * @deprecated
     * @see UserCredentialsLDAP#getUserInfos(String, boolean, boolean)           
     *            
     * @return User info or null if login does not exist
     */
    @Deprecated
    public EndUserData getUserInfos(String pLoginName, boolean pCaseSensitive) {
        return getUserInfos(pLoginName, pCaseSensitive, false);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public EndUserData getUserInfos(String pLoginName, boolean pCaseSensitive,
            boolean pLightUserData) {
        // Building filter
        EqualsFilter lFilter =
                new EqualsFilter(endUserDataAttributesMapper.getLogin(),
                        pLoginName);

        // Setting returning attributes, none in this case
        SearchControls lSc = new SearchControls();
        lSc.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        EndUser lUserBD = endUserDao.getUser(pLoginName, pCaseSensitive);
        // If user is in LDAP but not in BD, so error
        if (lUserBD == null) {
            return null;
        }
        endUserDataAttributesMapper.setUser(
                authorizationService.createEndUserData(lUserBD, pLightUserData));

        try {
            List<EndUserData> lListe =
                    ldapTemplate.search("", lFilter.encode(),
                            endUserDataAttributesMapper);

            if (lListe != null && !lListe.isEmpty()) {
                return lListe.get(0);
            }
            return null;
        }
        catch (AuthenticationException e) {
            // User is not in LDAP, so error
            return null;
        }
    }

    /**
     * Validate user, pass.
     * 
     * @param pUsername
     *            login name of the user
     * @param pPasswd
     *            User password
     * @param pCaseSensitive
     *            Login case sensitive
     * @return token used to identify the user.
     */
    public EndUser validateLogin(String pUsername, String pPasswd,
            Boolean pCaseSensitive) {

        // Agent DN construct
        DistinguishedName lDnAgent =
                new DistinguishedName(endUserDataAttributesMapper.getLogin()
                        + "=" + pUsername + ","
                        + ldapContextSource.getBaseLdapPath());

        ldapContextSource.setUserDn(lDnAgent.encode());
        ldapContextSource.setPassword(pPasswd);

        try {
            ldapContextSource.getReadOnlyContext();
        }
        catch (AuthenticationException e) {
            // User is not in LDAP, so error
            return null;
        }

        /* Here, user is in LDAP and password is valid */
        // get User in BD
        EndUser lUserBD = endUserDao.getUser(pUsername, pCaseSensitive);
        // If user is in LDAP but not in BD, so error
        if (lUserBD == null) {
            return null;
        }
        return lUserBD;
    }

    /** The authorization service. */
    private AuthorizationServiceImpl authorizationService;

    /**
     * Sets the authorization service impl.
     * 
     * @param pAuthorizationService
     *            the new authorization service impl
     */
    public void setAuthorizationServiceImpl(
            AuthorizationServiceImpl pAuthorizationService) {
        authorizationService = pAuthorizationService;
    }

    private EndUserDao endUserDao;

    public final void setEndUserDao(EndUserDao pEndUserDao) {
        endUserDao = pEndUserDao;
    }
}
