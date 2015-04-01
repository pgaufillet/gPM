/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.user.detail;

import java.util.Map;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.common.AbstractPresenter;
import org.topcased.gpm.ui.application.client.menu.admin.user.UserDetailCreationMenuBuilder;
import org.topcased.gpm.ui.application.shared.command.authorization.OpenAdminModuleResult;
import org.topcased.gpm.ui.component.client.container.field.GpmChoiceBoxValue;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUser;

import com.google.inject.Inject;

/**
 * The presenter for the UserCreationDetailView.
 * 
 * @author nveillet
 */
public class UserCreationDetailPresenter extends
        AbstractPresenter<UserCreationDetailDisplay> {

    private Map<String, UiAction> actions;

    private final UserDetailCreationMenuBuilder menuBuilder;

    /**
     * Create a presenter for the UserCreationDetailView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pMenuBuilder
     *            The menu builder.
     */
    @Inject
    public UserCreationDetailPresenter(UserCreationDetailDisplay pDisplay,
            EventBus pEventBus, final UserDetailCreationMenuBuilder pMenuBuilder) {
        super(pDisplay, pEventBus);
        menuBuilder = pMenuBuilder;
    }

    /**
     * Clear view
     */
    public void clear() {
        getDisplay().clear();
    }

    /**
     * Get user from view
     * 
     * @return the user
     */
    public UiUser getUser() {
        UiUser lUser = new UiUser();

        lUser.setLogin(getDisplay().getLogin());
        lUser.setName(getDisplay().getName().get());
        lUser.setForename(getDisplay().getFirstName().get());
        lUser.setEmailAdress(getDisplay().getMail().get());
        lUser.setPassWord(getDisplay().getPassword().get());
        lUser.setLanguage(getDisplay().getLanguage().get());

        return lUser;
    }

    /**
     * Initialize the details.
     * 
     * @param pResult
     *            the result
     */
    public void init(OpenAdminModuleResult pResult) {
        actions = pResult.getUserActions();
        getDisplay().getLanguage().setPossibleValues(
                GpmChoiceBoxValue.buildFromStrings(pResult.getAvailableLanguages()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    @Override
    protected void onBind() {
        getDisplay().setToolBar(menuBuilder.buildMenu(actions, null));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onUnbind()
     */
    @Override
    protected void onUnbind() {
        // Do nothing
    }

    /**
     * Validate view
     * 
     * @return the validation message
     */
    public String validate() {
        return getDisplay().validate();
    }
}
