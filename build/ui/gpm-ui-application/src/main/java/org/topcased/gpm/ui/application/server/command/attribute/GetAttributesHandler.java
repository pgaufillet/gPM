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

import java.util.List;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.attribute.GetAttributesAction;
import org.topcased.gpm.ui.application.shared.command.attribute.GetAttributesEditionResult;
import org.topcased.gpm.ui.application.shared.command.attribute.GetAttributesResult;
import org.topcased.gpm.ui.application.shared.command.attribute.GetAttributesVisualizationResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.attribute.UiAttribute;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * GetAttributesHandler
 * 
 * @author nveillet
 */
public class GetAttributesHandler extends
        AbstractCommandActionHandler<GetAttributesAction, GetAttributesResult> {

    /**
     * Create the GetAttributesHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public GetAttributesHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetAttributesResult execute(GetAttributesAction pAction,
            ExecutionContext pContext) throws ActionException {

        String lElementId = pAction.getElementId();
        UiSession lSession = null;

        switch (pAction.getAttributeContainer()) {
            case PRODUCT:
            default:
                lSession = getDefaultSessionByProductId(lElementId);
                break;
        }

        // Get attributes
        List<UiAttribute> lAttributes =
                getFacadeLocator().getAttributeFacade().getAttributes(lSession,
                        lElementId);

        GetAttributesResult lResult;

        if (DisplayMode.EDITION.equals(pAction.getDisplayMode())) {
            lResult =
                    new GetAttributesEditionResult(pAction.getProductName(),
                            pAction.getAttributeContainer(), lElementId,
                            lAttributes);
        }
        else {
            lResult =
                    new GetAttributesVisualizationResult(
                            pAction.getProductName(),
                            pAction.getAttributeContainer(), lElementId,
                            lAttributes);
        }
        return lResult;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<GetAttributesAction> getActionType() {
        return GetAttributesAction.class;
    }
}
