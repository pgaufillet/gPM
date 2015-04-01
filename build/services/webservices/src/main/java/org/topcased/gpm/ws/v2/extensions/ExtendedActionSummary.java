/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws.v2.extensions;

import java.io.Serializable;
import java.util.List;

/**
 * ExtendedActionSummary
 * 
 * @author nveillet
 */
public class ExtendedActionSummary implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -118220367047523100L;

    private String extendedActionName;

    private String inputDataTypeId;

    private List<GuiContext> guiContexts;

    private String menuEntryName;

    private String menuEntryParentName;

    /**
     * Create new ExtendedActionSummary
     */
    public ExtendedActionSummary() {

    }

    /**
     * Create new ExtendedActionSummary initialized
     * 
     * @param pExtendedActionName
     *            The extended action name
     * @param pInputDataTypeId
     *            The input data type identifier
     */
    public ExtendedActionSummary(String pExtendedActionName,
            String pInputDataTypeId) {
        extendedActionName = pExtendedActionName;
        inputDataTypeId = pInputDataTypeId;
    }

    /**
     * Get the extended action name
     * 
     * @return The extended action name
     */
    public String getExtendedActionName() {
        return extendedActionName;
    }

    /**
     * Set the extended action name
     * 
     * @param pExtendedActionName
     *            The extended action name to set
     */
    public void setExtendedActionName(String pExtendedActionName) {
        extendedActionName = pExtendedActionName;
    }

    /**
     * Get the input data type identifier
     * 
     * @return The input data type identifier
     */
    public String getInputDataTypeId() {
        return inputDataTypeId;
    }

    /**
     * Set the input data type identifier
     * 
     * @param pInputDataTypeId
     *            The input data type identifier to set
     */
    public void setInputDataTypeId(String pInputDataTypeId) {
        inputDataTypeId = pInputDataTypeId;
    }

    /**
     * Get the
     * 
     * @return The GuiContext list
     */
    public List<GuiContext> getGuiContexts() {
        return guiContexts;
    }

    /**
     * Set the GuiContext list
     * 
     * @param pGuiContexts
     *            The GuiContext list
     */
    public void setGuiContexts(List<GuiContext> pGuiContexts) {
        guiContexts = pGuiContexts;
    }

    /**
     * get menu entry name
     * 
     * @return the menu entry name
     */
    public String getMenuEntryName() {
        return menuEntryName;
    }

    /**
     * set menu entry name
     * 
     * @param pMenuEntryName
     *            the menu entry name to set
     */
    public void setMenuEntryName(String pMenuEntryName) {
        menuEntryName = pMenuEntryName;
    }

    /**
     * get menu entry parent name
     * 
     * @return the menu entry parent name
     */
    public String getMenuEntryParentName() {
        return menuEntryParentName;
    }

    /**
     * set menu entry parent name
     * 
     * @param pMenuEntryParentName
     *            the menu entry parent name to set
     */
    public void setMenuEntryParentName(String pMenuEntryParentName) {
        menuEntryParentName = pMenuEntryParentName;
    }

}
