/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.filter.edit;

import java.util.Map;

import net.customware.gwt.dispatch.shared.Result;

import org.topcased.gpm.ui.facade.shared.filter.field.UiFilterUsableField;

/**
 * PreSaveFilterResult
 * 
 * @author nveillet
 */
public class GetUsableFieldsResult implements Result {

    /** serialVersionUID */
    private static final long serialVersionUID = -5186691865508581228L;

    private Map<String, UiFilterUsableField> usableFields;

    /**
     * Empty constructor for serialization.
     */
    public GetUsableFieldsResult() {
    }

    /**
     * Create GetUsableFieldsResult with value
     * 
     * @param pUsableFields
     *            the usable fields
     */
    public GetUsableFieldsResult(Map<String, UiFilterUsableField> pUsableFields) {
        usableFields = pUsableFields;
    }

    /**
     * get usable fields
     * 
     * @return the usable fields
     */
    public Map<String, UiFilterUsableField> getUsablefields() {
        return usableFields;
    }

}
