/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.product;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import java.util.List;

import org.topcased.gpm.ui.application.client.popup.PopupView;
import org.topcased.gpm.ui.component.client.container.GpmFormPanel;
import org.topcased.gpm.ui.component.client.container.field.GpmListShifterSelector;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * View for select environment(s) for product creation.
 * 
 * @author nveillet
 */
public class SelectEnvironmentsView extends PopupView implements
        SelectEnvironmentsDisplay {

    private final static int HEIGHT = 300;

    private final static int NB_FIELDS = 1;

    private final static int WIDTH = 700;

    private static final String TEXT_TITLE =
            CONSTANTS.selectEnvironmentsTitle();

    private final GpmListShifterSelector<String> environments;

    private final GpmFormPanel form;

    private final Button selectButton;

    private HandlerRegistration selectButtonRegistration;

    /**
     * Create a select environment(s) view.
     */
    public SelectEnvironmentsView() {
        super(TEXT_TITLE);
        final ScrollPanel lPanel = new ScrollPanel();

        // Build form
        form = new GpmFormPanel(NB_FIELDS);
        form.addStyleName(ComponentResources.INSTANCE.css().gpmBigBorder());

        // Environments field
        environments = new GpmListShifterSelector<String>(false, false);
        form.addField(CONSTANTS.environments(), environments.getWidget());

        lPanel.addStyleName(ComponentResources.INSTANCE.css().gpmSmallBorder());
        lPanel.setWidget(form);

        selectButton = addButton(CONSTANTS.select());

        setContent(lPanel);

        setPixelSize(WIDTH, HEIGHT);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.Panel#clear()
     */
    @Override
    public void clear() {
        environments.clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.product.SelectEnvironmentsDisplay#getSelectedEnvironments()
     */
    @Override
    public List<String> getSelectedEnvironments() {
        return environments.getSelectedValues();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.product.SelectEnvironmentsDisplay#setEnvironments(java.util.List)
     */
    @Override
    public void setEnvironments(List<String> pEnvironmentNames) {
        environments.setSelectableValues(pEnvironmentNames);
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
