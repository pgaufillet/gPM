/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.popup.report;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.ExportFormat;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.util.CollectionUtil;
import org.topcased.gpm.ui.application.shared.command.export.ExportSheetAction;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.inject.Inject;

/**
 * A command to launch an XLS export with selected to the sheet displayed on the
 * detail panel.
 * 
 * @author tpanuel
 */
public class ExportSheetFieldsOnXlsCommand extends AbstractCommand implements
        ClickHandler {
    /**
     * Create an ExportSheetFieldsOnXlsCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public ExportSheetFieldsOnXlsCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(final ClickEvent pEvent) {
        fireEvent(
                GlobalEvent.EXPORT_SHEET.getType(),
                new ExportSheetAction(
                        getCurrentProductWorkspaceName(),
                        CollectionUtil.singleton(getCurrentSheetId()),
                        ExportFormat.EXCEL,
                        null,
                        getPopupManager().getSelectExportedFieldsPresenter().getSelectedFieldNames(),
                        LocaleInfo.getCurrentLocale().getLocaleName()),
                GlobalEvent.CLOSE_EXPORTED_FIELDS_SELECTION_POPUP.getType(),
                new ClosePopupAction());
    }
}