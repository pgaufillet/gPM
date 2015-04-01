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
 * View for a set of dictionary properties displayed on the dictionary
 * management space.
 * 
 * @author nveillet
 */
public class DictionaryListingView extends AbstractDictionaryListingView
        implements DictionaryListingDisplay {

    /**
     * Create a detail view for users.
     */
    public DictionaryListingView() {
        super();
        setTitle(CONSTANTS.dictionary());
    }
}