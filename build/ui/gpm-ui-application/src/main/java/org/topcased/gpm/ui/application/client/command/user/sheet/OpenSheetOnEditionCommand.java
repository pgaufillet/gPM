/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.user.sheet;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.MESSAGES;
import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.validation.IsSheetOpenValidator;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetEditionAction;
import org.topcased.gpm.ui.component.client.button.GpmImageButton;
import org.topcased.gpm.ui.component.client.util.GpmBasicActionHandler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to open a sheet on edition.
 * 
 * @author tpanuel
 */
public class OpenSheetOnEditionCommand extends AbstractCommand implements
        Command, GpmBasicActionHandler<String>, ClickHandler {
    private boolean forceRefresh;

    /**
     * Create an OpenSheetOnEditionCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public OpenSheetOnEditionCommand(final EventBus pEventBus) {
        super(pEventBus);
        forceRefresh = false;
    }

    /**
     * Force sheet refreshing.
     */
    public void forceRefresh() {
        forceRefresh = true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.command.filter.AbstractTreeFilterCommand#executeAction(java.lang.String)
     */
    @Override
    public void execute(final String pParam) {
        if (forceRefresh) {
            // Sheet is already opened: if sheet was not modified, do not ask confirmation
            if (isCurrentSheetModified()) {
                fireEvent(GlobalEvent.LOAD_SHEET.getType(),
                        new GetSheetEditionAction(
                                getCurrentProductWorkspaceName(), pParam));
            }
            else {
                fireEvent(GlobalEvent.LOAD_SHEET.getType(),
                        new GetSheetEditionAction(
                                getCurrentProductWorkspaceName(), pParam),
                        MESSAGES.confirmationRefreshCurrentEditedSheet());

            }
        }
        else {
            fireEvent(GlobalEvent.LOAD_SHEET.getType(),
                    new GetSheetEditionAction(getCurrentProductWorkspaceName(),
                            pParam), IsSheetOpenValidator.getInstance());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        execute(getCurrentSheetId());
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(final ClickEvent pEvent) {
        execute(((GpmImageButton) pEvent.getSource()).getId());
    }
}