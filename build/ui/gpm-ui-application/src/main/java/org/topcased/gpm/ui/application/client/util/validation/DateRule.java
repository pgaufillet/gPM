/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Benoit Hary (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.util.validation;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.MESSAGES;

import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.container.field.GpmDateBox;
import org.topcased.gpm.ui.component.client.container.field.GpmDateTimeBox;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * Rule implementation for date field. <h3>Constraint</h3>
 * <p>
 * Value is a date.
 * </p>
 * 
 * @author bhary
 */
public class DateRule extends AbstractRule {

    private static final String TIME_REGEXP;

    static {
        TIME_REGEXP = "^([0-1][0-9]|[2][0-3]):([0-5][0-9])$";

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.util.validation.AbstractRule#doCheck(org.topcased.gpm.ui.component.client.container.field.AbstractGpmField,
     *      java.lang.String)
     */
    @Override
    protected String doCheck(AbstractGpmField<?> pField, String pValue) {
        DateTimeFormat lFormat;

        if (pField instanceof GpmDateBox) {
            lFormat = ((GpmDateBox) pField).getFormat();
        }
        else {
            lFormat = ((GpmDateTimeBox) pField).getFormat();

            pValue = ((GpmDateTimeBox) pField).getAsString();
            //get the time part of the date
            String lTime =
                    pValue.substring(pValue.lastIndexOf(" ") + 1,
                            pValue.length());
            //check the format of the time
            if (!lTime.matches(TIME_REGEXP)) {
                return new String(
                        MESSAGES.fieldErrorTime(pField.getTranslatedFieldName()));
            }
            pValue = pValue.substring(0, pValue.lastIndexOf(" "));

        }

        try {
            lFormat.parse(pValue);
        }
        catch (Exception lException) {
            return new String(
                    MESSAGES.fieldErrorTime(pField.getTranslatedFieldName()));
        }

        return null;
    }
}