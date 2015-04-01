/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field.tree;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.ui.component.client.button.GpmClickEvent;
import org.topcased.gpm.ui.component.client.button.GpmDoubleClickEvent;
import org.topcased.gpm.ui.component.client.button.GpmImageButton;
import org.topcased.gpm.ui.component.client.popup.GpmPopupPanel;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.tree.GpmDynamicTree;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * GpmMultivaluedTreePopup
 * 
 * @author jeballar
 */
public class GpmMultivaluedTreePopup extends GpmPopupPanel {

    private static final String TEXT_TITLE =
            CONSTANTS.multivaluedTreePopupTitle();

    private final GpmDynamicTree<GpmTreeChoiceFieldItem> tree;

    private static final String STYLE_ITEM =
            ComponentResources.INSTANCE.css().gpmTreeShifterSelectorItem();

    private static final String STYLE_LIST =
            ComponentResources.INSTANCE.css().gpmListShifterSelectorList();

    private static final String STYLE_ITEM_SELECTED =
            ComponentResources.INSTANCE.css().gpmTreeShifterItemSelected();

    private static final String STYLE_ITEM_NOT_SELECTED =
            ComponentResources.INSTANCE.css().gpmTreeShifterItemNotSelected();

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

    // Internal Widgets Sizes

    private int listsWidth = DEFAULT_LIST_WIDTH;

    private int listsHeight = DEFAULT_LIST_HEIGHT;

    private ListWidget selectedList = new ListWidget();

    private final FlowPanel contentPane = new FlowPanel();

    /**
     * Constructor.
     * 
     * @param pTree
     *            the tree of selectable values
     */
    public GpmMultivaluedTreePopup(GpmDynamicTree<GpmTreeChoiceFieldItem> pTree) {
        super(true);
        tree = pTree;
        init();
    }

    private void init() {
        setHeader(new Label(TEXT_TITLE));
        getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.LEFT);
        getElement().getStyle().setPosition(Position.RELATIVE);

