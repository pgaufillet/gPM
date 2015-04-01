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

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.export.ExportFileResult;
import org.topcased.gpm.ui.application.shared.command.export.ExportFilterResultAction;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * ExportFilterResultHandler
 * 
 * @author nveillet
 */
public class ExportFilterResultHandler
        extends
        AbstractCommandActionHandler<ExportFilterResultAction, ExportFileResult> {

    /**
     * Create the ExportFilterResultHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public ExportFilterResultHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public ExportFileResult execute(ExportFilterResultAction pAction,
            ExecutionContext pContext) throws ActionException {

        String lExportFileId =
                getFacadeLocator().getExportImportFacade().exportFilterResult(
                        getSession(pAction.getProductName()),
                        pAction.getFilterId(), pAction.getExportFormat(),
                        pAction.getAdditionalParameters());

        return new ExportFileResult(lExportFileId, pAction.getExportFormat());
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<ExportFilterResultAction> getActionType() {
        return ExportFilterResultAction.class;
    }

}
