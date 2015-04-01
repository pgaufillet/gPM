/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.admin.product.product;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.FieldsContainerType;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.link.ExecuteLinkDeletionFilterAction;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to delete links on a product.
 * 
 * @author tpanuel
 */
public class DeleteProductLinkCommand extends AbstractCommand implements
        Command {
    private String linkType;

    /**
     * Create an DeleteProductLinkCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public DeleteProductLinkCommand(final EventBus pEventBus) {
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

        // If product was not modified, do not ask confirmation
        if (isCurrentProductModified()) {
            fireEvent(GlobalEvent.UNLINK_PRODUCT.getType(),
                    new ExecuteLinkDeletionFilterAction(null,
                            FieldsContainerType.PRODUCT, getCurrentProductId(),
                            linkType));
        }
        else {
            fireEvent(GlobalEvent.UNLINK_PRODUCT.getType(),
                    new ExecuteLinkDeletionFilterAction(null,
                            FieldsContainerType.PRODUCT, getCurrentProductId(),
                            linkType),
                    Ui18n.MESSAGES.confirmationCreateOrRemoveProductLink());
        }
    }
}