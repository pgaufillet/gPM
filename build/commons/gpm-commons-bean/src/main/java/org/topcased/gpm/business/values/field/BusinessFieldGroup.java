/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 *
 ******************************************************************/
package org.topcased.gpm.business.values.field;

import java.util.List;

/**
 * Interface used to access on a field group.
 * 
 * @author ogehin
 */
public interface BusinessFieldGroup {

    /**
     * Get the group name
     * 
     * @return the group name.
     */
    public String getGroupName();

    /**
     * Get the list of field names contained in this group.
     * 
     * @return the list of field names.
     */
    public List<String> getFieldNames();

    /**
     * Get the open property
     * 
     * @return if the group is open
     */
    public boolean isOpen();
}
