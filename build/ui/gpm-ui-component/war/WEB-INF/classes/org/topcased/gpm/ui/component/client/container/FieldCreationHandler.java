/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container;

import java.util.List;

import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;

/**
 * Handler executed before a display group initialization. Used for implement a
 * full lazy display group.
 * 
 * @author tpanuel
 */
public interface FieldCreationHandler {
    /**
     * Create fields for a GpmFieldGridPanel.
     * 
     * @return The fields.
     */
    public List<AbstractGpmField<?>> createFields();
}