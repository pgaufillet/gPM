/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.extended;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import org.topcased.gpm.ui.application.client.popup.PopupView;
import org.topcased.gpm.ui.component.client.container.FieldCreationHandler;
import org.topcased.gpm.ui.component.client.container.GpmDisplayGroupPanel;
import org.topcased.gpm.ui.component.client.container.GpmFieldSet;
import org.topcased.gpm.ui.component.client.container.GpmValuesContainerPanel;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * View for select a product on a popup.
 * 
 * @author tpanuel
 */
public class InputDataView extends PopupView implements InputDataDisplay {
    private final static double RATIO_WIDTH = 0.6;

    private final static double RATIO_HEIGHT = 0.6;

    private final GpmValuesContainerPanel valuesContainerPanel;

    private final Button executeButton;

    private HandlerRegistration executeButtonRegistration;

    /**
     * Create a product selection view.
     */
    public InputDataView() {
        super();
        final ScrollPanel lPanel = new ScrollPanel();

        valuesContainerPanel = new GpmValuesContainerPanel();

        lPanel.addStyleName(INSTANCE.css().gpmSmallBorder());
        lPanel.setWidget(valuesContainerPanel);

        executeButton = addButton(CONSTANTS.execute());

        setContent(lPanel);

        setRatioSize(RATIO_WIDTH, RATIO_HEIGHT);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.extended.InputDataDisplay#addFieldGroup(java.lang.String,
     *      org.topcased.gpm.ui.component.client.container.FieldCreationHandler,
     *      boolean)
     */
    @Override
    public void addFieldGroup(final String pGroupName,
            final FieldCreationHandler pHandler, final boolean pIsOpen) {
        final GpmDisplayGroupPanel lGroup =
                valuesContainerPanel.addFieldGroup(pGroupName);

        lGroup.setFieldCreationHandler(pHandler);
        if (pIsOpen) {
            lGroup.open();
        }
        else {
            lGroup.close();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.extended.InputDataDisplay#clearGroups()
     */
    @Override
    public void clearGroups() {
        valuesContainerPanel.clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.extended.InputDataDisplay#getInputDataFieldSet()
     */
    @Override
    public GpmFieldSet getInputDataFieldSet() {
        return valuesContainerPanel;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.extended.InputDataDisplay#setExecuteButtonHandler(com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void setExecuteButtonHandler(final ClickHandler pHandler) {
        // Remove old click handler : if exist
        if (executeButtonRegistration != null) {
            executeButtonRegistration.removeHandler();
        }
        executeButtonRegistration = executeButton.addClickHandler(pHandler);
    }
}