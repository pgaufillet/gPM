/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws.v2.extensions;

import java.io.Serializable;

/**
 * GuiContext
 * 
 * @author nveillet
 */
public enum GuiContext implements Serializable {

    /** Action always possible */
    ALWAYS,

    /** Action only on sheet creation mode */
    CTX_CREATE_SHEET,

    /** Action only on sheet visualization mode */
    CTX_VIEW_SHEET,

    /** Action only on sheet edition mode */
    CTX_EDIT_SHEET,

    /** Action only on sheet filter mode */
    SHEET_LIST;
}
