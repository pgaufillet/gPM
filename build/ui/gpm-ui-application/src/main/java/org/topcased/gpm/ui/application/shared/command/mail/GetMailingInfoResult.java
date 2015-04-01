/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.mail;

import java.util.List;

import net.customware.gwt.dispatch.shared.Result;

import org.topcased.gpm.business.util.Translation;

/**
 * GetMailingInfoResult
 * 
 * @author nveillet
 */
public class GetMailingInfoResult implements Result {

    /** serialVersionUID */
    private static final long serialVersionUID = -4533708579101168310L;

    private List<String> destinationUsers;

    private List<Translation> reportModelNames;

    private List<String> sheetReferences;

    /**
     * Empty constructor for serialization.
     */
    public GetMailingInfoResult() {
    }

    /**
     * Create GetMailingInfoResult with values
     * 
     * @param pDestinationUsers
     *            the destination users
     * @param pReportModels
     *            the report models
     * @param pSheetReferences
     *            the sheet references
     */
    public GetMailingInfoResult(List<String> pDestinationUsers,
            List<Translation> pReportModels, List<String> pSheetReferences) {
        destinationUsers = pDestinationUsers;
        sheetReferences = pSheetReferences;
        reportModelNames = pReportModels;
    }

    /**
     * get destination users
     * 
     * @return the destination users
     */
    public List<String> getDestinationUsers() {
        return destinationUsers;
    }

    /**
     * get report models
     * 
     * @return the report models
     */
    public List<Translation> getReportModels() {
        return reportModelNames;
    }

    /**
     * get sheet references
     * 
     * @return the sheet references
     */
    public List<String> getSheetReferences() {
        return sheetReferences;
    }

}
