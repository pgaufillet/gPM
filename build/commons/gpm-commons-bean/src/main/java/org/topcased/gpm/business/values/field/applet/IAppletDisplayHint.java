/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Pierre Hubert TSAAN (Atos Origin)
 *
 ******************************************************************/
package org.topcased.gpm.business.values.field.applet;

import java.util.List;
import java.util.Map;

/**
 * interface IApplet DisplayHint
 * 
 * @author phtsaan
 */
public interface IAppletDisplayHint {
    /**
     * Retrieves parameter valeu, name given
     * 
     * @param pSheetId
     *            the sheet id
     * @param pParamNames
     *            the parameter name
     * @return Map<parameter name, paramter value>
     */
    public Map<String, String> getParametersCouple(String pSheetId,
            List<String> pParamNames);
}
