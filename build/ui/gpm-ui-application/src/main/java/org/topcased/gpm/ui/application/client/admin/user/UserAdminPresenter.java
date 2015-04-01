/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.user;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.action.ActionName;
import org.topcased.gpm.ui.application.client.admin.user.detail.UserCreationDetailPresenter;
import org.topcased.gpm.ui.application.client.admin.user.detail.UserEditionDetailPresenter;
import org.topcased.gpm.ui.application.client.admin.user.listing.UserListingPresenter;
import org.topcased.gpm.ui.application.client.common.AbstractPresenter;
import org.topcased.gpm.ui.application.client.common.workspace.detail.EmptyDetailPresenter;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.client.event.EmptyAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.menu.admin.user.UserAdminMenuBuilder;
import org.topcased.gpm.ui.application.shared.command.authorization.OpenAdminModuleResult;
import org.topcased.gpm.ui.application.shared.command.user.CreateUserResult;
import org.topcased.gpm.ui.application.shared.command.user.DeleteUserResult;
import org.topcased.gpm.ui.application.shared.command.user.GetUserResult;

import com.google.inject.Inject;

/**
 * The presenter for the UserAdminView.
 * 
 * @author tpanuel
 */
public class UserAdminPresenter extends AbstractPresenter<UserAdminDisplay> {

    private final UserCreationDetailPresenter creationDetail;

    private final UserEditionDetailPresenter editionDetail;

    private final EmptyDetailPresenter emptyDetail;

    private final UserListingPresenter listing;

    private final UserAdminMenuBuilder menuBuilder;

    /**
     * Create a presenter for the UserAdminView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pListing
     *            The listing presenter.
     * @param pEditionDetail
     *            The edition detail presenter.
     * @param pCreationDetail
     *            The creation detail presenter.
     * @param pMenuBuilder
     *            The menu builder.
     */
    @Inject
    public UserAdminPresenter(final UserAdminDisplay pDisplay,
            final EventBus pEventBus, final UserListingPresenter pListing,
            final UserEditionDetailPresenter pEditionDetail,
            final UserCreationDetailPresenter pCreationDetail,
            final EmptyDetailPresenter pEmptyDetail,
            final UserAdminMenuBuilder pMenuBuilder) {
        super(pDisplay, pEventBus);
        listing = pListing;
        editionDetail = pEditionDetail;
        creationDetail = pCreationDetail;
        emptyDetail = pEmptyDetail;
        menuBuilder = pMenuBuilder;
    }

    /**
     * get creation detail presenter
     * 
     * @return the creation detail presenter
     */
    public UserCreationDetailPresenter getCreationDetail() {
        return creationDetail;
    }

    /**
     * get edition detail presenter
     * 
     * @return the edition detail presenter
     */
    public UserEditionDetailPresenter getEditionDetail() {
        return editionDetail;
    }

    /**
     * Initialize the workspace.
     * 
     * @param pResult
     *            The result.
     */
    public void init(final OpenAdminModuleResult pResult) {
        // Add the menu tool bar
        getDisplay().setToolBar(menuBuilder.buildMenu(pResult.getUserActions(), null));

        // Set the filters
        listing.setUsersByName(pResult.getUserListSortedByName());
        listing.setUsersByLogin(pResult.getUserListSortedByLogin());

        // Init details
        creationDetail.init(pResult);
        editionDetail.init(pResult);
        
        // Disable user edition if action is not present
      	editionDetail.getDisplay().enableEdition(pResult.getUserActions().containsKey(ActionName.USER_SAVE));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    @SuppressWarnings("rawtypes")
	@Override
    protected void onBind() {
        // Display sub panels
        getDisplay().setContent(listing.getDisplay(), emptyDetail.getDisplay());

        addEventHandler(GlobalEvent.LOAD_NEW_USER.getType(),
                new ActionEventHandler<EmptyAction>() {
                    @Override
                    public void execute(final EmptyAction pResult) {
                        getDisplay().setDetailContent(
                                creationDetail.getDisplay());
                        creationDetail.clear();
                        editionDetail.clear();
                    }
                });

        addEventHandler(GlobalEvent.LOAD_USER.getType(),
                new ActionEventHandler<GetUserResult>() {
                    @Override
                    public void execute(final GetUserResult pResult) {
                        getDisplay().setDetailContent(editionDetail.getDisplay());

                        if (pResult.getUser() != null) {
                            editionDetail.clear();
                            editionDetail.setUser(pResult.getUser());
                        }

                        editionDetail.setAffectation(pResult.getAffectation());

                        if (pResult.getUserListSortedByLogin() != null
                                && pResult.getUserListSortedByName() != null) {
                            listing.clear();
                            listing.setUsersByLogin(pResult.getUserListSortedByLogin());
                            listing.setUsersByName(pResult.getUserListSortedByName());
                        }
                    }
                });
        addEventHandler(GlobalEvent.CREATE_USER.getType(),
                new ActionEventHandler<CreateUserResult>() {
                    @Override
                    public void execute(final CreateUserResult pResult) {
                        getDisplay().setDetailContent(
                                editionDetail.getDisplay());
                        editionDetail.clear();
                        editionDetail.setUser(creationDetail.getUser());
                        creationDetail.clear();
                        listing.clear();
                        listing.setUsersByLogin(pResult.getUserListSortedByLogin());
                        listing.setUsersByName(pResult.getUserListSortedByName());
                    }
                });

        addEventHandler(GlobalEvent.DELETE_USER.getType(),
                new ActionEventHandler<DeleteUserResult>() {
                    @Override
                    public void execute(final DeleteUserResult pResult) {
                        getDisplay().setDetailContent(emptyDetail.getDisplay());
                        creationDetail.clear();
                        editionDetail.clear();
                        listing.clear();
                        listing.setUsersByLogin(pResult.getUserListSortedByLogin());
                        listing.setUsersByName(pResult.getUserListSortedByName());
                    }
                });

        // Bind sub panels
        listing.bind();
        creationDetail.bind();
        editionDetail.bind();
        emptyDetail.bind();
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onUnbind()
     */
    @Override
    protected void onUnbind() {
        // Nothing to do
    }
}