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

import java.util.Map;

import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;

/**
 * Interface implemented by all set of fields : sheet, link, product and input
 * data.
 * 
 * @author tpanuel
 */
public interface GpmFieldSet {
    /**
     * Get the displayed fields : fields of a closed group are not displayed.
     * 
     * @return The displayed fields.
     */
    public Map<String, AbstractGpmField<?>> getDisplayedFields();
}