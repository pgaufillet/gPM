/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin)
 *
 ******************************************************************/
package org.topcased.gpm.business.values.field.impl.cacheable;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.serialization.data.DisplayGroup;
import org.topcased.gpm.business.serialization.data.FieldRef;
import org.topcased.gpm.business.values.field.BusinessFieldGroup;

/**
 * Access on a field group.
 * 
 * @author ogehin
 */
public class CacheableFieldGroupAccess implements BusinessFieldGroup {

    private final DisplayGroup displayGroup;

    public CacheableFieldGroupAccess(DisplayGroup pDisplayGroup) {
        displayGroup = pDisplayGroup;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldGroup#getFieldNames()
     */
    @Override
    public List<String> getFieldNames() {
        List<String> lFieldNames = new ArrayList<String>();
        for (FieldRef lFieldRef : displayGroup.getFields()) {
            lFieldNames.add(lFieldRef.getName());
        }
        return lFieldNames;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldGroup#getGroupName()
     */
    @Override
    public String getGroupName() {
        return displayGroup.getName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldGroup#isOpen()
     */
    @Override
    public boolean isOpen() {
        return displayGroup.getOpened();
    }
}
