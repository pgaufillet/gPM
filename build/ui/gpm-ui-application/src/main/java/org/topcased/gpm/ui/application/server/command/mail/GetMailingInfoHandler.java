/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.mail;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.business.util.ExportFormat;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.mail.GetMailingInfoAction;
import org.topcased.gpm.ui.application.shared.command.mail.GetMailingInfoResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * GetMailingInfoHandler
 * 
 * @author nveillet
 */
public class GetMailingInfoHandler
        extends
        AbstractCommandActionHandler<GetMailingInfoAction, GetMailingInfoResult> {

    /**
     * Create the GetMailingInfoHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public GetMailingInfoHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetMailingInfoResult execute(GetMailingInfoAction pAction,
            ExecutionContext pContext) throws ActionException {

        UiSession lSession = getSession(pAction.getProductName());

        // Get users addresses
        List<String> lUserMails =
                getFacadeLocator().getUserFacade().getUserMailAddresses(
                        lSession);

        // Get report model names
        List<Translation> lReportModels =
                getFacadeLocator().getExportImportFacade().getAvailableReportModels(
                        lSession, pAction.getSheetIds(), ExportFormat.PDF);

        // Get sheet references
        List<String> lSheetReferences = new ArrayList<String>();
        for (String lSheetId : pAction.getSheetIds()) {
            lSheetReferences.add(getFacadeLocator().getSheetFacade().getSheet(
                    lSession, lSheetId, DisplayMode.VISUALIZATION).getFunctionalReference());
        }

        return new GetMailingInfoResult(lUserMails, lReportModels,
                lSheetReferences);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<GetMailingInfoAction> getActionType() {
        return GetMailingInfoAction.class;
    }
}
