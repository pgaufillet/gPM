/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.util.List;

/**
 * The Class ExtensionsContainer.
 * 
 * @author llatil
 */
public abstract class ExtensionsContainer extends AttributesContainer {

    /** Generated UID */
    private static final long serialVersionUID = -2848734280486506021L;

    /** The extension points. */
    private List<ExtensionPoint> extensionPoints;

    /** The extended actions. */
    private List<ExtendedAction> extendedActions;

    /**
     * get extendedActions.
     * 
     * @return the extendedActions
     */
    public List<ExtendedAction> getExtendedActions() {
        return extendedActions;
    }

    /**
     * set extendedActions.
     * 
     * @param pExtendedActions
     *            the extendedActions to set
     */
    public void setExtendedActions(List<ExtendedAction> pExtendedActions) {
        this.extendedActions = pExtendedActions;
    }

    /**
     * Gets the extension points.
     * 
     * @return the extension points
     */
    public final List<ExtensionPoint> getExtensionPoints() {
        return extensionPoints;
    }

    /**
     * Sets the extension points.
     * 
     * @param pExtensionPoints
     *            the extension points
     */
    public final void setExtensionPoints(List<ExtensionPoint> pExtensionPoints) {
        extensionPoints = pExtensionPoints;
    }
}
