/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup;

import org.topcased.gpm.ui.component.client.popup.GpmPopupPanel;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * View for popup.
 * 
 * @author tpanuel
 */
public abstract class PopupView extends GpmPopupPanel implements PopupDisplay {
    private HandlerRegistration closeRegistration;

    /**
     * Create a popup view.
     */
    public PopupView() {
        super(true);
    }

    /**
     * Create a popup view.
     * 
     * @param pCanClose
     *            Can the popup be closed.
     */
    public PopupView(boolean pCanClose) {
        super(pCanClose);
    }

    /**
     * Create a popup view.
     * 
     * @param pPopupTitle
     *            The popup title.
     */
    public PopupView(final String pPopupTitle) {
        super(true);
        setHeaderText(pPopupTitle);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.PopupDisplay#setHeaderText(java.lang.String)
     */
    @Override
    public void setHeaderText(final String pPopupTitle) {
        setHeader(new HTML(pPopupTitle));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.PopupDisplay#addCloseButtonHandler(com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void addCloseButtonHandler(final ClickHandler pHandler) {
        if (closeRegistration != null) {
            closeRegistration.removeHandler();
        }
        if (getCloseButton() != null) {
            closeRegistration = getCloseButton().addClickHandler(pHandler);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.widget.WidgetDisplay#asWidget()
     */
    @Override
    public Widget asWidget() {
        return this;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Display#startProcessing()
     */
    @Override
    public void startProcessing() {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Display#stopProcessing()
     */
    @Override
    public void stopProcessing() {
        // Nothing to do
    }
}