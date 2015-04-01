/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: ROSIER Florian (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;

import java.util.List;

import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.LazyPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * GpmFieldGridPanel is a LazyPanel to display a set of gPM fields in a grid.
 * <p>
 * The elements contained in the grid must be loaded when its container is
 * opened, as a result its visibility is initialized at <code>false</code>. <br />
 * It is a two columns grid, the first contains the field label and the second
 * gPM field it-self.
 * </p>
 * 
 * @author frosier
 */
public class GpmFieldGridPanel extends LazyPanel {
    public final static String MANDATORY_LABEL = " *";

    private final static int LABEL_WIDTH = 200;

    private static final int DEFAULT_COLUMNS_NUMBER = 2;

    private final String labelWidth;

    private FieldCreationHandler fieldCreationHandler;

    private List<AbstractGpmField<?>> fields;

    /**
     * Create a grid panel with default label size.
     */
    public GpmFieldGridPanel() {
        this(LABEL_WIDTH);
    }

    /**
     * Create a grid panel.
     * 
     * @param pLabelWidth
     *            The width for the labels.
     */
    public GpmFieldGridPanel(final int pLabelWidth) {
        labelWidth = pLabelWidth + "px";
    }

    /**
     * Set an handler executed before the display group initialization.
     * 
     * @param pHandler
     *            The new handler.
     */
    public void setFieldCreationHandler(final FieldCreationHandler pHandler) {
        fieldCreationHandler = pHandler;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.LazyPanel#createWidget()
     */
    @Override
    protected Widget createWidget() {
        fields = fieldCreationHandler.createFields();
        final Grid lGrid = new Grid(fields.size(), DEFAULT_COLUMNS_NUMBER);
        int lLineNumber = 0;

        // Populate each line with label and field contained in gPM fields.
        for (AbstractGpmField<?> lField : fields) {
            final Widget lLabel;

            if (lField.isMandatory() && lField.isUpdatable()) {
                lLabel =
                        new HTML(lField.getTranslatedFieldName()
                                + MANDATORY_LABEL);
            }
            else {
                lLabel = new HTML(lField.getTranslatedFieldName());
            }
            lLabel.setStylePrimaryName(INSTANCE.css().gpmFieldLabel());
            lLabel.setTitle(lField.getFieldDescription());

            lGrid.setWidget(lLineNumber, 0, lLabel);
            lGrid.setWidget(lLineNumber, 1, lField.getPanel());
            lField.getPanel().addStyleName(INSTANCE.css().gpmFieldGridField());

            if (lLineNumber % 2 == 0) {
                lGrid.getRowFormatter().setStylePrimaryName(lLineNumber,
                        INSTANCE.css().evenRow());
            }
            else {
                lGrid.getRowFormatter().setStylePrimaryName(lLineNumber,
                        INSTANCE.css().oddRow());
            }
            lLineNumber++;
        }
        lGrid.setStylePrimaryName(INSTANCE.css().gpmFieldGrid());
        lGrid.getColumnFormatter().setWidth(0, labelWidth);

        return lGrid;
    }

    /**
     * Get the list of fields.
     * 
     * @return The list of fields : null if not initialized.
     */
    public List<AbstractGpmField<?>> getFields() {
        return fields;
    }
}