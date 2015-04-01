/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.user;

import java.util.List;

import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUser;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUserAffectation;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUserLists;

/**
 * UserFacade
 * 
 * @author nveillet
 */
public interface UserFacade {

    /**
     * Create a user.
     * 
     * @param pSession
     *            Current session.
     * @param pUser
     *            User to create.
     */
    public void createUser(UiSession pSession, UiUser pUser);

    /**
     * Remove a user.
     * 
     * @param pSession
     *            Current session.
     * @param pLogin
     *            Login of user to delete.
     */
    public void deleteUser(UiSession pSession, String pLogin);

    /**
     * Get a user by its login.
     * 
     * @param pLogin
     *            the login.
     * @return the user.
     */
    public UiUser getUser(String pLogin);

    /**
     * Get a user profile checking that the user has access to its profile
     * modification
     * 
     * @param pRoleToken
     *            the user token
     * @return the User information
     */
    public UiUser getUserProfile(String pRoleToken);

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
            String pLogin);

    /**
     * Get user list sorted by their name/forename.
     * 
     * @return list of users.
     */
    public UiUserLists getUserLists();

    /**
     * Get all user mails
     * 
     * @param pSession
     *            the session
     * @return the list of user mails
     */
    public List<String> getUserMailAddresses(UiSession pSession);

    /**
     * Update a user.
     * 
     * @param pSession
     *            Current session.
     * @param pUser
     *            User to update.
     */
    public void updateUser(UiSession pSession, UiUser pUser);

    /**
     * Update a user profile.
     * 
     * @param pSession
     *            Current session.
     * @param pUser
     *            User to update.
     */
    public void updateUserProfile(UiSession pSession, UiUser pUser);

    /**
     * Update affectations for the given user login.
     * 
     * @param pSession
     *            Current session.
     * @param pLogin
     *            User login.
     * @param pAffectation
     *            User affectations.
     */
    public void updateUserAffectation(UiSession pSession, String pLogin,
            UiUserAffectation pAffectation);
}
