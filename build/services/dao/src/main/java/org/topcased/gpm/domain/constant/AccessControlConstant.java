/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.constant;

/**
 * Constant for Access Control service and domain. for example set the DB
 * attribute for Access control
 * 
 * @author mkargbo
 */
public enum AccessControlConstant {

    DB_ATTRIBUTE_PRODUCTCONTROL("productControl"), DB_ATTRIBUTE_STATECONTROL(
            "stateControl"), DB_ATTRIBUTE_ROLECONTROL("roleControl"), DB_ATTRIBUTE_TYPECONTROL(
            "typeControl"), DB_ATTRIBUTE_FIELD_FIELDCONTROL("fieldControl"), DB_ATTRIBUTE_TRANSITION_TRANSITIONNAME(
            "transitionName"), DB_ATTRIBUTE_APPLIACTION_ACTIONKEY("actionKey"), DB_ATTRIBUTE_APPLIACTION_BUSINESSPROCESS(
            "businessProcess"),

    TYPE_DEFAULTVALUES_CREATABLE(true), TYPE_DEFAULTVALUES_UPDATABLE(true), TYPE_DEFAULTVALUES_DELETABLE(
            true), TYPE_DEFAULTVALUES_CONFIDENTIAL(false),

    ACTION_DEFAULT_NOROLE_ENABLED(false), ACTION_DEFAULT_NOROLE_CONFIDENTIAL(
            true), ACTION_DEFAULT_ENABLED(true), ACTION_DEFAULT_CONFIDENTIAL(
            false),

    TRANSITION_DEFAULT_ALLOWED(true),

    TYPE_DEFAULT_CREATABLE(true), TYPE_DEFAULT_UPDATABLE(true), TYPE_DEFAULT_DELETABLE(
            true), TYPE_DEFAULT_CONFIDENTIAL(false);

    private String strValue;

    private Boolean boolValue;

    private AccessControlConstant(String pValue) {
        strValue = pValue;
    }

    private AccessControlConstant(boolean pValue) {
        boolValue = pValue;
        strValue = Boolean.toString(pValue).toLowerCase();
    }

    public String getAsString() {
        return strValue;
    }

    public Boolean getAsBoolean() {
        if (null == boolValue) {
            throw new RuntimeException("'AccessControlConstant." + strValue
                    + "' is not a boolean.");
        }
        return boolValue;
    }
}
