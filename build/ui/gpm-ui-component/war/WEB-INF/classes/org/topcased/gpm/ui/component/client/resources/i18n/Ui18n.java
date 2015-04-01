/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.resources.i18n;

import java.util.Map;

import com.google.gwt.core.client.GWT;

/**
 * i18n resources for application.
 * <p>
 * Handle static constants and messages (GWT paradigm).
 * </p>
 * <p>
 * Additional translations can be set per type (text or img). The values is
 * initialize at the application initialization.
 * </p>
 * 
 * @author mkargbo
 */
public class Ui18n {
    public static final GpmMessages MESSAGES = GWT.create(GpmMessages.class);

    public static final GpmConstants CONSTANTS = GWT.create(GpmConstants.class);

    /**
     * Default constructor.
     */
    public Ui18n() {
    }

    public void setTextConstant(Map<String, String> pTextConstant) {
    }

    public void setImgConstant(Map<String, String> pImgConstant) {
    }

    /**
     * Get text translation.
     * 
     * @param pKey
     *            Key of the translation to retrieve
     * @return Translated value if found, key otherwise
     */
    public String getConstant(final String pKey) {
        return pKey;
    }

    /**
     * Get image translation.
     * 
     * @param pKey
     *            Key of the translation to retrieve;
     * @return Image file name part if found, key otherwise
     */
    public String getImage(final String pKey) {
        return pKey;

    }
}