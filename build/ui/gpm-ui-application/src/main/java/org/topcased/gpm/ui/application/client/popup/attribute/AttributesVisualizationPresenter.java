/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.attribute;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;
import org.topcased.gpm.ui.application.shared.command.attribute.GetAttributesVisualizationResult;
import org.topcased.gpm.ui.facade.shared.attribute.UiAttribute;
import org.topcased.gpm.ui.facade.shared.attribute.UiAttributeContainer;

import com.google.inject.Inject;

/**
 * The presenter for the AttributesVisualizationView.
 * 
 * @author nveillet
 */
public class AttributesVisualizationPresenter extends
        PopupPresenter<AttributesVisualizationDisplay> {

    private UiAttributeContainer attributeContainer;

    private String elementId;

    private String productName;

    /**
     * Create a presenter for the AttributesView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     */
    @Inject
    public AttributesVisualizationPresenter(
            AttributesVisualizationDisplay pDisplay, EventBus pEventBus) {
        super(pDisplay, pEventBus);
    }

    /**
     * get attributeContainer
     * 
     * @return the attributeContainer
     */
    public UiAttributeContainer getAttributeContainer() {
        return attributeContainer;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.PopupPresenter#getClosePopupEvent()
     */
    @Override
    protected GlobalEvent<ClosePopupAction> getClosePopupEvent() {
        return GlobalEvent.CLOSE_ATTRIBUTES_POPUP;
    }

    /**
     * get elementId
     * 
     * @return the elementId
     */
    public String getElementId() {
        return elementId;
    }

    /**
     * get productName
     * 
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Initialize the view
     * 
     * @param pResult
     *            the result
     */
    public void init(GetAttributesVisualizationResult pResult) {
        getDisplay().clear();

        productName = pResult.getProductName();
        attributeContainer = pResult.getAttributeContainer();
        elementId = pResult.getElementId();

        for (UiAttribute lAttribute : pResult.getAttributes()) {
            getDisplay().addAttribute(lAttribute.getName(),
                    lAttribute.getValues());
        }
    }
}
