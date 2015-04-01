/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic.container.revision;

import org.topcased.gpm.domain.dynamic.container.DynamicValuesContainerGenerator;
import org.topcased.gpm.domain.fields.FieldsContainer;
import org.topcased.gpm.domain.revision.Revision;

/**
 * Generator of dynamic revisions
 * 
 * @author tpanuel
 */
public class DynamicRevisionGenerator extends
        DynamicValuesContainerGenerator<Revision> {
    private static final Source SOURCE =
            new Source(DynamicRevisionGenerator.class.getName());

    /**
     * Create a dynamic revisions generator
     * 
     * @param pRevisedType
     *            The type of the revised fields container
     */
    public DynamicRevisionGenerator(FieldsContainer pRevisedType) {
        super(SOURCE, pRevisedType, Revision.class, true);
    }
}