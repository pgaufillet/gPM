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

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.command.popup.attribute.UpdateAttributesCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;
import org.topcased.gpm.ui.application.shared.command.attribute.GetAttributesEditionResult;
import org.topcased.gpm.ui.facade.shared.attribute.UiAttribute;
import org.topcased.gpm.ui.facade.shared.attribute.UiAttributeContainer;

import com.google.inject.Inject;

/**
 * The presenter for the AttributesEditionView.
 * 
 * @author nveillet
 */
public class AttributesEditionPresenter extends
        PopupPresenter<AttributesEditionDisplay> {

    private UiAttributeContainer attributeContainer;

    private String elementId;

    private String productName;

    private final UpdateAttributesCommand updateAttributes;

    /**
     * Create a presenter for the AttributesView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pUpdateAttributes
     *            The update attributes command.
     */
    @Inject
    public AttributesEditionPresenter(AttributesEditionDisplay pDisplay,
            EventBus pEventBus, UpdateAttributesCommand pUpdateAttributes) {
        super(pDisplay, pEventBus);
        updateAttributes = pUpdateAttributes;
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
     * get attributes
     * 
     * @return the attributes
     */
    public List<UiAttribute> getAttributes() {
        List<UiAttribute> lAttributes = new ArrayList<UiAttribute>();

        for (Entry<String, List<String>> lAttribute : getDisplay().getAttributes().entrySet()) {
            lAttributes.add(new UiAttribute(lAttribute.getKey(),
                    lAttribute.getValue()));
        }

        return lAttributes;
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
    public void init(GetAttributesEditionResult pResult) {
        getDisplay().clear();

        productName = pResult.getProductName();
        attributeContainer = pResult.getAttributeContainer();
        elementId = pResult.getElementId();

        for (UiAttribute lAttribute : pResult.getAttributes()) {
            getDisplay().addAttribute(lAttribute.getName(),
                    lAttribute.getValues());
        }

        getDisplay().setSaveButtonHandler(updateAttributes);
    }
}
