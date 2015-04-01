/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.help.impl;

import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.help.service.HelpService;

/**
 * HelpServiceImpl
 * 
 * @author mfranche
 */
public class HelpServiceImpl extends ServiceImplBase implements HelpService {

    private String helpContentUrl;

    /**
     * get helpContentUrl
     * 
     * @return the helpContentUrl
     */
    public String getHelpContentUrl() {

        AttributeData[] lAttributeDatas =
                getAttributesService().getGlobalAttributes(
                        new String[] { AttributesService.HELP_CONTENT_URL });

        if (lAttributeDatas != null && lAttributeDatas.length == 1
                && lAttributeDatas[0] != null
                && lAttributeDatas[0].getValues() != null
                && lAttributeDatas[0].getValues().length == 1) {
            setHelpContentUrl(lAttributeDatas[0].getValues()[0]);
        }

        return helpContentUrl;
    }

    /**
     * set helpContentUrl
     * 
     * @param pHelpContentUrl
     *            the helpContentUrl to set
     */
    public void setHelpContentUrl(String pHelpContentUrl) {
        this.helpContentUrl = pHelpContentUrl;
    }

}
