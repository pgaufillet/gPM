/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.dictionary.listing;

import java.util.ArrayList;
import java.util.List;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.admin.dictionary.OpenDictionaryCategoryCommand;
import org.topcased.gpm.ui.application.client.common.AbstractPresenter;
import org.topcased.gpm.ui.component.client.container.field.GpmChoiceBoxValue;
import org.topcased.gpm.ui.facade.shared.dictionary.UiCategory;

import com.google.inject.Inject;

/**
 * The presenter for the DictionaryListingView.
 * 
 * @author nveillet
 */
public class DictionaryListingPresenter extends
        AbstractPresenter<DictionaryListingDisplay> {

    private OpenDictionaryCategoryCommand openCategory;

    /**
     * Create a presenter for the DictionaryListingView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pOpenCategory
     *            The command for open a category.
     */
    @Inject
    public DictionaryListingPresenter(DictionaryListingDisplay pDisplay,
            EventBus pEventBus, OpenDictionaryCategoryCommand pOpenCategory) {
        super(pDisplay, pEventBus);
        openCategory = pOpenCategory;
    }

    /**
     * Clear view
     */
    public void clear() {
        getDisplay().clear();
    }

    /**
     * get the selected category name
     * 
     * @return the category name
     */
    public String getSelectedCategory() {
        return getDisplay().getCategory().getCategoryValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    @Override
    protected void onBind() {
        getDisplay().setCategoryHandler(openCategory);
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
     * Set the available categories
     * 
     * @param pCategories
     *            the categories
     */
    public void setCategories(List<UiCategory> pCategories) {
        List<GpmChoiceBoxValue> lCategories =
                new ArrayList<GpmChoiceBoxValue>();
        lCategories.add(new GpmChoiceBoxValue("", ""));
        for (UiCategory lCategory : pCategories) {
            lCategories.add(new GpmChoiceBoxValue(lCategory.getName(),
                    lCategory.getTranslatedName()));
        }
        getDisplay().getCategory().setPossibleValues(lCategories);
    }
}
