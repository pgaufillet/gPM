/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard, Pierre Hubert TSAAN (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.user.detail;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.values.field.applet.IAppletDisplayHint;
import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.common.tab.TabElementPresenter;
import org.topcased.gpm.ui.application.client.user.ProductWorkspaceDisplay;
import org.topcased.gpm.ui.application.client.user.ProductWorkspacePresenter;

/**
 * Allow an external page object to interact with GWT sheets by Javascript calls
 * 
 * @author jeballar
 */
public class SheetEditionJsAccessor implements IAppletDisplayHint {
    /**
     * Instance of SheetEditionJsAccessor
     */
    private static boolean staticDefined = false;

    private final String sheetId = "sheetId";

    /**
     * Define bridge if not all ready done
     */
    public static void defineBridge() {
        if (!staticDefined) {
            defineBridgeMethod();
            staticDefined = true;
        }
    }

    /**
     * Define the bridge method
     */
    private static native void defineBridgeMethod() /*-{
                                                    $wnd.setFieldValue = function(pSheetId,pFieldName, pFieldValue) {
                                                    return @org.topcased.gpm.ui.application.client.user.detail.SheetEditionJsAccessor::setFieldValue(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(pSheetId,pFieldName, pFieldValue);
                                                    }
                                                    }-*/;

    /**
     * Set a field value from outside the app
     * 
     * @param pSheetId
     *            the sheet id
     * @param pFieldName
     *            the field name to set
     * @param pFieldValue
     *            the new field value
     * @return <CODE>true</CODE> if the field is set, else <CODE>false</CODE>
     *         (sheet or field not found for example)
     */
    public static boolean setFieldValue(String pSheetId, String pFieldName,
            String pFieldValue) {
        boolean lResult = false;
        Collection<TabElementPresenter<ProductWorkspaceDisplay>> lProductPresenters =
                Application.INJECTOR.getUserSpacePresenter().getPresenters();

        // Try to get the sheets from all products in workspace
        for (TabElementPresenter<ProductWorkspaceDisplay> lP : lProductPresenters) {
            ProductWorkspacePresenter lProductTab =
                    (ProductWorkspacePresenter) lP;
            try {
                // Trying to get the sheet as an editionPresenter
                SheetEditionPresenter lPresenter =
                        (SheetEditionPresenter) lProductTab.getDetail().getPresenterById(
                                pSheetId);
                if (lPresenter != null) {
                    // Set value
                    if (lPresenter.setFieldValue(pFieldName, pFieldValue)) {
                        lResult = true;
                    }
                    else {
                        throwException("Field \"" + pFieldName
                                + "\" can not be set as String");
                    }
                }
            }
            catch (ClassCastException lCast) {
                // Send and continue to try
                throwException("Sheet ID found in vizualisation mode. Can not edit field value");
            }
            catch (UnsupportedOperationException lOp) {
                // UiFields send this exception when it can not be set as a
                // String
                throwException("Field \"" + pFieldName
                        + "\" can not be set as String");
            }
        }
        return lResult;
    }

    /**
     * Get a field value by field Name
     * 
     * @param pSheetId
     *            the sheet id
     * @param pFieldName
     *            the field name to set
     * @return <B>field value</B> if the field is set, else <B>null</B> (sheet
     *         or field not found for example)
     */
    public static String getFieldValueByName(String pSheetId, String pFieldName) {
        String lFiedValue = null;
        SheetEditionPresenter lEPresenter = null;
        SheetVisualizationPresenter lVPresenter = null;

        Collection<TabElementPresenter<ProductWorkspaceDisplay>> lProductPresenters =
                Application.INJECTOR.getUserSpacePresenter().getPresenters();

        // Try to get the sheets from all products in workspace
        for (TabElementPresenter<ProductWorkspaceDisplay> lP : lProductPresenters) {
            ProductWorkspacePresenter lProductTab =
                    (ProductWorkspacePresenter) lP;
            try {
                TabElementPresenter<?> lPresenter =
                        lProductTab.getDetail().getPresenterById(pSheetId);

                // Trying to get the sheet as an editionPresenter
                if (lPresenter instanceof SheetEditionPresenter) {
                    lEPresenter = (SheetEditionPresenter) lPresenter;
                }
                else if (lPresenter instanceof SheetVisualizationPresenter) {
                    lVPresenter = (SheetVisualizationPresenter) lPresenter;
                }
                if (lVPresenter != null) {
                    // Get value
                    lFiedValue = lVPresenter.getFieldValueByName(pFieldName);
                }
                else if (lEPresenter != null) {
                    lFiedValue = lEPresenter.getFieldValueByName(pFieldName);
                }
            }
            catch (ClassCastException lCast) {
                // UiFields send this exception when it can not be set as a
                // String
                throwException(lCast.getMessage());
            }
            catch (UnsupportedOperationException lOp) {
                // UiFields send this exception when it can not be set as a
                // String
                throwException("Field \"" + pFieldName
                        + "\" can not be set as String");
            }
        }
        return lFiedValue;
    }

    /**
     * Throw an exception in Javascript
     * 
     * @param pExceptionMessage
     *            the message to throw
     */
    public native static void throwException(String pExceptionMessage) /*-{
                                                                       alert(pExceptionMessage);
                                                                       }-*/;

    @Override
    public Map<String, String> getParametersCouple(String pSheetId,
            List<String> pParamNames) {
        final Map<String, String> lParams = new HashMap<String, String>();
        if (pParamNames != null) {
            for (final String lName : pParamNames) {
                final String lFieldValue = getFieldValueByName(pSheetId, lName);
                lParams.put(lName, lFieldValue);
            }
        }
        lParams.put(sheetId, pSheetId);
        return lParams;
    }
}
