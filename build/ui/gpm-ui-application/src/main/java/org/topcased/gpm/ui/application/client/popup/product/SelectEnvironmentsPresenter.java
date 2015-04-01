/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.product;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.command.popup.product.SelectEnvironmentsCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;
import org.topcased.gpm.ui.application.shared.command.product.SelectEnvironmentsResult;

import com.google.inject.Inject;

/**
 * The presenter for the SelectEnvironmentsView.
 * 
 * @author nveillet
 */
public class SelectEnvironmentsPresenter extends
        PopupPresenter<SelectEnvironmentsDisplay> {

    private final SelectEnvironmentsCommand selectEnvironments;

    private String productTypeName;

    /**
     * Create a presenter for the SelectEnvironmentsView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pSelectEnvironments
     *            The select environments command.
     */
    @Inject
    public SelectEnvironmentsPresenter(
            final SelectEnvironmentsDisplay pDisplay, final EventBus pEventBus,
            final SelectEnvironmentsCommand pSelectEnvironments) {
        super(pDisplay, pEventBus);
        productTypeName = null;
        selectEnvironments = pSelectEnvironments;
    }

    /**
     * Initialize the popup form.
     * 
     * @param pResult
     *            The result.
     */
    public void init(final SelectEnvironmentsResult pResult) {
        getDisplay().clear();
        // Set the product type name
        productTypeName = pResult.getProductTypeName();
        // Set available environments
        getDisplay().setEnvironments(pResult.getEnvironmentNames());
        // Set the select button handler
        getDisplay().setSelectButtonHandler(selectEnvironments);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.PopupPresenter#getClosePopupEvent()
     */
    @Override
    protected GlobalEvent<ClosePopupAction> getClosePopupEvent() {
        return GlobalEvent.CLOSE_SELECT_ENV_POPUP;
    }

    /**
     * Get the product type name to create
     * 
     * @return the product type name
     */
    public String getProductTypeName() {
        return productTypeName;
    }
}
