/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field.formater;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * A formatter for Date values on medium format.
 * 
 * @author tpanuel
 */
public final class GpmMediumDateFormatter extends AbstractGpmDateFormatter {
    private final static GpmMediumDateFormatter INSTANCE =
            new GpmMediumDateFormatter();

    private GpmMediumDateFormatter() {
        super(DateTimeFormat.getMediumDateFormat());
    }

    /**
     * It is a singleton.
     * 
     * @return The string formatter.
     */
    public final static GpmMediumDateFormatter getInstance() {
        return INSTANCE;
    }
}
