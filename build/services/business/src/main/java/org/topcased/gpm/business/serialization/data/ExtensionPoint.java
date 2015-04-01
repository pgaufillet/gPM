/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * The Class ExtensionPoint.
 * 
 * @author llatil
 */
@XStreamAlias("extensionPoint")
public class ExtensionPoint extends NamedElement {

    /** Generated UID */
    private static final long serialVersionUID = 2381708985260321548L;

    /** The commands. */
    @XStreamImplicit(itemFieldName = "command")
    private List<NamedElement> commands;

    /** The attributes. */
    private List<Attribute> attributes;

    /** Technical identifier of the extension point. */
    private String id;

    /**
     * Constructs a new ExtensionPoint (default ctor).
     */
    public ExtensionPoint() {
    }

    /**
     * Constructs a new ExtensionPoint.
     * 
     * @param pName
     *            Name of the element
     */
    public ExtensionPoint(String pName) {
        setName(pName);
    }

    /**
     * Constructs a new ExtensionPoint.
     * 
     * @param pName
     *            Name of the element
     * @param pCommandNames
     *            List of command names associated with this extension point
     */
    public ExtensionPoint(String pName, List<String> pCommandNames) {
        setName(pName);
        copyInCommands(pCommandNames);
    }

    /**
     * Gets the commands.
     * 
     * @return the commands
     */
    public List<NamedElement> getCommands() {
        return commands;
    }

    /**
     * Sets the commands.
     * 
     * @param pCommands
     *            the commands
     */
    public void setCommands(List<NamedElement> pCommands) {
        commands = pCommands;
    }

    /**
     * Copy the given list into the commands.
     * 
     * @param pCommandNames
     *            the command names
     */
    public void copyInCommands(List<String> pCommandNames) {
        commands = new ArrayList<NamedElement>(pCommandNames.size());
        for (String lCmdName : pCommandNames) {
            commands.add(new NamedElement(lCmdName));
        }
    }

    /**
     * Gets the attributes.
     * 
     * @return the attributes
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * Sets the attributes.
     * 
     * @param pAttributes
     *            the attributes
     */
    public void setAttributes(List<Attribute> pAttributes) {
        attributes = pAttributes;
    }

    /**
     * get the ID
     * 
     * @return the ID
     */
    public String getId() {
        return id;
    }

    /**
     * set the ID
     * 
     * @param pId
     *            the ID
     */
    public void setId(String pId) {
        this.id = pId;
    }
}
