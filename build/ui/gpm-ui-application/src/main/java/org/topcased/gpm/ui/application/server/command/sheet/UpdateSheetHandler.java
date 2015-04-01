/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.sheet;

import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetEditionAction;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetResult;
import org.topcased.gpm.ui.application.shared.command.sheet.UpdateSheetAction;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.link.LinkFacade;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * UpdateSheetHandler
 * 
 * @author nveillet
 */
public class UpdateSheetHandler extends
        AbstractCommandActionHandler<UpdateSheetAction, GetSheetResult> {
    /**
     * Create the UpdateSheetHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public UpdateSheetHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetSheetResult execute(UpdateSheetAction pAction,
            ExecutionContext pContext) throws ActionException {
        final UiSession lSession = getSession(pAction.getProductName());

        // Add files content for the sheet
        addFileContent(lSession.getParent(), pAction.getFields());
        // Update sheet
        getFacadeLocator().getSheetFacade().updateSheet(lSession,
                pAction.getSheetId(), pAction.getVersion(), pAction.getFields());

        // Update links
        LinkFacade lLinkFacade = getFacadeLocator().getLinkFacade();
        for (Entry<String, List<UiField>> lLink : pAction.getLinks().entrySet()) {
            final List<UiField> lLinkFields = lLink.getValue();

            if (lLinkFields != null && !lLinkFields.isEmpty()) {
                // Add files content for the link
                addFileContent(lSession.getParent(), lLinkFields);
                lLinkFacade.updateLink(lSession, lLink.getKey(), lLinkFields);
            }
        }

        // return sheet in edition mode
        return pContext.execute(new GetSheetEditionAction(
                pAction.getProductName(), pAction.getSheetId()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<UpdateSheetAction> getActionType() {
        return UpdateSheetAction.class;
    }
}