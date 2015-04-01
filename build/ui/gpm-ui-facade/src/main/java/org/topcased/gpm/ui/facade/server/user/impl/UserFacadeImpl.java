/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.user.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.authorization.service.EndUserData;
import org.topcased.gpm.business.authorization.service.RoleData;
import org.topcased.gpm.business.authorization.service.UserAttributesData;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.serialization.data.User;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.business.util.log.GPMActionLogConstants;
import org.topcased.gpm.ui.facade.server.AbstractFacade;
import org.topcased.gpm.ui.facade.server.FacadeLocator;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.user.UserFacade;
import org.topcased.gpm.ui.facade.server.user.UserLoginComparator;
import org.topcased.gpm.ui.facade.server.user.UserNameComparator;
import org.topcased.gpm.ui.facade.server.user.UserRoleComparator;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUser;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUserAffectation;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUserLists;

/**
 * UserFacade
 * 
 * @author nveillet
 */
public class UserFacadeImpl extends AbstractFacade implements UserFacade {

    /** The name of the user attribute used to store the default language. */
    public static final String ATTR_USER_DEFAULT_LANGUAGE = "defaultLanguage";

    /** The key for the language. */
    private static final String LANGUAGE_KEY = "lang";
    
    /** Process String for logging */
    private static final String PROCESS = "Process";

    private EndUserData convertUser(UiUser pUser) {
        return new EndUserData(pUser.getLogin(), pUser.getName(),
                pUser.getEmailAdress(), pUser.getForename(), null,
                new UserAttributesData[0]);
    }

    /**
     * Create a user.
     * 
     * @param pSession
     *            Current session.
     * @param pUser
     *            User to create.
     */
    public void createUser(UiSession pSession, UiUser pUser) {
        getAuthorizationService().createUser(pSession.getRoleToken(),
                convertUser(pUser), pUser.getPassWord(), getContext(pSession));
        
        // Log
        gpmLogger.highInfo(pSession.getParent().getLogin(), GPMActionLogConstants.USER_CREATION, 
                pUser.getLogin());
        
        EndUserData lUserData =
                getAuthorizationService().getUserData(pUser.getLogin());
        getAuthorizationService().setValueToUserAttribute(lUserData,
                ATTR_USER_DEFAULT_LANGUAGE, pUser.getLanguage());
    }

    /**
     * Remove a user.
     * 
     * @param pSession
     *            Current session.
     * @param pLogin
     *            Login of user to delete.
     */
    public void deleteUser(UiSession pSession, String pLogin) {
        getAuthorizationService().removeUser(pSession.getRoleToken(), pLogin);
        
        // Log
        gpmLogger.mediumInfo(pSession.getParent().getLogin(), 
                GPMActionLogConstants.USER_DELETION, pLogin);
    }

