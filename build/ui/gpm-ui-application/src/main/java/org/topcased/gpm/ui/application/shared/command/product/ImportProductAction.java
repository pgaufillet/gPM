/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.product;

import net.customware.gwt.dispatch.shared.Action;

/**
 * ImportProductAction
 * 
 * @author tpanuel
 */
public class ImportProductAction implements Action<ImportProductResult> {
    private static final long serialVersionUID = -4742596025453009633L;

    private String fileName;

    /**
     * Create action.
     */
    public ImportProductAction() {
    }

    /**
     * Create action with values.
     * 
     * @param pFileName
     *            The name of the file to import.
     */
    public ImportProductAction(final String pFileName) {
        setFileName(pFileName);
    }

    /**
     * Get the file name.
     * 
     * @return The file name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Set the file name.
     * 
     * @param pFileName
     *            The file name.
     */
    public void setFileName(final String pFileName) {
        fileName = pFileName;
    }
}