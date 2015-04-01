/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.message;

import org.topcased.gpm.ui.application.client.popup.PopupDisplay;

/**
 * Display interface for the UploadMessageView.
 * 
 * @author tpanuel
 */
public interface UploadMessageDisplay extends PopupDisplay {
    /**
     * Set the file name.
     * 
     * @param pFileName
     *            The file name.
     */
    public void setFileName(String pFileName);
}