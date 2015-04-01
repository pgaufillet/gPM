/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.instantiation.fieldaccess;

/**
 * Maps the access to a ProductData.
 * 
 * @author nbousque
 */
public interface ProductAccess extends FieldCompositeAccess {

    /**
     * Get the environment names
     * 
     * @return the names of the environment used by the Product.
     */
    public String[] getEnvironmentNames();

    /**
     * Get the identifier of the product
     * 
     * @return the Id of the product.
     */
    public String getId();

    /**
     * Get the name of the process
     * 
     * @return the process name of the product.
     */
    public String getProcessName();

}