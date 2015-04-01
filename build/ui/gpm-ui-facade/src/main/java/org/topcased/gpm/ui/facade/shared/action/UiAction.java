/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.action;

import java.io.Serializable;

/**
 * UiAction
 * 
 * @author nveillet
 */
public class UiAction implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 233288552938403269L;

    private String name;

    private String translatedName;

    private ActionType type;
    
    private String confirmationMessage;

    /**
     * Empty constructor for serialization.
     */
    public UiAction() {
        type = ActionType.STANDARD;
    }

    /**
     * Create new standard UiAction with name
     * 
     * @param pName
     *            the name
     */
    public UiAction(String pName) {
        name = pName;
        type = ActionType.STANDARD;
    }

    /**
     * Create new UiAction with name and type
     * 
     * @param pName
     *            the name
     * @param pType
     *            the type
     */
    public UiAction(String pName, ActionType pType) {
        name = pName;
        type = pType;
    }

    /**
     * Create new UiAction with name, type and confirmation message
     * 
     * @param pName
     *            the name
     * @param pType
     *            the type
     * @param pConfirmationMessage
     *            the confirmation message
     */
    public UiAction(String pName, ActionType pType, String pConfirmationMessage) {
        name = pName;
        type = pType;
        confirmationMessage = pConfirmationMessage;
    }

    /**
     * get name
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * get translated name
     * 
     * @return the translated name
     */
    public String getTranslatedName() {
        return translatedName;
    }

    /**
     * get type
     * 
     * @return the type
     */
    public ActionType getType() {
        return type;
    }

    /**
     * set name
     * 
     * @param pName
     *            the name to set
     */
    public void setName(String pName) {
        name = pName;
    }

    /**
     * set translated name
     * 
     * @param pTranslatedName
     *            the translated name to set
     */
    public void setTranslatedName(String pTranslatedName) {
        translatedName = pTranslatedName;
    }

    /**
     * set type
     * 
     * @param pType
     *            the type to set
     */
    public void setType(ActionType pType) {
        type = pType;
    }

    /**
     * get the confirmation message
     * 
     * @return the confirmation message
     */
    public String getConfirmationMessage() {
        return confirmationMessage;
    }

    /**
     * set the confirmation message
     * 
     * @param pConfirmationMessage
     *            the confirmation message
     */
    public void setConfirmationMessage(String pConfirmationMessage) {
        this.confirmationMessage = pConfirmationMessage;
    }
}
