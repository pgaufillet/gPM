/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import org.topcased.gpm.business.fields.service.FieldsService;

/**
 * A ReferenceField maps a SheetType in gPM and is used for XML
 * marshalling/unmarshalling of a ReferenceField.
 * 
 * @author sidjelli
 */
public class ReferenceField extends MultipleField {

    private static final long serialVersionUID = 9083234960091542555L;

    /**
     * Reference field constructor
     */
    public ReferenceField() {
        fieldSeparator = FieldsService.REFERENCE_FIELD_SEPARATOR;
    }
}
