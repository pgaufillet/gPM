/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.util;

import java.io.Serializable;

/**
 * DisplayHintType
 * 
 * @author nveillet
 * @author phtsaan
 */
public enum StringDisplayHintType implements Serializable {

    /** Text in one line (default) */
    SINGLE_LINE,

    /** Text on several lines */
    MULTI_LINE,

    /** Text on several lines with editor */
    RICH_TEXT,

    /** External Url */
    URL,

    /** Internal Url: to a sheet */
    INTERNAL_URL,

    /** Web page frame */
    EXTERNAL_WEB_CONTENT,

    /** Grid */
    GRID,

    /** Choice with dynamic possible values */
    CHOICE_STRING,

    /** type for APPLET */
    APPLET;
}
