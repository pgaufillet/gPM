/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.popup.product;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.validation.ImportProductsViewValidator;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.product.ImportProductsDisplay;
import org.topcased.gpm.ui.application.client.util.CollectionUtil;
import org.topcased.gpm.ui.application.shared.command.product.ImportProductAction;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * A command to launch the import of products.
 * 
 * @author tpanuel
 */
public class LaunchProductsImportCommand extends AbstractCommand implements
        ClickHandler {
    /**
     * Create a LaunchProductsImportCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public LaunchProductsImportCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(final ClickEvent pEvent) {
        final ImportProductsDisplay lImportDisplay =
                getPopupManager().getImportProductsPresenter().getDisplay();

        uploadFileAndFireEvent(
                GlobalEvent.LAUNCH_IMPORT.getType(),
                new ImportProductAction(lImportDisplay.getFileName()),
                ImportProductsViewValidator.getInstance(),
                CollectionUtil.singleton(lImportDisplay.getUploadFileRegister()));
    }
}