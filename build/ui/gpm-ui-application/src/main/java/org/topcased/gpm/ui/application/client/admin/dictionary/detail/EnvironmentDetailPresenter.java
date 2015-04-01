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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.action.ActionName;
import org.topcased.gpm.ui.application.client.common.AbstractPresenter;
import org.topcased.gpm.ui.application.client.menu.admin.dictionary.EnvironmentCategoryEditionMenuBuilder;
import org.topcased.gpm.ui.component.client.container.field.GpmListShifterSelector;
import org.topcased.gpm.ui.facade.shared.action.UiAction;

import com.google.inject.Inject;

/**
 * The presenter for the EnvironmentDetailView.
 * 
 * @author nveillet
 */
public class EnvironmentDetailPresenter extends
        AbstractPresenter<EnvironmentDetailDisplay> {

    private String categoryName;

    private final List<String> categoryValues;

    private final EnvironmentCategoryEditionMenuBuilder menuBuilder;

    /**
     * Create a presenter for the DictionaryDetailView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pMenuBuilder
     *            The menu builder.
     */
    @Inject
    public EnvironmentDetailPresenter(EnvironmentDetailDisplay pDisplay,
            EventBus pEventBus,
            final EnvironmentCategoryEditionMenuBuilder pMenuBuilder) {
        super(pDisplay, pEventBus);
        menuBuilder = pMenuBuilder;
        categoryValues = new ArrayList<String>();
    }

    /**
     * Clear view
     */
    public void clear() {
        getDisplay().clear();
        categoryName = null;
        categoryValues.clear();
    }

    /**
     * get category name
     * 
     * @return the category name
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * get the category values
     * 
     * @return the category values
     */
    public List<String> getCategoryValues() {
        return getDisplay().getCategoryValues().getSelectedValues();
    }

    /**
     * Determine if values have changed
     * 
     * @return if values have changed
     */
    public boolean hasChangedValues() {
        List<String> lCategoryValues = getCategoryValues();

        // The same of number of line is need
        if (lCategoryValues.size() != categoryValues.size()) {
            return true;
        }
        // All elements must have same values
        for (int i = 0; i < lCategoryValues.size(); i++) {
            if (!lCategoryValues.get(i).equals(categoryValues.get(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    @Override
    protected void onBind() {
        Map<String, UiAction> lActions = new HashMap<String, UiAction>();
        lActions.put(ActionName.ENVIRONMENT_CATEGORY_SAVE, new UiAction(
                ActionName.ENVIRONMENT_CATEGORY_SAVE));
        getDisplay().setToolBar(menuBuilder.buildMenu(lActions, null));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onUnbind()
     */
    @Override
    protected void onUnbind() {
        // Do nothing
    }

    /**
     * Set the available category values
     * 
     * @param pValues
     *            the values
     */
    public void setAvailableCategoryValues(List<String> pValues) {
        GpmListShifterSelector<String> lCategoryValues =
                getDisplay().getCategoryValues();

        lCategoryValues.setSelectableValues(pValues);
    }

    /**
     * Set the category name
     * 
     * @param pCategoryName
     *            the category name
     */
    public void setCategoryName(String pCategoryName) {
        categoryName = pCategoryName;
        getDisplay().setName(categoryName);
    }

    /**
     * Set the category values
     * 
     * @param pValues
     *            the values
     */
    public void setCategoryValues(List<String> pValues) {
        categoryValues.clear();
        categoryValues.addAll(pValues);

        GpmListShifterSelector<String> lCategoryValues =
                getDisplay().getCategoryValues();

        lCategoryValues.setSelectedValues(categoryValues);
    }
}
