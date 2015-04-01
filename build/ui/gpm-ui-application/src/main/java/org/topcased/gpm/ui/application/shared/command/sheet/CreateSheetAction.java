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

import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;

/**
 * CreateSheetAction
 * 
 * @author nveillet
 */
public class CreateSheetAction extends AbstractCommandAction<GetSheetResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 8082044631273525415L;

    private List<UiField> fields;

    private String sheetId;

    /**
     * create action
     */
    public CreateSheetAction() {
    }

    /**
     * create action with values
     * 
     * @param pProductName
     *            the product name
     * @param pSheetId
     *            the sheet identifier
     * @param pFields
     *            the fields to create
     */
    public CreateSheetAction(String pProductName, String pSheetId,
            List<UiField> pFields) {
        super(pProductName);
        sheetId = pSheetId;
        fields = pFields;
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
     * get sheet identifier
     * 
     * @return the sheet identifier
     */
    public String getSheetId() {
        return sheetId;
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
     * set sheet identifier
     * 
     * @param pSheetId
     *            the sheet identifier to set
     */
    public void setSheetId(String pSheetId) {
        sheetId = pSheetId;
    }
}