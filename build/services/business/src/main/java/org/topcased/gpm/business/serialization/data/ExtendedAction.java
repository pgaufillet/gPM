/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Atos Origin
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * ExtendedAction.
 * 
 * @author ahaugommard
 */
@XStreamAlias("extendedAction")
public class ExtendedAction extends NamedElement {

    /** Generated UID */
    private static final long serialVersionUID = -5644959010481891490L;

    /** The extension point name. */
    @XStreamAsAttribute
    private String extensionPointName;

    /** The extension point name. */
    @XStreamAsAttribute
    private String confirmationMessage;

    /** The UI contexts. */
    private List<GuiContext> guiContexts;

    /** The menu entry. */
    private MenuEntry menuEntry;

    /** The input data type. */
    private InputDataType inputDataType;

    /**
     * get extensionPointName.
     * 
     * @return the extensionPointName
     */
    public String getExtensionPointName() {
        return extensionPointName;
    }

    /**
     * Get the confirmation message.
     * 
     * @return the confirmation message
     */
    public String getConfirmationMessage() {
        return confirmationMessage;
    }

    /**
     * get menuEntry.
     * 
     * @return the menuEntry
     */
    public MenuEntry getMenuEntry() {
        return menuEntry;
    }

    /**
     * get guiContexts.
     * 
     * @return the guiContexts
     */
    public List<GuiContext> getGuiContexts() {
        return guiContexts;
    }

    /**
     * Gets the input data type.
     * 
     * @return the input data type
     */
    public InputDataType getInputDataType() {
        return inputDataType;
    }
}
