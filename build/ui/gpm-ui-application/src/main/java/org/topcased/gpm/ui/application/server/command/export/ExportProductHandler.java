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

import java.util.List;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.business.util.ExportFormat;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.export.ExportFileResult;
import org.topcased.gpm.ui.application.shared.command.export.ExportProductAction;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * ExportProductHandler
 * 
 * @author nveillet
 */
public class ExportProductHandler extends
        AbstractCommandActionHandler<ExportProductAction, ExportFileResult> {

    /**
     * Create the ExportProductHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public ExportProductHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public ExportFileResult execute(ExportProductAction pAction,
            ExecutionContext pContext) throws ActionException {

        List<String> lProductIds = pAction.getProductIds();

        UiSession lSession = null;
        if (lProductIds.size() == 1) {
            lSession = getDefaultSessionByProductId(lProductIds.get(0));
        }

        if (lSession == null) {
            lSession = getDefaultSession();
        }

        return new ExportFileResult(
                getFacadeLocator().getExportImportFacade().exportProducts(
                        lSession, lProductIds), ExportFormat.XML);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<ExportProductAction> getActionType() {
        return ExportProductAction.class;
    }

}
