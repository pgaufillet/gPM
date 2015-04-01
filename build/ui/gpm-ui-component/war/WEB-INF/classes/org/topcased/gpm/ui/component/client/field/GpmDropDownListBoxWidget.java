/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.topcased.gpm.ui.component.client.field.formater.GpmFormatter;
import org.topcased.gpm.ui.component.client.layout.GpmHorizontalPanel;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * GpmListBoxWidget is a TextBox matched with a list. <br />
 * The selection of an item in the list set the associated value to the text
 * box. Each item has three values:
 * <ul>
 * <li>the display text value, displayed in the text box when selected</li>
 * <li>the select text, displayed in the</li>
 * <li>the Object value</li>
 * 
 * @param <T>
 *            The type of the object value of each element
 * @author frosier
 */
public class GpmDropDownListBoxWidget<T> extends GpmHorizontalPanel implements
        ClickHandler, HasValue<T> {
    private static final ImageResource SHOW_BUTTON_DEFAULT_URL =
            INSTANCE.images().hide();

    private static final ImageResource SHOW_BUTTON_READONLY_DEFAULT_URL =
            INSTANCE.images().menuHide();

    private static final String STYLE_DROP_DOWN_LIST_BOX_WIDGET =
            ComponentResources.INSTANCE.css().gpmDropDownListBoxWidget();

    private static final String STYLE_TEXT_BOX =
            ComponentResources.INSTANCE.css().gpmDropDownText();

    private static final String STYLE_TEXT_BOX_READONLY =
            ComponentResources.INSTANCE.css().gpmDropDownTextReadOnly();

    private static final String STYLE_BUTTON_READONLY =
            ComponentResources.INSTANCE.css().gpmDropDownButtonReadOnly();

    private static final String STYLE_LIST_BOX_BUTTON =
            ComponentResources.INSTANCE.css().gpmDropDownButton();

    private static final String STYLE_LIST_POPUP =
            ComponentResources.INSTANCE.css().gpmDropDownListPopup();

    private static final String STYLE_LIST_POPUP_CONTENT =
            ComponentResources.INSTANCE.css().gpmPopupContent();

    private static final String STYLE_LIST_POPUP_ELEMENT =
            ComponentResources.INSTANCE.css().gpmListPopupElement();

    private static final String STYLE_LIST_POPUP_ELEMENT_HOVER =
            ComponentResources.INSTANCE.css().gpmListPopupElementHover();

    private static final String STYLE_LIST_POPUP_ELEMENT_SELECTED =
            ComponentResources.INSTANCE.css().gpmListPopupElementSelected();

    private static final int DEFAULT_POPUP_SIZE = 200;
    
    private static final int DEFAULT_PIXEL_LENGTH = 7;

    private final List<ValueChangeHandler<T>> valueChangeHandlers =
            new ArrayList<ValueChangeHandler<T>>();

    private final Widget textArea;

    private final boolean editable;

    private ListPopup listPopup;

    private final PushButton expandButton;

    private final ArrayList<ListPopupElement> items =
            new ArrayList<ListPopupElement>();

    private final GpmFormatter<T> formatter;

    private T selectedValue;

    private T previousValue;

    /**
     * Creates an empty list box widget. The formatter behavior is only
     * implemented for StringFormatter, no error handling is done, meaning that
     * if widget T is different from String, the value set when parsing fails
     * will be null.
     * 
     * @param pFormatter
     *            The formatter that will be used to parse user entries
     */
    public GpmDropDownListBoxWidget(GpmFormatter<T> pFormatter) {
        super();

        editable = true;
        formatter = pFormatter;

        textArea = new TextBox();

        textArea.addStyleName(ComponentResources.INSTANCE.css().gpmTextArea());

        ((TextBox) textArea).addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent pEvent) {
                selectedValue = formatter.parse(((TextBox) textArea).getText());
            }
        });
        expandButton = new PushButton(new Image(SHOW_BUTTON_DEFAULT_URL));

        // Defines the onClick expand button. (onClick method)
        expandButton.addClickHandler(this);

        add(textArea);
        add(expandButton);

        setStylePrimaryName(STYLE_DROP_DOWN_LIST_BOX_WIDGET);
        textArea.addStyleName(STYLE_TEXT_BOX);
        expandButton.addStyleName(STYLE_LIST_BOX_BUTTON);
    }

    /**
     * Creates an empty list box widget. Widget will not be editable.
     */
    public GpmDropDownListBoxWidget() {
        super();

        editable = false;
        formatter = null;
        textArea = new HTML();
        expandButton =
                new PushButton(new Image(SHOW_BUTTON_READONLY_DEFAULT_URL));
        textArea.addStyleName(STYLE_TEXT_BOX_READONLY);
        textArea.addStyleName(ComponentResources.INSTANCE.css().gpmTextArea());
        expandButton.addStyleName(STYLE_BUTTON_READONLY);

        // Defines the onClick expand button. (onClick method)
        expandButton.addClickHandler(this);

        add(textArea);
        add(expandButton);

        setStylePrimaryName(STYLE_DROP_DOWN_LIST_BOX_WIDGET);
        textArea.addStyleName(STYLE_TEXT_BOX);
        expandButton.addStyleName(STYLE_LIST_BOX_BUTTON);
    }

    /**
     * Set items in the list popup.
     * 
     * @param pList
     *            The set of items.
     */
    public void setItems(final Collection<ListPopupElement> pList) {
        items.clear();
        addItems(pList);
    }

    /**
     * Add a set of items.
     * 
     * @param pList
     *            The set of items.
     */
    public void addItems(final Collection<ListPopupElement> pList) {
        items.addAll(pList);
    }

    /**
     * Add an item in the list popup values.
     * 
     * @param pItemLabel
     *            The text of the element when selected
     * @param pItemPopupLabel
     *            The text of the element when displayed in popup
     * @param pObject
     *            The object represented by the element
     */
    public void addItem(String pItemLabel, String pItemPopupLabel, T pObject) {
        items.add(new ListPopupElement(pItemLabel, pItemPopupLabel, pObject));
    }

    /**
     * get items
     * 
     * @return the items
     */
    public Collection<ListPopupElement> getItems() {
        return items;
    }

    /**
     * Get the list popup. Creates the popup instance, if the popup has not been
     * initilized yet.
     * 
     * @return The attached list popup.
     */
    private ListPopup getListPopup() {
        if (listPopup == null) {
            initListPopup();
        }
        if (textArea.getOffsetWidth() > 0 && !listPopup.isSized()) {
            initPopupSize();
        }

        return listPopup;
    }

    private void initListPopup() {
        // Defining of hide partners to ensure the popup don't hide with them.
        final Element[] lHidePartners = new Element[2];
        lHidePartners[0] = textArea.getElement();
        lHidePartners[1] = expandButton.getElement();
        listPopup = new ListPopup(items, lHidePartners);
    }

    /**
     * To initialize the size of the popup as regards the width of its attached
     * field.
     */
    private void initPopupSize() {
        int lWidth;
        if (editable) {
            lWidth =
                    (textArea.getOffsetWidth() + expandButton.getOffsetWidth());
        }
        else {
            lWidth = DEFAULT_POPUP_SIZE;
        }

        //Auto-adjust the drop down list size if an element is longer than the field size
        for (ListPopupElement lElement : items) {
            final int lElementWidth =
                    (lElement.getText().length()) * DEFAULT_PIXEL_LENGTH;

            if (lElementWidth > lWidth) {
                lWidth = lElementWidth;
            }
        }

        listPopup.getElement().setAttribute("style",
                "width: " + lWidth + Unit.PX.name() + ";");
        listPopup.setWidth(lWidth + Unit.PX.name());
        listPopup.setSized(true);
    }

    /**
     * Communicates on isDown state of the list popup.
     * 
     * @return <code>true</code> if the list popup is expanded.
     */
    public boolean isDown() {
        return getListPopup().isShowing();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(ClickEvent pEvent) {
        if (!isDown()) {
            // Build the list popup at first call.
            getListPopup().show(textArea);
        }
        else {
            getListPopup().hide();
        }
    }

    //********* Mapped methods with TextBox *********//
    /**
     * Get the value in the text box.
     * 
     * @return The text box value.
     */
    public T getValue() {
        return selectedValue;
    }

    /**
     * Enable disable field
     * 
     * @param pEnabled
     *            to disable
     */
    public void setEnabled(boolean pEnabled) {
        if (editable) {
            ((TextBox) textArea).setEnabled(pEnabled);
        }
        expandButton.setEnabled(pEnabled);
    }

    /**
     * Return the enable status of the widget
     * 
     * @return the enable status of the widget
     */
    public boolean isEnabled() {
        return editable && ((TextBox) textArea).isEnabled();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.HasValue#setValue(java.lang.Object)
     */
    @Override
    public void setValue(T pValue) {
        setValue(pValue, false);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.HasValue#setValue(java.lang.Object,
     *      boolean)
     */
    @Override
    public void setValue(T pValue, boolean pFireEvents) {
        previousValue = selectedValue;
        selectedValue = pValue;
        if (editable) {
            ((TextBox) textArea).setText(formatter.format(pValue));
        }
        getListPopup().setSelectedValue(pValue, pFireEvents);
        if (pFireEvents) {
            fireEvent(new ValueChangeEvent<T>(pValue) {
            });
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.logical.shared.HasValueChangeHandlers#addValueChangeHandler(com.google.gwt.event.logical.shared.ValueChangeHandler)
     */
    @Override
    public HandlerRegistration addValueChangeHandler(
            ValueChangeHandler<T> pHandler) {
        valueChangeHandlers.add(pHandler);
        return null;
    }

    /**
     * Reset the popup.
     */
    public void reset() {
        items.clear();
        listPopup = null;
    }

    /**
     * Undo previous value change (without sending ValueChange events)
     */
    public void rollBack() {
        setValue(previousValue, false);
    }

    /**
     * ListPopup
     * 
     * @param Type
     *            if the elements represented in the popup
     */
    private class ListPopup extends PopupPanel {

        private final FlowPanel listContainer;

        private boolean sized = false;

        private List<ListPopupElement> elements =
                new ArrayList<ListPopupElement>();

        /**
         * Mouse events that occur within an autoHide partner will not hide a
         * panel set to autoHide.
         */
        private final Element[] autoHidePartners;

        /**
         * Creates a list popup initilized with items and hide partners.
         * 
         * @param pItems
         *            The items to set in the list container.
         * @param pAutoHidePartners
         *            The hide partners of the list popup.
         */
        public ListPopup(final Collection<ListPopupElement> pItems,
                final Element[] pAutoHidePartners) {
            // Build in auto-hide mode.
            super(true);

            autoHidePartners = pAutoHidePartners;
            listContainer = new FlowPanel();

            add(listContainer);
            for (ListPopupElement lItem : pItems) {
                addElem(lItem);
            }

            setStylePrimaryName(STYLE_LIST_POPUP);

            // Scrolling definition is defined in the "popupContent" style name.
            setStylePrimaryName(getContainerElement(), STYLE_LIST_POPUP_CONTENT);

        }

        public void setSelectedValue(T pValue, boolean pCallHandlers) {
            for (ListPopupElement lElem : elements) {
                lElem.setSelected(lElem.getValueObject().equals(pValue),
                        pCallHandlers);
            }
        }

        /**
         * Add an element to the list popup.
         * 
         * @param pLabelText
         *            The text of the element when selected
         * @param pPopupText
         *            The text of the element when displayed in popup
         * @param pObject
         *            The object represented by the element
         */
        public void addElem(ListPopupElement pListPopupElement) {
            listContainer.add(pListPopupElement);
            elements.add(pListPopupElement);
        }

        /**
         * Show the popup relative to the UI object in parameter. Add all hide
         * partners initialized to the popup.
         * 
         * @param pObjectRelativeTo
         *            The objet to show relative to.
         */
        public void show(final UIObject pObjectRelativeTo) {
            super.showRelativeTo(pObjectRelativeTo);
            for (Element lAutoHidePartner : autoHidePartners) {
                addAutoHidePartner(lAutoHidePartner);
            }
        }

        /**
         * {@inheritDoc} Hide the popup. Remove all hide partners initialized
         * when showing the popup.
         * 
         * @see com.google.gwt.user.client.ui.PopupPanel#hide()
         */
        @Override
        public void hide() {
            for (Element lAutoHidePartner : autoHidePartners) {
                removeAutoHidePartner(lAutoHidePartner);
            }
            super.hide();
        }

        /**
         * Get the sized
         * 
         * @return if the list popup has been determined as sized.
         */
        public boolean isSized() {
            return sized;
        }

        /**
         * Inform the list popup is sized.
         * 
         * @param pSized
         *            The list popup sized state.
         */
        public void setSized(boolean pSized) {
            sized = pSized;
        }
    }

    /**
     * ListPopupElement defines an element of the list popup.
     * 
     * @param Type
     *            of the represented object
     */
    public class ListPopupElement extends HTML {
        /** Indicates if the element selected */
        private boolean selected = false;

        /** The text displayed in the popup for this element */
        private String labelText;

        /** The value object, returned as value of the selection */
        private T valueObject;

        /** The constant for space char in HTML, used for browser hack **/
        private static final String HTML_SPACE_CHARACTER = "&nbsp;";

        /**
         * Empty constructor.
         */
        private ListPopupElement() {
            super();
            sinkEvents(Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT);
            setStylePrimaryName(STYLE_LIST_POPUP_ELEMENT);
        }

        /**
         * Creates a list popup element initialized with the text item.
         * 
         * @param pLabelText
         *            The text of the element when selected
         * @param pPopupText
         *            The text of the element when displayed in popup
         * @param pObject
         *            The object represented by the element
         */
        public ListPopupElement(final String pLabelText,
                final String pPopupText, final T pObject) {
            this();
            labelText = pLabelText;
            valueObject = pObject;

            /* The following part is a hack for combo box item display
             * If the string of setHTML is empty, an empty div is added
             * but this div has no height and is not displayed in browsers: IE>6 & FF
             * Solution: add a space character to have a div with height
             */
            String lPopupText = pPopupText;
            if (pPopupText.trim().isEmpty()) {
                lPopupText = HTML_SPACE_CHARACTER;
            }
            // transform text in HTML for display
            setHTML(lPopupText);
        }

        /**
         * {@inheritDoc}
         * 
         * @see com.google.gwt.user.client.ui.Widget#onBrowserEvent(com.google.gwt.user.client.Event)
         */
        @Override
        public void onBrowserEvent(Event pEvent) {
            super.onBrowserEvent(pEvent);

            switch (DOM.eventGetType(pEvent)) {
                case Event.ONCLICK:
                    listPopup.hide();
                    setValue(valueObject, true); // Set the new value and fire event
                    break;
                case Event.ONMOUSEUP:
                case Event.ONMOUSEOVER:
                    addStyleName(STYLE_LIST_POPUP_ELEMENT_HOVER);
                    break;
                case Event.ONMOUSEOUT:
                    if (!isSelected()) {
                        removeStyleName(STYLE_LIST_POPUP_ELEMENT_HOVER);
                    }
                    break;
                default:
                    break;
            }
        }

        /**
         * Get the selected state of the element.
         * 
         * @return <code>true</code> if the element is selected.
         */
        public boolean isSelected() {
            return selected;
        }

        /**
         * Set the element as selected and affect correct style decoration.
         * 
         * @param pSelected
         *            The selected state.
         * @param pCallHandlers
         *            If the Value change handlers must be called or not
         */
        public void setSelected(boolean pSelected, boolean pCallHandlers) {
            final boolean lNeedPublishEvent =
                    (!selected && pSelected && pCallHandlers);

            selected = pSelected;
            if (selected) {
                addStyleName(STYLE_LIST_POPUP_ELEMENT_SELECTED);
                if (editable) {
                    ((TextBox) textArea).setText(labelText);
                }
                else {
                    ((HTML) textArea).setHTML(labelText);
                }
            }
            else {
                removeStyleName(STYLE_LIST_POPUP_ELEMENT_SELECTED);
                removeStyleName(STYLE_LIST_POPUP_ELEMENT_HOVER);
            }
            if (lNeedPublishEvent) {
                for (ValueChangeHandler<T> lHandler : valueChangeHandlers) {
                    lHandler.onValueChange(new ValueChangeEvent<T>(valueObject) {
                    });
                }
            }
        }

        /**
         * get popupText
         * 
         * @return the popupText
         */
        public String getPopupText() {
            return labelText;
        }

        /**
         * get valueObject
         * 
         * @return the valueObject
         */
        public T getValueObject() {
            return valueObject;
        }
    }
}