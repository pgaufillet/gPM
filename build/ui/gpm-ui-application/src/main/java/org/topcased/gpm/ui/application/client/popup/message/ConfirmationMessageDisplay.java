/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.message;

import org.topcased.gpm.ui.application.client.popup.PopupDisplay;

import com.google.gwt.event.dom.client.HasClickHandlers;

/**
 * Display interface for the QuestionMessageView.
 * 
 * @author tpanuel
 */
public interface ConfirmationMessageDisplay extends PopupDisplay {
    /**
     * Set the question message.
     * 
     * @param pQuestionMessage
     *            The question message.
     */
    public void setQuestionMessage(String pQuestionMessage);

    /**
     * Get the Yes button.
     * 
     * @return The Yes button.
     */
    public HasClickHandlers getYesButton();

    /**
     * Get the NO button.
     * 
     * @return The No button.
     */
    public HasClickHandlers getNoButton();
}