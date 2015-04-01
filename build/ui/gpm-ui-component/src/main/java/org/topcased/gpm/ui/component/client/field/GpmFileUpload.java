/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Ronan Capecchi (Amesys Conseil)
 ******************************************************************/

package org.topcased.gpm.ui.component.client.field;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FileUpload;

/**
 * GpmFileUpload is a file input made to prevent user from writing directly in
 * the input text field
 * <p>
 * On mouse down and and key down events are skipped to make the field read only
 * </p>
 * 
 * @author rcapecchi
 */
public class GpmFileUpload extends FileUpload {

    /**
     * Constructs a new file upload widget.
     */
    public GpmFileUpload() {
        super();
        sinkEvents(Event.ONMOUSEDOWN | Event.ONKEYDOWN);
    }

    /**
     * get the events to be computed for this kind of widget
     * 
     * @param pEvent
     *            : the event
     */
    @Override
    public void onBrowserEvent(Event pEvent) {
        super.onBrowserEvent(pEvent);

        switch (DOM.eventGetType(pEvent)) {
            case Event.ONMOUSEDOWN:
            case Event.ONKEYDOWN:
                // ignore input
                pEvent.preventDefault();
                pEvent.stopPropagation();
                break;
            default:
                break;
        }
    }
}
