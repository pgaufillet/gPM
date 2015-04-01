/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container;

import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * A from with fields.
 * 
 * @author tpanuel
 */
public class GpmFormPanel extends Grid {
    private final static String LABEL_WIDTH = "150px";

    private static final int DEFAULT_COLUMNS_NUMBER = 2;

    private int lineNumber;

    /**
     * Create a form with fields.
     * 
     * @param pNbField
     *            The number of fields.
     */
    public GpmFormPanel(final int pNbField) {
        super(pNbField, DEFAULT_COLUMNS_NUMBER);
        lineNumber = 0;
        getColumnFormatter().setWidth(0, LABEL_WIDTH);
        setStylePrimaryName(ComponentResources.INSTANCE.css().gpmFieldGrid());
    }

    /**
     * Add a field.
     * 
     * @param pLabel
     *            The field label.
     * @param pField
     *            The field.
     */
    public void addField(final String pLabel, final Widget pField) {
        final Widget lLabel = new HTML(pLabel);

        lLabel.setStylePrimaryName(ComponentResources.INSTANCE.css().gpmFieldLabel());

        setWidget(lineNumber, 0, lLabel);
        setWidget(lineNumber, 1, pField);

        if (lineNumber % 2 == 0) {
            getRowFormatter().setStylePrimaryName(lineNumber,
                    ComponentResources.INSTANCE.css().evenRow());
        }
        else {
            getRowFormatter().setStylePrimaryName(lineNumber,
                    ComponentResources.INSTANCE.css().oddRow());
        }
        lineNumber++;
    }
}