/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.util.validation;

/**
 * IValidator a validator that implements this interface can validate and return
 * an error message.
 * 
 * @author mkargbo
 */
public interface IValidator {

    /**
     * Launch validation process.
     * 
     * @return <code>Null</code> if the no error, an error message otherwise.
     */
    public String validate();
}