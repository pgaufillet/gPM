/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.dictionary.listing;

/**
 * Display interface for the EnvironmentListingView.
 * 
 * @author nveillet
 */
public interface EnvironmentListingDisplay extends
        AbstractDictionaryListingDisplay {

    /**
     * Set the environment access
     * 
     * @param pIsPublic
     *            is public access
     */
    public void setAccess(boolean pIsPublic);

    /**
     * Set the environment name
     * 
     * @param pName
     *            the name
     */
    public void setName(String pName);

}
