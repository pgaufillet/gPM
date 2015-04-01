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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.action.ActionName;
import org.topcased.gpm.ui.application.client.common.AbstractPresenter;
import org.topcased.gpm.ui.application.client.menu.admin.dictionary.DictionaryCategoryEditionMenuBuilder;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;
import org.topcased.gpm.ui.component.client.container.field.multivalued.GpmMultivaluedField;
import org.topcased.gpm.ui.facade.shared.action.UiAction;

import com.google.inject.Inject;

/**
 * The presenter for the DictionaryDetailView.
 * 
 * @author nveillet
 * @author phtsaan
 */
public class DictionaryDetailPresenter extends
        AbstractPresenter<DictionaryDetailDisplay> {

    private String categoryName;

    private final List<String> categoryValues;

    private final DictionaryCategoryEditionMenuBuilder menuBuilder;

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
    public DictionaryDetailPresenter(DictionaryDetailDisplay pDisplay,
            EventBus pEventBus,
            final DictionaryCategoryEditionMenuBuilder pMenuBuilder) {
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
        ArrayList<String> lValues = new ArrayList<String>();

        for (GpmTextBox<String> lCategoryValue : getDisplay().getCategoryValues()) {
            lValues.add(lCategoryValue.get());
        }

        return lValues;
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
        // Create actions
        Map<String, UiAction> lActions = new HashMap<String, UiAction>();
        lActions.put(ActionName.DICTIONARY_CATEGORY_SAVE, new UiAction(
                ActionName.DICTIONARY_CATEGORY_SAVE));
        lActions.put(ActionName.DICTIONARY_CATEGORY_SORT, new UiAction(
                ActionName.DICTIONARY_CATEGORY_SORT));

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

        GpmMultivaluedField<GpmTextBox<String>> lCategoryValues =
                getDisplay().getCategoryValues();

        lCategoryValues.getWidget().removeAll();

        for (String lValue : categoryValues) {
            GpmTextBox<String> lCategoryValue = lCategoryValues.addLine();
            lCategoryValue.set(lValue);
        }
    }

    /**
     * Sort category values
     */
    public void sortValues() {
        List<String> lValues = getCategoryValues();
        Collections.sort(lValues);

        GpmMultivaluedField<GpmTextBox<String>> lCategoryValues =
                getDisplay().getCategoryValues();
        lCategoryValues.getWidget().removeAll();

        for (String lValue : lValues) {
            GpmTextBox<String> lCategoryValue = lCategoryValues.addLine();
            lCategoryValue.set(lValue);
        }
    }

    /**
     * Validate view
     * 
     * @return the validation message
     */
    public String validate() {
        return getDisplay().validate();
    }

}
