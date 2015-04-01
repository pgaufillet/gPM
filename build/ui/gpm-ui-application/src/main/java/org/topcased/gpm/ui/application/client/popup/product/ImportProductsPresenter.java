/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.product;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.command.popup.product.LaunchProductsImportCommand;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;
import org.topcased.gpm.ui.application.shared.command.product.ImportProductResult;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;

import com.google.inject.Inject;

/**
 * The presenter for the ImportProductsView.
 * 
 * @author tpanuel
 */
public class ImportProductsPresenter extends
        PopupPresenter<ImportProductsDisplay> {

    /**
     * Create a presenter for the ImportProductsView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pProductsImport
     *            The import product command.
     */
    @Inject
    public ImportProductsPresenter(final ImportProductsDisplay pDisplay,
            final EventBus pEventBus,
            final LaunchProductsImportCommand pProductsImport) {
        super(pDisplay, pEventBus);
        getDisplay().setImportButtonHandler(pProductsImport);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.PopupPresenter#getClosePopupEvent()
     */
    @Override
    protected GlobalEvent<ClosePopupAction> getClosePopupEvent() {
        return GlobalEvent.CLOSE_IMPORT_PRODUCTS_POPUP;
    }

    /**
     * -{@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.PopupPresenter#onBind()
     */
    @Override
    protected void onBind() {
        super.onBind();
        addEventHandler(GlobalEvent.LAUNCH_IMPORT.getType(),
                new ActionEventHandler<ImportProductResult>() {
                    @Override
                    public void execute(final ImportProductResult pResult) {
                        unbind();
                        Application.INJECTOR.getInfoMessagePresenter().displayMessage(
                                Ui18n.CONSTANTS.productImportMessage());
                    }
                });
    }
}