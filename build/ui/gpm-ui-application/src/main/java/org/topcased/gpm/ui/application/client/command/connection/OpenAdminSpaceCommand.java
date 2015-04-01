/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.connection;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.EmptyAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * Command to open the administration space.
 * 
 * @author tpanuel
 */
public class OpenAdminSpaceCommand extends AbstractCommand implements
        ClickHandler {
    /**
     * Command constructor
     * 
     * @param pEventBus
     *            Event bus
     */
    @Inject
    public OpenAdminSpaceCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void onClick(ClickEvent pEvent) {
        fireEvent(GlobalEvent.OPEN_ADMIN_SPACE.getType(), new EmptyAction());
    }
}