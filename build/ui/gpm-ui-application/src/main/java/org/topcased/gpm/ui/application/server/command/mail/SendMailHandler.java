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

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.business.util.ExportFormat;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.mail.SendMailAction;
import org.topcased.gpm.ui.application.shared.command.mail.SendMailResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.exportimport.ExportImportFacade;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * SendMailHandler
 * 
 * @author nveillet
 */
public class SendMailHandler extends
        AbstractCommandActionHandler<SendMailAction, SendMailResult> {

    /**
     * Create the SendMailHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public SendMailHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public SendMailResult execute(SendMailAction pAction,
            ExecutionContext pContext) throws ActionException {

        UiSession lSession = getSession(pAction.getProductName());

        // Get locale user
        Locale lUserLocale =
                new Locale(getFacadeLocator().getI18nFacade().getUserLanguage(
                        lSession.getParent()));

        // Create mail attached file
        ExportImportFacade lExportFacade =
                getFacadeLocator().getExportImportFacade();

        String lExportId =
                lExportFacade.exportSheets(lSession, pAction.getSheetIds(),
                        ExportFormat.PDF, pAction.getReportModelName(), null,
                        lUserLocale);

        ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();
        lExportFacade.getExportedData(lSession, lExportId, lOutputStream);

        // Send mail
        getFacadeLocator().getMailFacade().sendMail(lSession,
                pAction.getDestinationUsers(), pAction.getSubject(),
                pAction.getBody(), lOutputStream.toByteArray());

        return new SendMailResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<SendMailAction> getActionType() {
        return SendMailAction.class;
    }

}
