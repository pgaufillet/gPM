/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;

/**
 * GpmDisplayGroupPanel is a GpmDisclosurePanel witch has :
 * <ul>
 * <li>an header as a label with he name of the group.</li>
 * <li>a content as a GpmGridPanel to display GpmFields.</li>
 * </ul>
 * <p>
 * The panel must be open or close manually before be displayed.
 * </p>
 * 
 * @author frosier
 */
public class GpmDisplayGroupPanel extends GpmDisclosurePanel implements
        GpmFieldSet {
    private final GpmFieldGridPanel fieldGridPanel;

    /**
     * Creates an empty display group panel. By default, this panel is closed.
     * 
     * @param pFullMode
     *            Indicates if the decorator must be a light or heavy decorator,
     *            <CODE>true</CODE> for heavy and <CODE>false</CODE> for light.
     */
    public GpmDisplayGroupPanel(boolean pFullMode) {
        super(pFullMode);
        fieldGridPanel = new GpmFieldGridPanel();
        setContent(fieldGridPanel);
    }

    /**
     * Set the name of the group panel.
     * 
     * @param pName
     *            The group name.
     */
    public void setName(final String pName) {
        setButtonText(pName);
    }

    /**
     * Get the name of the group panel
     * 
     * @return Get the name of the group panel
     */
    public String getName() {
        return getButtonText();
    }

    /**
     * Set an handler executed before the display group initialization.
     * 
     * @param pHandler
     *            The new handler.
     */
    public void setFieldCreationHandler(final FieldCreationHandler pHandler) {
        fieldGridPanel.setFieldCreationHandler(pHandler);
    }

    /**
     * Get the list of fields.
     * 
     * @return The list of fields : null if not initialized.
     */
    public List<AbstractGpmField<?>> getFields() {
        return fieldGridPanel.getFields();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.GpmFieldSet#getDisplayedFields()
     */
    @Override
    public Map<String, AbstractGpmField<?>> getDisplayedFields() {
        Map<String, AbstractGpmField<?>> lDisplayed =
                new HashMap<String, AbstractGpmField<?>>();
        if (isOpen()) {
            for (AbstractGpmField<?> lField : fieldGridPanel.getFields()) {
                lDisplayed.put(lField.getFieldName(), lField);
            }
        }
        return lDisplayed;
    }
}