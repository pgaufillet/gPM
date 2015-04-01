/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet.service.sheetAccess;

/**
 * Access to a choice field.
 * 
 * @author llatil
 */
public interface ChoiceFieldData extends SimpleFieldData {

    /**
     * Get the list of acceptable values for the field.
     * 
     * @return Array of values.
     */
    String[] getAcceptableValues();

    /**
     * Get all values selected in this choice field.
     * <p>
     * This method can be used to get the values of multi-valued choice fields,
     * but can also be used one single value choice field (in this case the
     * returned array contains one element).
     * 
     * @return Array containing the selected values.
     */
    String[] getValues();
}
