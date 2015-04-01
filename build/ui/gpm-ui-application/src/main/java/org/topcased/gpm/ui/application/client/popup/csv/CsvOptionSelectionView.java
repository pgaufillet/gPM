/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.csv;

import org.topcased.gpm.ui.application.client.popup.PopupView;
import org.topcased.gpm.ui.component.client.container.GpmFormPanel;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * View for select CSV export options.
 * 
 * @author tpanuel
 */
public class CsvOptionSelectionView extends PopupView implements
        CsvOptionSelectionDisplay {
    private final static double RATIO_WIDTH = 0.2;

    private final static double RATIO_HEIGHT = 0.2;

    private final static int NB_FIELDS = 3;

    private final GpmFormPanel form;

    private final ListBox escape;

    private final ListBox quote;

    private final ListBox separator;

    private final Button exportButton;

    /**
     * Select CSV export options view.
     */
    public CsvOptionSelectionView() {
        super(Ui18n.CONSTANTS.exportCsvOptionsTitle());
        final ScrollPanel lPanel = new ScrollPanel();

        // Build form
        form = new GpmFormPanel(NB_FIELDS);
        form.addStyleName(ComponentResources.INSTANCE.css().gpmBigBorder());
        // Separator character
        separator = new ListBox();
        separator.addItem(";");
        separator.addItem(":");
        separator.addItem(",");
        // Quote character
        quote = new ListBox();
        quote.addItem("\"");
        quote.addItem("'");
        // Escape character
        escape = new ListBox();
        escape.addItem("\"");
        escape.addItem("\\");
        form.addField(
                Ui18n.CONSTANTS.exportCsvOptionsSeparatorCharacterChoice(),
                separator);
        form.addField(Ui18n.CONSTANTS.exportCsvOptionsQuoteCharacterChoice(),
                quote);
        form.addField(Ui18n.CONSTANTS.exportCsvOptionsEscapeCharacterChoice(),
                escape);

        lPanel.addStyleName(ComponentResources.INSTANCE.css().gpmSmallBorder());
        lPanel.setWidget(form);

        exportButton = addButton(Ui18n.CONSTANTS.export());

        setContent(lPanel);

        setRatioSize(RATIO_WIDTH, RATIO_HEIGHT);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.mail.SendMailDisplay#setSendButtonHandler(com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void addExportButtonHandler(final ClickHandler pHandler) {
        exportButton.addClickHandler(pHandler);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.csv.CsvOptionSelectionDisplay#reset()
     */
    @Override
    public void reset() {
        escape.setSelectedIndex(0);
        quote.setSelectedIndex(0);
        separator.setSelectedIndex(0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.csv.CsvOptionSelectionDisplay#getEscapeCharacter()
     */
    @Override
    public String getEscapeCharacter() {
        return escape.getItemText(escape.getSelectedIndex());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.csv.CsvOptionSelectionDisplay#getQuoteCharacter()
     */
    @Override
    public String getQuoteCharacter() {
        return quote.getItemText(quote.getSelectedIndex());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.csv.CsvOptionSelectionDisplay#getSeparatorCharacter()
     */
    @Override
    public String getSeparatorCharacter() {
        return separator.getItemText(separator.getSelectedIndex());
    }
}