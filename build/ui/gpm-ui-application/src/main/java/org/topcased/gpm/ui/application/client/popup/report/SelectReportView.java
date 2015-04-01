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

import java.util.List;

import org.topcased.gpm.business.util.ChoiceDisplayHintType;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.application.client.popup.PopupView;
import org.topcased.gpm.ui.component.client.container.field.GpmChoiceBoxValue;
import org.topcased.gpm.ui.component.client.container.field.GpmRadioBox;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * View for select a report type on a popup.
 * 
 * @author tpanuel
 */
public class SelectReportView extends PopupView implements SelectReportDisplay {
    private final static double RATIO_WIDTH = 0.2;

    private final static double RATIO_HEIGHT = 0.3;

    private final GpmRadioBox reportNames;

    private final Button exportButton;

    private HandlerRegistration exportButtonRegistration;

    /**
     * Create a product selection view.
     */
    public SelectReportView() {
        super(CONSTANTS.reportSelectModelTitle());
        final ScrollPanel lPanel = new ScrollPanel();
        final SimplePanel lFrom = new SimplePanel();

        reportNames = new GpmRadioBox(ChoiceDisplayHintType.LIST);

        lFrom.addStyleName(INSTANCE.css().gpmBigBorder());
        lFrom.setWidget(reportNames.getWidget());

        lPanel.addStyleName(INSTANCE.css().gpmSmallBorder());
        lPanel.setWidget(lFrom);

        exportButton = addButton(CONSTANTS.export());

        setContent(lPanel);

        setRatioSize(RATIO_WIDTH, RATIO_HEIGHT);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.report.SelectReportDisplay#getSelectedReportName()
     */
    @Override
    public String getSelectedReportName() {
        return reportNames.getCategoryValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.report.SelectReportDisplay#setReportNames(java.util.List)
     */
    @Override
    public void setReportNames(final List<Translation> pReportNames) {
        reportNames.getWidget().clear();
        reportNames.setPossibleValues(GpmChoiceBoxValue.buildFromTranslations(pReportNames));
        if (!pReportNames.isEmpty()) {
            reportNames.setCategoryValue(pReportNames.get(0).getValue());
        }
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