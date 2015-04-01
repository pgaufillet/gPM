/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.product;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.product.ImportProductAction;
import org.topcased.gpm.ui.application.shared.command.product.ImportProductResult;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * ImportProductHandler
 * 
 * @author tpanuel
 */
public class ImportProductHandler extends
        AbstractCommandActionHandler<ImportProductAction, ImportProductResult> {
    /**
     * Create the ImportProductHandler.
     * 
     * @param pHttpSession
     *            The http session.
     */
    @Inject
    public ImportProductHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public ImportProductResult execute(final ImportProductAction pAction,
            final ExecutionContext pContext) throws ActionException {
        getFacadeLocator().getProductFacade().importProducts(
                getDefaultSession(),
                getUserSession().getTemporaryUploadedFile(pAction.getFileName()));

        return new ImportProductResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<ImportProductAction> getActionType() {
        return ImportProductAction.class;
    }
}