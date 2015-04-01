/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.filter.edit;

import java.util.List;

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.application.client.popup.PopupDisplay;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;

import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Display interface for the FilterEditionView.
 * 
 * @author jlouisy
 */
public interface FilterEditionSavePopupDisplay extends PopupDisplay {

    /**
     * Refresh popup.
     */
    void display();

    /**
     * Get description field value.
     * 
     * @return value.
     */
    String getDescription();

    /**
     * Get hidden field value.
     * 
     * @return value.
     */
    Boolean getHidden();

    /**
     * Get name field value.
     * 
     * @return value.
     */
    String getName();

    /**
     * Get name field.
     * 
     * @return name field.
     */
    GpmTextBox<String> getNameField();

    /**
     * Get usage field value.
     * 
     * @return value.
     */
    String getUsage();

    /**
     * Get visibility field value.
     * 
     * @return value.
     */
    String getVisibility();

    /**
     * set description field value
     * 
     * @param pDescription
     *            description
     */
    void setDescription(String pDescription);

    /**
     * Set hidden value.
     * 
     * @param pIsHidden
     *            value
     */
    void setHidden(Boolean pIsHidden);

    /**
     * Set name field value.
     * 
     * @param pName
     *            name.
     */
    void setName(String pName);

    /**
     * Set click handler on save button.
     * 
     * @param pClickHandler
     *            the click handler.
     * @param pIsEnabled <tt>true</tt> if the button is enabled, <tt>false</tt> otherwise.
     */
    void setSaveButtonHandler(ClickHandler pClickHandler, boolean pIsEnabled);

    /**
     * Set usage field value.
     * 
     * @param pUsage
     *            usage
     * @param pUsageList
     *            available usages
     */
    void setUsage(String pUsage, List<Translation> pUsageList);

    /**
     * Set visibility value.
     * 
     * @param pVisibility
     *            visibility
     * @param pVisibilityList
     *            available visibilities
     */
    void setVisibility(String pVisibility, List<Translation> pVisibilityList);

}