/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.mail;

import java.util.List;

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.application.client.popup.PopupDisplay;

import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Display interface for the SendMailView.
 * 
 * @author tpanuel
 */
public interface SendMailDisplay extends PopupDisplay {

    /**
     * Set the send button handler.
     * 
     * @param pHandler
     *            The handler.
     */
    public void setSendButtonHandler(ClickHandler pHandler);

    /**
     * Set the available mail address.
     * 
     * @param pAvailableMailAddress
     *            The available mail address.
     */
    public void setAvailableMailAddress(List<String> pAvailableMailAddress);

    /**
     * Set the reference of the attached sheets.
     * 
     * @param pAttachedSheetReferences
     *            the sheet references
     */
    public void setAttachedSheetReferences(List<String> pAttachedSheetReferences);

    /**
     * Set the list of report model.
     * 
     * @param pReportModels
     *            The report models.
     */
    public void setReportModel(final List<Translation> pReportModels);

    /**
     * Clear the form.
     */
    public void clear();

    /**
     * Get the destination address.
     * 
     * @return The destination address.
     */
    public List<String> getDestination();

    /**
     * Get the subject.
     * 
     * @return The subject.
     */
    public String getSubject();

    /**
     * Get the body.
     * 
     * @return The body.
     */
    public String getBody();

    /**
     * Get the report model.
     * 
     * @return The report model.
     */
    public String getReportModel();

    /**
     * Validate view
     * 
     * @return the validation message or null if validation succeeded
     */
    public String validate();
}