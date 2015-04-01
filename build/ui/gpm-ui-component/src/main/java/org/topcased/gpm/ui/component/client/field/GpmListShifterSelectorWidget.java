/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field;

import org.topcased.gpm.ui.component.client.button.GpmClickEvent;
import org.topcased.gpm.ui.component.client.button.GpmDoubleClickEvent;
import org.topcased.gpm.ui.component.client.button.GpmImageButton;
import org.topcased.gpm.ui.component.client.container.field.GpmListShifterSelector.ShiftMode;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * GpmListShifterSelector
 * 
 *@author jeballar
 *@param <T>
 *            the type of the elements in the list
 */
public class GpmListShifterSelectorWidget<T> extends FlowPanel {

    private static final String STYLE_GPM_LIST_SHIFTER_SELECTOR =
            ComponentResources.INSTANCE.css().gpmListShifterSelector();

    private static final String STYLE_ITEM =
            ComponentResources.INSTANCE.css().gpmListShifterSelectorItem();

    private static final String STYLE_LIST =
            ComponentResources.INSTANCE.css().gpmListShifterSelectorList();

    private static final String STYLE_LIST_TITLE =
            ComponentResources.INSTANCE.css().gpmListShifterSelectorListTitle();

    private static final String STYLE_ITEM_SELECTED =
            ComponentResources.INSTANCE.css().gpmListShifterItemSelected();

    private static final String STYLE_ITEM_NOT_SELECTED =
            ComponentResources.INSTANCE.css().gpmListShifterItemNotSelected();

    /** Pixel size between the two lists */
    private static final int LIST_SPACING = 50;

    private static final int TITLE_PADDING = 4;

    private static final int DEFAULT_LIST_WIDTH = 250;

    private static final int DEFAULT_LIST_HEIGHT = 200;

    private static final double HEIGHT_TITLE_RATIO = 0.9;

    // Stored for resize
    private ScrollPanel leftScroll = null;

    private ScrollPanel rightScroll = null;

    private Widget rightArrow = null;

    private Widget leftArrow = null;

    // Shift mode
    private final ShiftMode shiftMode;

    // Internal Widgets Sizes

    private int listsWidth = DEFAULT_LIST_WIDTH;

    private int listsHeight = DEFAULT_LIST_HEIGHT;

    private ListWidget valuesList = new ListWidget(true);

    private ListWidget selectedList = new ListWidget(false);

    private final Label valuesLabel = new Label();

    private final Label selectionLabel = new Label();

    private SelectionEventListener listener;

    /**
     * Constructor
     * 
     * @param pMode
     *            Indicates the shift mode for the listShifter
     */
    public GpmListShifterSelectorWidget(ShiftMode pMode) {
        shiftMode = pMode;
        init();
    }

    private void init() {
        addStyleName(STYLE_GPM_LIST_SHIFTER_SELECTOR);
        getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.LEFT);
        getElement().getStyle().setPosition(Position.RELATIVE);

