/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: XXX (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * HasFormat
 * 
 * @author mkargbo
 */
public interface HasFormat {

    public void setFormat(final DateTimeFormat pFormat);

    public DateTimeFormat getFormat();
}
