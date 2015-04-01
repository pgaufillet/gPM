/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.user.listing;

import java.util.List;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.admin.user.OpenUserOnEditionCommand;
import org.topcased.gpm.ui.application.client.common.AbstractPresenter;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUser;

import com.google.inject.Inject;

/**
 * The presenter for the UserListingView.
 * 
 * @author nveillet
 */
public class UserListingPresenter extends AbstractPresenter<UserListingDisplay> {

    private final OpenUserOnEditionCommand openUser;

    /**
     * Create a presenter for the UserListingView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pOpenUserCommand
     *            The command for open a user
     */
    @Inject
    public UserListingPresenter(UserListingDisplay pDisplay,
            EventBus pEventBus, OpenUserOnEditionCommand pOpenUserCommand) {
        super(pDisplay, pEventBus);
        openUser = pOpenUserCommand;
    }

    /**
     * Clear view
     */
    public void clear() {
        getDisplay().clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    @Override
    protected void onBind() {
        // Nothing to bind
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onUnbind()
     */
    @Override
    protected void onUnbind() {
        // Nothing to unbind
    }

    /**
     * Set the listing of user sorted by login
     * 
     * @param pUsers
     *            the user list
     */
    public void setUsersByLogin(List<UiUser> pUsers) {
        if (pUsers != null) {
            for (final UiUser lUser : pUsers) {
                getDisplay().addUserByLogin(lUser.getLogin(), lUser.getName(),
                        lUser.getForename(), openUser);
            }
        }
    }

    /**
     * Set the listing of user sorted by name
     * 
     * @param pUsers
     *            the user list
     */
    public void setUsersByName(List<UiUser> pUsers) {
        if (pUsers != null) {
            for (final UiUser lUser : pUsers) {
                getDisplay().addUserByName(lUser.getLogin(), lUser.getName(),
                        lUser.getForename(), openUser);
            }
        }
    }
}
