/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container.field;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.ui.component.client.field.GpmListShifterSelectorWidget;
import org.topcased.gpm.ui.component.client.field.GpmListShifterSelectorWidget.SelectionEventListener;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Selector with two List widgets. It handles the <T> type of Value represented,
 * that will be wrapped by the wrapper defined by calling the
 * <code>setWrapper</code> method or by default defined wrapper to be displayed
 * as Widgets in the lists
 * 
 * @author jeballar
 * @param <T>
 *            type of the represented objects
 */
public class GpmListShifterSelector<T> extends
        AbstractGpmField<GpmListShifterSelectorWidget<T>> implements
        SelectionEventListener {

    /**
     * Indicates if the output list will be sorted in the same order as the
     * initial list, or by the user selection order
     */
    private final boolean sortByInitialOrder;

    /** Indicates if a value can be selected multiple times */
    private final boolean multipleSelection;

    private final List<T> values = new ArrayList<T>();

    private final List<T> selectedValues = new ArrayList<T>();

    private ListShifterWrapper<T> wrapper = new DefaultListShifterWrapper();

    /**
     * Indicate to the shifter the shift mode
     */
    public enum ShiftMode {
        /** Shift when element is clicked */
        SHIFT_ON_CLICK,
        /** Shift when element is double clicked */
        SHIFT_ON_DOUBLE_CLICK,
        /** Shift when arrow button is clicked */
        SHIFT_WITH_BUTTON
    }

    /**
     * Constructor allowing to choose between different Shift mode
     * 
     * @see ShiftMode
     * @param pByInitialOrder
     *            Indicates if the output list will be sorted in the same order
     *            as the initial list, or by the user selection order
     * @param pMultipleOccurences
     *            Indicates if a value can be added multiple times
     * @param pMode
     *            Indicates the shift mode for the ListShifter
     */
    public GpmListShifterSelector(boolean pByInitialOrder,
            boolean pMultipleOccurences, ShiftMode pMode) {
        super(new GpmListShifterSelectorWidget<T>(pMode));
        getWidget().setSelectionEventListener(this);
        sortByInitialOrder = pByInitialOrder;
        multipleSelection = pMultipleOccurences;
    }

    /**
     * Constructor using default shift mode (shift on click)
     * 
     * @param pByInitialOrder
     *            Indicates if the output list will be sorted in the same order
     *            as the initial list, or by the user selection order
     * @param pMultipleOccurences
     *            Indicates if a value can be added multiple times
     */
    public GpmListShifterSelector(boolean pByInitialOrder,
            boolean pMultipleOccurences) {
        super(new GpmListShifterSelectorWidget<T>(ShiftMode.SHIFT_WITH_BUTTON));
        getWidget().setSelectionEventListener(this);
        sortByInitialOrder = pByInitialOrder;
        multipleSelection = pMultipleOccurences;
    }

    /**
     * Sets the values that can be selected by the user. The objects will be
     * wrapper by the wrapper defined by calling the <code>setWrapper</code>
     * method or by default defined wrapper
     * 
     * @see DefaultListShifterWrapper
     * @param pValues
     *            the values that the user can select
     */
    public void setSelectableValues(List<T> pValues) {
        getWidget().clearSource();
        selectedValues.clear();
        getWidget().clearValues();
        values.clear();
        values.addAll(pValues);
        for (T lValue : pValues) {
            getWidget().addSource(wrapper.wrapToUnSelected(lValue));
        }
    }

    /**
     * Sets the selected values. If a value in the list is not in the selectable
     * values list (comparison by reference), it will be ignored.
     * 
     * @param pValues
     *            the values to select
     */
    public void setSelectedValues(List<T> pValues) {
        clearSelectedValues();
        for (T lValue : pValues) {
            selectValue(lValue);
        }
    }

    private void selectValue(T pValue) {
        int lIndex = values.indexOf(pValue);
        if (lIndex == -1) {
            return;
        }
        if (sortByInitialOrder) {
            // Insert respecting source values order
            int i = 0;
            for (T lSelectedValue : selectedValues) {
                int lCurrentIndex = values.indexOf(lSelectedValue);
                if (lCurrentIndex > lIndex) {
                    break;
                }
                i++;
            }
            getWidget().insertValue(wrapper.wrapToSelected(pValue), i);
            selectedValues.add(i, pValue);
        }
        else {
            // Insert at end
            getWidget().addValue(wrapper.wrapToSelected(pValue));
            selectedValues.add(pValue);
        }
        if (!multipleSelection) {
            // Remove value in unSelected list if multiple selection disabled
            getWidget().hideSource(lIndex);
        }
    }

    private void unselectValue(T pValue) {
        int lIndex = selectedValues.indexOf(pValue);
        if (lIndex == -1) {
            return;
        }
        selectedValues.remove(lIndex);
        getWidget().removeValue(lIndex);
        if (!multipleSelection) {
            // If not multiple selection, unhide the source and refresh its widget
            getWidget().setSource(wrapper.wrapToUnSelected(pValue),
                    values.indexOf(pValue));
        }
    }

    private void clearSelectedValues() {
        selectedValues.clear();
        getWidget().clearValues();
    }

    /**
     * Set the wrapper that builds the Widget for the values
     * 
     * @see ListShifterWrapper
     * @param pWrapper
     *            the wrapper to set
     */
    public void setWrapper(ListShifterWrapper<T> pWrapper) {
        this.wrapper = pWrapper;
    }

    /**
     * Sets the text on top of the list
     * 
     * @param pValueTitle
     *            the text on top of the values list
     * @param pSelectionTitle
     *            the text on top of the selected values list
     */
    public void setTitles(String pValueTitle, String pSelectionTitle) {
        getWidget().setTitles(pValueTitle, pSelectionTitle);
    }

    /**
     * Sets the lists width
     * 
     * @param pWidth
     *            The new lists width
     */
    public void setListsWidth(int pWidth) {
        getWidget().setListsWidth(pWidth);
    }

    /**
     * Set the lists height
     * 
     * @param pHeight
     *            The new lists height
     */
    public void setListsHeight(int pHeight) {
        getWidget().setListsHeight(pHeight);
    }

    /**
     * This interface represents the wrapper for elements in the list. The
     * elements can change their display when they are in the "selected" list or
     * the "unselected" list.
     * 
     * @param <T>
     *            the type of the elements
     */
    public interface ListShifterWrapper<T> {
        /**
         * Returns the widget for the value when selected <br/>
         * Note: Element defined styles (by calling getElement().getStyle())
         * won't be displayed, only CSS defined styles will affect render
         * 
         * @param pField
         *            the field to wrap
         * @return The text for the value when selected
         */
        public Widget wrapToSelected(T pField);

        /**
         * Returns the widget for the value when not selected <br/>
         * Note: Element defined styles (by calling getElement().getStyle())
         * won't be displayed, only CSS defined styles will affect render
         * 
         * @param pField
         *            the field to wrap
         * @return The text for the value when not selected
         */
        public Widget wrapToUnSelected(T pField);
    }

    /**
     * Default wrapper which will return a Label containing the toString value
     * of the selected and not selected values
     * 
     * @param <T>
     *            the type of the element
     */
    private final class DefaultListShifterWrapper implements
            ListShifterWrapper<T> {

        /**
         * Build a private widget, with a nbsp char if the toString value is
         * empty
         * 
         * @param pField
         *            the field to display
         * @return the built widget
         */
        private Widget buildWidget(T pField) {
            Widget lLabel;
            if (pField.toString().trim().equals("")) {
                lLabel = new HTML("&nbsp;");
            }
            else {
                lLabel = new Label(pField.toString());
            }
            return lLabel;
        }

        @Override
        public Widget wrapToSelected(T pField) {
            return buildWidget(pField);
        }

        @Override
        public Widget wrapToUnSelected(T pField) {
            return buildWidget(pField);
        }
    }

    /**
     * {@inheritDoc} For now, does nothing
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#getEmptyClone()
     */
    @Override
    public AbstractGpmField<GpmListShifterSelectorWidget<T>> getEmptyClone() {
        // Not yet implemented because not used as field
        return null;
    }

    /**
     * {@inheritDoc} For now, does nothing
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean pEnabled) {
        // Not yet implemented because not used as field
    }

    /**
     * {@inheritDoc} For now, does nothing
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public void copy(BusinessField pOther) {
        // Not yet implemented because not used as field
    }

    @Override
    public String getAsString() {
        // Not yet implemented because not used as field
        return null;
    }

    /**
     * {@inheritDoc} For now, does nothing
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(BusinessField pOther) {
        // Not yet implemented because not used as field
        return false;
    }

    /**
     * {@inheritDoc} For now, does nothing
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isUpdatable()
     */
    @Override
    public boolean isUpdatable() {
        // Not yet implemented because not used as field
        return false;
    }

    /**
     * get selectedValues
     * 
     * @return the selectedValues
     */
    public List<T> getSelectedValues() {
        return selectedValues;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.field.GpmListShifterSelectorWidget.SelectionEventListener#selectValue(int)
     */
    @Override
    public void selectValue(int pIndex) {
        selectValue(values.get(pIndex));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.field.GpmListShifterSelectorWidget.SelectionEventListener#unselectValue(int)
     */
    @Override
    public void unselectValue(int pIndex) {
        unselectValue(selectedValues.get(pIndex));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#clear()
     */
    @Override
    public void clear() {
        clearSelectedValues();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}