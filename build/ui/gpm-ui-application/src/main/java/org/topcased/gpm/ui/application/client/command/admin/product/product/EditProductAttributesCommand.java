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

import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.attribute.GetAttributesAction;
import org.topcased.gpm.ui.facade.shared.attribute.UiAttributeContainer;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to edit product attributes.
 * 
 * @author tpanuel
 */
public class EditProductAttributesCommand extends AbstractCommand implements
        Command {
    /**
     * Create an EditProductAttributesCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public EditProductAttributesCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        fireEvent(GlobalEvent.LOAD_ATTRIBUTES.getType(),
                new GetAttributesAction(UiAttributeContainer.PRODUCT,
                        getCurrentProductId(), DisplayMode.EDITION));
    }
}