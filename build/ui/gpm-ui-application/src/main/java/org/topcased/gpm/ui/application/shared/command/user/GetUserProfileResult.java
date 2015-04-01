/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.user;

import java.util.List;

import org.topcased.gpm.ui.facade.shared.administration.user.UiUser;

/**
 * Get user for management result.
 * 
 * @author jlouisy
 */
public class GetUserProfileResult extends AbstractUserResult {

    private static final long serialVersionUID = -2996829566735222575L;

    private UiUser user;

    private boolean editable;

    private boolean passwordEditable;

    private List<String> availableLanguages;

    /**
     * Empty constructor for serialization.
     */
    public GetUserProfileResult() {
    }

    /**
     * Create GetUserResult with values.
     * 
     * @param pUser
     *            the user.
     * @param pEditable
     *            if the user can edit its profile or not
     * @param pPasswordEditable
     *            if the user can edit its passwords
     * @param pAvailableLanguages
     *            list of available languages for the user
     */
    public GetUserProfileResult(UiUser pUser, boolean pEditable,
            boolean pPasswordEditable, List<String> pAvailableLanguages) {
        super();
        user = pUser;
        editable = pEditable;
        passwordEditable = pPasswordEditable;
        availableLanguages = pAvailableLanguages;
    }

    /**
     * get user
     * 
     * @return the user
     */
    public UiUser getUser() {
        return user;
    }

    /**
     * get editable
     * 
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * get availableLanguages
     * 
     * @return the availableLanguages
     */
    public List<String> getAvailableLanguages() {
        return availableLanguages;
    }

    /**
     * get passwordEditable
     * 
     * @return the passwordEditable
     */
    public boolean isPasswordEditable() {
        return passwordEditable;
    }

}
