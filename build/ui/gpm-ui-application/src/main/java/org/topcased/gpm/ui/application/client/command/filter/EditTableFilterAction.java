/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.filter;

import org.topcased.gpm.ui.application.client.event.EmptyAction;

/**
 * open filter in edition mode
 * 
 * @author jlouisy
 */
public class EditTableFilterAction extends EmptyAction<EditTableFilterAction> {

    /** serialVersionUID */
    private static final long serialVersionUID = -7777602990846875865L;

    private String filterId;

    /**
     * Default constructor
     */
    public EditTableFilterAction() {
    }

    /**
     * Constructor with filter Id
     * 
     * @param pFilterId
     *            filter Id
     */
    public EditTableFilterAction(String pFilterId) {
        filterId = pFilterId;
    }

    public String getFilterId() {
        return filterId;
    }

}
