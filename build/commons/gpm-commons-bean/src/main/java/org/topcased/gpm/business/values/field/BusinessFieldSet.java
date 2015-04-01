/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 *
 ******************************************************************/
package org.topcased.gpm.business.values.field;

import org.topcased.gpm.business.values.field.multiple.BusinessMultipleField;
import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;
import org.topcased.gpm.business.values.field.simple.BusinessAttachedField;
import org.topcased.gpm.business.values.field.simple.BusinessBooleanField;
import org.topcased.gpm.business.values.field.simple.BusinessChoiceField;
import org.topcased.gpm.business.values.field.simple.BusinessDateField;
import org.topcased.gpm.business.values.field.simple.BusinessIntegerField;
import org.topcased.gpm.business.values.field.simple.BusinessPointerField;
import org.topcased.gpm.business.values.field.simple.BusinessRealField;
import org.topcased.gpm.business.values.field.simple.BusinessStringField;

/**
 * Interface used to access on set of field (container or multiple field).
 * 
 * @author tpanuel
 */
public interface BusinessFieldSet extends Iterable<BusinessField> {
    /**
     * Get an access on field without specifying it's type.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The access.
     */
    public BusinessField getField(final String pFieldName);

    /**
     * Get access on a mono valued simple field of type String.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The access.
     */
    public BusinessStringField getStringField(final String pFieldName);

    /**
     * Get access on a multi valued simple field of type String.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The access.
     */
    public BusinessMultivaluedField<BusinessStringField> getMultivaluedStringField(
            final String pFieldName);

    /**
     * Get access on a mono valued simple field of type Integer.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The access.
     */
    public BusinessIntegerField getIntegerField(final String pFieldName);

    /**
     * Get access on a multi valued simple field of type Integer.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The access.
     */
    public BusinessMultivaluedField<BusinessIntegerField> getMultivaluedIntegerField(
            final String pFieldName);

    /**
     * Get access on a mono valued simple field of type Real.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The access.
     */
    public BusinessRealField getRealField(final String pFieldName);

    /**
     * Get access on a multi valued simple field of type Real.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The access.
     */
    public BusinessMultivaluedField<BusinessRealField> getMultivaluedRealField(
            final String pFieldName);

    /**
     * Get access on a mono valued simple field of type Boolean.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The access.
     */
    public BusinessBooleanField getBooleanField(final String pFieldName);

    /**
     * Get access on a multi valued simple field of type Boolean.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The access.
     */
    public BusinessMultivaluedField<BusinessBooleanField> getMultivaluedBooleanField(
            final String pFieldName);

    /**
     * Get access on a mono valued simple field of type Date.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The access.
     */
    public BusinessDateField getDateField(final String pFieldName);

    /**
     * Get access on a multi valued simple field of type Date.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The access.
     */
    public BusinessMultivaluedField<BusinessDateField> getMultivaluedDateField(
            final String pFieldName);

    /**
     * Get access on a mono valued choice field.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The access.
     */
    public BusinessChoiceField getChoiceField(final String pFieldName);

    /**
     * Get access on a multi valued choice field.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The access.
     */
    public BusinessMultivaluedField<BusinessChoiceField> getMultivaluedChoiceField(
            final String pFieldName);

    /**
     * Get access on a mono valued pointer field.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The access.
     */
    public BusinessPointerField getPointerField(final String pFieldName);

    /**
     * Get access on a multi valued pointer field.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The access.
     */
    public BusinessMultivaluedField<BusinessPointerField> getMultivaluedPointerField(
            final String pFieldName);

    /**
     * Get access on a mono valued attached field.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The access.
     */
    public BusinessAttachedField getAttachedField(final String pFieldName);

    /**
     * Get access on a multi valued attached field.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The access.
     */
    public BusinessMultivaluedField<BusinessAttachedField> getMultivaluedAttachedField(
            final String pFieldName);

    /**
     * Get access on a mono valued multiple field.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The access.
     */
    public BusinessMultipleField getMultipleField(final String pFieldName);

    /**
     * Get access on a multi valued multiple field.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The access.
     */
    public BusinessMultivaluedField<BusinessMultipleField> getMultivaluedMultipleField(
            final String pFieldName);
}