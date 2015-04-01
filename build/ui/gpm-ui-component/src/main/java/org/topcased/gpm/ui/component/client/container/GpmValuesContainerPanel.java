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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.util.GpmBasicActionHandler;

import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * GpmValuesContainerPanel is a Panel that displays groups horizontally in the
 * order they have been added.
 * <p>
 * Initially groups are closed. <br />
 * As soon as a group is opened the adding of fields is prohibited.
 * </p>
 * 
 * @author frosier
 */
public class GpmValuesContainerPanel extends VerticalPanel implements GpmFieldSet {
    private final List<GpmDisplayGroupPanel> displayGroups;

    private final Map<String, GpmLinkGroupPanel> linkGroups;

    /**
     * Creates an empty values container panel.
     */
    public GpmValuesContainerPanel() {
        displayGroups = new ArrayList<GpmDisplayGroupPanel>();
        linkGroups = new HashMap<String, GpmLinkGroupPanel>();
        setStylePrimaryName(ComponentResources.INSTANCE.css().gpmValuesContainerPanel());
    }

    /**
     * Add a display group in the values container. <br />
     * Only the group name is necessary. The group is built and added in the
     * container.
     * 
     * @param pGroupName
     *            The name of the group to add.
     * @return The new display group.
     */
    public GpmDisplayGroupPanel addFieldGroup(final String pGroupName) {
        final GpmDisplayGroupPanel lDisplayGroupPanel =
                new GpmDisplayGroupPanel(true);

        lDisplayGroupPanel.setName(pGroupName);
        add(lDisplayGroupPanel);
        displayGroups.add(lDisplayGroupPanel);

        return lDisplayGroupPanel;
    }

    /**
     * Add a link group.
     * 
     * @param pLinkTypeName
     *            The link type name.
     * @param pGroupName
     *            The group name : link type name translated.
     * @param pLoadLink
     *            The load link command.
     * @return The new link group.
     */
    public GpmLinkGroupPanel addLinkGroup(final String pLinkTypeName,
            final String pGroupName,
            final GpmBasicActionHandler<String> pLoadLink) {
        final GpmLinkGroupPanel lLinkGroupPanel =
                new GpmLinkGroupPanel(pLinkTypeName);

        lLinkGroupPanel.setName(pGroupName);

        lLinkGroupPanel.setOpenHandler(pLoadLink);

        add(lLinkGroupPanel);
        linkGroups.put(pLinkTypeName, lLinkGroupPanel);

        return lLinkGroupPanel;
    }

    /**
     * get a link group
     * 
     * @param pLinkTypeName
     *            the link type name
     * @return the link group
     */
    public GpmLinkGroupPanel getLinkGroup(String pLinkTypeName) {
        return linkGroups.get(pLinkTypeName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.GpmFieldSet#getDisplayedFields()
     */
    @Override
    public Map<String, AbstractGpmField<?>> getDisplayedFields() {
        final Map<String, AbstractGpmField<?>> lUpdatableFields =
                new HashMap<String, AbstractGpmField<?>>();

        // Get the fields of each group
        for (GpmDisplayGroupPanel lDisplayGroup : displayGroups) {
            final List<AbstractGpmField<?>> lFields = lDisplayGroup.getFields();

            // If the group is not initialized the field is not displayed
            if (lFields != null) {
                // Put the fields on a map sorted by name
                for (AbstractGpmField<?> lUpdatableField : lFields) {
                    lUpdatableFields.put(lUpdatableField.getFieldName(),
                            lUpdatableField);
                }
            }
        }

        return lUpdatableFields;
    }
}