/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.report;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.ui.application.client.popup.PopupView;
import org.topcased.gpm.ui.component.client.container.GpmDisclosurePanel;
import org.topcased.gpm.ui.facade.shared.export.UiExportableField;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * View for select the list of fields to export on a popup.
 * 
 * @author tpanuel
 */
public class SelectExportedFieldsView extends PopupView implements
        SelectExportedFieldsDisplay {
    private final static double RATIO_WIDTH = 0.3;

    private final static double RATIO_HEIGHT = 0.8;

    private static final double DISCLOSURE_MARGIN = 12;

    private static final String MULTIPLE_SEPARATOR = "::";

    private final FlowPanel form;

    private final List<CheckBox> fields;

    private final Button exportButton;

    private HandlerRegistration exportButtonRegistration;

    /**
     * Create a exported fields selection view.
     */
    public SelectExportedFieldsView() {
        super(CONSTANTS.reportSelectFieldsTitle());
        final ScrollPanel lPanel = new ScrollPanel();

        form = new FlowPanel();
        form.addStyleName(INSTANCE.css().gpmBigBorder());

        lPanel.addStyleName(INSTANCE.css().gpmSmallBorder());
        lPanel.setWidget(form);

        exportButton = addButton(CONSTANTS.export());
        fields = new ArrayList<CheckBox>();

        setContent(lPanel);

        setRatioSize(RATIO_WIDTH, RATIO_HEIGHT);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.report.SelectExportedFieldsDisplay#getSelectedFieldNames()
     */
    @Override
    public List<String> getSelectedFieldNames() {
        final List<String> lSelectedFieldNames = new ArrayList<String>();

        for (final CheckBox lCheckBox : fields) {
            if (lCheckBox.getValue()) {
                lSelectedFieldNames.add(lCheckBox.getElement().getId());
            }
        }

        return lSelectedFieldNames;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.report.SelectExportedFieldsDisplay#addGroupField(java.lang.String,
     *      java.util.List)
     */
    @Override
    public void addGroupField(final String pGroupName,
            final List<UiExportableField> pFields) {
        if (pFields != null && !pFields.isEmpty()) {
            form.add(buildExportableFieldGroup(pGroupName, pFields, false));
        }
    }

    /**
     * Add fields into disclosure panels
     * 
     * @param pGroupName
     *            Group name
     * @param pFields
     *            the fields to add in this group
     * @param pSubFields
     *            <CODE>true</CODE> if this group represents subfields of a
     *            multiple field, else <CODE>false</CODE>
     * @return The filled disclosure panel
     */
    private GpmDisclosurePanel buildExportableFieldGroup(
            final String pGroupName, final List<UiExportableField> pFields,
            boolean pSubFields) {
        final GpmDisclosurePanel lGroup = new GpmDisclosurePanel();
        final VerticalPanel lFieldPanel = new VerticalPanel();

        lGroup.setButtonText(pGroupName);
        // Display all exportable fields
        for (UiExportableField lField : pFields) {
            final List<UiExportableField> lSubFields =
                    lField.getExportableFields();

            if (lSubFields == null || lSubFields.isEmpty()) {
                // Simple field
                final CheckBox lCheckBox =
                        new CheckBox(lField.getTranslatedName());

                if (pSubFields) {
                    lCheckBox.getElement().setId(
                            pGroupName + MULTIPLE_SEPARATOR + lField.getName());
                }
                else {
                    lCheckBox.getElement().setId(lField.getName());
                }
                lCheckBox.setValue(true);
                fields.add(lCheckBox);
                lFieldPanel.add(lCheckBox);
            }
            else {
                // Multiple field : display all sub fields
                final GpmDisclosurePanel lSubGroup =
                        buildExportableFieldGroup(lField.getName(), lSubFields,
                                true);

                lSubGroup.getElement().getStyle().setMarginLeft(
                        DISCLOSURE_MARGIN, Unit.PX);
                lFieldPanel.add(lSubGroup);
            }
        }
        lGroup.setContent(lFieldPanel);
        lGroup.open();

        return lGroup;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.report.SelectExportedFieldsDisplay#reset()
     */
    @Override
    public void reset() {
        form.clear();
        fields.clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.report.SelectReportDisplay#setExportButtonHandler(com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void setExportButtonHandler(final ClickHandler pHandler) {
        // Remove old click handler : if exist
        if (exportButtonRegistration != null) {
            exportButtonRegistration.removeHandler();
        }
        exportButtonRegistration = exportButton.addClickHandler(pHandler);
    }
}