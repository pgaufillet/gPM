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
import java.util.List;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.AddCriterionAction;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.FilterEditionAddCriteriaGroupCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;

import com.google.inject.Inject;

/**
 * The presenter for the FilterEditionView.
 * 
 * @author jlouisy
 */
public class FilterEditionSelectCriteriaGroupPopupPresenter extends
        PopupPresenter<FilterEditionSelectCriteriaGroupPopupDisplay> {

    private static final String AND = " (AND)";

    private static final String GROUP = "Group ";

    private static final String NEW_GROUP = "New Group (OR)";

    private AddCriterionAction addCriterionAction;

    /**
     * Create a presenter for the FilterView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pFilterEditionAddCriteriaGroupCommand
     *            Add listener to OK button.
     */
    @Inject
    public FilterEditionSelectCriteriaGroupPopupPresenter(
            final FilterEditionSelectCriteriaGroupPopupDisplay pDisplay,
            final EventBus pEventBus,
            FilterEditionAddCriteriaGroupCommand pFilterEditionAddCriteriaGroupCommand) {
        super(pDisplay, pEventBus);
        getDisplay().setOkButtonHandler(pFilterEditionAddCriteriaGroupCommand);
        getDisplay().setHeaderText("Select group to add criterion in");
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
     * Set available groups
     * 
     * @param pCriteriaGroupsCount
     *            the amount of available groups
     */
    public void setAvailableGroups(int pCriteriaGroupsCount) {
        List<String> lGroups = new ArrayList<String>();
        lGroups.add(NEW_GROUP);
        for (int i = 1; i <= pCriteriaGroupsCount; i++) {
            lGroups.add(GROUP + i + AND);
        }
        getDisplay().setAvailableGroups(lGroups);
    }

    /**
     * Get currently selected criteria group
     * 
     * @return the currently selected group index
     */
    public int getSelectedCriteriaGroup() {
        String lGroup = getDisplay().getSelectedGroup();
        if (NEW_GROUP.equals(lGroup)) {
            return -1;
        }
        String lStringWithoutGroup = lGroup.substring(GROUP.length());
        int lIndexAnd = lStringWithoutGroup.indexOf(AND);
        return Integer.valueOf(lStringWithoutGroup.substring(0, lIndexAnd)) - 1;
    }

    /**
     * Set Add criterion action.
     * 
     * @param pAddCriterionAction
     *            add criterion action.
     */
    public void setAddCriterionAction(AddCriterionAction pAddCriterionAction) {
        addCriterionAction = pAddCriterionAction;
    }

    /**
     * Get Add criterion action.
     * 
     * @return add criterion action.
     */
    public AddCriterionAction getAddCriterionAction() {
        return addCriterionAction;
    }
}