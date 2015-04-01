/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Joël GIAUFER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field.multivalued;

import java.util.List;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.ui.component.client.container.GpmFieldGridPanel;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Abstract class for the GpmMultipleMultiValuedFields. Generate the main
 * Composite widget which will received the multivalued Multiple fields. Defines
 * the GpmButtons to add or remove an element from the list of GpmComponents and
 * implements list manipulation of these GpmComponents. By default the add
 * button is <b>not</b> displayed
 * 
 * @author jgiaufer
 */
public class GpmMultipleMultivaluedWidget extends
        AbstractGpmMultivaluedWidget<GpmMultipleMultivaluedElement> {
    private final boolean displayHeader;

    private int nbFields;

    /**
     * Create a GpmMultipleMultivaluedWidget.
     * 
     * @param pFields
     *            The fields.
     * @param pAddButtonAvailable
     *            If the add button is available.
     * @param pRemoveButtonAvailable
     *            If the remove button is available.
     * @param pMoveButtonAvailable
     *            If the move button is available.
     * @param pDisplayHeader
     *            If the header need to be displayed.
     */
    public GpmMultipleMultivaluedWidget(
            final List<AbstractGpmField<?>> pFields,
            final boolean pAddButtonAvailable,
            final boolean pRemoveButtonAvailable,
            final boolean pMoveButtonAvailable, final boolean pDisplayHeader) {
        super(new GpmMultipleMultivaluedElement(pFields), pAddButtonAvailable,
                pRemoveButtonAvailable, pMoveButtonAvailable);
        nbFields = pFields.size();
        if (pRemoveButtonAvailable) {
            nbFields++;
        }
        if (pMoveButtonAvailable) {
            nbFields += 2;
        }
        displayHeader = pDisplayHeader;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.field.multivalued.AbstractGpmMultivaluedWidget#doRefreshDisplay()
     */
    protected void doRefreshDisplay() {
        final Grid lTable;
        int lRowCount = 0;
        int lColumnCount = 0;

        if (displayHeader) {
            lTable = new Grid(getElements().size() + 1, nbFields);
            // Initialize header
            for (BusinessField lSubField : getTemplateField()) {
                // Add the title of each field
                final Widget lHeader;

                if (lSubField.isMandatory() && lSubField.isUpdatable()) {
                    lHeader =
                            new HTML(
                                    ((AbstractGpmField<?>) lSubField).getTranslatedFieldName()
                                            + GpmFieldGridPanel.MANDATORY_LABEL);
                }
                else {
                    lHeader =
                            new HTML(
                                    ((AbstractGpmField<?>) lSubField).getTranslatedFieldName());
                }

                lHeader.setTitle(lSubField.getFieldDescription());
                lTable.setWidget(lRowCount, lColumnCount, lHeader);
                lTable.getCellFormatter().addStyleName(
                        lRowCount,
                        lColumnCount,
                        ComponentResources.INSTANCE.css().gpmMultipleUnderline());
                lColumnCount++;
            }
            lTable.getRowFormatter().addStyleName(lRowCount,
                    ComponentResources.INSTANCE.css().gpmFieldLabel());
            lRowCount++;
        }
        else {
            lTable = new Grid(getElements().size(), nbFields);
        }

        // Initialize content
        for (final GpmMultivaluedElement<GpmMultipleMultivaluedElement> lElement : getElements()) {
            final boolean lNeedUnderLine =
                    lRowCount < (lTable.getRowCount() - 1)
                            || isAddButtonAvailable();

            lColumnCount = 0;
            // Give the row formater at each element
            lElement.getElement().setFormatInfo(lTable.getRowFormatter(),
                    lRowCount);
            // Add each field
            for (BusinessField lSubField : lElement.getElement()) {
                lTable.setWidget(lRowCount, lColumnCount,
                        ((AbstractGpmField<?>) lSubField).getPanel());
                if (lNeedUnderLine) {
                    lTable.getCellFormatter().addStyleName(
                            lRowCount,
                            lColumnCount,
                            ComponentResources.INSTANCE.css().gpmMultipleUnderline());
                }
                lColumnCount++;
            }
            // Add CSS if deleted
            if (!lElement.isEnabled()) {
                lTable.getRowFormatter().addStyleName(
                        lRowCount,
                        ComponentResources.INSTANCE.css().gpmMultivaluedWidgetLineDeleted());
            }
            // Add move button
            if (isMoveButtonsAvailable()) {
                lTable.setWidget(lRowCount, lColumnCount,
                        lElement.getUpButton());
                lTable.getCellFormatter().getElement(lRowCount, lColumnCount).getStyle().setPadding(
                        0, Unit.PX);
                if (lNeedUnderLine) {
                    lTable.getCellFormatter().addStyleName(
                            lRowCount,
                            lColumnCount,
                            ComponentResources.INSTANCE.css().gpmMultipleUnderline());
                }
                lColumnCount++;
                lTable.setWidget(lRowCount, lColumnCount,
                        lElement.getDownButton());
                lTable.getCellFormatter().getElement(lRowCount, lColumnCount).getStyle().setPadding(
                        0, Unit.PX);
                if (lNeedUnderLine) {
                    lTable.getCellFormatter().addStyleName(
                            lRowCount,
                            lColumnCount,
                            ComponentResources.INSTANCE.css().gpmMultipleUnderline());
                }
                lColumnCount++;
            }
            // Add remove button
            if (isRemoveButtonAvailable()) {
                final FlowPanel lRemovePanel = new FlowPanel();

                lRemovePanel.add(lElement.getRemoveButton());
                lRemovePanel.add(lElement.getUndoButton());
                lTable.setWidget(lRowCount, lColumnCount, lRemovePanel);
                lTable.getCellFormatter().getElement(lRowCount, lColumnCount).getStyle().setPadding(
                        0, Unit.PX);
                if (lNeedUnderLine) {
                    lTable.getCellFormatter().addStyleName(
                            lRowCount,
                            lColumnCount,
                            ComponentResources.INSTANCE.css().gpmMultipleUnderline());
                }
            }
            lRowCount++;
        }

        lTable.addStyleName(ComponentResources.INSTANCE.css().gpmMultiple());

        add(lTable);
    }

    /**
     * Test if the header is displayed.
     * 
     * @return If the header is displayed.
     */
    public boolean isDisplayHeader() {
        return displayHeader;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.field.multivalued.AbstractGpmMultivaluedWidget#isEnabled()
     */
    public boolean isEnabled() {
        boolean lResult = true;
        for (final GpmMultivaluedElement<?> lElement : getElements()) {
            lResult = lResult && lElement.isEnabled();
        }
        return lResult;
    }
}