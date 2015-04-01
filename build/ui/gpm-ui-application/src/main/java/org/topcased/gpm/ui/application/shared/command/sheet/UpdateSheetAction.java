/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.sheet;

import java.util.List;
import java.util.Map;

import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;

/**
 * UpdateSheetAction
 * 
 * @author nveillet
 */
public class UpdateSheetAction extends AbstractCommandAction<GetSheetResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -7754936991161073262L;

    private List<UiField> fields;

    private Map<String, List<UiField>> links;

    private String sheetId;

    private int version;

    /**
     * create action
     */
    public UpdateSheetAction() {
    }

    /**
     * create action with values
     * 
     * @param pProductName
     *            the product name
     * @param pSheetId
     *            the sheet identifier
     * @param pVersion
     *            the sheet version
     * @param pFields
     *            the fields to update
     * @param pLinks
     *            the links to update
     */
    public UpdateSheetAction(String pProductName, String pSheetId,
            int pVersion, List<UiField> pFields,
            Map<String, List<UiField>> pLinks) {
        super(pProductName);
        sheetId = pSheetId;
        version = pVersion;
        fields = pFields;
        links = pLinks;
    }

    /**
     * get fields
     * 
     * @return the fields
     */
    public List<UiField> getFields() {
        return fields;
    }

    /**
     * get links
     * 
     * @return the links
     */
    public Map<String, List<UiField>> getLinks() {
        return links;
    }

    /**
     * get sheet identifier
     * 
     * @return the sheet identifier
     */
    public String getSheetId() {
        return sheetId;
    }

    /**
     * get version
     * 
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * set fields
     * 
     * @param pFields
     *            the fields to set
     */
    public void setFields(List<UiField> pFields) {
        fields = pFields;
    }

    /**
     * set links
     * 
     * @param pLinks
     *            the links to set
     */
    public void setLinks(Map<String, List<UiField>> pLinks) {
        links = pLinks;
    }

    /**
     * set sheet identifier
     * 
     * @param pSheetId
     *            the sheet identifier to set
     */
    public void setSheetId(String pSheetId) {
        sheetId = pSheetId;
    }

    /**
     * set version
     * 
     * @param pVersion
     *            the version to set
     */
    public void setVersion(int pVersion) {
        version = pVersion;
    }

}