        // List Labels
        valuesLabel.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.LEFT);
        valuesLabel.addStyleName(STYLE_LIST_TITLE);
        selectionLabel.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.RIGHT);
        selectionLabel.addStyleName(STYLE_LIST_TITLE);
        valuesLabel.getElement().getStyle().setPaddingLeft(TITLE_PADDING,
                Unit.PX);
        selectionLabel.getElement().getStyle().setPaddingLeft(TITLE_PADDING,
                Unit.PX);
        add(valuesLabel);
        add(selectionLabel);
        valuesLabel.setVisible(false);
        selectionLabel.setVisible(false);

        // Values list
        leftScroll = new ScrollPanel();
        leftScroll.add(valuesList);
        leftScroll.addStyleName(STYLE_LIST);
        leftScroll.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.LEFT);
        leftScroll.getElement().getStyle().setProperty("clear", "left");

        // Selection list
        rightScroll = new ScrollPanel();
        rightScroll.add(selectedList);
        rightScroll.addStyleName(STYLE_LIST);
        rightScroll.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.RIGHT);
        rightScroll.getElement().getStyle().setProperty("clear", "right");
        add(rightScroll);

        // Arrow pictures or buttons depending on shift mode
        if (shiftMode == ShiftMode.SHIFT_WITH_BUTTON) {
            GpmImageButton lRightArrow =
                    new GpmImageButton(
                            ComponentResources.INSTANCE.images().show());
            lRightArrow.getElement().getStyle().setPosition(Position.ABSOLUTE);
            GpmImageButton lLeftArrow =
                    new GpmImageButton(
                            ComponentResources.INSTANCE.images().arrowLeftBlackSmall());
            lLeftArrow.getElement().getStyle().setPosition(Position.ABSOLUTE);
            lRightArrow.addClickHandler(new ClickHandler() {
                @SuppressWarnings("unchecked")
				@Override
                public void onClick(ClickEvent pEvent) {
                    for (int i = 0; i < valuesList.getWidgetCount(); i++) {
                        WidgetButton lButton =
                                (WidgetButton) valuesList.getWidget(i);
                        if (lButton.isSelected()) {
                            listener.selectValue(i);
                            lButton.inverseSelection();
                        }
                    }
                }
            });
            lLeftArrow.addClickHandler(new ClickHandler() {
                @SuppressWarnings("unchecked")
				@Override
                public void onClick(ClickEvent pEvent) {
                    int i = 0;
                    while (i < selectedList.getWidgetCount()) {
                        WidgetButton lButton =
                                ((WidgetButton) selectedList.getWidget(i));
                        if (lButton.isSelected()) {
                            listener.unselectValue(i);
                            i--;
                        }
                        i++;
                    }
                }
            });
            rightArrow = lRightArrow;
            leftArrow = lLeftArrow;
        }
        else {
            rightArrow = new Image(ComponentResources.INSTANCE.images().show());
            rightArrow.getElement().getStyle().setPosition(Position.ABSOLUTE);
            leftArrow =
                    new Image(
                            ComponentResources.INSTANCE.images().arrowLeftBlackSmall());
            leftArrow.getElement().getStyle().setPosition(Position.ABSOLUTE);
        }
        add(rightArrow);
        add(leftArrow);

        disableTextSelectInternal(getElement(), true);

        internalSetWidgetsSize();
    }

    /**
     * Static method to enable/disable text selection in Element
     * 
     * @param pE
     *            The Element to target selection deactivation
     * @param pDisable
     *            true to disable, false to re-enable
     */
    protected native static void disableTextSelectInternal(Element pE,
            boolean pDisable)
    /*-{
        if (pDisable) {
            pE.ondrag = function () { return false; };
            pE.onselectstart = function () { return false; };
            pE.style.MozUserSelect="none"
        } else {
            pE.ondrag = null;
            pE.onselectstart = null;
            pE.style.MozUserSelect="text"
        }
    }-*/;

    /**
     * This method resize internal widgets depending on the attributes of the
     * class
     */
    private void internalSetWidgetsSize() {
        // Global widget size (calculated)
        setWidth(listsWidth * 2 + LIST_SPACING + "px");

        // Labels
        valuesLabel.getElement().getStyle().setWidth(
                listsWidth - TITLE_PADDING, Unit.PX);
        selectionLabel.getElement().getStyle().setWidth(
                listsWidth - TITLE_PADDING, Unit.PX);

        // Scrolls (lists)
        leftScroll.getElement().getStyle().setWidth(listsWidth, Unit.PX);
        leftScroll.getElement().getStyle().setHeight(listsHeight, Unit.PX);
        add(leftScroll);
        rightScroll.getElement().getStyle().setWidth(listsWidth, Unit.PX);
        rightScroll.getElement().getStyle().setHeight(listsHeight, Unit.PX);

        // Arrows
        rightArrow.getElement().getStyle().setLeft(listsWidth + TITLE_PADDING,
                Unit.PX);
        rightArrow.getElement().getStyle().setTop(listsHeight / 2, Unit.PX);
        leftArrow.getElement().getStyle().setRight(listsWidth + TITLE_PADDING,
                Unit.PX);
        leftArrow.getElement().getStyle().setTop(listsHeight / 2, Unit.PX);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.UIObject#setPixelSize(int, int)
     */
    @Override
    public void setPixelSize(int pWidth, int pHeight) {
        listsWidth = (pWidth - LIST_SPACING) / 2;
        listsHeight = new Double(pHeight * HEIGHT_TITLE_RATIO).intValue();
        internalSetWidgetsSize();
    }

    /**
     * Sets the lists width
     * 
     * @param pWidth
     *            The new lists width
     */
    public void setListsWidth(int pWidth) {
        listsWidth = pWidth;
        internalSetWidgetsSize();
    }

    /**
     * Set the lists height
     * 
     * @param pHeight
     *            The new lists height
     */
    public void setListsHeight(int pHeight) {
        listsHeight = pHeight;
        internalSetWidgetsSize();
    }

    /**
     * Clear the source values
     */
    public void clearSource() {
        valuesList.clear();
    }

    /**
     * Add a source value
     * 
     * @param pWidget
     *            the Widget for the value to add
     */
    public void addSource(Widget pWidget) {
        valuesList.add(pWidget);
    }

    /**
     * Set a new source at the specified index
     * 
     * @param pWidget
     *            the Widget for the value to update
     * @param pIndex
     *            the index of the value to update
     */
    public void setSource(Widget pWidget, int pIndex) {
        valuesList.remove(pIndex);
        valuesList.insert(pWidget, pIndex);
    }

    /**
     * Hide a Widget from the source value, when its is selected for example
     * 
     * @param pIndex
     *            the index of the value to hide
     */
    public void hideSource(int pIndex) {
        valuesList.getWidget(pIndex).setVisible(false);
    }

    /**
     * Add a value in selected values. This value must be present in source
     * values, or this method will do nothing
     * 
     * @param pValue
     *            the Widget of the value to select
     */
    public void addValue(Widget pValue) {
        selectedList.add(pValue);
    }

    /**
     * Clear all selected values
     */
    public void clearValues() {
        selectedList.clear();
    }

    /**
     * Insert a new value at a specific index
     * 
     * @param pWidget
     *            The Widget of the value to insert
     * @param pIndex
     *            The index for the new Widget
     */
    public void insertValue(Widget pWidget, int pIndex) {
        selectedList.insert(pWidget, pIndex);
    }

    /**
     * Remove a value from the values list
     * 
     * @param pIndex
     *            the index of the value to remove
     */
    public void removeValue(int pIndex) {
        selectedList.remove(pIndex);
    }

    /**
     * Set the listener of selections actions
     * 
     * @param pListener
     *            the listener that will be called when elements are clicked
     */
    public void setSelectionEventListener(SelectionEventListener pListener) {
        listener = pListener;
    }

    /**
     * Sets the text on top of the list. Null values will display nothing on the
     * corresponding list
     * 
     * @param pValueTitle
     *            Define a text on top of the values list
     * @param pSelectionTitle
     *            Define a text on top of the selected values list
     */
    public void setTitles(String pValueTitle, String pSelectionTitle) {
        valuesLabel.setText(pValueTitle);
        selectionLabel.setText(pSelectionTitle);
        if (pValueTitle != null) {
            valuesLabel.setVisible(true);
        }
        if (pSelectionTitle != null) {
            selectionLabel.setVisible(true);
        }
    }

    /**
     * Private interface to handle Click and double clicks
     */
    private interface PrivateHandler extends ClickHandler, DoubleClickHandler {
    };

    private class WidgetButton extends FlowPanel {
        private final PrivateHandler clickHandler;

        private boolean selected = false;

        public WidgetButton(PrivateHandler pHandler) {
            // Init
            clickHandler = pHandler;
            sinkEvents(Event.ONCLICK | Event.ONDBLCLICK);

            addStyleName(STYLE_ITEM);
            if (shiftMode == ShiftMode.SHIFT_WITH_BUTTON) {
                addStyleName(STYLE_ITEM_NOT_SELECTED);
            }
        }

        public boolean isSelected() {
            return selected;
        }

        /**
         * {@inheritDoc} Transmit Events to handlers when necessary
         * 
         * @see com.google.gwt.user.client.ui.Widget#onBrowserEvent(com.google.gwt.user.client.Event)
         */
        public void onBrowserEvent(Event pEvent) {
            if (shiftMode != ShiftMode.SHIFT_ON_DOUBLE_CLICK
                    && DOM.eventGetType(pEvent) == Event.ONCLICK) {
                clickHandler.onClick(new GpmClickEvent(this));
            }
            else if (shiftMode != ShiftMode.SHIFT_ON_CLICK
                    && DOM.eventGetType(pEvent) == Event.ONDBLCLICK) {
                clickHandler.onDoubleClick(new GpmDoubleClickEvent(this));
            }
        }

        public void setWidget(final Widget pWidget) {
            getChildren().add(pWidget);
            getElement().appendChild(pWidget.getElement());
            adopt(pWidget);
        }

        /**
         * Set the selection inverse to its actual state, when in ShiftMode on
         * arrow
         */
        public void inverseSelection() {
            selected = !selected;
            if (selected) {
                removeStyleName(STYLE_ITEM_NOT_SELECTED);
                addStyleName(STYLE_ITEM_SELECTED);
            }
            else {
                removeStyleName(STYLE_ITEM_SELECTED);
                addStyleName(STYLE_ITEM_NOT_SELECTED);
            }
        }
    }

    private class ListWidget extends FlowPanel {

        private final boolean isSourceList;

        private PrivateHandler clickHandler = new PrivateHandler() {
            @SuppressWarnings("unchecked")
			@Override
            public void onClick(ClickEvent pEvent) {
                if (listener != null) {
                    // SHIFT WITH BUTTON
                    if (shiftMode == ShiftMode.SHIFT_WITH_BUTTON) {
                        ((WidgetButton) pEvent.getSource()).inverseSelection();
                    }
                    // SHIFT ON CLICK
                    else {
                        if (isSourceList) {
                            int lIndex =
                                    valuesList.getWidgetIndex((Widget) pEvent.getSource());

                            listener.selectValue(lIndex);
                        }
                        else {
                            int lIndex =
                                    selectedList.getWidgetIndex((Widget) pEvent.getSource());

                            listener.unselectValue(lIndex);
                        }
                    }
                }
            }

            @Override
            public void onDoubleClick(DoubleClickEvent pEvent) {
                if (isSourceList) {
                    int lIndex =
                            valuesList.getWidgetIndex((Widget) pEvent.getSource());

                    listener.selectValue(lIndex);
                }
                else {
                    int lIndex =
                            selectedList.getWidgetIndex((Widget) pEvent.getSource());

                    listener.unselectValue(lIndex);
                }
            }
        };

        /**
         * Constructor
         * 
         * @param pSourceList
         *            Indicates if the list is the source list or the
         *            destination list
         */
        public ListWidget(boolean pSourceList) {
            isSourceList = pSourceList;
        }

        /**
         * {@inheritDoc} It will be hidden behind a mask that will handle clicks
         * for selection actions
         * 
         * @see com.google.gwt.user.client.ui.FlowPanel#add(com.google.gwt.user.client.ui.Widget)
         */
        @Override
        public void add(Widget pWidget) {
            super.add(buildPanel(pWidget));
        }

        @Override
        public void insert(Widget pWidget, int pBeforeIndex) {
            super.insert(buildPanel(pWidget), pBeforeIndex);
        }

        private Widget buildPanel(final Widget pWidget) {
            final WidgetButton lElement = new WidgetButton(clickHandler);
            lElement.setWidget(pWidget);
            return lElement;
        }
    }

    /**
     * SelectionEventListener
     */
    public interface SelectionEventListener {
        /**
         * Called to select a value
         * 
         * @param pIndex
         *            the index of the selected value
         */
        public void selectValue(int pIndex);

        /**
         * Called to unselect a value
         * 
         * @param pIndex
         *            the index of the unselected value
         */
        public void unselectValue(int pIndex);

    }
}
