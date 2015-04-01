/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.filter.edit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.FilterEditionSaveCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;
import org.topcased.gpm.ui.application.client.util.validation.MandatoryRule;
import org.topcased.gpm.ui.application.shared.command.filter.edit.PreSaveFilterResult;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;
import org.topcased.gpm.ui.component.client.util.validation.IRule;
import org.topcased.gpm.ui.component.client.util.validation.Validator;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterUsage;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterVisibility;

import com.google.inject.Inject;

/**
 * The presenter for the FilterEditionView.
 * 
 * @author jlouisy
 */
public class FilterEditionSavePopupPresenter extends
        PopupPresenter<FilterEditionSavePopupDisplay> {

    private boolean isPreSavePanelLoaded;

    private FilterEditionSaveCommand filterEditionSaveCommand;

    private Validator validator;

    private ArrayList<IRule> rules;

    private final static Map<UiFilterVisibility, String> VISIBILITY_TRANSLATIONS =
            new HashMap<UiFilterVisibility, String>();

    private final static Map<UiFilterUsage, String> USAGE_TRANSLATIONS =
            new HashMap<UiFilterUsage, String>();

    static {
        VISIBILITY_TRANSLATIONS.put(UiFilterVisibility.INSTANCE,
                Ui18n.CONSTANTS.filterEditionSaveVisibilityInstance());
        VISIBILITY_TRANSLATIONS.put(UiFilterVisibility.PRODUCT,
                Ui18n.CONSTANTS.filterEditionSaveVisibilityProduct());
        VISIBILITY_TRANSLATIONS.put(UiFilterVisibility.USER,
                Ui18n.CONSTANTS.filterEditionSaveVisibilityUser());

        USAGE_TRANSLATIONS.put(UiFilterUsage.BOTH_VIEWS,
                Ui18n.CONSTANTS.filterEditionSaveUsageBoth());
        USAGE_TRANSLATIONS.put(UiFilterUsage.TABLE_VIEW,
                Ui18n.CONSTANTS.filterEditionSaveUsageTable());
        USAGE_TRANSLATIONS.put(UiFilterUsage.TREE_VIEW,
                Ui18n.CONSTANTS.filterEditionSaveUsageTree());
    }

    /**
     * Create a presenter for the FilterView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pFilterEditionSaveCommand
     *            Command for filter saving.
     */
    @Inject
    public FilterEditionSavePopupPresenter(
            final FilterEditionSavePopupDisplay pDisplay,
            final EventBus pEventBus,
            FilterEditionSaveCommand pFilterEditionSaveCommand) {
        super(pDisplay, pEventBus);
        filterEditionSaveCommand = pFilterEditionSaveCommand;
        validator = new Validator();
        rules = new ArrayList<IRule>();
    }

    /**
     * Give useful info to the view.
     * 
     * @param pResult
     *            Result of presave command.
     */
    public void displayPreSave(PreSaveFilterResult pResult) {
        List<Translation> lVisibilityList = new ArrayList<Translation>();
        for (UiFilterVisibility lUiFilterVisibility : pResult.getAvailableVisibilities()) {
            lVisibilityList.add(new Translation(lUiFilterVisibility.getKey(),
                    VISIBILITY_TRANSLATIONS.get(lUiFilterVisibility)));
        }
        UiFilterVisibility lVisibility = pResult.getFilterVisibility();
        if (lVisibility == null) {
            lVisibility = UiFilterVisibility.INSTANCE;
        }
        getDisplay().setVisibility(lVisibility.getKey(), lVisibilityList);

        List<Translation> lUsageList = new ArrayList<Translation>();
        for (UiFilterUsage lUiFilterUsage : UiFilterUsage.values()) {
            lUsageList.add(new Translation(lUiFilterUsage.getKey(),
                    USAGE_TRANSLATIONS.get(lUiFilterUsage)));
        }

        UiFilterUsage lUsage = pResult.getFilterUsage();
        if (lUsage == null) {
            lUsage = UiFilterUsage.TABLE_VIEW;
        }
        getDisplay().setUsage(lUsage.getKey(), lUsageList);

        getDisplay().setName(pResult.getFilterName());
        getDisplay().setDescription(pResult.getFilterDescription());
        getDisplay().setHidden(pResult.isHidden());

        getDisplay().display();

        isPreSavePanelLoaded = true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.PopupPresenter#getClosePopupEvent()
     */
    @Override
    protected GlobalEvent<ClosePopupAction> getClosePopupEvent() {
        return GlobalEvent.CLOSE_FILTER_POPUP;
    }

    /**
     * Has presave panel been loaded already?
     * 
     * @return True if panel has been loaded.
     */
    public boolean isPreSavePanelLoaded() {
        return isPreSavePanelLoaded;
    }

    /**
     * Reset view.
     */
    public void reset() {
        rules.clear();
        rules.add(new MandatoryRule());
        validator.addValidation(getDisplay().getNameField(), rules);
        isPreSavePanelLoaded = false;
        getDisplay().setSaveButtonHandler(filterEditionSaveCommand, true);
    }

    /**
     * Build UiFilter.
     * 
     * @param pFilter
     *            The filter to fill attributes in.
     */
    public void setFilterAttributes(UiFilter pFilter) {
        pFilter.setName(getDisplay().getName());
        pFilter.setDescription(getDisplay().getDescription());
        pFilter.setUsage(UiFilterUsage.fromKey(getDisplay().getUsage()));
        pFilter.setVisibility(UiFilterVisibility.fromKey(getDisplay().getVisibility()));
        if (getDisplay().getHidden() == null) {
            pFilter.setHidden(false);
        }
        else {
            pFilter.setHidden(getDisplay().getHidden());
        }
    }

    /**
     * Validate data
     * 
     * @return true if data is validated.
     */
    public boolean validate() {
        String lMessage = validator.validate();
        boolean lOK = (lMessage == null);
        if (!lOK) {
            Application.INJECTOR.getErrorMessagePresenter().displayMessage(
                    lMessage);
        }
        return lOK;
    }

}