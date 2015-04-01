/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.export;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.InvalidValueException;
import org.topcased.gpm.business.util.ExportFormat;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.export.AbstractExportResult;
import org.topcased.gpm.ui.application.shared.command.export.ExportFileResult;
import org.topcased.gpm.ui.application.shared.command.export.ExportSheetAction;
import org.topcased.gpm.ui.application.shared.command.export.SelectFieldNamesResult;
import org.topcased.gpm.ui.application.shared.command.export.SelectReportModelResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.exportimport.ExportImportFacade;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * ExportSheetHandler
 * 
 * @author nveillet
 */
public class ExportSheetHandler extends
        AbstractCommandActionHandler<ExportSheetAction, AbstractExportResult> {

    /**
     * Create the ExportSheetHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public ExportSheetHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public AbstractExportResult execute(ExportSheetAction pAction,
            ExecutionContext pContext) throws ActionException {
        ExportImportFacade lExportImportFacade =
                getFacadeLocator().getExportImportFacade();

        // checks if the maximum export able sheet number is not exceed 
        checkMaxExportableSheets(pAction.getSheetIds(), lExportImportFacade);

        UiSession lSession = getSession(pAction.getProductName());

        String lReportModelName = pAction.getReportModelName();
        ExportFormat lExportFormat = pAction.getExportFormat();

        if (lReportModelName == null
                && (ExportFormat.PDF.equals(lExportFormat) || ExportFormat.EXCEL.equals(lExportFormat))) {
            // Get Available report model name
            List<Translation> lAvailableReportModels =
                    lExportImportFacade.getAvailableReportModels(lSession,
                            pAction.getSheetIds(), lExportFormat);

            if (lAvailableReportModels.size() > 1) {
                return new SelectReportModelResult(lAvailableReportModels,
                        lExportFormat);
            }
            else if (lAvailableReportModels.size() == 1) {
                lReportModelName = lAvailableReportModels.get(0).getValue();
            }
        }

        if (lReportModelName == null
                && pAction.getFieldNames() == null
                && (ExportFormat.EXCEL.equals(lExportFormat) || ExportFormat.XML.equals(lExportFormat))) {

            // avoid duplicated entries by using a Set
            Set<String> lTypeIds =
                    new HashSet<String>(
                            getFacadeLocator().getSheetFacade().getSheetTypeIdBySheetIds(
                                    pAction.getSheetIds()));

            if (lTypeIds.size() == 1) {
                // return exportable fields
                return new SelectFieldNamesResult(
                        lExportImportFacade.getExportableFields(lSession,
                                pAction.getSheetIds()), lExportFormat);
            }
        }

        return new ExportFileResult(lExportImportFacade.exportSheets(lSession,
                pAction.getSheetIds(), lExportFormat, lReportModelName,
                pAction.getFieldNames(), new Locale(pAction.getLocale())),
                lExportFormat);
    }

    /**
     * Check the Maximum number of sheets allowed to be exported
     * 
     * @param pSheetIds
     *            List of Sheets ids
     * @param pExportImportFacade
     *            the Export facade use to get the parameter
     *@throws AuthorizationException
     *             avoid the user to export more than allowed number of sheets
     */
    private void checkMaxExportableSheets(List<String> pSheetIds,
            ExportImportFacade pExportImportFacade) {
        int lMaxExportable = pExportImportFacade.getMaxExportableSheets();
        if ((null != pSheetIds) && (pSheetIds.size() > lMaxExportable)) {
            throw new InvalidValueException("Too many sheets selected (max "
                    + lMaxExportable + ").", 0);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<ExportSheetAction> getActionType() {
        return ExportSheetAction.class;
    }
}
