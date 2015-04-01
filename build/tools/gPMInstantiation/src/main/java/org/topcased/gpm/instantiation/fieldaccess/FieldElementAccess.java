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

import org.topcased.gpm.business.fields.AttachedFieldModificationData;
import org.topcased.gpm.business.fields.TextAreaSize;

/**
 * A FieldElementAccess maps to a single field (mono or multi valued).
 * 
 * @author nbousque
 */
public interface FieldElementAccess extends FieldAccess {

    /**
     * @return the array of value the field has.
     */
    public String[] getValues();

    /**
     * @param pValues
     *            the new values of the field. Old values are lost.
     */
    public void setValues(String[] pValues);

    /**
     * @return true if a field is confidential, false otherwise.
     */
    public boolean isConfidential();

    /**
     * @return a textual description of the field.
     */
    public String getDescription();

    /**
     * @return true if a field is Mandatory (need an actual value to be saved),
     *         false if not.
     */
    public boolean isMandatory();

    /**
     * @return true if a field can be modified
     */
    public boolean isUpdateable();

    /**
     * @return true if a field can be exported (XLS, PDF, XML), false otherwise.
     */
    public boolean isExportable();

    /**
     * @return the localized name of the field. Used in user interfaces.
     */
    public String getI18nName();

    /**
     * @return Available values in a case of a CHOICE field.
     */
    public String[] getAvailableValues();

    /**
     * @return File data in the case of a field containing a file;
     */
    public AttachedFieldModificationData getFileValue();

    /**
     * @param pData
     *            the new File data information.
     */
    public void setFileValue(AttachedFieldModificationData pData);

    /**
     * @return the preferred display size of the text.
     */
    public TextAreaSize getTextAreaSize();
}