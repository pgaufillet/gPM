/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.fields.impl;

import org.topcased.gpm.business.serialization.data.InputData;

/**
 * CacheableInputData
 * 
 * @author ahaugomm
 */
@SuppressWarnings("serial")
public class CacheableInputData extends CacheableValuesContainer {
    /**
     * Constructor for mutable / immutable switch
     */
    public CacheableInputData() {
        super();
    }

    /**
     * Create a CacheableInputData from the InputData object
     * 
     * @param pInputData
     *            an InputData object
     * @param pType
     *            the Cacheable Input data type.
     */
    public CacheableInputData(InputData pInputData, CacheableInputDataType pType) {
        super(pInputData, pType);
    }
}
