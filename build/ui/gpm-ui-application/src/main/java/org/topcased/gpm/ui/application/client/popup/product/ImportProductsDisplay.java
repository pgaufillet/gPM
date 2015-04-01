/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.product;

import org.topcased.gpm.ui.application.client.popup.PopupDisplay;
import org.topcased.gpm.ui.component.client.container.field.GpmUploadFileRegister;
import org.topcased.gpm.ui.component.client.util.validation.Validator;

import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Display interface for the ImportProductsView.
 * 
 * @author tpanuel
 */
public interface ImportProductsDisplay extends PopupDisplay {
    /**
     * Clear the form.
     */
    public void clear();

    /**
     * Set the import button handler.
     * 
     * @param pHandler
     *            The handler.
     */
    public void setImportButtonHandler(ClickHandler pHandler);

    /**
     * Get the file name.
     * 
     * @return The file name.
     */
    public String getFileName();

    /**
     * Get the upload file register.
     * 
     * @return The upload file register.
     */
    public GpmUploadFileRegister getUploadFileRegister();

    /**
     * Get validator.
     * 
     * @return The validator.
     */
    public Validator getValidator();
}