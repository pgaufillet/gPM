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

import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;

/**
 * SendMailAction
 * 
 * @author nveillet
 */
public class SendMailAction extends AbstractCommandAction<SendMailResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -1114085558711099003L;

    private String body;

    private List<String> destinationUsers;

    private String reportModelName;

    private List<String> sheetIds;

    private String subject;

    /**
     * create action
     */
    public SendMailAction() {
    }

    /**
     * create action with values
     * 
     * @param pProductName
     *            the product name
     * @param pDestinationUsers
     *            the destination users
     * @param pSubject
     *            the subject
     * @param pBody
     *            the body
     * @param pSheetIds
     *            the sheet identifiers
     * @param pReportModelName
     *            the report model name
     */
    public SendMailAction(String pProductName, List<String> pDestinationUsers,
            String pSubject, String pBody, List<String> pSheetIds,
            String pReportModelName) {
        super(pProductName);
        destinationUsers = pDestinationUsers;
        subject = pSubject;
        body = pBody;
        sheetIds = pSheetIds;
        reportModelName = pReportModelName;
    }

    /**
     * get body
     * 
     * @return the body
     */
    public String getBody() {
        return body;
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
     * get report model name
     * 
     * @return the report model name
     */
    public String getReportModelName() {
        return reportModelName;
    }

    /**
     * get sheet identifiers
     * 
     * @return the sheet identifiers
     */
    public List<String> getSheetIds() {
        return sheetIds;
    }

    /**
     * get subject
     * 
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * set body
     * 
     * @param pBody
     *            the body to set
     */
    public void setBody(String pBody) {
        body = pBody;
    }

    /**
     * set report model name
     * 
     * @param pReportModelName
     *            the report model name to set
     */
    public void setReportModelName(String pReportModelName) {
        reportModelName = pReportModelName;
    }

    /**
     * set sheet identifiers
     * 
     * @param pSheetIds
     *            the sheet identifiers to set
     */
    public void setSheetIds(List<String> pSheetIds) {
        sheetIds = pSheetIds;
    }

    /**
     * set subject
     * 
     * @param pSubject
     *            the subject to set
     */
    public void setSubject(String pSubject) {
        subject = pSubject;
    }

    /**
     * set destination users
     * 
     * @param pDestinationUsers
     *            the destination users to set
     */
    public void setTo(List<String> pDestinationUsers) {
        destinationUsers = pDestinationUsers;
    }

}
