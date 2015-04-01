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

import java.util.ArrayList;

import org.topcased.gpm.ui.component.client.container.field.GpmListShifterSelector;
import org.topcased.gpm.ui.component.client.container.field.GpmListShifterSelector.ShiftMode;

/**
 * View for a set of environment category displayed on the dictionary management
 * space.
 * 
 * @author nveillet
 */
public class EnvironmentDetailView extends AbstractDictionaryDetailView
        implements EnvironmentDetailDisplay {

    private GpmListShifterSelector<String> categoryValues;

    /**
     * Create a detail view for users.
     */
    public EnvironmentDetailView() {
        super();

        categoryValues =
                new GpmListShifterSelector<String>(true, false,
                        ShiftMode.SHIFT_WITH_BUTTON);
        categoryValues.setFieldName(CONSTANTS.adminDictionaryFieldCategoryValues());

        getFormPanel().addField(categoryValues.getFieldName(),
                categoryValues.getWidget());
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.Panel#clear()
     */
    @Override
    public void clear() {
        super.clear();
        categoryValues.clear();
        categoryValues.setSelectableValues(new ArrayList<String>());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.dictionary.detail.DictionaryDetailDisplay#getCategoryValues()
     */
    @Override
    public GpmListShifterSelector<String> getCategoryValues() {
        return categoryValues;
    }
}