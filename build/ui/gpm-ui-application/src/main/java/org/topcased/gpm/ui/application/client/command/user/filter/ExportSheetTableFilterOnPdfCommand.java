/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.user.filter;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.ExportFormat;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.export.ExportFilterResultAction;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to export a filter on PDF format.
 * 
 * @author tpanuel
 */
public class ExportSheetTableFilterOnPdfCommand extends AbstractCommand
        implements Command {
    /**
     * Create an ExportFilterOnPdfCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public ExportSheetTableFilterOnPdfCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        fireEvent(GlobalEvent.EXPORT_FILTER.getType(),
                new ExportFilterResultAction(getCurrentProductWorkspaceName(),
                        getCurrentSheetTableFilterId(), ExportFormat.PDF, null));
    }
}