    /**
     * Get a user by its login.
     * 
     * @param pLogin
     *            the login.
     * @return the user.
     */
    public UiUser getUser(String pLogin) {
        return getUser(pLogin, true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.server.user.UserFacade#getUser(java.lang.String)
     */
    public UiUser getUserProfile(String pRoleToken) {

        return getUser(getAuthorizationService().getLogin(pRoleToken));
    }

    private UiUser getUser(String pUserLogin, boolean pWithMailAdress) {
        EndUserData lUserData =
                getAuthorizationService().getUserData(pUserLogin);
        if (lUserData == null) {
            return null;
        }
        String lLanguage =
                getAuthorizationService().getValueOfUserAttribute(lUserData,
                        LANGUAGE_KEY);
        if (lLanguage == null) {
            lLanguage =
                    getAuthorizationService().getValueOfUserAttribute(
                            lUserData, ATTR_USER_DEFAULT_LANGUAGE);
        }
        UiUser lUser =
                new UiUser(lUserData.getLogin(), lUserData.getForname(),
                        lUserData.getName(), lLanguage);
        if (pWithMailAdress) {
            lUser.setEmailAdress(lUserData.getMailAddr());
        }
        return lUser;
    }

    /**
     * Get user affectations. Products are ordered by name, Roles by
     * instanciation order.
     * 
     * @param pSession
     *            Current user session.
     * @param pLogin
     *            User login.
     * @return user affectations.
     */
    public UiUserAffectation getUserAffectation(UiSession pSession,
            String pLogin) {
        Collection<RoleData> lRoleDatas =
                getAuthorizationService().getRoles(pLogin,
                        pSession.getParent().getProcessName());
        List<String> lRolesOrder =
                new ArrayList<String>(getAuthorizationService().getRolesNames(
                        pSession.getParent().getProcessName()));

        List<String> lProcessRoles = new ArrayList<String>();
        TreeMap<String, List<String>> lProductRoles =
                new TreeMap<String, List<String>>();

        TreeSet<String> lAllUsedRoles =
                new TreeSet<String>(UserRoleComparator.getInstance(lRolesOrder));

        for (RoleData lRoleData : lRoleDatas) {
            String lProductName = lRoleData.getProductName();
            if (lProductName == null) {
                lProcessRoles.add(lRoleData.getRoleName());
            }
            else {
                if (!lProductRoles.containsKey(lProductName)) {
                    lProductRoles.put(lProductName, new ArrayList<String>());
                }
                lProductRoles.get(lProductName).add(lRoleData.getRoleName());
            }
            lAllUsedRoles.add(lRoleData.getRoleName());
        }

        List<Translation> lRoles =
                FacadeLocator.instance().getI18nFacade().getTranslations(
                        pSession.getParent(), lAllUsedRoles);

        return new UiUserAffectation(lProcessRoles, lProductRoles, lRoles);
    }

    /**
     * get all users with login, name foreneme
     * 
     * @return List<UiUser>
     */
    private List<UiUser> getUsers() {
        List<UiUser> lResultUserList = new ArrayList<UiUser>();
        List<User> lLightList = getAuthorizationService().getUsers();
        for (User lUser : lLightList) {
            UiUser lAUser = new UiUser();
            lAUser.setLogin(lUser.getLogin());
            lAUser.setName(lUser.getName());
            lAUser.setForename(lUser.getForname());
            lResultUserList.add(lAUser);
        }

        return lResultUserList;
    }

    /**
     * Get user list sorted by their login.
     * 
     * @return list of users.
     */
    private List<UiUser> getUserListSortedByLogin(List<UiUser> pUserLists) {
        Collections.sort(pUserLists, UserLoginComparator.getInstance());
        return pUserLists;
    }

    /**
     * Get user list sorted by their name/forename.
     * 
     * @return list of users.
     */
    private List<UiUser> getUserListSortedByName(List<UiUser> pUserLists) {
        List<UiUser> lUserList = new ArrayList<UiUser>(pUserLists);
        Collections.sort(lUserList, UserNameComparator.getInstance());
        return lUserList;
    }

    /**
     * {@inheritDoc}
     */
    public UiUserLists getUserLists() {
        List<UiUser> lUserList = getUsers();
        UiUserLists lUserLists =
                new UiUserLists(getUserListSortedByLogin(lUserList),
                        getUserListSortedByName(lUserList));
        return lUserLists;
    }

    /**
     * Get all user mails
     * 
     * @param pSession
     *            the session
     * @return the list of user mails
     */
    public List<String> getUserMailAddresses(UiSession pSession) {

        List<User> lUsers = getAuthorizationService().getUsers();

        ArrayList<String> lUserMails = new ArrayList<String>();
        for (User lUser : lUsers) {
            if (StringUtils.isNotEmpty(lUser.getMail())) {
                lUserMails.add(lUser.getMail());
            }
        }

        Collections.sort(lUserMails);

        return lUserMails;
    }

    /**
     * Update a user.
     * 
     * @param pSession
     *            Current session.
     * @param pUser
     *            User to create.
     */
    public void updateUser(UiSession pSession, UiUser pUser) {
        EndUserData lUser = convertUser(pUser);

        // update user language
        getAuthorizationService().setValueToUserAttribute(lUser,
                ATTR_USER_DEFAULT_LANGUAGE, pUser.getLanguage());

        // update user
        getAuthorizationService().updateUser(pSession.getRoleToken(), lUser,
                pUser.getPassWord(), getContext(pSession));
        
        // Log
        gpmLogger.mediumInfo(pSession.getParent().getLogin(), GPMActionLogConstants.USER_UPDATE, 
                pUser.getLogin());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.server.user.UserFacade#updateUser(org.topcased.gpm.ui.facade.server.authorization.UiSession,
     *      org.topcased.gpm.ui.facade.shared.administration.user.UiUser)
     */
    public void updateUserProfile(UiSession pSession, UiUser pUser) {
        EndUserData lUser = convertUser(pUser);

        // If no password change, or old password is valid
        if (pUser.getNewPassword().isEmpty()
                || null != getAuthorizationService().validateLogin(
                        pUser.getLogin(), pUser.getPassWord())) {
            // update user (controls in service)
            getAuthorizationService().updateUser(pSession.getRoleToken(),
                    lUser, pUser.getNewPassword(), getContext(pSession));

            // update user language
            getAuthorizationService().setValueToUserAttribute(lUser,
                    ATTR_USER_DEFAULT_LANGUAGE, pUser.getLanguage());
            
            // Log
            gpmLogger.mediumInfo(pSession.getParent().getLogin(), GPMActionLogConstants.USER_UPDATE, 
                    pUser.getLogin());
        }
        else {
            throw new GDMException("Invalid password");
        }
    }

    /**
     * Update affectations for the given user login.
     * 
     * @param pSession
     *            Current session.
     * @param pLogin
     *            User login.
     * @param pNewAffectation
     *            User affectations.
     */
    public void updateUserAffectation(UiSession pSession, String pLogin,
            UiUserAffectation pNewAffectation) {
    	
    	UiUserAffectation lCurrentAffectation = getUserAffectation(pSession, pLogin);
    	Collection<RoleData> lOldProductRoles = new ArrayList<RoleData>();
        for (String lProductName : lCurrentAffectation.getProductAffectations().keySet()) {
            for (String lProductRoleName : lCurrentAffectation.getProductAffectations().get(
                    lProductName)) {
                lOldProductRoles.add(new RoleData(lProductRoleName, lProductName));
            }
        }
    	
        Collection<RoleData> lNewRoles = new ArrayList<RoleData>();
        for (String lProcessRoleName : pNewAffectation.getProcessAffectations()) {
            lNewRoles.add(new RoleData(lProcessRoleName, null));
        }
        for (String lProductName : pNewAffectation.getProductAffectations().keySet()) {
            for (String lProductRoleName : pNewAffectation.getProductAffectations().get(
                    lProductName)) {
                lNewRoles.add(new RoleData(lProductRoleName, lProductName));
            }
        }
        
        getAuthorizationService().setRoles(pSession.getRoleToken(), pLogin,
                pSession.getParent().getProcessName(), lOldProductRoles, lNewRoles, getContext(pSession));
        
        StringBuilder lRolesLog = new StringBuilder();
        for (RoleData lRoleData : lNewRoles) {
        	String lProductName = PROCESS;
        	if (lRoleData.getProductName() != null && !lRoleData.getProductName().equals("")) {
        		lProductName = lRoleData.getProductName();
        	}
        	lRolesLog.append(lProductName + "#" + lRoleData.getRoleName() + ";");
        }
        if (lRolesLog.length() > 0) {
        	lRolesLog = lRolesLog.deleteCharAt(lRolesLog.length() - 1);
        }

        // Log
        gpmLogger.lowInfo(pSession.getParent().getLogin(), GPMActionLogConstants.USER_RIGHT_UPDATE, 
                pLogin, lRolesLog.toString());
        
    }
}
