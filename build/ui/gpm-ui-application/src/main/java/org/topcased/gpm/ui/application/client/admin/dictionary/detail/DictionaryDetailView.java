/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.dictionary.detail;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import org.topcased.gpm.ui.application.client.util.CollectionUtil;
import org.topcased.gpm.ui.application.client.util.validation.DuplicateValueRule;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;
import org.topcased.gpm.ui.component.client.container.field.multivalued.GpmMultivaluedField;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;
import org.topcased.gpm.ui.component.client.util.validation.IRule;
import org.topcased.gpm.ui.component.client.util.validation.Validator;

/**
 * View for a set of dictionary category displayed on the dictionary management
 * space.
 * 
 * @author nveillet
 * @author phtsaan
 */
public class DictionaryDetailView extends AbstractDictionaryDetailView
        implements DictionaryDetailDisplay {

    private GpmMultivaluedField<GpmTextBox<String>> categoryValues;

    private final Validator validator;
    
    private static final int CATEGORY_VALUE_FIELD_SIZE = 100;

    /**
     * Create a detail view for users.
     */
    public DictionaryDetailView() {
        super();

        GpmTextBox<String> lCategoryValueField =
                new GpmTextBox<String>(GpmStringFormatter.getInstance());

        lCategoryValueField.setSize(CATEGORY_VALUE_FIELD_SIZE);

        categoryValues =
                new GpmMultivaluedField<GpmTextBox<String>>(
                        lCategoryValueField, true, true, true);
        categoryValues.setFieldName(CONSTANTS.adminDictionaryFieldCategoryValues());

        getFormPanel().addField(categoryValues.getFieldName(),
                categoryValues.getWidget());

        clear();

        // create and initialize validator
        validator = new Validator();
        validator.addValidation((AbstractGpmField<?>) getCategoryValues(),
                CollectionUtil.singleton((IRule) new DuplicateValueRule()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.Panel#clear()
     */
    @Override
    public void clear() {
        super.clear();
        categoryValues.getWidget().removeAll();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.dictionary.detail.DictionaryDetailDisplay#getCategoryValues()
     */
    @Override
    public GpmMultivaluedField<GpmTextBox<String>> getCategoryValues() {
        return categoryValues;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.dictionary.DictionaryDetailDisplay#validate()
     */
    @Override
    public String validate() {
        return validator.validate();
    }
}