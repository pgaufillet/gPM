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

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.util.FieldType;
import org.topcased.gpm.business.util.StringDisplayHintType;
import org.topcased.gpm.ui.component.client.util.validation.IRule;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.container.field.multivalued.UiMultivaluedField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiStringField;

/**
 * RuleManager manages the rules creation according to the fields properties.
 * <p>
 * to create a rule, only this manager must be used.
 * </p>
 * 
 * @author mkargbo
 */
public class RuleManager {

    /**
     * Build the rules for the specified field.
     * 
     * @param pField
     *            Business field.
     * @return Get the available rules for the field.
     */
    public static List<IRule> getRules(final UiField pField) {
        if (pField instanceof UiMultivaluedField) {
            return handleMultivalued(pField);
        }
        else {
            return handleSimple(pField);
        }
    }

    /**
     * Build the rules for simple fields.
     * 
     * @param pField
     *            UI Field
     * @return Available rules for the simple field.
     */
    private static List<IRule> handleSimple(final UiField pField) {
        final List<IRule> lRules = new ArrayList<IRule>();
        if (pField.isMandatory()) {
            if (FieldType.STRING.equals(pField.getFieldType())) {
                if (!StringDisplayHintType.INTERNAL_URL.equals(((UiStringField) pField).getStringDisplayHintType())) {
                    lRules.add(new MandatoryRule());
                }
            }
            else {
                lRules.add(new MandatoryRule());
            }
        }

        switch (pField.getFieldType()) {
            case STRING:
                final UiStringField lStringField = (UiStringField) pField;
                switch (lStringField.getStringDisplayHintType()) {
                    case SINGLE_LINE:
                    case MULTI_LINE:
                    case INTERNAL_URL:
                    case RICH_TEXT:
                        lRules.add(new SizeRule(
                                ((UiStringField) pField).getSize()));
                        break;
                    case EXTERNAL_WEB_CONTENT:

                    case URL:
                        lRules.add(new SizeRule(
                                ((UiStringField) pField).getSize()));
                        lRules.add(new UrlRule());
                        break;
                    default:
                        //Do nothing
                }
                break;
            case INTEGER:
                lRules.add(new IntegerRule());
                break;
            case REAL:
                lRules.add(new RealRule());
                break;
            case DATE:
                lRules.add(new DateRule());
                break;
            default:
                //Do nothing
        }
        return lRules;
    }

    /**
     * Build rules for multivalued fields.
     * 
     * @param pField
     *            Multivalued field
     * @return Available rules for the multivalued field
     */
    private static List<IRule> handleMultivalued(final UiField pField) {
        UiMultivaluedField lMultivaluedField = (UiMultivaluedField) pField;
        return getRules(lMultivaluedField.getTemplateField());
    }
}
