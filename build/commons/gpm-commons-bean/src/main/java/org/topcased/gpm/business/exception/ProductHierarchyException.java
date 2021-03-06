/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Pierre Hubert TSAAN (Atos Origin)
 ******************************************************************/

package org.topcased.gpm.business.exception;

/**
 * Exception thrown when product hierarchy is not well assigned.
 * 
 * @author phtsaan
 */
public class ProductHierarchyException extends BusinessException {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -5696664020261331623L;

    /**
     * Construct a new message exception
     * 
     * @param pValue
     *            Invalid value
     */
    public ProductHierarchyException(String pValue) {
        super(pValue, pValue);
    }
}
