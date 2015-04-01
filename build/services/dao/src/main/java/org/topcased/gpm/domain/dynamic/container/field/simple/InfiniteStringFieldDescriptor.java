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

import org.topcased.gpm.domain.fields.SimpleField;
import org.topcased.gpm.domain.fields.StringValue;

/**
 * Describe dynamic mapping used to represent an Infinite String Simple field
 * 
 * @author mfranche
 */
public class InfiniteStringFieldDescriptor extends ManyToOneFieldDescriptor {

    /**
     * Describe the data model of a infinite string simple field
     * 
     * @param pField
     *            The simple field
     */
    public InfiniteStringFieldDescriptor(SimpleField pField) {
        super(pField, StringValue.class, StringValue.class, true);
    }
}
