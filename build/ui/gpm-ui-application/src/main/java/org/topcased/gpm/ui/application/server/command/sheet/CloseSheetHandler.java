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

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.CloseContainerResult;
import org.topcased.gpm.ui.application.shared.command.sheet.CloseSheetAction;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.sheet.SheetFacade;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * CloseSheetHandler
 * 
 * @author nveillet
 */
public class CloseSheetHandler extends
        AbstractCommandActionHandler<CloseSheetAction, CloseContainerResult> {

    /**
     * Create the CloseSheetHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public CloseSheetHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public CloseContainerResult execute(CloseSheetAction pAction,
            ExecutionContext pContext) throws ActionException {

        SheetFacade lSheetFacade = getFacadeLocator().getSheetFacade();
        UiSession lSession = getSession(pAction.getProductName());

        // remove sheet to the facade sheet cache
        lSheetFacade.clearCache(lSession, pAction.getSheetId());

        // Unlock
        lSheetFacade.unLockSheet(lSession, pAction.getSheetId());

        return new CloseContainerResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<CloseSheetAction> getActionType() {
        return CloseSheetAction.class;
    }

}
