/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.dictionary.detail;

import org.topcased.gpm.ui.component.client.container.field.GpmListShifterSelector;

/**
 * Display interface for the EnvironmentDetailView.
 * 
 * @author nveillet
 */
public interface EnvironmentDetailDisplay extends
        AbstractDictionaryDetailDisplay {

    /**
     * Get the category values field
     * 
     * @return the category values field
     */
    public GpmListShifterSelector<String> getCategoryValues();

}