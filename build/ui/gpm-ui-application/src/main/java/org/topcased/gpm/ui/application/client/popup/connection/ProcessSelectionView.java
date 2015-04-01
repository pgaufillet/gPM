/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.connection;

import java.util.List;

import org.topcased.gpm.business.util.ChoiceDisplayHintType;
import org.topcased.gpm.ui.application.client.popup.PopupView;
import org.topcased.gpm.ui.component.client.container.field.GpmChoiceBoxValue;
import org.topcased.gpm.ui.component.client.container.field.GpmRadioBox;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * View for select a process on a popup.
 * 
 * @author nveillet
 */
public class ProcessSelectionView extends PopupView implements
        ProcessSelectionDisplay {

    private final static int HEIGHT = 200;

    private final static int WIDTH = 200;

    private final GpmRadioBox process;

    private final Button selectButton;

    private HandlerRegistration selectButtonRegistration;

    /**
     * Create a select process view.
     */
    public ProcessSelectionView() {
        // Popup that can not be close.
        super(false);
        setAutoHideEnabled(false);

        // Header
        setHeaderText(Ui18n.CONSTANTS.selectProcessTitle());

        final ScrollPanel lPanel = new ScrollPanel();
        final SimplePanel lForm = new SimplePanel();

        lForm.addStyleName(ComponentResources.INSTANCE.css().gpmBigBorder());

        // Process
        process = new GpmRadioBox(ChoiceDisplayHintType.NOT_LIST);
        lForm.setWidget(process.getWidget());

        lPanel.addStyleName(ComponentResources.INSTANCE.css().gpmSmallBorder());
        lPanel.setWidget(lForm);

        selectButton = addButton(Ui18n.CONSTANTS.select());

        setContent(lPanel);

        setPixelSize(WIDTH, HEIGHT);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.connection.ProcessSelectionDisplay#getSelectedProcess()
     */
    @Override
    public String getSelectedProcess() {
        return process.getCategoryValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.connection.ProcessSelectionDisplay#setProcess(java.util.List)
     */
    @Override
    public void setProcess(List<String> pProcessNames) {
        process.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(pProcessNames));
        process.setCategoryValue(pProcessNames.get(0));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.product.SelectEnvironmentsDisplay#setSelectButtonHandler(com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void setSelectButtonHandler(ClickHandler pHandler) {
        // Remove old click handler : if exist
        if (selectButtonRegistration != null) {
            selectButtonRegistration.removeHandler();
        }
        selectButtonRegistration = selectButton.addClickHandler(pHandler);
    }
}
