/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Cyril Marchive (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Class mapping an extension Points to exclude content.
 * 
 * @author cmarchive
 */
@XStreamAlias("extensionPointsToExclude")
public class ExtensionPointsToExcludeData implements Serializable {

    /**
     * Default serial ID 
     */
    private static final long serialVersionUID = 1L;

    /** Array list containing extension point to exclude. */
    @XStreamImplicit(itemFieldName = "exclude")
    private Set<ExcludeData> commandsToExclude;

    /**
     * Get a list of {@link ExcludeData} to exclude from execution
     * @return the list of {@link ExcludeData} to exclude
     */
    public Set<ExcludeData> getCommandsToExclude() {
        return commandsToExclude;
    }

    /**
     * Set the commands to exclude from execution
     * @param pCommandsToExclude a set of command to be excluded
     */
    public void setCommandsToExclude(final Set<ExcludeData> pCommandsToExclude) {
        this.commandsToExclude = pCommandsToExclude;
    }

    /**
     * Retrieve all commands to exclude
     * 
     * @return the commands to exclude
     */
    public Set<String> retrieveCommandsToExclude() {
        Set<String> lResult = new HashSet<String>();
        if (commandsToExclude != null) {
            for (ExcludeData lExcludeData : commandsToExclude) {
                lResult.add(lExcludeData.getName());
            }
        }

        return lResult;
    }
}
