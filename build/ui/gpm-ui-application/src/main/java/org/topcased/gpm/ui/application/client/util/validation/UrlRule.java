/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.util.validation;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.MESSAGES;

import org.topcased.gpm.business.values.link.IUnsupportedProtocol;
import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;

/**
 * Rule implementation for url field. <h3>Constraint</h3>
 * <p>
 * Value is an url address.
 * </p>
 * 
 * @author mkargbo
 */
public class UrlRule extends AbstractRule implements IUnsupportedProtocol {

    private static final String URL_REGEXP;

    private static final String TAG = "_LINK_";

    static {
        // SCHEME
        StringBuilder lRegExp = new StringBuilder();
        lRegExp.append("((^(https?|ftp|file):\\/\\/)|(^www\\.))");

        // USER AND PASS (optional)
        lRegExp.append("([a-z0-9+!*(),;?&=\\$_.-]+(\\:[a-z0-9+!*(),;?&=\\$_.-]+)?@)?");

        // HOSTNAME OR IP
        lRegExp.append("[a-z0-9+\\$_-]+(\\.[a-z0-9+\\$_-]+)*");
        // http://x = allowed (ex. http://localhost, http://routerlogin)

        // PORT (optional)
        lRegExp.append("(\\:[0-9]{2,5})?");

        // PATH (optional)
        lRegExp.append("(\\/([a-z0-9+\\$_-]\\.?)+)*\\/?");

        // GET Query (optional)
        lRegExp.append("(\\?[a-z\\+&\\$_.-][a-z0-9;:@/&%=\\+\\$_.-]*)?");

        // ANCHOR (optional)
        lRegExp.append("(#[a-z_.-][a-z0-9\\+\\$_.-]*)?");
        URL_REGEXP = lRegExp.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.util.validation.AbstractRule#doCheck(org.topcased.gpm.ui.component.client.container.field.AbstractGpmField,
     *      java.lang.String)
     */
    @Override
    protected String doCheck(AbstractGpmField<?> pField, String pValue) {
        if (pValue != null && pValue.length() > 0
                && !pValue.toLowerCase().matches(URL_REGEXP)) {
            return new String(
                    MESSAGES.fieldErrorUrl(pField.getTranslatedFieldName()));
        }
        else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayPopupMessage(String pMessage) {
        final String lMessage =
                MESSAGES.errorUnsupportedProtocol().replace(TAG, pMessage);
        Application.INJECTOR.getErrorMessagePresenter().displayMessage(lMessage);
    }
}