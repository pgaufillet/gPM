/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.exportimport;

import java.io.OutputStream;
import java.util.List;

import org.topcased.gpm.business.exportation.ExportProperties;
import org.topcased.gpm.business.exportation.ExportProperties.ExportFlag;
import org.topcased.gpm.business.exportation.service.ExportService;
import org.topcased.gpm.ui.facade.server.FacadeCommand;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;

/**
 * ExportProductsFacadeCommand
 * 
 * @author nveillet
 */
public class ExportProductsFacadeCommand implements FacadeCommand {

    private ExportService exportService;

    private List<String> productsIds;

    private UiSession session;

    /**
     * Constructor
     * 
     * @param pExportService
     *            {@link ExportService}
     * @param pSession
     *            the session
     * @param pProductsIds
     *            the product identifiers to export
     */
    public ExportProductsFacadeCommand(ExportService pExportService,
            UiSession pSession, List<String> pProductsIds) {
        exportService = pExportService;
        session = pSession;
        productsIds = pProductsIds;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.server.FacadeCommand#execute(java.io.OutputStream)
     */
    @Override
    public void execute(OutputStream pOutputStream) {
        ExportProperties lProperties = new ExportProperties();

        lProperties.setAllFlags(ExportFlag.NO);
        lProperties.setProductsFlag(ExportFlag.LIMITED);

        lProperties.setLimitedElementsId(productsIds.toArray(new String[0]));

        exportService.exportData(session.getRoleToken(), pOutputStream,
                lProperties);
    }
}
