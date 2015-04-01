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
 * This interface maps to a MultipleField (and is considered as a field
 * container).
 * 
 * @author nbousque
 */
public interface MultipleFieldAccess extends FieldCompositeAccess {

    /**
     * @return
     */
    public long getRef();

    /**
     * @return true if a field can be exported (XLS, PDF, XML), false otherwise.
     */
    public boolean isExportable();

    /**
     * @return a textual description of the field.
     */
    public boolean isConfidential();

    /**
     * @return the localized name of the field. Used in user interfaces.
     */
    public String getI18nName();
}