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

import org.topcased.gpm.ui.component.client.container.field.GpmCheckBox;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;

/**
 * Display interface for the EnvironmentCreationDetailView.
 * 
 * @author nveillet
 */
public interface EnvironmentCreationDetailDisplay extends
        AbstractDictionaryDetailDisplay {
    /**
     * Validate view
     * 
     * @return the validation message
     */
    public String validate();

    /**
     * Get the name
     * 
     * @return the name
     */
    public GpmTextBox<String> getName();

    /**
     * Get the public field
     * 
     * @return the public field
     */
    public GpmCheckBox getPublic();
}