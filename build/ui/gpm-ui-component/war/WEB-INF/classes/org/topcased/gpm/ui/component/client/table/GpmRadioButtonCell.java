/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.table;

import org.topcased.gpm.business.util.search.FilterResultId;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;

/**
 * An radio button cell for the gPM table.
 * 
 * @author tpanuel
 */
public class GpmRadioButtonCell extends AbstractCell<FilterResultId> {
    private final static String PREFIX = "TABLE_RADIO_BOX_";

    private final static String CLICK_EVENT = "click";

    private static int staticCounter = 0;

    private final String groupName;

    private String selected;

    /**
     * Create a radio button cell.
     */
    public GpmRadioButtonCell() {
        groupName = PREFIX + staticCounter;
        staticCounter++;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.cell.client.AbstractCell#onBrowserEvent(com.google.gwt.dom.client.Element,
     *      java.lang.Object, java.lang.Object,
     *      com.google.gwt.dom.client.NativeEvent,
     *      com.google.gwt.cell.client.ValueUpdater)
     */
    @Override
    public Object onBrowserEvent(final Element pParent,
            final FilterResultId pValue, final Object pViewData,
            final NativeEvent pEvent,
            final ValueUpdater<FilterResultId> pValueUpdater) {
        if (pEvent.getType().equals(CLICK_EVENT)) {
            selected = pValue.getId();
        }

        return pViewData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.cell.client.AbstractCell#render(java.lang.Object,
     *      java.lang.Object, java.lang.StringBuilder)
     */
    @Override
    public void render(final FilterResultId pData, final Object pViewData,
            final StringBuilder pStringBuilder) {
        pStringBuilder.append("<input type=\"radio\" name=\"" + groupName
                + "\"/>");
    }

    /**
     * Clear the selected element id.
     */
    public void clearSelectedId() {
        selected = null;
    }

    /**
     * Get the selected element id.
     * 
     * @return The selected element id.
     */
    public String getSelectedId() {
        return selected;
    }
}