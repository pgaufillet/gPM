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

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.FieldsContainerType;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.link.ExecuteLinkDeletionFilterAction;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to delete links on a sheet.
 * 
 * @author tpanuel
 */
public class DeleteSheetLinkCommand extends AbstractCommand implements Command {
    private String linkType;

    /**
     * Create an DeleteSheetLinkCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public DeleteSheetLinkCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * Get the link type.
     * 
     * @return The link type.
     */
    public String getLinkType() {
        return linkType;
    }

    /**
     * Set the link type.
     * 
     * @param pLinkType
     *            The link type.
     */
    public void setLinkType(final String pLinkType) {
        linkType = pLinkType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        // Set link type to  filter popup
        getPopupManager().getFilterPopupPresenter().setTypeName(linkType);

        // If sheet was not modified, do not ask confirmation
        if (isCurrentSheetModified()) {
            fireEvent(GlobalEvent.UNLINK_SHEET.getType(),
                    new ExecuteLinkDeletionFilterAction(
                            getCurrentProductWorkspaceName(),
                            FieldsContainerType.SHEET, getCurrentSheetId(),
                            linkType));
        }
        else {
            fireEvent(GlobalEvent.UNLINK_SHEET.getType(),
                    new ExecuteLinkDeletionFilterAction(
                            getCurrentProductWorkspaceName(),
                            FieldsContainerType.SHEET, getCurrentSheetId(),
                            linkType),
                    Ui18n.MESSAGES.confirmationCreateOrRemoveLink());
        }
    }
}