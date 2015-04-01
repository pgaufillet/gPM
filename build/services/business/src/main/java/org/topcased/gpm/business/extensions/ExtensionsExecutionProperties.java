/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions;

import java.util.LinkedList;
import java.util.List;

/**
 * Properties used by the extension points executions.
 * 
 * @author tpanuel
 */
public class ExtensionsExecutionProperties {
    private ExtensionsExecutionFlag extensionsExecutionFlag;

    private List<String> extensionPointsName;

    /**
     * By default, all the extension points are executed.
     */
    public ExtensionsExecutionProperties() {
        extensionsExecutionFlag = ExtensionsExecutionFlag.EXECUTE_ALL;
        extensionPointsName = new LinkedList<String>();
    }

    /**
     * Get the extensions execution flag.
     * 
     * @return The extensions execution flag.
     */
    public ExtensionsExecutionFlag getExtensionsExecutionFlag() {
        return extensionsExecutionFlag;
    }

    /**
     * Set the extensions execution flag.
     * 
     * @param pExtensionsExecutionFlag
     *            The new extensions execution flag.
     */
    public void setExtensionsExecutionFlag(
            final ExtensionsExecutionFlag pExtensionsExecutionFlag) {
        extensionsExecutionFlag = pExtensionsExecutionFlag;
    }

    /**
     * Get the name of the skipped or exported extension points.
     * 
     * @return The name of the skipped or exported extension points.
     */
    public List<String> getExtensionPointsName() {
        return extensionPointsName;
    }

    /**
     * Set the name of the skipped or exported extension points.
     * 
     * @param pExtensionPointsName
     *            The new name of the skipped or exported extension points.
     */
    public void setExtensionPointsName(final List<String> pExtensionPointsName) {
        extensionPointsName = pExtensionPointsName;
    }

    /**
     * Flag used to import a specific type of data.
     * 
     * @author tpanuel
     */
    public enum ExtensionsExecutionFlag {
        // Execute all the extension points
        EXECUTE_ALL,
        // Only a specific list of extension points are executed
        EXECUTE_LIST,
        // All the extension points are executed, except a specific list
        SKIP_LIST,
        // No extension points are executed
        SKIP_ALL
    }
}