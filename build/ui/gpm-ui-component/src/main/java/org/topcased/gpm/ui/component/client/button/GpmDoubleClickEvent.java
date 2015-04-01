/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.button;

import com.google.gwt.event.dom.client.DoubleClickEvent;

/**
 * A click event.
 * 
 * @author jeballar
 */
public class GpmDoubleClickEvent extends DoubleClickEvent {
    private final Object source;

    /**
     * Construct a click event.
     * 
     * @param pSource
     *            The source.
     */
    public GpmDoubleClickEvent(final Object pSource) {
        super();
        source = pSource;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.shared.GwtEvent#getSource()
     */
    @Override
    public Object getSource() {
        return source;
    }
}