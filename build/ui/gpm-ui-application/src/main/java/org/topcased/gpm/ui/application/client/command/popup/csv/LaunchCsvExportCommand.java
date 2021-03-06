/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.popup.csv;

import java.util.HashMap;
import java.util.Map;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.ExportFormat;
import org.topcased.gpm.business.util.ExportParameter;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.csv.CsvOptionSelectionDisplay;
import org.topcased.gpm.ui.application.shared.command.export.ExportFilterResultAction;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * A command to export a filter on CSV format.
 * 
 * @author tpanuel
 */
public class LaunchCsvExportCommand extends AbstractCommand implements
        ClickHandler {
    /**
     * Create an ExportFilterOnCsvCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public LaunchCsvExportCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(final ClickEvent pEvent) {
        final CsvOptionSelectionDisplay lCsvDisplay =
                getPopupManager().getCsvOptionSelectionPresenter().getDisplay();
        final Map<ExportParameter, String> lAdditionalParameters =
                new HashMap<ExportParameter, String>();

        lAdditionalParameters.put(ExportParameter.CSV_ESCAPE_CHARACTER,
                lCsvDisplay.getEscapeCharacter());
        lAdditionalParameters.put(ExportParameter.CSV_QUOTE_CHARACTER,
                lCsvDisplay.getQuoteCharacter());
        lAdditionalParameters.put(ExportParameter.CSV_SEPARATOR_CHARACTER,
                lCsvDisplay.getSeparatorCharacter());

        fireEvent(GlobalEvent.EXPORT_FILTER.getType(),
                new ExportFilterResultAction(getCurrentProductWorkspaceName(),
                        getCurrentSheetTableFilterId(), ExportFormat.CSV,
                        lAdditionalParameters),
                GlobalEvent.CLOSE_CSV_POPUP.getType(), new ClosePopupAction());
    }
}