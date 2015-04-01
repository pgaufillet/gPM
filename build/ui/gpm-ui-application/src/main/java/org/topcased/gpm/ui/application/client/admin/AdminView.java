/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import org.topcased.gpm.ui.component.client.layout.GpmTabLayoutPanel;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * View for the administration space.
 * 
 * @author tpanuel
 */
public class AdminView extends GpmTabLayoutPanel implements AdminDisplay {
    /**
     * Create an administration space.
     */
    public AdminView() {
        super(false, true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.AdminDisplay#setTabs(com.google.gwt.user.client.ui.Widget,
     *      com.google.gwt.user.client.ui.Widget,
     *      com.google.gwt.user.client.ui.Widget)
     */
    @Override
    public void setTabs(final Widget pProductPanel, final Widget pUserPanel,
            final Widget pDicoPanel) {
        add(pProductPanel, CONSTANTS.product(), null);
        add(pUserPanel, CONSTANTS.user(), null);
        add(pDicoPanel, CONSTANTS.dictionary(), null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.AdminDisplay#addTabChangeHandler(SelectionHandler)
     */
    @Override
    public void addTabChangeHandler(SelectionHandler<Integer> pHandler) {
        addSelectionHandler(pHandler);
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