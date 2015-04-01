/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic.container.field.simple;

import org.topcased.gpm.domain.fields.AttachedField;
import org.topcased.gpm.domain.fields.AttachedFieldValue;

/**
 * Describe dynamic mapping used to represent an Attached Field
 * 
 * @author tpanuel
 */
public class AttachedFieldDescriptor extends ManyToOneFieldDescriptor {
    /**
     * Describe the data model of an attached field
     * 
     * @param pField
     *            The attached field
     */
    public AttachedFieldDescriptor(AttachedField pField) {
        super(pField, AttachedFieldValue.class, AttachedFieldValue.class, true);
    }
}