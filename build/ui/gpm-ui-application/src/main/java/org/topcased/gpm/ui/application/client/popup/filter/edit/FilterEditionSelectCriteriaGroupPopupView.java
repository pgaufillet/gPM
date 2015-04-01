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

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import java.util.List;

import org.topcased.gpm.business.util.ChoiceDisplayHintType;
import org.topcased.gpm.ui.application.client.popup.PopupView;
import org.topcased.gpm.ui.component.client.container.field.GpmChoiceBoxValue;
import org.topcased.gpm.ui.component.client.container.field.GpmRadioBox;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * View for saving a filter on a popup.
 * 
 * @author jlouisy
 */
public class FilterEditionSelectCriteriaGroupPopupView extends PopupView
        implements FilterEditionSelectCriteriaGroupPopupDisplay {

    private GpmRadioBox radioBox;

    private ButtonBase okButton;

    private HandlerRegistration okButtonHandler;

    /**
     * Create a product selection view.
     */
    public FilterEditionSelectCriteriaGroupPopupView() {
        super();
        radioBox = new GpmRadioBox(ChoiceDisplayHintType.LIST);
        okButton = addButton(CONSTANTS.ok());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSelectCriteriaGroupPopupDisplay#setAvailableGroups(java.util.List)
     */
    @Override
    public void setAvailableGroups(List<String> pAvailableGroups) {
        radioBox.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(pAvailableGroups));
        radioBox.setCategoryValue(pAvailableGroups.get(0));

        ScrollPanel lPanel = new ScrollPanel();
        lPanel.addStyleName(INSTANCE.css().gpmSmallBorder());
        lPanel.setWidget(radioBox.getWidget());
        setContent(lPanel);
    }

    @Override
    public String getSelectedGroup() {
        return radioBox.getCategoryValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSelectCriteriaGroupPopupDisplay#setOkButtonHandler(com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void setOkButtonHandler(ClickHandler pClickHandler) {
        if (okButtonHandler != null) {
            okButtonHandler.removeHandler();
        }
        okButtonHandler = okButton.addClickHandler(pClickHandler);
    }

}