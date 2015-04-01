/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.message;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;

import com.google.inject.Inject;

/**
 * The presenter for the UploadMessageView.
 * 
 * @author tpanuel
 */
public class UploadMessagePresenter extends
        PopupPresenter<UploadMessageDisplay> {
    /**
     * Create a presenter for the UploadMessageView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     */
    @Inject
    public UploadMessagePresenter(final UploadMessageDisplay pDisplay,
            final EventBus pEventBus) {
        super(pDisplay, pEventBus);
    }

    /**
     * Display an upload.
     * 
     * @param pFileName
     *            The file name.
     */
    public void displayUpload(final String pFileName) {
        getDisplay().setFileName(pFileName);
        bind();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.PopupPresenter#getClosePopupEvent()
     */
    @Override
    protected GlobalEvent<ClosePopupAction> getClosePopupEvent() {
        return null;
    }
}