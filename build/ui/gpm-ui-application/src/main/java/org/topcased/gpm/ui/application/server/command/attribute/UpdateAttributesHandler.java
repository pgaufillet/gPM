/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.attribute;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.attribute.UpdateAttributesAction;
import org.topcased.gpm.ui.application.shared.command.attribute.UpdateAttributesResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * UpdateAttributesHandler
 * 
 * @author nveillet
 */
public class UpdateAttributesHandler
        extends
        AbstractCommandActionHandler<UpdateAttributesAction, UpdateAttributesResult> {

    /**
     * Create the UpdateAttributesHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public UpdateAttributesHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public UpdateAttributesResult execute(UpdateAttributesAction pAction,
            ExecutionContext pContext) throws ActionException {

        UiSession lSession = null;

        switch (pAction.getAttributeContainer()) {
            case PRODUCT:
            default:
                lSession = getDefaultSessionByProductId(pAction.getElementId());
                break;
        }

        // Update attributes
        getFacadeLocator().getAttributeFacade().setAttributes(lSession,
                pAction.getElementId(), pAction.getAttributes());

        return new UpdateAttributesResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<UpdateAttributesAction> getActionType() {
        return UpdateAttributesAction.class;
    }
}