        // Values list
        leftScroll = new ScrollPanel();
        leftScroll.add(tree);
        leftScroll.addStyleName(STYLE_LIST);
        leftScroll.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.LEFT);
        leftScroll.getElement().getStyle().setProperty("clear", "left");
        contentPane.add(leftScroll);

        // Selection list
        rightScroll = new ScrollPanel();
        rightScroll.add(selectedList);
        rightScroll.addStyleName(STYLE_LIST);
        rightScroll.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.RIGHT);
        rightScroll.getElement().getStyle().setProperty("clear", "right");
        contentPane.add(rightScroll);

        // Arrow buttons
        GpmImageButton lRightArrow =
                new GpmImageButton(ComponentResources.INSTANCE.images().show());
        lRightArrow.getElement().getStyle().setPosition(Position.ABSOLUTE);
        GpmImageButton lLeftArrow =
                new GpmImageButton(
                        ComponentResources.INSTANCE.images().arrowLeftBlackSmall());
        lLeftArrow.getElement().getStyle().setPosition(Position.ABSOLUTE);
        lRightArrow.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent pEvent) {
                GpmTreeChoiceFieldItem lItem =
                        (GpmTreeChoiceFieldItem) tree.getSelectedItem();
                if (lItem != null) {
                    selectedList.add(lItem);
                }
            }
        });
        lLeftArrow.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent pEvent) {
                int i = 0;
                while (i < selectedList.getWidgetCount()) {
                    WidgetButton lButton =
                            ((WidgetButton) selectedList.getWidget(i));
                    if (lButton.isSelected()) {
                        selectedList.remove(i);
                        i--;
                    }
                    i++;
                }
            }
        });
        rightArrow = lRightArrow;
        leftArrow = lLeftArrow;
        contentPane.add(rightArrow);
        contentPane.add(leftArrow);

        setContent(contentPane);

        internalSetWidgetsSize();
    }

    /**
     * This method resize internal widgets depending on the attributes of the
     * class
     */
    private void internalSetWidgetsSize() {
        // Global widget size (calculated)
        contentPane.setWidth(listsWidth * 2 + LIST_SPACING + "px");

        // Scrolls (lists)
        leftScroll.getElement().getStyle().setWidth(listsWidth, Unit.PX);
        leftScroll.getElement().getStyle().setHeight(listsHeight, Unit.PX);
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
     * Set the currently selected values
     * 
     * @param pSelectedItems
     *            The list of the new selected values
     */
    public void setValues(List<GpmTreeChoiceFieldItem> pSelectedItems) {
        selectedList.clear();
        for (GpmTreeChoiceFieldItem lItem : pSelectedItems) {
            selectedList.add(lItem);
        }
    }

    /**
     * Add a new value in selected values
     * 
     * @param pValue
     *            the represented value
     */
    public void selectValue(GpmTreeChoiceFieldItem pValue) {
        selectedList.add(pValue);
    }

    /**
     * Remove a value from selected values
     * 
     * @param pItem
     */
    public void unSelectValue(GpmTreeChoiceFieldItem pItem) {
        selectedList.remove(pItem);
    }

    /**
     * Private interface to handle Click and double clicks
     */
    private interface PrivateHandler extends ClickHandler, DoubleClickHandler {
    };

    private class WidgetButton extends Label {
        private final PrivateHandler clickHandler;

        private boolean selected = false;

        private final GpmTreeChoiceFieldItem value;

        /**
         * Create a button with a label and a mask to handle click
         * 
         * @param pHandler
         *            the click handler
         * @param pValue
         *            the represented value
         * @param pDisplayedValue
         *            the displayed value
         */
        public WidgetButton(PrivateHandler pHandler,
                GpmTreeChoiceFieldItem pValue) {
            // Init
            clickHandler = pHandler;
            sinkEvents(Event.ONCLICK | Event.ONDBLCLICK);

            value = pValue;

            setText(pValue.toString());

            addStyleName(STYLE_ITEM);
            addStyleName(STYLE_ITEM_NOT_SELECTED);
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
            if (DOM.eventGetType(pEvent) == Event.ONCLICK) {
                clickHandler.onClick(new GpmClickEvent(this));
            }
            else if (DOM.eventGetType(pEvent) == Event.ONDBLCLICK) {
                clickHandler.onDoubleClick(new GpmDoubleClickEvent(this));
            }
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

        /**
         * get value
         * 
         * @return the value
         */
        public GpmTreeChoiceFieldItem getValue() {
            return value;
        }
    }

    private class ListWidget extends FlowPanel {
        private PrivateHandler clickHandler = new PrivateHandler() {
            @Override
            public void onClick(ClickEvent pEvent) {
                // SELECT/DESELECT WHEN CLICKED
                ((WidgetButton) pEvent.getSource()).inverseSelection();
            }

            @Override
            public void onDoubleClick(DoubleClickEvent pEvent) {
                // SHIFT WHEN DB-CLICKED
                int lIndex =
                        selectedList.getWidgetIndex((Widget) pEvent.getSource());
                ((WidgetButton) pEvent.getSource()).inverseSelection();
                remove(lIndex);
            }
        };

        /**
         * Constructor
         */
        public ListWidget() {
            // Do nothing
        }

        public void remove(GpmTreeChoiceFieldItem pItem) {
            int i = 0;
            while (i < getWidgetCount()
                    && ((WidgetButton) getWidget(i)).getValue() == pItem) {
                i++;
            }
            // If found
            if (i != getWidgetCount()) {
                remove(i);
            }
        }

        public void add(GpmTreeChoiceFieldItem pValue) {
            Widget lPanel = buildPanel(pValue);
            int i = 0;
            while (i < getWidgetCount()
                    && ((WidgetButton) getWidget(i)).getValue().getIndex() < pValue.getIndex()) {
                i++;
            }
            // Insert only if different, else value is already selected
            if (i == getWidgetCount()
                    || ((WidgetButton) getWidget(i)).getValue().getIndex() != pValue.getIndex()) {
                super.insert(lPanel, i);
            }
        }

        private Widget buildPanel(GpmTreeChoiceFieldItem pValue) {
            final WidgetButton lElement =
                    new WidgetButton(clickHandler, pValue);
            return lElement;
        }

        public List<GpmTreeChoiceFieldItem> getItems() {
            List<GpmTreeChoiceFieldItem> lItems =
                    new ArrayList<GpmTreeChoiceFieldItem>(getWidgetCount());
            for (int i = 0; i < getWidgetCount(); i++) {
                lItems.add(((WidgetButton) getWidget(i)).getValue());
            }
            return lItems;
        }
    }

    /**
     * Returns the items selected by the user
     * 
     * @return the items selected by the user
     */
    public List<GpmTreeChoiceFieldItem> getSelectedItems() {
        return selectedList.getItems();
    }
}
