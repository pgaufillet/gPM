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

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

/**
 * View for a set of environment properties displayed on the environment
 * management space.
 * 
 * @author nveillet
 */
public class EnvironmentListingView extends AbstractDictionaryListingView
        implements EnvironmentListingDisplay {

    /**
     * Create a detail view for users.
     */
    public EnvironmentListingView() {
        super();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.dictionary.listing.EnvironmentListingDisplay#setAccess(boolean)
     */
    @Override
    public void setAccess(boolean pIsPublic) {
        String lAccess;
        if (pIsPublic) {
            lAccess = CONSTANTS.adminDictionaryEnvironmentPublic();
        }
        else {
            lAccess = CONSTANTS.adminDictionaryEnvironmentPrivate();
        }

        setTitle(getTitle() + " (" + lAccess + ")");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.dictionary.listing.EnvironmentListingDisplay#setName(java.lang.String)
     */
    @Override
    public void setName(String pName) {
        setTitle(CONSTANTS.environment() + ": " + pName);
    }
}