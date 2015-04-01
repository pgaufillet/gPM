/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.service;

import java.util.HashMap;
import java.util.Map;

/**
 * ApplicationAction
 * 
 * @author ahaugommard
 */
public enum ApplicationAction {

    SHEET_CREATE("Sheet.Create"), SHEET_EDIT("Sheet.Edit"), SHEET_DELETE(
            "Sheet.Delete"), SHEET_DUPLICATE("Sheet.Duplication.SameProduct"), SHEET_VIEW(
            "Sheet.Visualize"), SHEET_SEARCH_NEW("Sheet.Search.New"), SHEET_SEARCH_EDIT(
            "Sheet.Search.Edit"), SHEET_SEARCH_DELETE("Sheet.Search.Delete"), USER_CREATE(
            "Administration.User.Create"), USER_MODIFY(
            "Administration.User.Modify"), USER_DELETE(
            "Administration.User.Delete"), PRODUCT_CREATE(
            "Administration.Product.Create"), PRODUCT_VIEW(
            "Administration.Product.Visualize"), PRODUCT_EDIT(
            "Administration.Product.Edit"), PRODUCT_DELETE(
            "Administration.Product.Delete"), PRODUCT_EXPORT(
            "Administration.Product.Export"), PRODUCT_IMPORT(
            "Administration.Product.Import"), PRODUCT_SEARCH_NEW(
            "Administration.Product.Search.New"), PRODUCT_SEARCH_EDIT(
            "Administration.Product.Search.Edit"), PRODUCT_SEARCH_DELETE(
            "Administration.Product.Search.Delete"), DICT_MODIFY(
            "Administration.Dictionary.Modify"), ENV_MODIFY(
            "Administration.Environment.Modify"), EXPORT_XLS(
            "Util.Export.Excel"), EXPORT_PDF("Util.Export.PDF"), EXPORT_XML(
            "Util.Export.XML"), HELP_CONTENT("Help.Content"), HELP_ABOUT(
            "Help.About");

    private final String actionKey;

    ApplicationAction(String pActionKey) {
        actionKey = pActionKey;
    }

    /**
     * Get the key as string value
     * 
     * @return Action key as string
     */
    public String actionKey() {
        return actionKey;
    }

    public String toString() {
        return actionKey;
    }

    private static final Map<String, ApplicationAction> VALUES =
            new HashMap<String, ApplicationAction>();

    public static ApplicationAction fromString(String pActionKey) {
        if (VALUES.isEmpty()) {
            for (ApplicationAction lApplicationAction : values()) {
                VALUES.put(lApplicationAction.actionKey(), lApplicationAction);
            }
        }
        return VALUES.get(pActionKey);
    }
}
