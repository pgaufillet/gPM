/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.command.popup.user.UpdateAffectationCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;
import org.topcased.gpm.ui.application.shared.command.user.GetAffectationResult;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUserAffectation;

import com.google.inject.Inject;

/**
 * The presenter for the UserAffectationView.
 * 
 * @author nveillet
 */
public class UserAffectationPresenter extends
        PopupPresenter<UserAffectationDisplay> {

    private final UpdateAffectationCommand updateAffectation;

    /**
     * Create a presenter for the UserAffectationView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pUpdateAffectation
     *            The update affectation command.
     */
    @Inject
    public UserAffectationPresenter(UserAffectationDisplay pDisplay,
            EventBus pEventBus, UpdateAffectationCommand pUpdateAffectation) {
        super(pDisplay, pEventBus);
        updateAffectation = pUpdateAffectation;
    }

    /**
     * Get the user affectation
     * 
     * @return the affectation
     */
    public UiUserAffectation getAffectation() {
        return new UiUserAffectation(getDisplay().getProcessAffectations(),
                getDisplay().getProductAffectations());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.PopupPresenter#getClosePopupEvent()
     */
    @Override
    protected GlobalEvent<ClosePopupAction> getClosePopupEvent() {
        return GlobalEvent.CLOSE_USER_AFFECTATION_POPUP;
    }

    /**
     * Initialize the popup form.
     * 
     * @param pResult
     *            The result.
     */
    public void init(final GetAffectationResult pResult) {
        String[] lDisabledRoleNames = pResult.getAffectation().getDisabledRoleNames();
        
        getDisplay().initAffectation(pResult.getProducts(),
                pResult.getAffectation().getRoleTranslations(), lDisabledRoleNames);

        getDisplay().setProcessAffectation(
                pResult.getAffectation().getProcessAffectations(), lDisabledRoleNames);

        for (Entry<String, List<String>> lEntryAffectation :
            pResult.getAffectation().getProductAffectations().entrySet()) {
            getDisplay().setProductAffectation(lEntryAffectation.getKey(),
                    lEntryAffectation.getValue(), lDisabledRoleNames);
        }

        getDisplay().refreshUnusedProducts();

        // Set the save button handler
        getDisplay().setSaveButtonHandler(updateAffectation);
    }
}
